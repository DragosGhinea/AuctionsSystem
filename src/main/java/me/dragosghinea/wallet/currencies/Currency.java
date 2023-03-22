package me.dragosghinea.wallet.currencies;

import java.math.BigDecimal;

public interface Currency {

    String getCurrencyName();

    String getSingularName();

    String getPluralName();

    String getSymbol();

    BigDecimal getPointsValue();

    BigDecimal getPointsAmount(BigDecimal currencyAmount);

    BigDecimal getCurrencyAmount(BigDecimal points);
}
