package me.dragosghinea.services;

import me.dragosghinea.exceptions.BidTooLow;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BidHistoryService {
    Auction getAuction();

    Optional<Bid> getLatestBid();

    Optional<Bid> getHighestBid(UUID userId);

    //gets only the highest bid of each user that has
    //participated and returns them chronologically
    List<Bid> getBidsDistinctUsers();

    boolean addBid(UUID userId, BigDecimal points, boolean takePoints) throws BidTooLow;

    boolean addBid(UUID userId, BigDecimal amount, Currency currency, boolean takePoints) throws BidTooLow;

    boolean removeLatestBid(UUID userId, boolean returnPoints);

    boolean removeAllBids(UUID userId, boolean returnPoints);
}
