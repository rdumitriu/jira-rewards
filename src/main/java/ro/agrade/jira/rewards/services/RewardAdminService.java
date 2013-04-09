/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

/**
 * The service used to do the administration of the sprints
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardAdminService {

    /**
     * Gets the types
     * @param id the id
     * @return the RewardType
     */
    public abstract RewardType getRewardType(long id);

    /**
     * Gets all the reward types
     * @return the reward
     */
    public abstract List<RewardType> getRewardTypes();

    /**
     * Creates a reward type
     * @param rt the structure containing the name of the reward, e.g. 'Beer', pl 'Beers'
     * @return the reward type, created
     */
    public abstract RewardType addRewardType(RewardType rt);


    /**
     * Gets the specified sprint
     * @param id the id
     * @return the sprint
     */
    public abstract RewardSprint getRewardSprint(long id);

    /**
     * Gets my sprints
     * @return the sprints I'm invited or I started, including the proposed ones
     */
    public abstract List<RewardSprint> getUserRewardSprints(String user, Date validAt);

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
     * Updates a sprint status
     * @param rs the reward sprint
     * @return the reward sprint
     */
    public abstract RewardSprint advanceSprintStatus(RewardSprint rs);

    /**
     * Adds a guest to the sprint
     * @param rs the reward sprint
     * @return the reward sprint
     */
    public abstract RewardSprint addSprintGuest(RewardSprint rs, String guest);

    /**
     * Removes a guest from the sprint. You should not be able to remove a reward
     * linked person.
     * @param rs the reward sprint
     * @return the reward sprint
     */
    public abstract RewardSprint removeSprintGuest(RewardSprint rs, String guest);
}
