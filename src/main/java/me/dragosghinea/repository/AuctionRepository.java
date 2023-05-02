package me.dragosghinea.repository;

import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.model.enums.AuctionState;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository<T extends Auction> {

    boolean addAuction(T auction);

    boolean removeAuctionById(UUID auctionId);

    boolean updateAuction(T auction);

    Optional<T> getAuctionById(UUID auctionId);

    List<T> getAllAuctions();

    boolean setReward(UUID auctionId, UUID rewardId);

    boolean setState(UUID auctionId, AuctionState state);

}
