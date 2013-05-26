/*
 * Created on 4/9/13
 */
package ro.agrade.jira.rewards.dao;

import java.util.*;

import ro.agrade.jira.rewards.services.Reward;

/**
 * The reward data service
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardDataService {

    /**
     * Gets the specified reward
     * @param id the id
     * @return the sprint
     */
    public abstract Reward getReward(long id);

    /**
     * Gets the specified rewards for the given sprint
     *
     * @param sprintId the id of the sprint
     * @return the list of rewards for the sprint
     */
    public abstract List<Reward> getRewardsForSprint(long sprintId);

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
}
