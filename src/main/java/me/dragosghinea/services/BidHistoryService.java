package me.dragosghinea.services;

import me.dragosghinea.exceptions.BidTooLowException;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Bid;
import me.dragosghinea.model.enums.Currency;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The BidHistoryService interface provides methods for managing the bid history of an auction.
 */
public interface BidHistoryService {

    /**
     * Returns the auction associated with this bid history.
     *
     * @return the auction associated with this bid history
     */
    Auction getAuction();

    /**
     * Returns an optional containing the latest bid in the bid history, or an empty optional if no bids have been made yet.
     *
     * @return an optional containing the latest bid in the bid history, or an empty optional if no bids have been made yet
     */
    Optional<Bid> getLatestBid();

    /**
     * Returns an optional containing the highest bid made by the specified user, or an empty optional if the user has not made any bids.
     *
     * @param userId the ID of the user whose highest bid is to be retrieved
     * @return an optional containing the highest bid made by the specified user, or an empty optional if the user has not made any bids
     */
    Optional<Bid> getHighestBid(UUID userId);

    /**
     * Returns a list containing the highest bid made by each user that has participated in the auction, in chronological order.
     *
     * @return A List containing the highest bid made by each user that participated in the auction, ordered chronologically.
     */
    List<Bid> getBidsDistinctUsers();

    /**
     * Adds a bid to the bid history.
     *
     * @param userId     The ID of the user making the bid.
     * @param points     The number of points being bid.
     * @param takePoints Whether to take the points from the user's account (if true) or just use them for the bid (if false).
     * @return true if the bid was successfully added, false otherwise.
     * @throws BidTooLowException if the bid is lower than the current highest bid or the minimum bid amount allowed.
     */
    boolean addBid(UUID userId, BigDecimal points, boolean takePoints) throws BidTooLowException;

    /**
     * Adds a bid to the bid history.
     *
     * @param userId     The ID of the user making the bid.
     * @param amount     The amount being bid.
     * @param currency   The currency of the bid amount.
     * @param takePoints Whether to take the points from the user's account (if true) or just use them for the bid (if false).
     * @return true if the bid was successfully added, false otherwise.
     * @throws BidTooLowException if the bid is lower than the current highest bid or the minimum bid amount allowed.
     */
    boolean addBid(UUID userId, BigDecimal amount, Currency currency, boolean takePoints) throws BidTooLowException;

    /**
     * Removes the latest bid made by a given user.
     *
     * @param userId       The ID of the user whose latest bid should be removed.
     * @param returnPoints Whether to return the points used for the bid to the user's account (if true) or not (if false).
     * @return true if the bid was successfully removed, false otherwise.
     */
    boolean removeLatestBid(UUID userId, boolean returnPoints);

    /**
     * Removes all bids made by a given user.
     *
     * @param userId       The ID of the user whose bids should be removed.
     * @param returnPoints Whether to return the points used for the bids to the user's account (if true) or not (if false).
     * @return true if the bids were successfully removed, false otherwise.
     */
    boolean removeAllBids(UUID userId, boolean returnPoints);
}