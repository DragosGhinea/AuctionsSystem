package me.dragosghinea.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserNotFoundException extends RuntimeException{

    private final UUID userId;

    public UserNotFoundException(UUID userId){
        super("No user found with the id "+userId);
        this.userId = userId;
    }

    public UserNotFoundException(){
        super("No user found for the specified information");
        userId = null;
    }
}
