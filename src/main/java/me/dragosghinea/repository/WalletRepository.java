package me.dragosghinea.repository;

import me.dragosghinea.model.Wallet;
import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface WalletRepository {

    Optional<Wallet> getWallet(UUID userId);

    boolean updatePreferredCurrency(UUID userId, Currency currency);

    boolean setWalletPointsBalance(UUID userId, BigDecimal points);
}
