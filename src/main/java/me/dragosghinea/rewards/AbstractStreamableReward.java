package me.dragosghinea.rewards;

import java.util.stream.Stream;

public interface AbstractStreamableReward {

    Stream<? extends AbstractReward> getRewardsStream();
}
