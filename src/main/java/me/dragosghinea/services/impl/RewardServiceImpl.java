package me.dragosghinea.services.impl;

import lombok.RequiredArgsConstructor;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.RewardRepository;
import me.dragosghinea.services.RewardService;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository<Reward> rewardRepository;

    @Override
    public boolean addReward(Reward reward) {
        return rewardRepository.addReward(reward);
    }

    @Override
    public boolean removeReward(UUID rewardId) {
        return rewardRepository.removeReward(rewardId);
    }

    @Override
    public Optional<Reward> getReward(UUID rewardId) {
        return rewardRepository.getReward(rewardId);
    }
}
