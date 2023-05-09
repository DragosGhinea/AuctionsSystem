package me.dragosghinea.services;

import me.dragosghinea.exceptions.IncompatibleAuctionException;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public non-sealed interface LongAuctionService extends AuctionService {

    @Override
    boolean addAuction(Auction auction) throws IncompatibleAuctionException;

    @Override
    boolean removeAuction(Auction auction) throws IncompatibleAuctionException;

    @Override
    Optional<LongAuction> getAuctionById(UUID auctionId);

    List<LongAuction> getAuctions();

}
