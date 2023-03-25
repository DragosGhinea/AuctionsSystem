package me.dragosghinea.rewards;

public interface AbstractMultipleRewards {

    boolean addReward(AbstractReward reward);

    boolean removeReward(AbstractReward reward);

    boolean containsReward(AbstractReward reward);
}
