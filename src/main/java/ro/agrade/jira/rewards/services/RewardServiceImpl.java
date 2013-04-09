/*
 * Created on 4/10/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

/**
 * The reward service implementation
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardServiceImpl implements RewardService {

    /**
     * Gets the specified reward
     *
     * @param id the id
     * @return the sprint
     */
    @Override
    public Reward getReward(long id) {
        return null;  //::TODO::
    }

    /**
     * Gets the specified rewards for the given sprint
     *
     * @param sprintId the id of the sprint
     * @return the list of rewards for the sprint
     */
    @Override
    public List<Reward> getRewardsForSprint(long sprintId) {
        return null;  //::TODO::
    }

    /**
     * Adds the reward
     *
     * @param reward the reward
     * @return the reward
     */
    @Override
    public Reward addReward(Reward reward) {
        return null;  //::TODO::
    }

    /**
     * Updates the reward
     *
     * @param reward the reward
     */
    @Override
    public void updateReward(Reward reward) {
        //::TODO::
    }

    /**
     * Removes the reward
     *
     * @param id the reward id
     */
    @Override
    public void deleteReward(long id) {
        //::TODO::
    }

    /**
     * Grant reward to someone
     *
     * @param reward     the reward
     * @param grantee    the person receiving the prize
     * @param resolution the resolution
     * @return the reward
     */
    @Override
    public Reward grantRewardTo(Reward reward, String grantee, String resolution) {
        return null;  //::TODO::
    }

    /**
     * Update specifics of the reward
     *
     * @param reward      the reward
     * @param quantity    the new quantity
     * @param summary     the summary
     * @param description the description
     * @param dateEnds    the end date of the Reward
     * @return the reward
     */
    @Override
    public Reward updateReward(Reward reward, RewardType type, long quantity, String summary, String description, Date dateEnds) {
        return null;  //::TODO::
    }

    /**
     * Update specifics of the reward
     *
     * @param reward the reward
     * @param sprint the new sprint
     * @return the reward
     */
    @Override
    public Reward changeRewardSprint(Reward reward, RewardSprint sprint) {
        return null;  //::TODO::
    }
}
