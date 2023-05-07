package me.dragosghinea.repository;

import me.dragosghinea.model.abstracts.Reward;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface RewardRepository<T extends Reward> {

    boolean addReward(T reward) throws SQLException;

    boolean removeReward(UUID rewardId) throws SQLException;

    Optional<T> getReward(UUID rewardId) throws SQLException;

    boolean addIncludedReward(UUID rewardId, Reward reward) throws SQLException;

    boolean addIncludedReward(UUID rewardId, String rewardInfo) throws SQLException;

    boolean removeIncludedReward(UUID rewardId, Reward reward) throws SQLException;

    boolean removeIncludedReward(UUID rewardId, String rewardInfo) throws SQLException;
}
