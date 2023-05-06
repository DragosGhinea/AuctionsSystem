package me.dragosghinea.services.updater;

import java.util.List;

public interface AuctionStateChangeListener {

    void onStateChange(StateChangeEventData eventData);

    void onStateChange(List<StateChangeEventData> allEventData);
}
