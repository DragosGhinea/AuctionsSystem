package me.dragosghinea.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
public class BundleReward extends Reward{

    private List<Reward> rewards = new ArrayList<>();

    {
        super.setRewardType(RewardType.BUNDLE);
    }
}
