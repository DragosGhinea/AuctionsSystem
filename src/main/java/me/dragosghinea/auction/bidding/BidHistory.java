package me.dragosghinea.auction.bidding;

import me.dragosghinea.auction.Auction;

import java.util.List;
import java.util.Optional;

public interface BidHistory {

    Auction getAuction();

    Optional<Bid> getLatestBid();

    Optional<Bid> getWinner();

    //gets bids sorted chronologically
    List<Bid> getBids();

    //gets only the highest bid of each user that has
    //participated and returns them chronologically
    List<Bid> getBidsDistinctUsers();

}
