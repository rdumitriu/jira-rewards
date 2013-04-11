/*
 * Created on 4/10/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

import ro.agrade.jira.rewards.dao.RewardDataService;

/**
 * The reward service implementation
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardServiceImpl implements RewardService {
    private RewardDataService rds;
    private RewardAdminService adminds;

    public RewardServiceImpl(RewardDataService rds, RewardAdminService adminds) {
        this.rds = rds;
        this.adminds = adminds;
    }

    /**
     * Gets the specified reward
     *
     * @param id the id
     * @return the sprint
     */
    @Override
    public Reward getReward(long id) {
        return rds.getReward(id);
    }

    /**
     * Gets the specified rewards for the given sprint
     *
     * @param sprintId the id of the sprint
     * @return the list of rewards for the sprint
     */
    @Override
    public List<Reward> getRewardsForSprint(long sprintId) {
        return rds.getRewardForSprint(sprintId);
    }

    /**
     * Adds the reward
     *
     * @param reward the reward
     * @return the reward
     */
    @Override
    public Reward addReward(Reward reward) {
        //::TODO:: date of the reward should be < date of the sprint
        return rds.addReward(reward);
    }

    /**
     * Updates the reward
     *
     * @param reward the reward
     */
    @Override
    public void updateReward(Reward reward) {
        //::TODO:: date of the reward should be < date of the sprint
        rds.updateReward(reward);
    }

    /**
     * Removes the reward
     *
     * @param id the reward id
     */
    @Override
    public void deleteReward(long id) {
        rds.deleteReward(id);
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
        reward.setToUser(grantee);
        reward.setResolution(resolution);
        updateReward(reward);
        RewardSprint sprint = adminds.getRewardSprint(reward.getSprintId());
        adminds.addSprintGuest(sprint, grantee);
        return reward;
    }
}
