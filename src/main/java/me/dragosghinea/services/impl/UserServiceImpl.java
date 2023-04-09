package me.dragosghinea.services.impl;

import me.dragosghinea.exceptions.AuctionNotFound;
import me.dragosghinea.model.UserDetails;
import me.dragosghinea.model.Wallet;
import me.dragosghinea.services.BlitzAuctionService;
import me.dragosghinea.services.LongAuctionService;
import me.dragosghinea.services.UserService;
import me.dragosghinea.model.User;

import java.util.*;
import java.util.function.Predicate;

public class UserServiceImpl implements UserService {

    private static final Map<UUID, User> users = new HashMap<>();
    private BlitzAuctionService blitzAuctionService = new BlitzAuctionServiceImpl();
    private LongAuctionService longAuctionService = new LongAuctionServiceImpl();

    @Override
    public Optional<User> getUserById(UUID uuid) {
        return Optional.ofNullable(users.getOrDefault(uuid, null));
    }

    @Override
    public Optional<User> findFirstUser(Predicate<User> condition) {
        return users.values().stream().filter(condition).findFirst();
    }

    @Override
    public List<User> findAllUsers(Predicate<User> condition) {
        return users.values().stream().filter(condition).toList();
    }

    @Override
    public Optional<User> createUser(UserDetails userDetails) {
        Predicate<UserDetails> isSimilar = (usrDetails) -> {
            if (usrDetails.getEmail().equalsIgnoreCase(userDetails.getEmail()))
                return true;
            if (usrDetails.getUsername().equalsIgnoreCase(userDetails.getUsername()))
                return true;

            return false;
        };

        if (users.values().stream().map(User::getUserDetails).anyMatch(isSimilar)) {
            return Optional.empty();
        }
        User newUser = new User(userDetails);
        newUser.setWallet(new Wallet(newUser));
        users.put(newUser.getUserId(), newUser);
        return Optional.of(newUser);
    }

    @Override
    public boolean addUser(User user) {
        return users.putIfAbsent(user.getUserId(), user) == null;
    }

    @Override
    public boolean removeUser(User user) {
        return users.remove(user.getUserId(), user);
    }

    @Override
    public List<User> removeAllUsers(Predicate<User> condition) {
        Iterator<Map.Entry<UUID, User>> entriesIterator = users.entrySet().iterator();
        List<User> removed = new ArrayList<>();
        while (entriesIterator.hasNext()) {
            Map.Entry<UUID, User> entry = entriesIterator.next();
            if (condition.test(entry.getValue())) {
                removed.add(entry.getValue());
                entriesIterator.remove();
            }
        }
        return removed;
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
