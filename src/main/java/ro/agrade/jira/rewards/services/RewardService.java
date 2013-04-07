/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

/**
 * The reward interface
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardService {

    /**
     * Gets the specified sprint
     * @param id the id
     * @return the sprint
     */
    public abstract Reward getReward(long id);

    /**
     * Adds the reward
     * @param reward the reward
     * @return the reward
     */
    public abstract Reward addReward(Reward reward);

    /**
     * Updates the reward
     * @param reward the reward
     */
    public abstract void updateReward(Reward reward);

    /**
     * Removes the reward
     * @param id the reward id
     */
    public abstract void deleteReward(long id);

    /**
     * Grant reward to someone
     * @param reward the reward
     * @param grantee the person receiving the prize
     * @param resolution the resolution
     * @return the reward
     */
    public abstract Reward grantRewardTo(Reward reward, String grantee, String resolution);

    /**
     * Update specifics of the reward
     * @param reward the reward
     * @param quantity the new quantity
     * @param summary the summary
     * @param description the description
     * @param dateEnds the end date of the Reward
     * @return the reward
     */
    public abstract Reward updateReward(Reward reward, RewardType type,
                                        long quantity,
                                        String summary, String description,
                                        Date dateEnds);

    /**
     * Update specifics of the reward
     * @param reward the reward
     * @param sprint the new sprint
     * @return the reward
     */
    public abstract Reward changeRewardSprint(Reward reward, RewardSprint sprint);
}
