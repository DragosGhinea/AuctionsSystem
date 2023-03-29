package me.dragosghinea.model.abstracts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.enums.RewardType;

import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Reward {

    private String rewardName;
    private String rewardDescription;
    private UUID auctionId;
    private RewardType rewardType;
}
