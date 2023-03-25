package me.dragosghinea.rewards;

import java.util.List;
import java.util.UUID;

public interface AbstractReward {

    List<String> getRewardDescription();

    void setRewardDescription(List<String> newDescription);

    AbstractReward getReward();

    boolean triggerReward(UUID userID);
}
