package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {
    private UUID userId;
    @Setter
    private Wallet wallet;
    private UserAuctions userAuctions;

    private UserDetails userDetails;

    public User(UserDetails details) {
        this.userDetails = details;
    }

    void setUserDetails(UserDetails details){
        userDetails = details;
    }

}
