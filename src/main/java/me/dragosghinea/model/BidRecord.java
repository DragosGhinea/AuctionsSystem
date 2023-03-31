package me.dragosghinea.model;

import me.dragosghinea.model.abstracts.Bid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record BidRecord(UUID userId, UUID auctionId, BigDecimal pointsBid, BigDecimal totalBidValue, LocalDateTime bidDate) implements Bid {


    @Override
    public UUID getUserId() {
        return userId;
    }

    @Override
    public UUID getAuctionId() {
        return auctionId;
    }

    @Override
    public BigDecimal getPointsBid() {
        return pointsBid;
    }

    @Override
    public BigDecimal getTotalBidValue() {
        return totalBidValue;
    }

    @Override
    public LocalDateTime getBidDate() {
        return bidDate;
    }
}
