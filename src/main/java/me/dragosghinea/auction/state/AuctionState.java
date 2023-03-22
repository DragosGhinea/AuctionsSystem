package me.dragosghinea.auction.state;

import me.dragosghinea.auction.Auction;

public interface AuctionState {

    String getStateName();
    String getStateDescription();
    Boolean isValid(Auction auction);
    AuctionState getNextState(Auction auction);
    void forceOn(Auction auction);
}
