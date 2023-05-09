package me.dragosghinea.exceptions;

import me.dragosghinea.model.abstracts.Reward;

public class IncompatibleRewardException extends RuntimeException {

    public IncompatibleRewardException(Class<? extends Reward> givenType, Class<? extends Reward> expectedType){
        super("Incompatible reward type. Expected: "+expectedType.toString()+" Found: "+givenType.toString());
    }
}
