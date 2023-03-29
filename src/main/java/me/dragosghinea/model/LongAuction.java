package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
public class LongAuction extends Auction {

    private Duration extendTime;
    private LocalDateTime endDate;
}
