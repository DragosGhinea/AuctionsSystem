package me.dragosghinea.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class User {
    private UUID userId = UUID.randomUUID();

    @Setter
    private Wallet wallet = new Wallet(this);
    private UserAuctions userAuctions = new UserAuctions();

    private UserDetails userDetails;

    public User(){}

    /**
     * Creates a new user with the given details
     *
     * @param details
     * If the user id of details is null, it will be generated
     * If the user id of details is not null, it will be used for the user
     */
    public User(UserDetails details) {
        this.userDetails = (UserDetails) details.clone();
        if(userDetails.getUserId()==null)
            userDetails.setUserId(userId);
        else
            userId = userDetails.getUserId();
    }

    public User(UserDetails details, UserAuctions userAuctions){
        this.userDetails = (UserDetails) details.clone();
        if(userDetails.getUserId()==null)
            userDetails.setUserId(userId);
        else
            userId = userDetails.getUserId();
        this.userAuctions = userAuctions;
    }

    public void setUserDetails(UserDetails details){
        userDetails = (UserDetails) details.clone();
        userDetails.setUserId(userId);
    }

}
