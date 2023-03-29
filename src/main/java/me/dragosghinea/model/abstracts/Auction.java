package me.dragosghinea.model.abstracts;

import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.BidHistory;
import me.dragosghinea.model.enums.AuctionState;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class Auction {
    private UUID auctionId;
    private LocalDateTime startDate;

    private BidHistory bidHistory;
    private AuctionState auctionState;

    private Reward reward;
}
