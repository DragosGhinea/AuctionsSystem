package me.dragosghinea.model.abstracts;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.enums.AuctionState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.Supplier;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@ToString
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
}
