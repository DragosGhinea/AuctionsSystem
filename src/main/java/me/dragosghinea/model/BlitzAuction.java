package me.dragosghinea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;

@Getter
@Setter
@Builder
@SuperBuilder
public class BlitzAuction extends Auction {
    private Duration bidDuration;
    private Duration preparingDuration;
}
