package me.dragosghinea.services.impl;

import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.services.LongAuctionService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class LongAuctionServiceImpl implements LongAuctionService {
    private final static Map<UUID, LongAuction> auctions = new HashMap<>();

    @Override
    public boolean addAuction(Auction auction) {
        if(auction instanceof LongAuction longAuction) {
            return auctions.compute(auction.getAuctionId(), (key, value) -> {
                if(value == null)
                    return longAuction;

                return value;
            }) == longAuction;
        }
        return false;
    }

    @Override
    public boolean removeAuction(Auction auction) {
        if(auction instanceof LongAuction longAuction) {
            return auctions.remove(auction.getAuctionId(), longAuction);
        }
        return false;
    }

    @Override
    public boolean removeAuction(UUID auctionId) {
        return auctions.remove(auctionId) != null;
    }

    @Override
    public AuctionState getActualState(Auction auction) {
        if(!(auction instanceof LongAuction longAuction))
            return AuctionState.UNKNOWN;

        AuctionState state = auction.getAuctionState();
        LocalDateTime now = LocalDateTime.now();

        if(state.equals(AuctionState.CANCELLED))
            return state;

        if(longAuction.getStartDate().isAfter(now)){
            return AuctionState.NOT_STARTED;
        }

        if(longAuction.getOverTime().isBefore(now))
            return AuctionState.ENDED;

        if(!longAuction.getEndDate().isEqual(longAuction.getOverTime()))
            return AuctionState.OVERTIME;

        return AuctionState.ONGOING;
    }

    @Override
    public Optional<LongAuction> getAuctionById(UUID auctionId) {
        return Optional.ofNullable(auctions.getOrDefault(auctionId, null));
    }

    @Override
    public List<LongAuction> getAuctions(Predicate<LongAuction> check) {
        return auctions.values().stream().filter(check).toList();
    }
}
