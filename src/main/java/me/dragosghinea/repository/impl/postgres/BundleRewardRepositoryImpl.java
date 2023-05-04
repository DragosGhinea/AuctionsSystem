package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.exceptions.DatabaseBundleRewardException;
import me.dragosghinea.mapper.RewardMapper;
import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.RewardRepository;

import java.sql.*;
import java.util.Optional;
import java.util.UUID;

public class BundleRewardRepositoryImpl implements RewardRepository<BundleReward> {
    private static final RewardMapper rewardMapper = RewardMapper.getInstance();

    @Override
    public boolean addReward(BundleReward reward) {
        String rewardInsertSql = "INSERT INTO Reward (reward_id, reward_name, reward_description, reward_type) VALUES (?, ?, ?, ?)";
        String multiRewardInsertSql = "INSERT INTO BundleReward (reward_id, included_reward_id) VALUES (?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement rewardStmt = conn.prepareStatement(rewardInsertSql);
                PreparedStatement multiRewardStmt = conn.prepareStatement(multiRewardInsertSql)
        ) {
            conn.setAutoCommit(false);

            rewardStmt.setObject(1, reward.getRewardId());
            rewardStmt.setObject(2, reward.getRewardName());
            rewardStmt.setObject(3, reward.getRewardDescription());
            rewardStmt.setObject(4, reward.getRewardType().toString());

            for(Reward includedReward : reward.getRewards()) {
                if(includedReward instanceof BundleReward)
                    throw new DatabaseBundleRewardException();
                multiRewardStmt.setObject(1, reward.getRewardId());
                multiRewardStmt.setObject(2, includedReward.getRewardId());
                multiRewardStmt.addBatch();
            }

            rewardStmt.executeUpdate();
            multiRewardStmt.executeBatch();

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
    public Optional<BundleReward> getReward(UUID rewardId) {
        String sql = "SELECT * FROM Reward r LEFT JOIN BundleReward sr ON r.reward_id = sr.reward_id WHERE r.reward_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                CallableStatement callableStatement = conn.prepareCall("{? = call get_bundle_reward_info(?)}")
        ) {
            stmt.setObject(1, rewardId);

            callableStatement.setObject(2, rewardId);

            callableStatement.registerOutParameter(1, Types.OTHER);
            callableStatement.execute();

            return Optional.ofNullable(
                    rewardMapper.mapToBundleReward(stmt.executeQuery(),
                            (ResultSet) callableStatement.getObject(1)
                    ));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, Reward reward) {
        if(reward instanceof BundleReward)
            return false;

        String sql = "INSERT INTO BundleReward (reward_id, included_reward_id) VALUES (?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);
            stmt.setObject(2, reward.getRewardId());

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, Reward reward) {
        if(reward instanceof BundleReward)
            return false;

        String sql = "DELETE FROM BundleReward WHERE reward_id = ? AND included_reward_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);
            stmt.setObject(2, reward.getRewardId());

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, String rewardInfo) {
        return false;
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, String rewardInfo) {
        return false;
    }

}
