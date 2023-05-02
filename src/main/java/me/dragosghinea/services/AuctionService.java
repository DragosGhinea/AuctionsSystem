package me.dragosghinea.services;

import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public sealed interface AuctionService permits BlitzAuctionService, LongAuctionService {

    boolean startAuction(Auction auction);

    boolean cancelAuction(Auction auction);

    Optional<? extends Auction> getAuctionById(UUID auctionId);

    boolean addAuction(Auction auction);

    boolean removeAuction(Auction auction);

    boolean removeAuction(UUID auctionId);

    AuctionState getActualState(Auction auction);

    AuctionState updateAuctionState(Auction auction);

    boolean updateAuction(Auction auction);
}
