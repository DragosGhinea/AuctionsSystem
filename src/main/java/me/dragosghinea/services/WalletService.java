package me.dragosghinea.services;

import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;

public interface WalletService {

    boolean addPointsToWallet(BigDecimal points);

    boolean removePointsFromWallet(BigDecimal points);

    boolean setPointsBalance(BigDecimal points);

    BigDecimal getPointsBalance();

    boolean setPreferredCurrency(Currency currency);
}
