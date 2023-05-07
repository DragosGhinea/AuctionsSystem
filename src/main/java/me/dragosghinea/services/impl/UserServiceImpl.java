package me.dragosghinea.services.impl;

import lombok.RequiredArgsConstructor;
import me.dragosghinea.exceptions.AuctionNotFound;
import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.repository.UserRepository;
import me.dragosghinea.services.UserService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> getUserById(UUID uuid) {
        try {
            return userRepository.getUserById(uuid);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String credential){
        try {
            if (credential.contains("@")) {
                return userRepository.getUserByEmail(credential);
            } else {
                return userRepository.getUserByUsername(credential);
            }
        }catch(SQLException x){
            x.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> createUser(UserDetails userDetails) {
        try {
            User newUser = new User(userDetails);
            if (userRepository.addUser(newUser))
                return Optional.of(newUser);
            else
                return Optional.empty();
        }catch(SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean addUser(User user) {
        try {
            return userRepository.addUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeUser(User user) {
        try {
            return userRepository.removeUserById(user.getUserId());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addAuctionToUser(UUID userId, UUID auctionId) throws AuctionNotFound {
        try {
            return userRepository.addAuctionToUser(userId, auctionId);
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean removeAuctionFromUser(UUID userId, UUID auctionId) {
        try {
            return userRepository.removeAuctionFromUser(userId, auctionId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Auction> getUserAuctions(UUID userId) {
        try {
            return userRepository.getUserAuctions(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
