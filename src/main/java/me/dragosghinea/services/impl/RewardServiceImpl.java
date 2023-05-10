package me.dragosghinea.services.impl;

import lombok.RequiredArgsConstructor;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.RewardRepository;
import me.dragosghinea.services.RewardService;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {
    private final RewardRepository<Reward> rewardRepository;

    @Override
    public boolean addReward(Reward reward) {
        try {
            return rewardRepository.addReward(reward);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeReward(UUID rewardId) {
        try {
            return rewardRepository.removeReward(rewardId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<Reward> getReward(UUID rewardId) {
        try {
            return rewardRepository.getReward(rewardId);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
