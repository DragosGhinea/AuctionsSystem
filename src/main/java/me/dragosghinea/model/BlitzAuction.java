package me.dragosghinea.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;

@Getter
@Setter
@Builder
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BlitzAuction extends Auction {
    private Duration bidDuration;
    private Duration preparingDuration;
}
