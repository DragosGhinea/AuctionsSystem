package me.dragosghinea.rewards;

import java.util.List;
import java.util.UUID;

public class SingleItemReward implements AbstractReward{
    private String reward;

    private List<String> description;

    public SingleItemReward(String reward, List<String> description){
        this.reward = reward;
        this.description = description;
    }

    @Override
    public AbstractReward getReward() {
        return this;
    }

    @Override
    public List<String> getRewardDescription() {
        return description;
    }

    @Override
    public void setRewardDescription(List<String> newDescription) {
        description = newDescription;
    }

    @Override
    public boolean triggerReward(UUID userID) {
        return false;
    }
}
