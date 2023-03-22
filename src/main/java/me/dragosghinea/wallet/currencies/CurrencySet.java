package me.dragosghinea.wallet.currencies;

import java.math.BigDecimal;
import java.math.RoundingMode;

//the values are taken from
//https://www.coinbase.com/converter/
//using the UNIT (universal) currency
public enum CurrencySet implements Currency{
    PNT("Point", "Points", "p", BigDecimal.valueOf(1)), //our fake currency
    RON("Leu", "Lei", "lei", BigDecimal.valueOf(111.27882532444771)),
    EUR("Euro", "Euros", "€", BigDecimal.valueOf(547.0695085737132)),
    USD("American Dollar", "American Dollars", "$", BigDecimal.valueOf(507.2046925550859)),
    JPY("Yen", "Yen", "¥", BigDecimal.valueOf(3.828233618406692)),
    CAD("Canadian Dollar", "Canadian Dollars", "$", BigDecimal.valueOf(370.4803297645486));

    private String singularName;
    private String pluralName;
    private BigDecimal unitValue;
    private String symbol;

    CurrencySet(String singularName, String pluralName, String symbol, BigDecimal unitValue){
        this.singularName = singularName;
        this.pluralName = pluralName;
        this.unitValue = unitValue;
        this.symbol=symbol;
    }

    public String getCurrencyName() {
        return super.name();
    }

    public String getSingularName() {
        return singularName;
    }

    public String getPluralName() {
        return pluralName;
    }

    public BigDecimal getPointsValue() {
        return unitValue;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPointsAmount(BigDecimal currencyAmount) {
        return unitValue.multiply(currencyAmount);
    }

    public BigDecimal getCurrencyAmount(BigDecimal points) {
        return points.divide(points, RoundingMode.HALF_EVEN);
    }
}
