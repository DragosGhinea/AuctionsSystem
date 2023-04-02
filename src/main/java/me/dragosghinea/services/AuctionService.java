package me.dragosghinea.services;

import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface AuctionService {

    default boolean startAuction(Auction auction) {
        if (auction.getStartDate().isBefore(LocalDateTime.now()))
            return false;

        auction.setStartDate(LocalDateTime.now());
        return false;
    }

    default boolean cancelAuction(Auction auction) {
        if (auction.getAuctionState().equals(AuctionState.CANCELLED))
            return false;

        auction.setAuctionState(AuctionState.CANCELLED);
        return true;
    }

    Optional<? extends Auction> getAuctionById(UUID auctionId);

    boolean addAuction(Auction auction);

    boolean removeAuction(Auction auction);

    boolean removeAuction(UUID auctionId);

    AuctionState getActualState(Auction auction);

}
