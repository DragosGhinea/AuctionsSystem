package me.dragosghinea.auction.bidding;

import me.dragosghinea.user.details.UserDetails;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

public class Bid {

    private final UUID userId;
    private final UUID auctionId;
    private final BigDecimal pointsBid;
    private final Date bidDate;

    private Boolean isPaid = false;

    public Bid(UUID userId, UUID auctionId, BigDecimal pointsBid, Date bidDate){
        this.userId = userId;
        this.auctionId = auctionId;
        this.pointsBid = pointsBid;
        this.bidDate = bidDate;
    }

    public BigDecimal getPointsBid() {
        return pointsBid;
    }

    public Date getBidDate() {
        return (Date)bidDate.clone();
    }

    public UUID getAuctionId() {
        return auctionId;
    }

    public UUID getUserId() {
        return userId;
    }

    //checks if the user has enough points in their wallet
    //to pay for this bid at the moment the method is called
    public boolean validNow(BigDecimal pointsAmount){
        //no implementation yet
        return false;
    }

    public boolean isPaid(){
        return isPaid;
    }

    //to safely pay the bid if the user has the points
    //to pay it
    public boolean payBid(BigDecimal pointsAmount){
        //no implementation yet
        isPaid = true;
        return false;
    }
}
