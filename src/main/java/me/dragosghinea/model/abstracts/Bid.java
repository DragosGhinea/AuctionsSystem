package me.dragosghinea.model.abstracts;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface Bid {

    UUID getUserId();

    UUID getAuctionId();

    BigDecimal getPointsBid();

    BigDecimal getTotalBidValue();

    LocalDateTime getBidDate();
}
