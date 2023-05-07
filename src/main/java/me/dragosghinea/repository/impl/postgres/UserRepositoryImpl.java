package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.exceptions.AuctionNotFound;
import me.dragosghinea.mapper.AuctionMapper;
import me.dragosghinea.mapper.UserMapper;
import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.repository.UserRepository;
import me.dragosghinea.repository.WalletRepository;

import java.sql.Date;
import java.sql.*;
import java.util.*;

public class UserRepositoryImpl implements UserRepository {
    private static final UserMapper userMapper = UserMapper.getInstance();
    private static final AuctionMapper auctionMapper = AuctionMapper.getInstance();

    private final WalletRepository walletRepository = new WalletRepositoryImpl();

    @Override
    public Optional<User> getUserByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM UserDetails WHERE email ILIKE ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, email);
            UserDetails details = userMapper.mapToUserDetails(stmt.executeQuery());
            if(details == null)
                return Optional.empty();

            User user = new User(details);
            user.getUserAuctions().setAuctions(getUserAuctionIds(user.getUserId()));
            user.setWallet(walletRepository.getWallet(user.getUserId()).orElse(null));
            return Optional.of(user);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM UserDetails WHERE username ILIKE ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, username);
            UserDetails details = userMapper.mapToUserDetails(stmt.executeQuery());
            if(details == null)
                return Optional.empty();

            User user = new User(details);
            user.getUserAuctions().setAuctions(getUserAuctionIds(user.getUserId()));
            user.setWallet(walletRepository.getWallet(user.getUserId()).orElse(null));
            return Optional.of(user);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        String sql = "INSERT INTO UserDetails (user_id, email, username, birth_date, first_name, last_name, password_hash) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlWallet = "INSERT INTO Wallet (user_id, points, preferred_currency) VALUES (?, ?, ?)";

        UserDetails details = user.getUserDetails();

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                PreparedStatement stmtWallet = conn.prepareStatement(sqlWallet)
        ){
            conn.setAutoCommit(false);
            stmt.setObject(1, details.getUserId()); // set user_id
            stmt.setString(2, details.getEmail()); // set email
            stmt.setString(3, details.getUsername()); // set username
            stmt.setDate(4, Date.valueOf(details.getBirthDate())); // set birth_date
            stmt.setString(5, details.getFirstName()); // set first_name
            stmt.setString(6, details.getLastName()); // set last_name
            stmt.setString(7, details.getPasswordHash()); // set password_hash

            stmt.executeUpdate();

            stmtWallet.setObject(1, details.getUserId()); // set user_id
            stmtWallet.setBigDecimal(2, user.getWallet().getPoints()); // set points
            stmtWallet.setString(3, user.getWallet().getPreferredCurrency().getCurrencyName()); // set preferred_currency

            stmtWallet.executeUpdate();

            conn.commit();
            return true;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean removeUserById(UUID id) throws SQLException {
        String sql = "DELETE FROM UserDetails WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, id);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public Optional<User> getUserById(UUID id) throws SQLException {
        String sql = "SELECT * FROM UserDetails WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, id);
            UserDetails details = userMapper.mapToUserDetails(stmt.executeQuery());
            if(details == null)
                return Optional.empty();

            User user = new User(details);
            user.getUserAuctions().setAuctions(getUserAuctionIds(user.getUserId()));
            user.setWallet(walletRepository.getWallet(user.getUserId()).orElse(null));
            return Optional.of(user);
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public List<Auction> getUserAuctions(UUID userId) throws SQLException {
        List<Auction> toReturn = new ArrayList<>();

        String sql = "SELECT * FROM Auction a LEFT JOIN BlitzAuction b ON a.auction_id = b.auction_id " +
                     "LEFT JOIN LongAuction l ON a.auction_id = l.auction_id " +
                     "WHERE a.auction_id IN (SELECT auction_id FROM UserAuctions WHERE user_id = ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            toReturn.addAll(auctionMapper.mapToAuctionList(stmt.executeQuery()));
        } catch (SQLException e) {
            throw e;
        }

        return toReturn;
    }

    @Override
    public Set<UUID> getUserAuctionIds(UUID userId) throws SQLException {
        Set<UUID> toReturn = new HashSet<>();

        String sql = "SELECT auction_id FROM UserAuctions WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            ResultSet set = stmt.executeQuery();
            while(set.next()){
                toReturn.add(set.getObject("auction_id", UUID.class));
            }
        } catch (SQLException e) {
            throw e;
        }

        return toReturn;
    }

    @Override
    public boolean addAuctionToUser(UUID userId, UUID auctionId) throws SQLException {
        String sql = "INSERT INTO UserAuctions (user_id, auction_id) VALUES (?, ?)";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            stmt.setObject(2, auctionId);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            //23503 is the error code for foreign key violation
            if (e.getErrorCode() == 23503 && e.getMessage().contains("auction_id")){
                throw new AuctionNotFound(auctionId);
            }
            throw e;
        }
    }

    @Override
    public boolean removeAuctionFromUser(UUID userId, UUID auctionId) throws SQLException {
        String sql = "DELETE FROM UserAuction WHERE user_id = ? AND auction_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);
            stmt.setObject(2, auctionId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }
}
