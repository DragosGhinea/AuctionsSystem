package me.dragosghinea.services;

import me.dragosghinea.exceptions.IncompatibleAuction;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public non-sealed interface BlitzAuctionService extends AuctionService {

    @Override
    boolean addAuction(Auction auction) throws IncompatibleAuction;

    @Override
    boolean removeAuction(Auction auction) throws IncompatibleAuction;

    @Override
    Optional<BlitzAuction> getAuctionById(UUID auctionId);

    List<BlitzAuction> getAuctions();

}
