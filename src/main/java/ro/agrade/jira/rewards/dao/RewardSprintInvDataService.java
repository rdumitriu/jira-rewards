/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.dao;

import java.util.*;

/**
 * The guests
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardSprintInvDataService {

    /**
     * Gets all the guests for the given sprint
     * @param sprintId the sprint
     * @return the list of guests
     */
    public abstract Set<String> getGuestsForSprint(long sprintId);

    /**
     * Adds a guest
     * @param sprintId the sprint id
     * @param guestName the name of the guest
     */
    public abstract void addGuest(long sprintId, String guestName);

    /**
     * Removes all guests from the sprint
     * @param sprintId the sprint id
     */
    public void removeGuests(long sprintId);
}
