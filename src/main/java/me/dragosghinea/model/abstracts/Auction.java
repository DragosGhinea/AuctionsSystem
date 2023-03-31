package me.dragosghinea.model.abstracts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.enums.AuctionState;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
@Builder
public abstract class Auction {
    private UUID auctionId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private BidHistory bidHistory;
    private AuctionState auctionState;

    private Reward reward;

    public Auction(){

    }
}
