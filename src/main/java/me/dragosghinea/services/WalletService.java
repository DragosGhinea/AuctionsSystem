package me.dragosghinea.services;

import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;

/**
 * The WalletService interface provides methods for managing a user's wallet.
 */
public interface WalletService {

    /**
     * Adds the given amount of points to the user's wallet.
     *
     * @param points the amount of points to add
     * @return true if the operation was successful, false otherwise
     */
    boolean addPointsToWallet(BigDecimal points);

    /**
     * Removes the given amount of points from the user's wallet.
     *
     * @param points the amount of points to remove
     * @return true if the operation was successful, false otherwise
     */
    boolean removePointsFromWallet(BigDecimal points);

    /**
     * Sets the user's points balance to the given amount.
     *
     * @param points the new points balance
     * @return true if the operation was successful, false otherwise
     */
    boolean setPointsBalance(BigDecimal points);

    /**
     * Returns the user's current points balance.
     *
     * @return the user's points balance
     */
    BigDecimal getPointsBalance();

    /**
     * Sets the user's preferred currency.
     *
     * @param currency the new preferred currency
     * @return true if the operation was successful, false otherwise
     */
    boolean setPreferredCurrency(Currency currency);
}
