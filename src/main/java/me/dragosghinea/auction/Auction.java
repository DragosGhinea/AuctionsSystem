package me.dragosghinea.auction;

import me.dragosghinea.auction.bidding.BidHistory;
import me.dragosghinea.auction.state.AuctionState;

import java.util.Date;

public abstract class Auction {

    private Date startDate;
    private Date endDate;

    protected BidHistory bidHistory;
    protected AuctionState auctionState;

    public Auction(){

    }

    public Date getStartDate() {
        return (Date)startDate.clone();
    }

    public Date getEndDate() {
        return (Date)endDate.clone();
    }
}
