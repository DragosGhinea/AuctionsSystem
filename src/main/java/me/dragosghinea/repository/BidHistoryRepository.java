package me.dragosghinea.repository;

import me.dragosghinea.model.abstracts.Bid;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BidHistoryRepository {

    boolean addBid(Bid bid);

    boolean removeBid(Bid bid);

    boolean removeBidList(Collection<Bid> bids);

    boolean removeAllBidsForUser(UUID auctionId, UUID userId);

    List<Bid> getBids(UUID auctionId, UUID userId);

    List<Bid> getBids(UUID auctionId);
}
