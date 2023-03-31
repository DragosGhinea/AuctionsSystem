package me.dragosghinea.services.impl;

import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.BidRecord;
import me.dragosghinea.model.User;
import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.model.enums.Currency;
import me.dragosghinea.services.BidHistoryService;
import me.dragosghinea.services.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class BidHistoryServiceImpl implements BidHistoryService {
    private final BidHistory bidHistory;
    private final UserService userService = new UserServiceImpl();

    private final Map<UUID, Bid> highestBids = new LinkedHashMap<>();

    public BidHistoryServiceImpl(BidHistory bidHistory){
        this.bidHistory = bidHistory;
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
    public boolean addBid(UUID userId, BigDecimal points, boolean takePoints) {
        if(points.compareTo(BigDecimal.ZERO)<0)
            return false;

        if(getLatestBid().map(Bid::getPointsBid).orElse(BigDecimal.ZERO).compareTo(points)<0)
            return false;

        BigDecimal needsToPay;
        Bid bid = highestBids.getOrDefault(userId, null);
        if(bid == null)
            needsToPay = points;
        else
            needsToPay = points.subtract(bid.getPointsBid());


        if(takePoints){
            Boolean tookPoints = userService.getUserById(userId)
                                    .map(User::getWallet)
                                    .map(wallet -> wallet.removePoints(needsToPay))
                                    .orElse(false);

            if(!tookPoints)
                return false;
        }
        else if(userService.getUserById(userId).isEmpty())
            return false;

        bidHistory.getBids().add(new BidRecord(userId, getAuction().getAuctionId(), needsToPay, points, LocalDateTime.now()));
        return true;
    }

    @Override
    public boolean addBid(UUID userId, BigDecimal amount, Currency currency, boolean takePoints) {
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
        bidHistory.getBids().removeIf(bid -> bid.getUserId().equals(userId));
        return true;
    }


}
