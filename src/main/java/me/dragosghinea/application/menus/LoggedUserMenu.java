package me.dragosghinea.application.menus;

import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.services.BlitzAuctionService;
import me.dragosghinea.services.LongAuctionService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.impl.BlitzAuctionServiceImpl;
import me.dragosghinea.services.impl.LongAuctionServiceImpl;
import me.dragosghinea.services.impl.UserServiceImpl;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class LoggedUserMenu implements Menu {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Scanner getInputSource() {
        return scanner;
    }

    private final User user;
    private boolean shouldExit;
    private final LongAuctionService longAuctionService = new LongAuctionServiceImpl();
    private final BlitzAuctionService blitzAuctionService = new BlitzAuctionServiceImpl();
    private final UserService userService = new UserServiceImpl();

    public LoggedUserMenu(User user) {
        this.user = user;
    }

    @Override
    public boolean shouldExit() {
        return shouldExit;
    }

    @Override
    public void receiveInput(String input) {
        OPTION op = OPTION.getById(input);

        switch (op) {
            case PRINT_DETAILS -> {
                UserDetails details = user.getUserDetails();
                getOutputSource().println("------------ Details ------------");
                getOutputSource().println("Username: " + details.getUsername());
                getOutputSource().println("Email: " + details.getEmail());
                getOutputSource().println("First Name: " + details.getFirstName());
                getOutputSource().println("Last Name: " + details.getLastName());
                getOutputSource().println("Birth Date: " + details.getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                getOutputSource().println("---------------------------------");
            }
            case VIEW_BID_AUCTIONS -> {
                Iterator<UUID> auctionIdIterator = user.getUserAuctions().getAuctions().iterator();
                if (!auctionIdIterator.hasNext())
                    getOutputSource().println("You have not bid on any auction yet!");
                else {
                    while (auctionIdIterator.hasNext()) {
                        UUID auctionId = auctionIdIterator.next();
                        longAuctionService.getAuctionById(auctionId).ifPresentOrElse(
                                (auction) -> getOutputSource().println(auction),
                                () -> {
                                    blitzAuctionService.getAuctionById(auctionId).ifPresentOrElse(
                                            (auction) -> getOutputSource().println(auction),
                                            auctionIdIterator::remove
                                    );
                                }
                        );
                    }
                }
            }
            case BROWSE_MOST_BIDS -> {
                List<Auction> union = new ArrayList<>(blitzAuctionService.getAuctions(a -> true));
                union.addAll(longAuctionService.getAuctions(a -> true));
                new AuctionsBrowseMenu(
                        user,
                        union,
                        Comparator.comparing(a -> -a.getBidHistory().getBids().size()),
                        "by most bids"
                ).start();
            }
            case BROWSE_HIGHEST_BID -> {
                List<Auction> union = new ArrayList<>(blitzAuctionService.getAuctions(a -> true));
                union.addAll(longAuctionService.getAuctions(a -> true));
                new AuctionsBrowseMenu(
                        user,
                        union,
                        Comparator.comparing(a -> {
                            int sz = a.getBidHistory().getBids().size();
                            if (sz == 0)
                                return BigDecimal.ZERO;
                            return a.getBidHistory().getBids().get(sz - 1).getTotalBidValue().multiply(BigDecimal.valueOf(-1));
                        }),
                        "by highest bid"
                ).start();
            }
            case BROWSE_END_SOON -> {
                List<Auction> union = new ArrayList<>(longAuctionService.getAuctions(a -> true));
                new AuctionsBrowseMenu(
                        user,
                        union,
                        Comparator.comparing(Auction::getEndDate),
                        "by ending soon"
                ).start();
            }
            case WALLET -> {
                new WalletMenu(user).start();
            }
            case DELETE_ACCOUNT -> {
                if (userService.removeUser(user)) {
                    getOutputSource().println("The account was successfully removed!");
                    shouldExit = true;
                } else
                    getOutputSource().println("Could not remove the account!");

            }
            default -> {
                getOutputSource().println("Unknown option '" + input + "'!");
            }
        }
    }

    @Override
    public String menuOptions() {
        return String.format("""
                 User Options (%s)
                   1 - Print user details
                   2 - View auctions you bid on
                   3 - Browse auctions (by most bids)
                   4 - Browse auctions (by highest bid)
                   5 - Browse auctions (by ending soon)
                   6 - Wallet
                   > 'delete account' to delete your account
                 Type 'exit' to log out
                """, user.getUserDetails().getUsername());
    }

    private enum OPTION {
        PRINT_DETAILS("1"),
        VIEW_BID_AUCTIONS("2"),
        BROWSE_MOST_BIDS("3"),
        BROWSE_HIGHEST_BID("4"),
        BROWSE_END_SOON("5"),
        WALLET("6"),
        DELETE_ACCOUNT("delete account"),
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