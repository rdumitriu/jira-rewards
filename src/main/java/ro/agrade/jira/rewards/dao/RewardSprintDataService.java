/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.dao;

import java.util.*;

import ro.agrade.jira.rewards.services.RewardSprint;
import ro.agrade.jira.rewards.services.SprintStatus;

/**
 * The reward sprint
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardSprintDataService {
    /**
     * Gets the specified sprint
     * @param id the id
     * @return the sprint
     */
    public abstract RewardSprint getRewardSprint(long id);

    /**
     * Gets a user reward sprint
     * @return the sprints added by an user
     */
    public abstract List<RewardSprint> getRewardSprints(String user,
                                                        Date validAt,
                                                        SprintStatus status);

    /**
     * Gets the specified sprints (all with the specified status)
     * @return the sprints
     */
    public abstract List<RewardSprint> getRewardSprints(SprintStatus status);

    /**
     * Adds a sprint
     * @param rs the reward sprint
     * @return the RewardType
     */
    public abstract RewardSprint addRewardSprint(RewardSprint rs);

    /**
     * Updates a sprint
     * @param rs the reward sprint
     */
    public abstract void updateRewardSprint(RewardSprint rs);

    /**
     * Removes a sprint
     * @param id the reward sprint id
     */
    public abstract void deleteRewardSprint(long id);
}
