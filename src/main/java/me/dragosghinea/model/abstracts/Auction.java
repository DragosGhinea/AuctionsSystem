package me.dragosghinea.model.abstracts;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.enums.AuctionState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
public abstract class Auction {
    private UUID auctionId = UUID.randomUUID();
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BidHistory bidHistory = new BidHistory(this);
    private AuctionState auctionState = AuctionState.UNKNOWN;

    private Reward reward;

    private BigDecimal startingBidAmount;
    private BigDecimal minimumBidGap;

    public Auction(){

    }

    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("--- Auction Info ---\n")
                .append("Reward: ").append(reward.getRewardName()).append("\n")
                .append("> Start Date: ").append(startDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n")
                .append("> End Date: ").append(endDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)).append("\n")
                .append("> Current State: ").append(auctionState.getStateName()).append("\n")
                .append("> Number of bids: ").append(bidHistory.getBids().size()).append("\n")
                .append("> ").append(bidHistory.toString()).append("\n")
                .append("---------------------\n");
        return sBuilder.toString();
    }
}
