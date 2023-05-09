package me.dragosghinea.services;

import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * The UserService interface provides methods for managing users and their auctions.
 */
public interface UserService {

    /**
     * Retrieves a User by their UUID.
     *
     * @param uuid the UUID of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional otherwise
     */
    Optional<User> getUserById(UUID uuid);

    /**
     * Retrieves a User by their username or email.
     *
     * @param credential the username or email of the User to retrieve
     * @return an Optional containing the User if found, or an empty Optional otherwise
     */
    Optional<User> getUserByUsernameOrEmail(String credential);

    /**
     * Creates a new User with the provided user details.
     *
     * @param userDetails the details of the User to create
     * @return an Optional containing the created User if successful, or an empty Optional otherwise
     */
    Optional<User> createUser(UserDetails userDetails);

    /**
     * Adds a User to the system.
     *
     * @param user the User to add
     * @return true if the User was added successfully, false otherwise
     */
    boolean addUser(User user);

    /**
     * Removes a User from the system.
     *
     * @param user the User to remove
     * @return true if the User was removed successfully, false otherwise
     */
    boolean removeUser(User user);

    /**
     * Adds an Auction to a User's list of auctions.
     *
     * @param userId the UUID of the User
     * @param auctionId the UUID of the Auction to add
     * @return true if the Auction was added successfully, false otherwise
     */
    boolean addAuctionToUser(UUID userId, UUID auctionId);

    /**
     * Removes an Auction from a User's list of auctions.
     *
     * @param userId the UUID of the User
     * @param auctionId the UUID of the Auction to remove
     * @return true if the Auction was removed successfully, false otherwise
     */
    boolean removeAuctionFromUser(UUID userId, UUID auctionId);

    /**
     * Retrieves all Auctions associated with a User.
     *
     * @param userId the UUID of the User
     * @return a List of Auctions associated with the User
     */
    List<Auction> getUserAuctions(UUID userId);
}
