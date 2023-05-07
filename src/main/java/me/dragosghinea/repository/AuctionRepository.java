package me.dragosghinea.repository;

import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuctionRepository<T extends Auction> {

    boolean addAuction(T auction) throws SQLException;

    boolean removeAuctionById(UUID auctionId) throws SQLException;

    boolean updateAuction(T auction) throws SQLException;

    Optional<T> getAuctionById(UUID auctionId) throws SQLException;

    List<T> getAuctionsByIds(List<UUID> auctionIds) throws SQLException;

    List<T> getAllAuctions() throws SQLException;

    boolean setReward(UUID auctionId, UUID rewardId) throws SQLException;

    boolean setState(UUID auctionId, AuctionState state) throws SQLException;

}
