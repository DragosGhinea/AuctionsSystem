package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.BidMapper;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.repository.BidHistoryRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class BidHistoryRepositoryImpl implements BidHistoryRepository {
    private static final BidMapper bidMapper = BidMapper.getInstance();

    @Override
    public boolean addBid(Bid bid) {
        String sql = "INSERT INTO Bid (user_id, auction_id, bid_date, points_bid, total_bid_value) VALUES (?, ?, ?, ?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setObject(1, bid.getUserId()); //set user_id
            stmt.setObject(2, bid.getAuctionId()); //set auction_id
            stmt.setTimestamp(3, Timestamp.valueOf(bid.getBidDate())); //set bid_date
            stmt.setBigDecimal(4, bid.getPointsBid()); //set points_bid
            stmt.setBigDecimal(5, bid.getTotalBidValue()); //set total_bid_value

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeBid(Bid bid) {
        String sql = "DELETE FROM Bid WHERE user_id = ? AND auction_id = ? AND bid_date = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, bid.getUserId()); //set user_id
            stmt.setObject(2, bid.getAuctionId()); //set auction_id
            stmt.setTimestamp(3, Timestamp.valueOf(bid.getBidDate())); //set bid_date

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeBidList(Collection<Bid> bids) {
        String sql = "DELETE FROM Bid WHERE (user_id, auction_id, bid_date) IN (" +
                    String.join(",", Collections.nCopies(bids.size(), "(?,?,?)"))+
                ")";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            int index = 1;
            for(Bid bid : bids){
                stmt.setObject(index++, bid.getUserId()); //set user_id
                stmt.setObject(index++, bid.getAuctionId()); //set auction_id
                stmt.setTimestamp(index++, Timestamp.valueOf(bid.getBidDate())); //set bid_date
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean removeAllBidsForUser(UUID auctionId, UUID userId) {
        String sql = "DELETE FROM Bid WHERE user_id = ? AND auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            stmt.setObject(2, auctionId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Bid> getBids(UUID auctionId, UUID userId) {
        String sql = "SELECT * FROM Bid WHERE user_id = ? AND auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            stmt.setObject(2, auctionId);

            return bidMapper.mapToBids(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Bid> getBids(UUID auctionId) {
        String sql = "SELECT * FROM Bid WHERE auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, auctionId);

            return bidMapper.mapToBids(stmt.executeQuery());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
