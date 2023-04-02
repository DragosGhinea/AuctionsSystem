package me.dragosghinea.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Auction;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BlitzAuction extends Auction {
    private Duration bidDuration;
    private Duration preparingDuration;
    public LocalDateTime getActualStartDate(){
        return super.getStartDate().plus(preparingDuration);
    }

    @Override
    public String toString() {
        return "<Blitz Auction>\n"+super.toString();
    }
}
