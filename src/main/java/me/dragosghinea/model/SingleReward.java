package me.dragosghinea.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.model.enums.RewardType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Builder
public class SingleReward extends Reward {

    private String rewardInfo;


    {
        super.setRewardType(RewardType.SINGLE);
    }
}
