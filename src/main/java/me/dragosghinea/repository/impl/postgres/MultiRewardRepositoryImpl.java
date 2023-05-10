package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.RewardMapper;
import me.dragosghinea.model.MultiReward;
import me.dragosghinea.model.SingleReward;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.repository.RewardRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class MultiRewardRepositoryImpl implements RewardRepository<MultiReward> {
    private static final RewardMapper rewardMapper = RewardMapper.getInstance();

    @Override
    public boolean addReward(MultiReward reward) throws SQLException {
        String rewardInsertSql = "INSERT INTO Reward (reward_id, reward_name, reward_description, reward_type) VALUES (?, ?, ?, ?)";
        String multiRewardInsertSql = "INSERT INTO MultiReward (reward_id, reward_info) VALUES (?, ?)";

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

            for(String rewardInfo : reward.getRewardInfo()) {
                multiRewardStmt.setObject(1, reward.getRewardId());
                multiRewardStmt.setString(2, rewardInfo);
                multiRewardStmt.addBatch();
            }

            rewardStmt.executeUpdate();
            multiRewardStmt.executeBatch();

            conn.commit();
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean removeReward(UUID rewardId) throws SQLException {
        String sql = "DELETE FROM Reward WHERE reward_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Optional<MultiReward> getReward(UUID rewardId) throws SQLException {
        String sql = "SELECT * FROM Reward r LEFT JOIN MultiReward sr ON r.reward_id = sr.reward_id WHERE r.reward_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);

            return Optional.ofNullable(rewardMapper.mapToMultiReward(stmt.executeQuery()));
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, Reward reward) throws SQLException {
        String sql = "INSERT INTO MultiReward (reward_id, reward_info) VALUES (?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            if(reward instanceof SingleReward singleReward) {
                stmt.setObject(1, rewardId);
                stmt.setString(2, singleReward.getRewardInfo());

                return stmt.executeUpdate() == 1;
            }
            else if(reward instanceof MultiReward multiReward){
                for(String rewardInfo : multiReward.getRewardInfo()){
                    stmt.setObject(1, rewardId);
                    stmt.setString(2, rewardInfo);

                    stmt.addBatch();
                }

                return stmt.executeBatch().length == multiReward.getRewardInfo().size();
            }
        } catch (SQLException e) {
            throw e;
        }

        return false;
    }

    @Override
    public boolean addIncludedReward(UUID rewardId, String rewardInfo) throws SQLException {
        String sql = "INSERT INTO MultiReward (reward_id, reward_info) VALUES (?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);
            stmt.setString(2, rewardInfo);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, Reward reward) throws SQLException {
        String sql = "DELETE FROM MultiReward WHERE reward_id = ? AND reward_info = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            if(reward instanceof SingleReward singleReward) {
                stmt.setObject(1, rewardId);
                stmt.setString(2, singleReward.getRewardInfo());

                return stmt.executeUpdate() == 1;
            }
            else if(reward instanceof MultiReward multiReward){
                for(String rewardInfo : multiReward.getRewardInfo()){
                    stmt.setObject(1, rewardId);
                    stmt.setString(2, rewardInfo);

                    stmt.addBatch();
                }

                return stmt.executeBatch().length == multiReward.getRewardInfo().size();
            }
        } catch (SQLException e) {
            throw e;
        }

        return false;
    }

    @Override
    public boolean removeIncludedReward(UUID rewardId, String rewardInfo) throws SQLException {
        String sql = "DELETE FROM MultiReward WHERE reward_id = ? AND reward_info = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);
            stmt.setString(2, rewardInfo);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }
}
