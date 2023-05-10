package me.dragosghinea.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AuctionNotFoundException extends RuntimeException {

    private final UUID auctionId;

    public AuctionNotFoundException(UUID auctionId){
        super("No auction found with the id "+auctionId);
        this.auctionId = auctionId;
    }

    public AuctionNotFoundException(){
        super("No auction found for the specified information");
        auctionId = null;
    }

}
