package me.dragosghinea.services.impl;

import me.dragosghinea.exceptions.AuctionNotFound;
import me.dragosghinea.model.User;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.repository.UserRepository;
import me.dragosghinea.repository.impl.postgres.UserRepositoryImpl;
import me.dragosghinea.services.BlitzAuctionService;
import me.dragosghinea.services.LongAuctionService;
import me.dragosghinea.services.UserService;

import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository = new UserRepositoryImpl();

    private BlitzAuctionService blitzAuctionService = new BlitzAuctionServiceImpl();
    private LongAuctionService longAuctionService = new LongAuctionServiceImpl();

    @Override
    public Optional<User> getUserById(UUID uuid) {
        return userRepository.getUserById(uuid);
    }

    @Override
    public Optional<User> getUserByUsernameOrEmail(String credential){
        if(credential.contains("@")) {
            return userRepository.getUserByEmail(credential);
        }
        else {
            return userRepository.getUserByUsername(credential);
        }
    }

    @Override
    public Optional<User> createUser(UserDetails userDetails) {
        User newUser = new User(userDetails);
        if(userRepository.addUser(newUser))
            return Optional.of(newUser);
        else
            return Optional.empty();
    }

    @Override
    public boolean addUser(User user) {
        return userRepository.addUser(user);
    }

    @Override
    public boolean removeUser(User user) {
        return userRepository.removeUserById(user.getUserId());
    }

    @Override
    public boolean addAuctionToUser(UUID userId, UUID auctionId) throws AuctionNotFound {
        if(longAuctionService.getAuctionById(auctionId).isEmpty() && blitzAuctionService.getAuctionById(auctionId).isEmpty())
            throw new AuctionNotFound(auctionId);
        return getUserById(userId)
                .map(user -> user.getUserAuctions().getAuctions().add(auctionId))
                .orElse(false);
    }

    @Override
    public boolean removeAuctionFromUser(UUID userId, UUID auctionId) {
        return getUserById(userId)
                .map(user -> user.getUserAuctions().getAuctions().remove(auctionId))
                .orElse(false);
    }
}
