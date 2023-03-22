package me.dragosghinea.wallet;

import me.dragosghinea.user.User;
import me.dragosghinea.wallet.currencies.Currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public interface Wallet {

    List<Currency> getAvailableCurrencies();

    void addCurrency(Currency currency);

    Boolean removeCurrency(Currency currency);

    Boolean buyPoints(BigDecimal currencyAmount);

    Boolean buyPoints(BigDecimal currencyAmount, Currency currencyFrom);

    Boolean sellPoints(BigDecimal currencyAmount);

    Boolean sellPoints(BigDecimal pointsAmount, Currency currencyTo);

    Currency getPreferredCurrency();

    User getOwner();

    BigDecimal getPointsAmount();

    Boolean hasPoints(BigDecimal amount);
}
