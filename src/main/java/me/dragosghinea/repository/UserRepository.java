package me.dragosghinea.repository;

import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    boolean addUser(User user);

    boolean removeUserById(UUID id);

    Optional<User> getUserById(UUID id);

    Optional<User> getUserByEmail(String email);

    Optional<User> getUserByUsername(String username);
}
