package me.dragosghinea.model.abstracts;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.enums.RewardType;

import java.util.UUID;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class Reward {

    private UUID rewardId;
    private String rewardName;
    private String rewardDescription;
    private RewardType rewardType = RewardType.UNKNOWN;
}
