package me.dragosghinea.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BlitzAuction extends Auction {
    private Duration bidDuration;
    private Duration preparingDuration;
    public LocalDateTime getActualStartDate(){
        return super.getStartDate().plus(preparingDuration);
    }
}
