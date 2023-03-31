package me.dragosghinea.services;

import me.dragosghinea.model.abstracts.Reward;

public interface RewardService {

    /** A method to add a reward to an auction's reward.
     * Based on the reward type already stored in the auction
     * and the type of reward passed as argument
     * different action will be taken.
     *
     * For bundled rewards, the parameter is added to the reward list
     * whole. Exception to the rule is a bundled parameter, for which
     * each inner reward will be added to this bundle.
     *
     *
     * @param reward
     * The reward to be added
     *
     * @return
     * true if the reward was added successfully,
     * false if for whatever reasons the reward is rejected
     */
    boolean addReward(Reward reward);

    /**
     * A method to remove a reward from the list
     * based on a comparation with the given parameter.
     * Different implementations may have different behaviours.
     *
     * @param reward
     * The reward to remove
     * @return
     * true if the reward was removed successfully,
     * false otherwise
     */
    boolean removeReward(Reward reward);

    /**
     * Converts the current reward to a bundle
     *
     * @param name
     * The name of the new bundle
     *
     * @param description
     * The description of the new bundle
     *
     * @return
     * true if the bundle was created
     * false if rejected for whatever reasons
     */
    boolean transformBundle(String name, String description);

    /**
     * Converts the bundle to either a multi or single reward.
     * @return
     * true if the conversion was successful
     * false if rejected
     */
    boolean transformUnbundle();
}
