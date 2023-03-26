package me.dragosghinea.user;

import me.dragosghinea.user.auctions.UserAuctions;
import me.dragosghinea.user.details.UserDetails;
import me.dragosghinea.wallet.Wallet;

import java.util.UUID;

public class User {

    UUID userId;
    UserDetails details;
    UserAuctions userAuctions;
    Wallet wallet;

    public User(){

    }

    public UUID getUserId() {
        return userId;
    }

    public UserDetails getDetails() {
        return (UserDetails) details.clone();
    }

    public void setDetails(UserDetails details){
        this.details = details;
    }
}
