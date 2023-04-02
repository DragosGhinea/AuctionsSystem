package me.dragosghinea.services;

import me.dragosghinea.model.LongAuction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface LongAuctionService extends AuctionService {

    @Override
    Optional<LongAuction> getAuctionById(UUID auctionId);

    List<LongAuction> getAuctions(Predicate<LongAuction> check);

}
