package me.dragosghinea.repository;

import me.dragosghinea.model.abstracts.Bid;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BidHistoryRepository {

    boolean addBid(Bid bid) throws SQLException;

    boolean removeBid(Bid bid) throws SQLException;

    boolean removeBidList(Collection<Bid> bids) throws SQLException;

    boolean removeAllBidsForUser(UUID auctionId, UUID userId) throws SQLException;

    List<Bid> getBids(UUID auctionId, UUID userId) throws SQLException;

    List<Bid> getBids(UUID auctionId) throws SQLException;
}
