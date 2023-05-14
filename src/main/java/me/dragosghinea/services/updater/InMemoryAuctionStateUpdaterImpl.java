package me.dragosghinea.services.updater;

import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.impl.postgres.BlitzAuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.LongAuctionRepositoryImpl;
import me.dragosghinea.services.AuctionService;
import me.dragosghinea.services.impl.BlitzAuctionServiceImpl;
import me.dragosghinea.services.impl.LongAuctionServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//We monitor the state of all auctions registered
//and notify when an auction changes state
public class InMemoryAuctionStateUpdaterImpl implements AuctionStateUpdater{
    private final AuctionService auctionServiceLong;
    private final AuctionService auctionServiceBlitz;

    private final ScheduledExecutorService scheduledExecutorService;

    public void close(){
        scheduledExecutorService.shutdown();
    }

    private InMemoryAuctionStateUpdaterImpl(AuctionService auctionServiceLong, AuctionService auctionServiceBlitz){
        this.auctionServiceLong = auctionServiceLong;
        this.auctionServiceBlitz = auctionServiceBlitz;

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            List<StateChangeEventData> toNotify = new ArrayList<>();
            for(LongAuction auction : toCheckAndUpdateLong){
                AuctionState oldState = auction.getAuctionState();
                AuctionState newState = this.auctionServiceLong.getActualState(auction);
                if(oldState != newState){
                    auction.setAuctionState(newState);
                    StateChangeEventData data = new StateChangeEventData(auction.getAuctionId(), oldState, newState, "Long", auction);
                    notifyObservers(data);
                    toNotify.add(data);
                }
            }

            if(!toNotify.isEmpty()){
                notifyObserversOfAll(toNotify);
                toNotify = new ArrayList<>();
            }

            for(BlitzAuction auction : toCheckAndUpdateBlitz){
                AuctionState oldState = auction.getAuctionState();
                AuctionState newState = this.auctionServiceBlitz.getActualState(auction);
                if(oldState != newState){
                    auction.setAuctionState(newState);
                    StateChangeEventData data = new StateChangeEventData(auction.getAuctionId(), oldState, newState, "Blitz", auction);
                    notifyObservers(data);
                    toNotify.add(data);
                }
            }

            if(!toNotify.isEmpty()){
                notifyObserversOfAll(toNotify);
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static final InMemoryAuctionStateUpdaterImpl instance = new InMemoryAuctionStateUpdaterImpl(
            new LongAuctionServiceImpl(new LongAuctionRepositoryImpl()),
            new BlitzAuctionServiceImpl(new BlitzAuctionRepositoryImpl())
    );

    public static InMemoryAuctionStateUpdaterImpl getInstance(){
        return instance;
    }

    List<BlitzAuction> toCheckAndUpdateBlitz = new CopyOnWriteArrayList<>();
    List<LongAuction> toCheckAndUpdateLong = new CopyOnWriteArrayList<>();
    List<AuctionStateChangeListener> listeners = new ArrayList<>();

    @Override
    public void attach(AuctionStateChangeListener observer) {
        listeners.add(observer);
    }

    @Override
    public void detach(AuctionStateChangeListener observer) {
        listeners.remove(observer);
    }

    @Override
    public void notifyObservers(StateChangeEventData data) {
        for(AuctionStateChangeListener listener : listeners){
            listener.onStateChange(data);
        }
    }

    @Override
    public void notifyObserversOfAll(List<StateChangeEventData> allEventData) {
        for(AuctionStateChangeListener listener : listeners){
            listener.onStateChange(allEventData);
        }
    }

    public void addBlitzAuctionListToCheck(List<BlitzAuction> blitzAuctionList){
        toCheckAndUpdateBlitz.addAll(blitzAuctionList);
    }

    public void addLongAuctionListToCheck(List<LongAuction> longAuctionList){
        toCheckAndUpdateLong.addAll(longAuctionList);
    }

    public void addAuctionToCheck(BlitzAuction auction){
        toCheckAndUpdateBlitz.add(auction);
    }

    public void addAuctionToCheck(LongAuction auction){
        toCheckAndUpdateLong.add(auction);
    }

    public void removeAuctionToCheck(LongAuction auction){
        toCheckAndUpdateLong.remove(auction);
    }

    public void removeAuctionToCheck(BlitzAuction auction){
        toCheckAndUpdateBlitz.remove(auction);
    }

    public void removeAuctionToCheck(UUID auctionId){
        for(BlitzAuction auction : toCheckAndUpdateBlitz){
            if(auction.getAuctionId().equals(auctionId)){
                toCheckAndUpdateBlitz.remove(auction);
                return;
            }
        }

        for(LongAuction auction : toCheckAndUpdateLong){
            if(auction.getAuctionId().equals(auctionId)){
                toCheckAndUpdateLong.remove(auction);
                return;
            }
        }
    }
}
