package me.dragosghinea.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.model.enums.RewardType;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class MultiReward extends Reward {

    private List<String> rewardInfo = new ArrayList<>();

    {
        super.setRewardType(RewardType.MULTIPLE);
    }
}
