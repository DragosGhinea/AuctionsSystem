package me.dragosghinea.repository;

import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {

    Optional<Wallet> getWallet(UUID userId) throws SQLException;

    boolean updatePreferredCurrency(UUID userId, Currency currency) throws SQLException;

    boolean setWalletPointsBalance(UUID userId, BigDecimal points) throws SQLException;
}
