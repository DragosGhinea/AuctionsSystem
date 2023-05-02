package me.dragosghinea.repository;

import me.dragosghinea.model.abstracts.Reward;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RewardRepository<T extends Reward> {

    boolean addReward(T reward);

    boolean removeReward(UUID rewardId);

    Optional<T> getReward(UUID rewardId);

    boolean addIncludedReward(UUID rewardId, Reward reward);

    boolean addIncludedReward(UUID rewardId, String rewardInfo);

    boolean removeIncludedReward(UUID rewardId, Reward reward);

    boolean removeIncludedReward(UUID rewardId, String rewardInfo);
}
