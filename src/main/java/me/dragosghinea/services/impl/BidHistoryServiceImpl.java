package me.dragosghinea.services.impl;

import me.dragosghinea.exceptions.BidTooLow;
import me.dragosghinea.exceptions.UserNotFound;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.BidRecord;
import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.model.enums.Currency;
import me.dragosghinea.repository.BidHistoryRepository;
import me.dragosghinea.repository.WalletRepository;
import me.dragosghinea.repository.impl.postgres.WalletRepositoryImpl;
import me.dragosghinea.services.BidHistoryService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.services.WalletService;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class BidHistoryServiceImpl implements BidHistoryService {
    private final BidHistory bidHistory;
    private final UserService userService;
    private final Map<UUID, Bid> highestBids = new LinkedHashMap<>();
    private final BidHistoryRepository bidHistoryRepository;

    public BidHistoryServiceImpl(BidHistory bidHistory, UserService userService, BidHistoryRepository bidHistoryRepository){
        this.bidHistory = bidHistory;
        this.userService = userService;
        this.bidHistoryRepository = bidHistoryRepository;
        bidHistory.getBids().forEach(bid -> highestBids.put(bid.getUserId(), bid));
    }

    @Override
    public Auction getAuction() {
        return bidHistory.getAuction();
    }

    @Override
    public Optional<Bid> getLatestBid() {
        if(bidHistory.getBids().isEmpty())
            return Optional.empty();
        return Optional.of(bidHistory.getBids().get(bidHistory.getBids().size()-1));
    }

    @Override
    public Optional<Bid> getHighestBid(UUID userId) {
        return Optional.ofNullable(highestBids.getOrDefault(userId, null));
    }


    @Override
    public List<Bid> getBidsDistinctUsers() {
        Map<UUID, Bid> bids = new LinkedHashMap<>();
        bidHistory.getBids().forEach(bid -> {
            bids.compute(bid.getUserId(), (key, value) -> bid);
        });
        return bids.values().stream().toList();
    }

    @Override
    public boolean addBid(UUID userId, BigDecimal points, boolean takePoints) throws BidTooLow, UserNotFound{
        if(points.compareTo(BigDecimal.ZERO)<0) {
            return false;
        }

        BigDecimal latest = getLatestBid().map(Bid::getTotalBidValue).orElse(BigDecimal.ZERO);
        if(latest.compareTo(points)>=0) {
            throw new BidTooLow(latest, points, userId);
        }


        BigDecimal needsToPay = points.subtract(
                getHighestBid(userId)
                .map(Bid::getTotalBidValue)
                .orElse(BigDecimal.ZERO)
        );


        if(takePoints){
            Optional<User> user = userService.getUserById(userId);
            if(user.isEmpty())
                throw new UserNotFound(userId);
            final WalletRepository walletRepository = new WalletRepositoryImpl();
            Boolean tookPoints = user
                                    .map(User::getWallet)
                                    .map(wallet -> {
                                        WalletService walletService = new WalletServiceImpl(walletRepository, wallet);
                                        return walletService.removePointsFromWallet(needsToPay);
                                    })
                                    .orElse(false);

            if(!tookPoints) {
                return false;
            }
        }
        else if(userService.getUserById(userId).isEmpty())
            throw new UserNotFound(userId);

        Bid newBid = new BidRecord(userId, getAuction().getAuctionId(), needsToPay, points, LocalDateTime.now());
        try {
            bidHistoryRepository.addBid(newBid);
            highestBids.put(userId, newBid);
            bidHistory.getBids().add(newBid);
        }catch(SQLException x){
            return false;
        }
        return true;
    }

    @Override
    public boolean addBid(UUID userId, BigDecimal amount, Currency currency, boolean takePoints) throws BidTooLow, UserNotFound{
        return addBid(userId, currency.getPointsAmount(amount), takePoints);
    }

    @Override
    public boolean removeLatestBid(UUID userId, boolean returnPoints) {
        Bid bid = highestBids.getOrDefault(userId, null);
        if(bid == null)
            return false;

        Optional<User> user = userService.getUserById(userId);
        if(user.isEmpty())
            return false;

        if(bidHistory.getBids().remove(bid)) {
            if(returnPoints)
                user.get().getWallet().addPoints(bid.getPointsBid());
            ListIterator<Bid> listIterator = bidHistory.getBids().listIterator(bidHistory.getBids().size());
            while(listIterator.hasPrevious()){
                Bid previousBid = listIterator.previous();
                if(previousBid.getUserId().equals(userId)){
                    highestBids.replace(userId, previousBid); //atomic operation
                    break;
                }
            }

            highestBids.compute(userId, (key, value) -> {
               if(value == bid)
                   return null;
               else
                   return value;
            });

            try {
                bidHistoryRepository.removeBid(bid);
            }catch (SQLException x){
                return false;
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean removeAllBids(UUID userId, boolean returnPoints) {
        if(returnPoints){
            Boolean givenPoints = userService.getUserById(userId)
                    .map(User::getWallet)
                    .map(wallet -> {
                        BigDecimal points = bidHistory.getBids().stream()
                                .filter(bid -> bid.getUserId().equals(userId))
                                .map(Bid::getPointsBid)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                        wallet.addPoints(points);
                        return true;
                    })
                    .orElse(false);

            if(!givenPoints)
                return false;
        }

        try {
            bidHistoryRepository.removeAllBidsForUser(getAuction().getAuctionId(), userId);
            bidHistory.getBids().removeIf(bid -> bid.getUserId().equals(userId));
        }catch(SQLException x){
            return false;
        }
        return true;
    }


}
