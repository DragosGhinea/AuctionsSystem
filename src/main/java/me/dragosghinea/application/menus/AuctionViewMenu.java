package me.dragosghinea.application.menus;

import me.dragosghinea.exceptions.BidTooLowException;
import me.dragosghinea.exceptions.UserNotFoundException;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.impl.postgres.BidHistoryRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.BlitzAuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.LongAuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import me.dragosghinea.services.AuctionService;
import me.dragosghinea.services.AuditService;
import me.dragosghinea.services.BidHistoryService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.enums.AuditAction;
import me.dragosghinea.services.impl.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class AuctionViewMenu implements Menu{

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Scanner getInputSource() {
        return scanner;
    }
    private final Auction auction;
    private final User user;
    private final BidHistoryService bidHistoryService;
    private AuctionService auctionService;

    private final UserService userService = new UserServiceImpl(new UserRepositoryImpl());

    private static final AuditService auditService = AuditServiceImpl.getInstance();

    public AuctionViewMenu(User user, Auction auction) {
        auditService.logInfoAction(AuditAction.AUCTION_BROWSING,
                "User "+user.getUserDetails().getUsername()+" is currently checking out the auction with ID "+auction.getAuctionId(),
                user.getUserDetails().getUsername()
        );
        this.user = user;
        this.auction = auction;
        bidHistoryService = new BidHistoryServiceImpl(
                    auction.getBidHistory(),
                    userService,
                    new BidHistoryRepositoryImpl()
                );
    }

    @Override
    public void receiveInput(String input) {
        OPTION op = OPTION.getById(input);
        auctionService.updateAuction(auction);

        switch (op) {
            case PLACE_BID -> {
                BigDecimal minimumBid = bidHistoryService.getLatestBid()
                        .map(bid -> bid.getTotalBidValue().add(auction.getMinimumBidGap()))
                        .orElse(auction.getStartingBidAmount());


                getOutputSource().println("How much you want to bid? (Minimum "+user.getWallet().toString(minimumBid)+")");
                String amountS = getInputSource().nextLine();
                while(true){
                    try {
                        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(amountS)).abs();
                        amount = user.getWallet().getPreferredCurrency().getPointsAmount(amount);

                        //recalculate because someone might bid in the meantime
                        minimumBid = bidHistoryService.getLatestBid()
                                .map(bid -> bid.getTotalBidValue().add(auction.getMinimumBidGap()))
                                .orElse(auction.getStartingBidAmount());

                        if(amount.compareTo(minimumBid) < 0){
                            getOutputSource().println("Bid amount is too low.");
                            break;
                        }

                        if(auction.getAuctionState().equals(AuctionState.ENDED) || auction.getAuctionState().equals(AuctionState.CANCELLED)){
                            getOutputSource().println("This auction can no longer receive bids!");
                            break;
                        }

                        if(!auction.getAuctionState().equals(AuctionState.ONGOING) && !auction.getAuctionState().equals(AuctionState.OVERTIME)){
                            getOutputSource().println("This auction can not receive bids yet!");
                            break;
                        }

                        try {
                            if (bidHistoryService.addBid(user.getUserId(), amount, true)) {
                                userService.addAuctionToUser(user.getUserId(), auction.getAuctionId());
                                if (auction instanceof BlitzAuction blitzAuction) {
                                    blitzAuction.setEndDate(LocalDateTime.now().plus(blitzAuction.getBidDuration()));
                                    auctionService.updateAuction(auction);
                                }
                                else if(auction instanceof LongAuction longAuction){
                                    //overtime is initially equal to the end date,
                                    //end date is guaranteed to be after the current time,
                                    //we extend the overtime by the extendTime if a bid is placed in the last 30 minutes
                                    if(LocalDateTime.now().plusMinutes(30).isAfter(longAuction.getOverTime())){
                                        longAuction.setOverTime(longAuction.getOverTime().plus(longAuction.getExtendTime()));
                                        auctionService.updateAuction(longAuction);
                                    }
                                }
                                auditService.logInfoAction(
                                        AuditAction.BID_PLACE,
                                        "User "+user.getUserDetails().getUsername()+" has placed a bid of "+amount+" points" +
                                                "on the auction with ID "+auction.getAuctionId(),
                                        user.getUserDetails().getUsername()
                                );
                            } else {
                                auditService.logWarnAction(
                                        AuditAction.BID_PLACE,
                                        "User "+user.getUserDetails().getUsername()+" has tried to place a bid of "+amount+" points" +
                                                "on the auction with ID "+auction.getAuctionId()+" but failed.",
                                        user.getUserDetails().getUsername()
                                );
                                getOutputSource().println("The bid couldn't be placed! Are you sure you have the money?");
                            }
                        }
                        catch(UserNotFoundException x){
                            x.printStackTrace();
                            auditService.logFatalAction(AuditAction.USER_ERROR, "User "+user.getUserDetails().getUsername()+" was not found in the database, even though they are logged in.");
                            getOutputSource().println("For some reason, you were not found in the database.");
                        }
                        catch(BidTooLowException x){
                            auditService.logWarnAction(AuditAction.BID_PLACE, x.getMessage(), user.getUserDetails().getUsername());
                            getOutputSource().println(x.getMessage());
                        }

                        break;
                    }catch(Exception x){
                        getOutputSource().println("Invalid number. Reenter: ");
                        amountS = getInputSource().nextLine();
                    }
                }
            }
            case CANCEL_ALL -> {
                if(!bidHistoryService.getLatestBid().map(bid -> !bid.getUserId().equals(user.getUserId())).orElse(false)){
                    getOutputSource().println("This operation can not be performed.");
                    return;
                }

                if(!bidHistoryService.removeAllBids(user.getUserId(), true)){
                    getOutputSource().println("Nothing to remove.");
                }
                else{
                    auditService.logInfoAction(
                            AuditAction.BID_WITHDRAW,
                            "User "+user.getUserDetails().getUsername()+" has withdrawn all bids on auction with ID "+auction.getAuctionId(),
                            user.getUserDetails().getUsername()
                    );
                    userService.removeAuctionFromUser(user.getUserId(), auction.getAuctionId());
                }
            }
            default -> {
                getOutputSource().println("Unknown option '" + input + "'!");
            }
        }
    }

    @Override
    public void start() {
        if(auction instanceof BlitzAuction){
            auctionService = new BlitzAuctionServiceImpl(new BlitzAuctionRepositoryImpl());
            AuctionState state = auctionService.getActualState(auction);
            if(!state.equals(AuctionState.PREPARING) && !state.equals(AuctionState.ENDED)){
                getOutputSource().println("You can only join a blitz auction if it's preparing or ended.");
            }
        }
        else{
            auctionService = new LongAuctionServiceImpl(new LongAuctionRepositoryImpl());
        }

        auctionService.updateAuctionState(auction);

        Menu.super.start();
    }

    @Override
    public String menuOptions() {
        StringBuilder sBuilder = new StringBuilder();
        if(auction instanceof LongAuction)
            sBuilder.append("  Long Auction (");
        else if(auction instanceof BlitzAuction)
            sBuilder.append("  Blitz Auction (");
        else
            sBuilder.append("  Auction (");
        sBuilder.append(auction.getAuctionState().getStateName()).append(")\n");
        sBuilder.append(auction.getAuctionState().getStateDescription()).append("\n");
        sBuilder.append("Reward: ").append(auction.getReward().getRewardName()).append("\n");
        sBuilder.append("Description: ").append(auction.getReward().getRewardDescription()).append("\n");

        String highestBid = bidHistoryService.getLatestBid()
                .map(bid -> user.getWallet().toString(bid.getTotalBidValue())).orElse("None");

        BigDecimal minimumBid = bidHistoryService.getLatestBid()
                .map(bid -> bid.getTotalBidValue().add(auction.getMinimumBidGap()))
                .orElse(auction.getStartingBidAmount());
        sBuilder.append("Highest Bid: ").append(highestBid).append("\n");
        sBuilder.append("Minimum new Bid: ").append(user.getWallet().toString(minimumBid)).append("\n");

        bidHistoryService.getHighestBid(user.getUserId()).ifPresent(bid -> {
            sBuilder.append("Your highest Bid: ").append(user.getWallet().toString(bid.getTotalBidValue())).append("\n");
        });

        sBuilder.append("------------------\n");
        sBuilder.append("1 - Place a bid\n");
        sBuilder.append("2 - Cancel all bids (only if not latest)\n");
        sBuilder.append("Type 'exit' to go back\n");

        return sBuilder.toString();
    }

    private enum OPTION {
        PLACE_BID("1"),
        CANCEL_ALL("2"),
        UNKNOWN("");

        private final String id;

        OPTION(String id) {
            this.id = id;
        }

        public static OPTION getById(String id) {
            for (OPTION option : values())
                if (option.id.equals(id))
                    return option;
            return UNKNOWN;
        }
    }


}
