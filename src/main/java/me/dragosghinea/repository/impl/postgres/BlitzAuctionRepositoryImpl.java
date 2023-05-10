package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.AuctionMapper;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.enums.AuctionState;
import me.dragosghinea.repository.AuctionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BlitzAuctionRepositoryImpl implements AuctionRepository<BlitzAuction> {
    private static final AuctionMapper auctionMapper = AuctionMapper.getInstance();

    @Override
    public boolean addAuction(BlitzAuction auction) throws SQLException {
        String auctionInsertSql = "INSERT INTO Auction (auction_id, start_date, end_date, auction_state, starting_bid_amount, minimum_bid_gap, reward_id, auction_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String blitzAuctionInsertSql = "INSERT INTO BlitzAuction (auction_id, bid_duration, preparing_duration) VALUES (?, ?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement auctionInsertStmt = conn.prepareStatement(auctionInsertSql);
                PreparedStatement blitzInsertStmt = conn.prepareStatement(blitzAuctionInsertSql)
        ) {
            conn.setAutoCommit(false);

            auctionInsertStmt.setObject(1, auction.getAuctionId()); //set auction_id
            auctionInsertStmt.setTimestamp(2, Timestamp.valueOf(auction.getStartDate())); //set start_date
            auctionInsertStmt.setTimestamp(3, Timestamp.valueOf(auction.getEndDate())); //set end_date
            auctionInsertStmt.setString(4, auction.getAuctionState().toString()); //set auction_state
            auctionInsertStmt.setBigDecimal(5, auction.getStartingBidAmount()); //set starting_bid_amount
            auctionInsertStmt.setBigDecimal(6, auction.getMinimumBidGap()); //set minimum_bid_gap
            auctionInsertStmt.setObject(7, auction.getReward().getRewardId()); //set reward_id
            auctionInsertStmt.setString(8, "Blitz"); //set auction_type

            auctionInsertStmt.executeUpdate();

            blitzInsertStmt.setObject(1, auction.getAuctionId()); //set auction_id
            blitzInsertStmt.setLong(2, auction.getBidDuration().getSeconds()); //set bid_duration
            blitzInsertStmt.setLong(3, auction.getPreparingDuration().getSeconds()); //set preparing_duration

            blitzInsertStmt.executeUpdate();

            conn.commit();
            return true; //conn will be closed by try with resources, changes will commit
                         //if it fails, changes should rollback
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean removeAuctionById(UUID auctionId) throws SQLException {
        String sql = "DELETE FROM Auction WHERE auction_id = ? AND auction_type = 'Blitz'";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, auctionId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean updateAuction(BlitzAuction auction) throws SQLException {
        String auctionUpdateSql = "UPDATE Auction SET start_date = ?, end_date = ?, auction_state = ?, starting_bid_amount = ?, minimum_bid_gap = ?, reward_id = ? WHERE auction_id = ?";
        String blitzAuctionUpdateSql = "UPDATE BlitzAuction SET bid_duration = ?, preparing_duration = ? WHERE auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement auctionUpdateStmt = conn.prepareStatement(auctionUpdateSql);
                PreparedStatement blitzUpdateStmt = conn.prepareStatement(blitzAuctionUpdateSql)
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

            blitzUpdateStmt.setLong(1, auction.getBidDuration().getSeconds()); //set bid_duration
            blitzUpdateStmt.setLong(2, auction.getPreparingDuration().getSeconds()); //set preparing_duration
            blitzUpdateStmt.setObject(3, auction.getAuctionId()); //set auction_id

            blitzUpdateStmt.executeUpdate();

            conn.commit();
            return true; //conn will be closed by try with resources, changes will commit
            //if it fails, changes should rollback
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Optional<BlitzAuction> getAuctionById(UUID auctionId) throws SQLException {
        String sql = "SELECT * FROM Auction a LEFT JOIN BlitzAuction b ON a.auction_id = b.auction_id WHERE a.auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, auctionId);

            return Optional.ofNullable(auctionMapper.mapToBlitzAuction(stmt.executeQuery()));
        } catch (SQLException e) {
            throw e;
        }

    }

    @Override
    public List<BlitzAuction> getAllAuctions() throws SQLException {
        String sql = "SELECT * FROM Auction a LEFT JOIN BlitzAuction b ON a.auction_id = b.auction_id WHERE a.auction_type = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, "Blitz");

            return auctionMapper.mapToBlitzAuctionList(stmt.executeQuery());
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean setReward(UUID auctionId, UUID rewardId) throws SQLException {
        String sql = "UPDATE Auction SET reward_id = ? WHERE auction_id = ? AND auction_type = 'Blitz'";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, rewardId);
            stmt.setObject(2, auctionId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean setState(UUID auctionId, AuctionState state) throws SQLException {
        String sql = "UPDATE Auction SET auction_state = ? WHERE auction_id = ? AND auction_type = 'Blitz'";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, state.toString());
            stmt.setObject(2, auctionId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public List<BlitzAuction> getAuctionsByIds(List<UUID> auctionIds) throws SQLException {
        String sql = "SELECT * FROM Auction a LEFT JOIN BlitzAuction b ON a.auction_id = b.auction_id WHERE a.auction_id IN (?) AND a.auction_type = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setArray(1, conn.createArrayOf("uuid", auctionIds.toArray()));
            stmt.setString(2, "Blitz");

            return auctionMapper.mapToBlitzAuctionList(stmt.executeQuery());
        } catch (SQLException e) {
            throw e;
        }
    }
}
