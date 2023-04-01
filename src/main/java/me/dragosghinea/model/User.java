package me.dragosghinea.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {
    private UUID userId = UUID.randomUUID();

    @Setter
    private Wallet wallet;
    private UserAuctions userAuctions = new UserAuctions();

    private UserDetails userDetails;

    public User(UserDetails details) {
        this.userDetails = (UserDetails) details.clone();
    }

    public User(UserDetails details, UserAuctions userAuctions){
        this.userDetails = (UserDetails) details.clone();
        userDetails.setUserId(userId);
        this.userAuctions = userAuctions;
    }

    public void setUserDetails(UserDetails details){
        userDetails = (UserDetails) details.clone();
        userDetails.setUserId(userId);
    }

}
