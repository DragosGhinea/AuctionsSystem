package me.dragosghinea.services.impl;

import me.dragosghinea.exceptions.IncompatibleReward;
import me.dragosghinea.model.BlitzAuction;
import me.dragosghinea.model.BundleReward;
import me.dragosghinea.model.MultiReward;
import me.dragosghinea.model.SingleReward;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.model.abstracts.Reward;
import me.dragosghinea.model.enums.RewardType;
import me.dragosghinea.repository.AuctionRepository;
import me.dragosghinea.repository.RewardRepository;
import me.dragosghinea.repository.impl.postgres.AuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.BlitzAuctionRepositoryImpl;
import me.dragosghinea.repository.impl.postgres.RewardRepositoryImpl;
import me.dragosghinea.services.AuctionRewardsManagerService;

public class AuctionRewardsManagerServiceImpl implements AuctionRewardsManagerService {
    private final Auction auction;
    private final RewardRepository<Reward> rewardRepository = new RewardRepositoryImpl();
    private final AuctionRepository<Auction> auctionRepository = new AuctionRepositoryImpl();


    public AuctionRewardsManagerServiceImpl(Auction auction){
        this.auction = auction;
    }

    @Override
    public boolean setReward(Reward reward) {
        auctionRepository.setReward(auction.getAuctionId(), reward.getRewardId());
        auction.setReward(reward);

        return false;
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
            if(reward instanceof BundleReward bundleReward) {
                bundleReward.getRewards().forEach(this::addReward);
            }
            else {
                rewardRepository.addIncludedReward(auctionReward.getRewardId(), reward);
                auctionReward.getRewards().add(reward);
            }
            return true;
        }

        if(auction.getReward().getRewardType().equals(RewardType.SINGLE)) {
            MultiReward newReward = MultiReward.builder()
                    .rewardName(reward.getRewardName())
                    .rewardDescription(reward.getRewardDescription())
                    .build();

            rewardRepository.addReward(newReward);
            auction.setReward(newReward);
        }

        if(!(auction.getReward() instanceof MultiReward auctionReward))
            return false;

        rewardRepository.addIncludedReward(auction.getReward().getRewardId(), reward);

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
     * @throws IncompatibleReward if reward is not a bundle
     */
    @Override
    public boolean removeReward(Reward reward) {
        if(reward instanceof BundleReward bundleReward) {
            rewardRepository.removeIncludedReward(auction.getReward().getRewardId(), reward);
            return bundleReward.getRewards().remove(reward);
        }

        throw new IncompatibleReward(auction.getReward().getClass(), BundleReward.class);
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
        rewardRepository.addReward(newReward);
        auction.setReward(newReward);
        return true;
    }

    /**
     * Extracts the content of a bundle and places it
     * in a MultiReward
     *
     * @return
     * true if unpacked
     * @throws IncompatibleReward if stored reward is not a bundle
     */
    @Override
    public boolean transformUnbundle() throws IncompatibleReward {
        if(auction.getReward() instanceof BundleReward reward){
            MultiReward newReward = MultiReward.builder()
                    .rewardName(auction.getReward().getRewardName())
                    .rewardDescription(auction.getReward().getRewardDescription())
                    .build();

            rewardRepository.addReward(newReward);
            auction.setReward(newReward);
            reward.getRewards().forEach(this::addReward);
            return true;
        }

        throw new IncompatibleReward(auction.getReward().getClass(), BundleReward.class);
    }
}
