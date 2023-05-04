package me.dragosghinea.repository;

import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {

    boolean addUser(User user);

    boolean removeUserById(UUID id);

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);

    List<Auction> getUserAuctions(UUID userId);

    Set<UUID> getUserAuctionIds(UUID userId);

    boolean addAuctionToUser(UUID userId, UUID auctionId);

    boolean removeAuctionFromUser(UUID userId, UUID auctionId);
}
