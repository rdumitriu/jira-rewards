/*
 * Created on 4/10/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

import ro.agrade.jira.rewards.dao.RewardDataService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The reward service implementation
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardServiceImpl implements RewardService {
    private static final Log LOG = LogFactory.getLog(RewardServiceImpl.class);

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
        return rds.getRewardsForSprint(sprintId);
    }

    /**
     * Gets the specified rewards for the given sprint
     *
     * @param sprintId the id of the sprint
     * @return the list of rewards for the sprint
     */
    @Override
    public List<Reward> getRewardsForSprintAndIssue(long sprintId, long issueId) {
        return rds.getRewardsForSprintAndIssue(sprintId, issueId);
    }

    /**
     * Adds the reward
     *
     * @param reward the reward
     * @return the reward
     */
    @Override
    public Reward addReward(Reward reward) {
        verifySprintFor(reward);
        return rds.addReward(reward);
    }

    /**
     * Updates the reward
     *
     * @param reward the reward
     */
    @Override
    public void updateReward(Reward reward) {
        verifySprintFor(reward);
        rds.updateReward(reward);
    }

    private void verifySprintFor(Reward reward) {
        RewardSprint sprint = adminds.getRewardSprint(reward.getSprintId());
        if(sprint == null) {
            String msg = String.format("Reward sprint %d does not exist", reward.getSprintId());
            LOG.error(msg);
            throw new RewardException(msg);
        }
        if(sprint.getWhen() != null && reward.getDateEnds() != null &&
           sprint.getWhen().before(reward.getDateEnds())) {
            String msg = String.format("Reward sprint %d ends (%s) before the reward! (%s)",
                                        reward.getSprintId(), sprint.getWhen(), reward.getDateEnds());
            LOG.error(msg);
            throw new RewardException(msg);
        }
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
