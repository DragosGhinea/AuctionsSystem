package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.RewardMapper;
import me.dragosghinea.model.SingleReward;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.RewardRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SingleRewardRepositoryImpl implements RewardRepository<SingleReward> {
    private static final RewardMapper rewardMapper = RewardMapper.getInstance();

    @Override
    public boolean addReward(SingleReward reward) {
        String rewardInsertSql = "INSERT INTO Reward (reward_id, reward_name, reward_description, reward_type) VALUES (?, ?, ?, ?)";
        String singleRewardInsertSql = "INSERT INTO SingleReward (reward_id, reward_info) VALUES (?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement rewardStmt = conn.prepareStatement(rewardInsertSql);
                PreparedStatement singleRewardStmt = conn.prepareStatement(singleRewardInsertSql)
        ) {
            conn.setAutoCommit(false);

            rewardStmt.setObject(1, reward.getRewardId());
            rewardStmt.setObject(2, reward.getRewardName());
            rewardStmt.setObject(3, reward.getRewardDescription());
            rewardStmt.setObject(4, reward.getRewardType().toString());

            singleRewardStmt.setObject(1, reward.getRewardId());
            singleRewardStmt.setObject(2, reward.getRewardInfo());

            rewardStmt.executeUpdate();
            singleRewardStmt.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeReward(UUID rewardId) {
        String sql = "DELETE FROM Reward WHERE reward_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<SingleReward> getReward(UUID rewardId) {
        String sql = "SELECT * FROM Reward r LEFT JOIN SingleReward sr ON r.reward_id = sr.reward_id WHERE r.reward_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);

            return Optional.ofNullable(rewardMapper.mapToSingleReward(stmt.executeQuery()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, Reward reward) {
        return false;
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, String rewardInfo) {
        return false;
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, Reward reward) {
        return false;
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, String rewardInfo) {
        return false;
    }
}
