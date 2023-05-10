package me.dragosghinea.mapper;

import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.MultiReward;
import me.dragosghinea.model.SingleReward;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.model.enums.RewardType;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class RewardMapper {

    private final static RewardMapper instance = new RewardMapper();

    private RewardMapper(){};

    public static RewardMapper getInstance() {
        return instance;
    }


    private SingleReward directMapToSingleReward(ResultSet set) throws SQLException {
        return SingleReward.builder()
                .rewardId(set.getObject("reward_id", UUID.class))
                .rewardName(set.getString("reward_name"))
                .rewardDescription(set.getString("reward_description"))
                .rewardType(RewardType.valueOf(set.getString("reward_type")))
                .rewardInfo(set.getString("reward_info"))
                .build();
    }

    private MultiReward directMapToMultiReward(ResultSet set) throws SQLException {
        return MultiReward.builder()
                .rewardId(set.getObject("reward_id", UUID.class))
                .rewardName(set.getString("reward_name"))
                .rewardDescription(set.getString("reward_description"))
                .rewardType(RewardType.valueOf(set.getString("reward_type")))
                .rewardInfo(new ArrayList<>(Arrays.asList(set.getString("reward_info").split("\t"))))
                .build();
    }

    private BundleReward directMapToBundleReward(ResultSet bundleReward, ResultSet rewardsData) throws SQLException {
        List<Reward> rewards = new ArrayList<>();
        BundleReward toReturn = BundleReward.builder()
                .rewardId(bundleReward.getObject("reward_id", UUID.class))
                .rewardName(bundleReward.getString("reward_name"))
                .rewardDescription(bundleReward.getString("reward_description"))
                .rewardType(RewardType.valueOf(bundleReward.getString("reward_type")))
                .rewards(rewards)
                .build();

        while(rewardsData.next()){
            if(rewardsData.getString("reward_type").equals("SINGLE"))
                rewards.add(directMapToSingleReward(rewardsData));
            else if(rewardsData.getString("reward_type").equals("MULTI"))
                rewards.add(directMapToMultiReward(rewardsData));
        }

        return toReturn;
    }

    public SingleReward mapToSingleReward(ResultSet set) throws SQLException {
        if(!set.next())
            return null;

        return directMapToSingleReward(set);
    }

    public MultiReward mapToMultiReward(ResultSet set) throws SQLException {
        if(!set.next())
            return null;

        return directMapToMultiReward(set);
    }

    public BundleReward mapToBundleReward(ResultSet bundleReward, ResultSet rewardData) throws SQLException {
        if(!bundleReward.next())
            return null;

        return directMapToBundleReward(bundleReward, rewardData);
    }
}
