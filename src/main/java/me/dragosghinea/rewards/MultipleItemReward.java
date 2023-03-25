package me.dragosghinea.rewards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class MultipleItemReward  implements AbstractReward, AbstractMultipleRewards, AbstractStreamableReward{
    private List<AbstractReward> rewards = new ArrayList<>();
    private List<String> description;

    public MultipleItemReward(List<String> description){
        this.description = description;
    }

    @Override
    public List<String> getRewardDescription() {
        return this.description;
    }

    @Override
    public void setRewardDescription(List<String> newDescription) {
        this.description = newDescription;
    }

    @Override
    public AbstractReward getReward() {
        return this;
    }

    @Override
    public boolean triggerReward(UUID userID) {
        //to add
        return false;
    }

    @Override
    public Stream<? extends AbstractReward> getRewardsStream() {
        return rewards.stream();
    }

    @Override
    public boolean addReward(AbstractReward reward) {
        return rewards.add(reward);
    }

    @Override
    public boolean removeReward(AbstractReward reward) {
        return rewards.remove(reward);
    }

    @Override
    public boolean containsReward(AbstractReward reward) {
        return rewards.contains(reward);
    }
}
