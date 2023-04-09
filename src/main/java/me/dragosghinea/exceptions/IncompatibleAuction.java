package me.dragosghinea.exceptions;

import me.dragosghinea.model.abstracts.Auction;

public class IncompatibleAuction extends RuntimeException {

    public IncompatibleAuction(Class<? extends Auction> givenType, Class<? extends Auction> expectedType){
        super("Incompatible auction type. Expected: "+expectedType.toString()+" Given: "+givenType.toString());
    }
}
