package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.AuctionMapper;
import me.dragosghinea.model.LongAuction;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.AuctionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LongAuctionRepositoryImpl implements AuctionRepository<LongAuction> {
    private final AuctionMapper auctionMapper = AuctionMapper.getInstance();

    @Override
    public boolean addAuction(LongAuction auction) {
        String auctionInsertSql = "INSERT INTO Auction (auction_id, start_date, end_date, auction_state, starting_bid_amount, minimum_bid_gap, reward_id, auction_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String longAuctionInsertSql = "INSERT INTO LongAuction (auction_id, extend_time, overtime) VALUES (?, ?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement auctionInsertStmt = conn.prepareStatement(auctionInsertSql);
                PreparedStatement longInsertStmt = conn.prepareStatement(longAuctionInsertSql)
        ) {
            conn.setAutoCommit(false);

            auctionInsertStmt.setObject(1, auction.getAuctionId()); //set auction_id
            auctionInsertStmt.setTimestamp(2, Timestamp.valueOf(auction.getStartDate())); //set start_date
            auctionInsertStmt.setTimestamp(3, Timestamp.valueOf(auction.getEndDate())); //set end_date
            auctionInsertStmt.setString(4, auction.getAuctionState().toString()); //set auction_state
            auctionInsertStmt.setBigDecimal(5, auction.getStartingBidAmount()); //set starting_bid_amount
            auctionInsertStmt.setBigDecimal(6, auction.getMinimumBidGap()); //set minimum_bid_gap
            auctionInsertStmt.setObject(7, auction.getReward().getRewardId()); //set reward_id
            auctionInsertStmt.setString(8, "Long"); //set auction_type

            auctionInsertStmt.executeUpdate();

            longInsertStmt.setObject(1, auction.getAuctionId()); //set auction_id
            longInsertStmt.setLong(2, auction.getExtendTime().getSeconds()); //set extend_time
            longInsertStmt.setObject(3, auction.getOverTime()); //set overtime

            longInsertStmt.executeUpdate();

            conn.commit();
            return true; //conn will be closed by try with resources, changes will commit
            //if it fails, changes should rollback
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAuctionById(UUID auctionId) {
        String sql = "DELETE FROM Auction WHERE auction_id = ? AND auction_type = 'Long'";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, auctionId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateAuction(LongAuction auction) {
        String auctionUpdateSql = "UPDATE Auction SET start_date = ?, end_date = ?, auction_state = ?, starting_bid_amount = ?, minimum_bid_gap = ?, reward_id = ? WHERE auction_id = ?";
        String longAuctionUpdateSql = "UPDATE LongAuction SET extend_time = ?, overtime = ? WHERE auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement auctionUpdateStmt = conn.prepareStatement(auctionUpdateSql);
                PreparedStatement longUpdateStmt = conn.prepareStatement(longAuctionUpdateSql)
        ) {
            conn.setAutoCommit(false);

            auctionUpdateStmt.setTimestamp(1, Timestamp.valueOf(auction.getStartDate())); //set start_date
            auctionUpdateStmt.setTimestamp(2, Timestamp.valueOf(auction.getEndDate())); //set end_date
            auctionUpdateStmt.setString(3, auction.getAuctionState().toString()); //set auction_state
            auctionUpdateStmt.setBigDecimal(4, auction.getStartingBidAmount()); //set starting_bid_amount
            auctionUpdateStmt.setBigDecimal(5, auction.getMinimumBidGap()); //set minimum_bid_gap
            auctionUpdateStmt.setObject(6, auction.getReward().getRewardId()); //set reward_id
            auctionUpdateStmt.setObject(7, auction.getAuctionId()); //set auction_id

            auctionUpdateStmt.executeUpdate();

            longUpdateStmt.setLong(1, auction.getExtendTime().getSeconds()); //set extend_time
            longUpdateStmt.setObject(2, auction.getOverTime()); //set overtime
            longUpdateStmt.setObject(3, auction.getAuctionId()); //set auction_id

            longUpdateStmt.executeUpdate();

            return true; //conn will be closed by try with resources, changes will commit
            //if it fails, changes should rollback
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<LongAuction> getAuctionById(UUID auctionId) {
        String sql = "SELECT * FROM Auction a LEFT JOIN LongAuction b ON a.auction_id = b.auction_id WHERE a.auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, auctionId);

            return Optional.ofNullable(auctionMapper.mapToLongAuction(stmt.executeQuery()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public List<LongAuction> getAllAuctions() {
        String sql = "SELECT * FROM Auction a LEFT JOIN LongAuction b ON a.auction_id = b.auction_id WHERE a.auction_type = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "Long");

            return auctionMapper.mapToLongAuctionList(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    @Override
    public boolean setReward(UUID auctionId, UUID rewardId) {
        String sql = "UPDATE Auction SET reward_id = ? WHERE auction_id = ? AND auction_type = 'Long'";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);
            stmt.setObject(2, auctionId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean setState(UUID auctionId, AuctionState state) {
        String sql = "UPDATE Auction SET auction_state = ? WHERE auction_id = ? AND auction_type = 'Long'";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, state.toString());
            stmt.setObject(2, auctionId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public List<LongAuction> getAuctionsByIds(List<UUID> auctionIds) {
        String sql = "SELECT * FROM Auction a LEFT JOIN LongAuction b ON a.auction_id = b.auction_id WHERE a.auction_id IN (?) AND a.auction_type = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setArray(1, conn.createArrayOf("uuid", auctionIds.toArray()));
            stmt.setString(2, "Long");

            return auctionMapper.mapToLongAuctionList(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
