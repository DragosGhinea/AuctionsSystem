package me.dragosghinea.services;

import me.dragosghinea.model.abstracts.Reward;

import java.util.Optional;
import java.util.UUID;

/**
 * The RewardService interface provides methods for managing rewards.
 */
public interface RewardService {

    boolean addReward(Reward reward);

    boolean removeReward(UUID rewardId);

    Optional<Reward> getReward(UUID rewardId);
}
