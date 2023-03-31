package me.dragosghinea.services.impl;

import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.MultiReward;
import me.dragosghinea.model.SingleReward;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.model.enums.RewardType;
import me.dragosghinea.services.RewardService;

public class RewardServiceImpl implements RewardService {
    private final Auction auction;

    public RewardServiceImpl(Auction auction){
        this.auction = auction;
    }

    /**
     * Adds a reward to the auction.
     * The method converts SingleReward of an auction to MultiReward.
     * It also unpacks any bundle reward received as parameter.
     *
     * @param reward
     * The reward to be added
     *
     * @return
     * false if the reward is not one of the classes
     * BundleReward | MultiReward | SingleReward
     */
    @Override
    public boolean addReward(Reward reward) {
        if(auction.getReward() instanceof BundleReward auctionReward){
            if(reward instanceof BundleReward bundleReward)
                bundleReward.getRewards().forEach(this::addReward);
            else
                auctionReward.getRewards().add(reward);
            return true;
        }

        if(auction.getReward().getRewardType().equals(RewardType.SINGLE)) {
            MultiReward newReward = MultiReward.builder()
                    .rewardName(reward.getRewardName())
                    .rewardDescription(reward.getRewardDescription())
                    .build();

            auction.setReward(newReward);
        }

        if(!(auction.getReward() instanceof MultiReward auctionReward))
            return false;



        if(reward instanceof SingleReward singleReward){
            auctionReward.getRewardInfo().add(singleReward.getRewardInfo());
        }
        else if(reward instanceof MultiReward multiReward){
            auctionReward.getRewardInfo().addAll(multiReward.getRewardInfo());
        }
        else if(reward instanceof BundleReward bundleReward){
            bundleReward.getRewards().forEach(this::addReward);
        }
        else{
            return false;
        }

        return true;
    }

    /**
     * Only works if the auction reward is a bundle
     *
     * @param reward
     * The reward to remove
     * @return
     * true if the reward was in the list,
     * false if the reward was not found in the list
     * or the auction reward is not a bundle
     */
    @Override
    public boolean removeReward(Reward reward) {
        if(reward instanceof BundleReward bundleReward)
            return bundleReward.getRewards().remove(reward);

        return false;
    }

    /**
     * Encapsulates SingleReward and MultiReward in a BundleReward
     *
     * @param name
     * The name of the new bundle
     *
     * @param description
     * The description of the new bundle
     *
     * @return
     * true if successful,
     * false if already a bundle
     */
    @Override
    public boolean transformBundle(String name, String description) {
        if(auction.getReward() instanceof BundleReward)
            return false;

        BundleReward newReward = BundleReward.builder()
                .rewardName(name)
                .rewardDescription(description)
                .build();
        newReward.getRewards().add(auction.getReward());
        auction.setReward(newReward);
        return true;
    }

    /**
     * Extracts the content of a bundle and places it
     * in a MultiReward
     *
     * @return
     * false if auction reward not a bundle
     */
    @Override
    public boolean transformUnbundle() {
        if(auction.getReward() instanceof BundleReward reward){
            MultiReward newReward = MultiReward.builder()
                    .rewardName(auction.getReward().getRewardName())
                    .rewardDescription(auction.getReward().getRewardDescription())
                    .build();

            auction.setReward(newReward);
            reward.getRewards().forEach(this::addReward);
            return true;
        }

        return false;
    }
}
