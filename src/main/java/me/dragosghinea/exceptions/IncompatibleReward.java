package me.dragosghinea.exceptions;

import me.dragosghinea.model.abstracts.Reward;

public class IncompatibleReward extends RuntimeException {

    public IncompatibleReward(Class<? extends Reward> givenType, Class<? extends Reward> expectedType){
        super("Incompatible reward type. Expected: "+expectedType.toString()+" Found: "+givenType.toString());
    }
}
