package me.dragosghinea.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Bid {
    private final UUID userId;
    private final UUID auctionId;
    private final BigDecimal pointsBid;
    @Setter
    private final BigDecimal totalBidValue;
    private final LocalDateTime bidDate;

    /*public Bid(UUID userId, UUID auctionId, BigDecimal pointsBid, LocalDateTime bidDate) {
        this.userId = userId;
        this.auctionId = auctionId;
        this.pointsBid = pointsBid;
        this.bidDate = bidDate;
    }*/

}
