package me.dragosghinea.services.impl;

import lombok.RequiredArgsConstructor;
import me.dragosghinea.exceptions.IncompatibleAuctionException;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.AuctionRepository;
import me.dragosghinea.services.LongAuctionService;
import me.dragosghinea.services.updater.InMemoryAuctionStateUpdaterImpl;
import me.dragosghinea.services.updater.StateChangeEventData;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class LongAuctionServiceImpl implements LongAuctionService {
    private final AuctionRepository<LongAuction> auctionRepository;

    @Override
    public boolean startAuction(Auction auction) {
        if(!(auction instanceof LongAuction))
            throw new IncompatibleAuctionException(auction.getClass(), LongAuction.class);

        if (auction.getStartDate().isBefore(LocalDateTime.now()))
            return false;

        auction.setStartDate(LocalDateTime.now());
        try {
            auctionRepository.updateAuction((LongAuction) auction);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean cancelAuction(Auction auction) {
        if (auction.getAuctionState().equals(AuctionState.CANCELLED))
            return false;

        StateChangeEventData stateChangeEventData = new StateChangeEventData(auction.getAuctionId(), auction.getAuctionState(), AuctionState.CANCELLED, "Long", auction);
        InMemoryAuctionStateUpdaterImpl.getInstance().notifyObservers(stateChangeEventData);
        try {
            auctionRepository.setState(auction.getAuctionId(), AuctionState.CANCELLED);
            auction.setAuctionState(AuctionState.CANCELLED);
        }catch(SQLException x){
            x.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean addAuction(Auction auction) throws IncompatibleAuctionException {
        if(auction instanceof LongAuction longAuction) {
            InMemoryAuctionStateUpdaterImpl.getInstance().addAuctionToCheck(longAuction);
            try {
                return auctionRepository.addAuction(longAuction);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        throw new IncompatibleAuctionException(auction.getClass(), LongAuction.class);
    }

    @Override
    public boolean removeAuction(Auction auction) {
        if(auction instanceof LongAuction longAuction) {
            InMemoryAuctionStateUpdaterImpl.getInstance().removeAuctionToCheck(longAuction);
            try {
                return auctionRepository.removeAuctionById(longAuction.getAuctionId());
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        throw new IncompatibleAuctionException(auction.getClass(), LongAuction.class);
    }

    @Override
    public boolean removeAuction(UUID auctionId) {
        InMemoryAuctionStateUpdaterImpl.getInstance().removeAuctionToCheck(auctionId);
        try {
            return auctionRepository.removeAuctionById(auctionId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Deprecated // the update should be done automatically by the AuctionStateUpdater
    public AuctionState updateAuctionState(Auction auction) {
        AuctionState old = auction.getAuctionState();
        AuctionState newState = getActualState(auction);

        if(newState.equals(old))
            return old;

        try {
            auctionRepository.setState(auction.getAuctionId(), newState);
            auction.setAuctionState(newState);
            return newState;
        } catch (SQLException e) {
            e.printStackTrace();
            return old;
        }
    }

    @Override
    public boolean updateAuction(Auction auction) {
        if(auction instanceof LongAuction longAuction) {
            try {
                return auctionRepository.updateAuction(longAuction);
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        throw new IncompatibleAuctionException(auction.getClass(), LongAuction.class);
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
        try {
            LongAuction auction = auctionRepository.getAuctionById(auctionId).orElse(null);
            if (auction == null)
                return Optional.empty();

            InMemoryAuctionStateUpdaterImpl.getInstance().addAuctionToCheck(auction);
            return Optional.of(auction);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public List<LongAuction> getAuctions() {
        try {
            List<LongAuction> longAuctionList = auctionRepository.getAllAuctions();
            InMemoryAuctionStateUpdaterImpl.getInstance().addLongAuctionListToCheck(longAuctionList);
            return longAuctionList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
