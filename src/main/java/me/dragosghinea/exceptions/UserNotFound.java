package me.dragosghinea.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFound extends RuntimeException{

    private final UUID userId;

    public UserNotFound(UUID userId){
        super("No user found with the id "+userId);
        this.userId = userId;
    }

    public UserNotFound(){
        super("No user found for the specified information");
        userId = null;
    }
}
