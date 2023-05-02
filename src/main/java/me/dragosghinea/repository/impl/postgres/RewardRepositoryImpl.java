package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.MultiReward;
import me.dragosghinea.model.SingleReward;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.RewardRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class RewardRepositoryImpl implements RewardRepository<Reward> {
    private final RewardRepository<SingleReward> singleRewardRepository = new SingleRewardRepositoryImpl();
    private final RewardRepository<MultiReward> multiRewardRewardRepository = new MultiRewardRepositoryImpl();
    private final RewardRepository<BundleReward> bundleRewardRewardRepository = new BundleRewardRepositoryImpl();

    @Override
    public boolean addReward(Reward reward) {
        if(reward instanceof SingleReward)
            return singleRewardRepository.addReward((SingleReward) reward);
        else if(reward instanceof MultiReward)
            return multiRewardRewardRepository.addReward((MultiReward) reward);
        else if(reward instanceof BundleReward)
            return bundleRewardRewardRepository.addReward((BundleReward) reward);
        return false;
    }

    @Override
    public boolean removeReward(UUID rewardId) {
        if(!singleRewardRepository.removeReward(rewardId))
            if(!multiRewardRewardRepository.removeReward(rewardId))
                return bundleRewardRewardRepository.removeReward(rewardId);
        return true;
    }

    @Override
    public Optional<Reward> getReward(UUID rewardId) {
        Optional<? extends Reward> reward = singleRewardRepository.getReward(rewardId);
        if(reward.isPresent())
            return reward.map(r -> (Reward) r);
        reward = multiRewardRewardRepository.getReward(rewardId);
        if(reward.isPresent())
            return reward.map(r -> (Reward) r);
        return bundleRewardRewardRepository.getReward(rewardId).map(r -> r);
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, Reward reward) {
        if(reward instanceof SingleReward)
            return singleRewardRepository.addIncludedReward(rewardId, reward);
        else if(reward instanceof MultiReward)
            return multiRewardRewardRepository.addIncludedReward(rewardId, reward);
        else if(reward instanceof BundleReward)
            return bundleRewardRewardRepository.addIncludedReward(rewardId, reward);
        return false;
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, String rewardInfo) {
        String sql = "SELECT reward_type FROM Reward WHERE reward_id = ?";

        String rewardType;
        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);

            ResultSet set = stmt.executeQuery();
            if(!set.next())
                return false;
            rewardType = set.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return switch (rewardType) {
            case "SINGLE" -> singleRewardRepository.addIncludedReward(rewardId, rewardInfo);
            case "MULTI" -> multiRewardRewardRepository.addIncludedReward(rewardId, rewardInfo);
            case "BUNDLE" -> bundleRewardRewardRepository.addIncludedReward(rewardId, rewardInfo);
            default -> false;
        };
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, Reward reward) {
        if(reward instanceof SingleReward)
            return singleRewardRepository.removeIncludedReward(rewardId, reward);
        else if(reward instanceof MultiReward)
            return multiRewardRewardRepository.removeIncludedReward(rewardId, reward);
        else if(reward instanceof BundleReward)
            return bundleRewardRewardRepository.removeIncludedReward(rewardId, reward);
        return false;
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, String rewardInfo) {
        String sql = "SELECT reward_type FROM Reward WHERE reward_id = ?";

        String rewardType;
        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);

            ResultSet set = stmt.executeQuery();
            if(!set.next())
                return false;
            rewardType = set.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return switch (rewardType) {
            case "SINGLE" -> singleRewardRepository.removeIncludedReward(rewardId, rewardInfo);
            case "MULTI" -> multiRewardRewardRepository.removeIncludedReward(rewardId, rewardInfo);
            case "BUNDLE" -> bundleRewardRewardRepository.removeIncludedReward(rewardId, rewardInfo);
            default -> false;
        };
    }
}
