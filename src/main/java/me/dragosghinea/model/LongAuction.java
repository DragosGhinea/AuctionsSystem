package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
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

    @Override
    public String toString() {
        return "<Long Auction>\n"+super.toString();
    }
}
