package me.dragosghinea.repository.impl.postgres;

import me.dragosghinea.config.DatabaseConnection;
import me.dragosghinea.mapper.WalletMapper;
import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.enums.Currency;
import me.dragosghinea.repository.WalletRepository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class WalletRepositoryImpl implements WalletRepository {
    private static final WalletMapper walletMapper = WalletMapper.getInstance();

    @Override
    public Optional<Wallet> getWallet(UUID userId) throws SQLException {
        String sql = "SELECT * FROM Wallet WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, userId);

            return Optional.ofNullable(walletMapper.mapToWallet(stmt.executeQuery()));
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean updatePreferredCurrency(UUID userId, Currency currency) throws SQLException {
        String sql = "UPDATE Wallet SET preferred_currency = ? WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setObject(1, currency.getCurrencyName());
            stmt.setObject(2, userId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }

    @Override
    public boolean setWalletPointsBalance(UUID userId, BigDecimal points) throws SQLException {
        String sql = "UPDATE Wallet SET points = ? WHERE user_id = ?";

        try(
                Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setBigDecimal(1, points);
            stmt.setObject(2, userId);

            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            throw e;
        }
    }
}
