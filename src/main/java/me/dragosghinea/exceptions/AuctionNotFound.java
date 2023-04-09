package me.dragosghinea.exceptions;

import lombok.Getter;

import java.util.UUID;

@Getter
public class AuctionNotFound extends RuntimeException {

    private final UUID auctionId;

    public AuctionNotFound(UUID auctionId){
        super("No auction found with the id "+auctionId);
        this.auctionId = auctionId;
    }

    public AuctionNotFound(){
        super("No auction found for the specified information");
        auctionId = null;
    }

}
