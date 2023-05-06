package me.dragosghinea.services.impl;

import lombok.RequiredArgsConstructor;
import me.dragosghinea.exceptions.IncompatibleAuction;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.AuctionRepository;
import me.dragosghinea.services.BlitzAuctionService;
import me.dragosghinea.services.updater.InMemoryAuctionStateUpdaterImpl;
import me.dragosghinea.services.updater.StateChangeEventData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class BlitzAuctionServiceImpl implements BlitzAuctionService {
    private final AuctionRepository<BlitzAuction> auctionRepository;

    @Override
    public boolean startAuction(Auction auction) {
        if(!(auction instanceof BlitzAuction))
            throw new IncompatibleAuction(auction.getClass(), BlitzAuction.class);

        if (auction.getStartDate().isBefore(LocalDateTime.now()))
            return false;

        auction.setStartDate(LocalDateTime.now());
        auctionRepository.updateAuction((BlitzAuction) auction);
        return false;
    }

    @Override
    public boolean cancelAuction(Auction auction) {
        if (auction.getAuctionState().equals(AuctionState.CANCELLED))
            return false;

        StateChangeEventData stateChangeEventData = new StateChangeEventData(auction.getAuctionId(), auction.getAuctionState(), AuctionState.CANCELLED, "Blitz", auction);
        InMemoryAuctionStateUpdaterImpl.getInstance().notifyObservers(stateChangeEventData);
        auction.setAuctionState(AuctionState.CANCELLED);
        auctionRepository.setState(auction.getAuctionId(), AuctionState.CANCELLED);
        return true;
    }

    @Override
    public boolean addAuction(Auction auction) throws IncompatibleAuction {
        if(auction instanceof BlitzAuction blitzAuction) {
            InMemoryAuctionStateUpdaterImpl.getInstance().addAuctionToCheck(blitzAuction);
            return auctionRepository.addAuction(blitzAuction);
        }
        throw new IncompatibleAuction(auction.getClass(), BlitzAuction.class);
    }

    @Override
    public boolean removeAuction(Auction auction) throws IncompatibleAuction{
        if(auction instanceof BlitzAuction blitzAuction) {
            InMemoryAuctionStateUpdaterImpl.getInstance().removeAuctionToCheck(blitzAuction);
            return auctionRepository.removeAuctionById(blitzAuction.getAuctionId());
        }

        throw new IncompatibleAuction(auction.getClass(), BlitzAuction.class);
    }

    @Override
    public boolean removeAuction(UUID auctionId) {
        InMemoryAuctionStateUpdaterImpl.getInstance().removeAuctionToCheck(auctionId);
        return auctionRepository.removeAuctionById(auctionId);
    }

    @Override
    @Deprecated // the update should be done automatically by the AuctionStateUpdater
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
        if(auction instanceof BlitzAuction blitzAuction)
            return auctionRepository.updateAuction(blitzAuction);

        throw new IncompatibleAuction(auction.getClass(), BlitzAuction.class);
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

        if(blitzAuction.getEndDate().isBefore(now))
            return AuctionState.ENDED;

        return AuctionState.ONGOING;
    }

    @Override
    public Optional<BlitzAuction> getAuctionById(UUID auctionId) {
        BlitzAuction auction = auctionRepository.getAuctionById(auctionId).orElse(null);
        if(auction == null)
            return Optional.empty();

        InMemoryAuctionStateUpdaterImpl.getInstance().addAuctionToCheck(auction);
        return Optional.of(auction);
    }

    @Override
    public List<BlitzAuction> getAuctions() {
        List<BlitzAuction> blitzAuctions = auctionRepository.getAllAuctions();
        InMemoryAuctionStateUpdaterImpl.getInstance().addBlitzAuctionListToCheck(blitzAuctions);
        return  blitzAuctions;
    }
}
