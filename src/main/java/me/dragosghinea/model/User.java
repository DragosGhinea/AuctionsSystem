package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {
    private UUID userId;

    @Setter
    private Wallet wallet;
    private UserAuctions userAuctions = new UserAuctions();

    private UserDetails userDetails;

    public User(UserDetails details) {
        this.userDetails = details;
    }

    public User(UserDetails details, UserAuctions userAuctions){
        this.userDetails = details;
        this.userAuctions = userAuctions;
    }

    void setUserDetails(UserDetails details){
        userDetails = details;
    }

}
