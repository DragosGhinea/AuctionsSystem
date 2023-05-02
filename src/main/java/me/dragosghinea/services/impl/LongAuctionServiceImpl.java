package me.dragosghinea.services.impl;

import me.dragosghinea.exceptions.IncompatibleAuction;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.AuctionRepository;
import me.dragosghinea.repository.impl.postgres.LongAuctionRepositoryImpl;
import me.dragosghinea.services.LongAuctionService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LongAuctionServiceImpl implements LongAuctionService {
    private final static AuctionRepository<LongAuction> auctionRepository = new LongAuctionRepositoryImpl();

    @Override
    public boolean startAuction(Auction auction) {
        if(!(auction instanceof LongAuction))
            throw new IncompatibleAuction(auction.getClass(), LongAuction.class);

        if (auction.getStartDate().isBefore(LocalDateTime.now()))
            return false;

        auction.setStartDate(LocalDateTime.now());
        auctionRepository.updateAuction((LongAuction) auction);
        return false;
    }

    @Override
    public boolean cancelAuction(Auction auction) {
        if (auction.getAuctionState().equals(AuctionState.CANCELLED))
            return false;

        auction.setAuctionState(AuctionState.CANCELLED);
        auctionRepository.setState(auction.getAuctionId(), AuctionState.CANCELLED);
        return true;
    }

    @Override
    public boolean addAuction(Auction auction) throws IncompatibleAuction {
        if(auction instanceof LongAuction longAuction) {
            return auctionRepository.addAuction(longAuction);
        }
        throw new IncompatibleAuction(auction.getClass(), LongAuction.class);
    }

    @Override
    public boolean removeAuction(Auction auction) {
        if(auction instanceof LongAuction longAuction) {
            return auctionRepository.removeAuctionById(longAuction.getAuctionId());
        }
        throw new IncompatibleAuction(auction.getClass(), LongAuction.class);
    }

    @Override
    public boolean removeAuction(UUID auctionId) {
        return auctionRepository.removeAuctionById(auctionId);
    }

    @Override
    public AuctionState updateAuctionState(Auction auction) {
        AuctionState old = auction.getAuctionState();
        AuctionState newState = getActualState(auction);

        if(newState.equals(old))
            return old;

        auctionRepository.setState(auction.getAuctionId(), newState);
        auction.setAuctionState(newState);
        return newState;
    }

    @Override
    public boolean updateAuction(Auction auction) {
        if(auction instanceof LongAuction longAuction)
            return auctionRepository.updateAuction(longAuction);

        throw new IncompatibleAuction(auction.getClass(), LongAuction.class);
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
        LongAuction auction = auctionRepository.getAuctionById(auctionId).orElse(null);
        if(auction == null)
            return Optional.empty();

        updateAuctionState(auction);
        return Optional.of(auction);
    }

    @Override
    public List<LongAuction> getAuctions() {
        return auctionRepository.getAllAuctions().stream()
                .peek(this::updateAuctionState)
                .toList();
    }
}
