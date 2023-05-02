package me.dragosghinea.services;

import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    Optional<User> getUserById(UUID uuid);
    Optional<User> getUserByUsernameOrEmail(String credential);

    Optional<User> createUser(UserDetails userDetails);

    boolean addUser(User user);

    boolean removeUser(User user);

    boolean addAuctionToUser(UUID userId, UUID auctionId);

    boolean removeAuctionFromUser(UUID userId, UUID auctionId);

    List<Auction> getUserAuctions(UUID userId);
}
