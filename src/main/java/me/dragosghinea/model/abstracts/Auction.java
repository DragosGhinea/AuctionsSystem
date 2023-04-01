package me.dragosghinea.model.abstracts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.enums.AuctionState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public abstract class Auction {
    private UUID auctionId = UUID.randomUUID();
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BidHistory bidHistory;
    private AuctionState auctionState = AuctionState.UNKNOWN;

    private Reward reward;

    private BigDecimal startingBidAmount;
    private BigDecimal minimumBidGap;

    public Auction(){

    }
}
