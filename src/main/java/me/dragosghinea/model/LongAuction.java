package me.dragosghinea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LongAuction extends Auction {

    private Duration extendTime;
    private LocalDateTime overTime;

    public LongAuction(){

    }

    public LongAuction(Duration extendTime, LocalDateTime endDate){
        this.extendTime = extendTime;
        super.setEndDate(endDate);
        this.overTime = endDate;
    }
}
