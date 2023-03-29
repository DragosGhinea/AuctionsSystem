package me.dragosghinea.services;

import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public interface UserService {

    Optional<User> getUserById(UUID uuid);

    Optional<User> findFirstUser(Predicate<User> condition);

    List<User> findAllUsers(Predicate<User> condition);

    Optional<User> createUser(UserDetails userDetails);

    boolean addUser(User user);

    boolean removeUser(User user);

    List<User> removeAllUsers(Predicate<User> condition);
}
