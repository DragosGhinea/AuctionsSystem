package me.dragosghinea.repository;

import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserRepository {

    boolean addUser(User user) throws SQLException;

    boolean removeUserById(UUID id) throws SQLException;

    Optional<User> getUserById(UUID id) throws SQLException;

    Optional<User> getUserByEmail(String email) throws SQLException;

    Optional<User> getUserByUsername(String username) throws SQLException;

    List<Auction> getUserAuctions(UUID userId) throws SQLException;

    Set<UUID> getUserAuctionIds(UUID userId) throws SQLException;

    boolean addAuctionToUser(UUID userId, UUID auctionId) throws SQLException;

    boolean removeAuctionFromUser(UUID userId, UUID auctionId) throws SQLException;
}
