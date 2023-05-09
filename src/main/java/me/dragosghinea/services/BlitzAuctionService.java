package me.dragosghinea.services;

import me.dragosghinea.exceptions.IncompatibleAuctionException;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public non-sealed interface BlitzAuctionService extends AuctionService {

    @Override
    boolean addAuction(Auction auction) throws IncompatibleAuctionException;

    @Override
    boolean removeAuction(Auction auction) throws IncompatibleAuctionException;

    @Override
    Optional<BlitzAuction> getAuctionById(UUID auctionId);

    List<BlitzAuction> getAuctions();

}
