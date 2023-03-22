package me.dragosghinea.wallet;

import me.dragosghinea.user.User;
import me.dragosghinea.wallet.currencies.Currency;
import me.dragosghinea.wallet.currencies.CurrencySet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultWallet implements Wallet {

    private List<Currency> availableCurrencies = new ArrayList<Currency>();
    private User owner;
    private Currency preferredCurrency;

    private BigDecimal points = BigDecimal.valueOf(0);

    public DefaultWallet(User owner){
        this.owner = owner;
        Collections.addAll(availableCurrencies, CurrencySet.values());
        preferredCurrency = CurrencySet.PNT;
    }

    public List<Currency> getAvailableCurrencies() {
        return availableCurrencies;
    }

    public Currency getPreferredCurrency() {
        return preferredCurrency;
    }

    public User getOwner() {
        return owner;
    }

    public void addCurrency(Currency currency) {
        availableCurrencies.add(currency);
    }

    public Boolean removeCurrency(Currency currency) {
        return availableCurrencies.remove(currency);
    }

    public BigDecimal getPointsAmount() {
        return points;
    }

    public Boolean hasPoints(BigDecimal amount) {
        return points.compareTo(amount) >= 0;
    }

    public synchronized Boolean sellPoints(BigDecimal currencyAmount, Currency currencyTo) {
        BigDecimal points = currencyTo.getPointsAmount(currencyAmount);
        if(!hasPoints(points))
            return false;
        this.points = this.points.subtract(points);
        return true;
    }

    public synchronized Boolean buyPoints(BigDecimal currencyAmount, Currency currencyFrom) {
        //some external logic of paying for the points
        //...
        //end of some external logic
        BigDecimal points = currencyFrom.getPointsAmount(currencyAmount);
        this.points = this.points.add(points);
        return true;
    }


    public Boolean buyPoints(BigDecimal currencyAmount) {
        return buyPoints(currencyAmount, preferredCurrency);
    }

    public Boolean sellPoints(BigDecimal currencyAmount) {
        return sellPoints(currencyAmount, preferredCurrency);
    }
}
