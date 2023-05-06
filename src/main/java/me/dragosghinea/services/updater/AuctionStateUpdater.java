package me.dragosghinea.services.updater;

import java.util.List;

public interface AuctionStateUpdater {

    void attach(AuctionStateChangeListener observer);
    void detach(AuctionStateChangeListener observer);
    void notifyObservers(StateChangeEventData eventData);

    void notifyObserversOfAll(List<StateChangeEventData> allEventData);

}
