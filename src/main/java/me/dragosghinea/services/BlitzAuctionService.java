package me.dragosghinea.services;

import me.dragosghinea.model.BlitzAuction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface BlitzAuctionService extends AuctionService {

    @Override
    Optional<BlitzAuction> getAuctionById(UUID auctionId);

    List<BlitzAuction> getAuctions(Predicate<BlitzAuction> check);

}
