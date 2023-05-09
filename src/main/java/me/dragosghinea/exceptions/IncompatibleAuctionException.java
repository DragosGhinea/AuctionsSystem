package me.dragosghinea.exceptions;

import me.dragosghinea.model.abstracts.Auction;

public class IncompatibleAuctionException extends RuntimeException {

    public IncompatibleAuctionException(Class<? extends Auction> givenType, Class<? extends Auction> expectedType){
        super("Incompatible auction type. Expected: "+expectedType.toString()+" Given: "+givenType.toString());
    }
}
