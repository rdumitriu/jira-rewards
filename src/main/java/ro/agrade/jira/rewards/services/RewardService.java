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
     * Gets the specified reward
     * @param id the id
     * @return the sprint
     */
    public abstract Reward getReward(long id);

    /**
     * Gets the specified rewards for the given sprint
     * @param sprintId the id of the sprint
     * @return the list of rewards for the sprint
     */
    public abstract List<Reward> getRewardsForSprint(long sprintId);

    List<Reward> getRewardsForSprintAndIssue(long sprintId, long issueId);

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
}
