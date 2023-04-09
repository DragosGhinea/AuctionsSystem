package me.dragosghinea.services.impl;

import me.dragosghinea.exceptions.IncompatibleAuction;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.services.BlitzAuctionService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

public class BlitzAuctionServiceImpl implements BlitzAuctionService {
    private final static Map<UUID, BlitzAuction> auctions = new HashMap<>();

    @Override
    public boolean addAuction(Auction auction) throws IncompatibleAuction {
        if(auction instanceof BlitzAuction blitzAuction) {
            return auctions.compute(auction.getAuctionId(), (key, value) -> {
                if(value == null)
                    return blitzAuction;

                return value;
            }) == blitzAuction;
        }
        throw new IncompatibleAuction(auction.getClass(), BlitzAuction.class);
    }

    @Override
    public boolean removeAuction(Auction auction) throws IncompatibleAuction{
        if(auction instanceof BlitzAuction blitzAuction) {
            return auctions.remove(auction.getAuctionId(), blitzAuction);
        }

        throw new IncompatibleAuction(auction.getClass(), BlitzAuction.class);
    }

    @Override
    public boolean removeAuction(UUID auctionId) {
        return auctions.remove(auctionId) != null;
    }

    @Override
    public AuctionState getActualState(Auction auction) {
        if(!(auction instanceof BlitzAuction blitzAuction))
            return AuctionState.UNKNOWN;

        AuctionState state = auction.getAuctionState();
        LocalDateTime now = LocalDateTime.now();

        if(state.equals(AuctionState.CANCELLED))
            return state;

        if(blitzAuction.getStartDate().isAfter(now)){
            return AuctionState.NOT_STARTED;
        }

        if(blitzAuction.getActualStartDate().isAfter(now))
            return AuctionState.PREPARING;

        if(!blitzAuction.getEndDate().isBefore(now))
            return AuctionState.ENDED;

        return AuctionState.ONGOING;
    }

    @Override
    public Optional<BlitzAuction> getAuctionById(UUID auctionId) {
        BlitzAuction auction = auctions.getOrDefault(auctionId, null);
        if(auction == null)
            return Optional.empty();

        auction.setAuctionState(getActualState(auction));
        return Optional.of(auction);
    }

    @Override
    public List<BlitzAuction> getAuctions(Predicate<BlitzAuction> check) {
        return auctions.values().stream()
                .filter(check)
                .peek(auction -> auction.setAuctionState(getActualState(auction)))
                .toList();
    }
}
