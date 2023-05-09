package me.dragosghinea.services;

import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.enums.AuctionState;

import java.util.Optional;
import java.util.UUID;

/**
 The AuctionService interface provides methods for managing auctions.
 This interface is sealed, and permits two implementations: BlitzAuctionService and LongAuctionService.
 */
public sealed interface AuctionService permits BlitzAuctionService, LongAuctionService {

    /**
     Starts the specified auction.
     @param auction the auction to start
     @return true if the auction was successfully started, false otherwise
     */
    boolean startAuction(Auction auction);

    /**
     Cancels the specified auction.
     @param auction the auction to cancel
     @return true if the auction was successfully cancelled, false otherwise
     */
    boolean cancelAuction(Auction auction);


    /**
     Retrieves the auction with the specified ID, if it exists.
     @param auctionId the ID of the auction to retrieve
     @return an Optional containing the auction with the specified ID, or empty if no such auction exists
     */
    Optional<? extends Auction> getAuctionById(UUID auctionId);


    /**
     Adds the specified auction to the service and database.
     @param auction the auction to add
     @return true if the auction was successfully added, false otherwise
     */
    boolean addAuction(Auction auction);


    /**
     Removes the specified auction from the service and database.
     @param auction the auction to remove
     @return true if the auction was successfully removed, false otherwise
     */
    boolean removeAuction(Auction auction);


    /**
     Removes the auction with the specified ID from the service and database.
     @param auctionId the ID of the auction to remove
     @return true if the auction was successfully removed, false otherwise
     */
    boolean removeAuction(UUID auctionId);


    /**
     Retrieves the actual state of the specified auction.
     @param auction the auction to retrieve the state of
     @return the actual state of the specified auction
     */
    AuctionState getActualState(Auction auction);


    /**
     Updates the state of the specified auction and returns the new state.
     @param auction the auction to update
     @return the new state of the specified auction
     */
    AuctionState updateAuctionState(Auction auction);


    /**
     Updates the specified auction.
     @param auction the auction to update
     @return true if the auction was successfully updated, false otherwise
     */
    boolean updateAuction(Auction auction);
}
