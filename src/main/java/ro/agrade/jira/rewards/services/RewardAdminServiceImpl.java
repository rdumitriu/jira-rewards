/*
 * Created on 4/10/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

import ro.agrade.jira.rewards.dao.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Implementation for our reward service
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardAdminServiceImpl implements RewardAdminService {
    private static final Log LOG = LogFactory.getLog(RewardDataServiceImpl.class);

    private RewardSprintDataService sprintDS;
    private RewardSprintInvDataService sprintGstDS;
    private RewardTypeDataService typeDS;

    public RewardAdminServiceImpl(RewardSprintDataService sprintDS,
                                  RewardSprintInvDataService sprintGstDS,
                                  RewardTypeDataService typeDS) {
        this.sprintDS = sprintDS;
        this.sprintGstDS = sprintGstDS;
        this.typeDS = typeDS;
    }

    /**
     * Gets the types
     *
     * @param id the id
     * @return the RewardType
     */
    @Override
    public RewardType getRewardType(long id) {
        return typeDS.getRewardType(id);
    }

    /**
     * Gets all the reward types
     *
     * @return the reward
     */
    @Override
    public List<RewardType> getRewardTypes() {
        return typeDS.getRewardTypes();
    }

    /**
     * Creates a reward type
     *
     * @param rt the reward type
     * @return the reward type, created
     */
    @Override
    public RewardType addRewardType(RewardType rt) {
        return typeDS.addRewardType(rt);
    }

    /**
     * Gets the specified sprint
     *
     * @param id the id
     * @return the sprint
     */
    @Override
    public RewardSprint getRewardSprint(long id) {
        RewardSprint rs = sprintDS.getRewardSprint(id);
        augmentRewardSprint(rs);
        return rs;
    }

    /**
     * Gets my sprints
     *
     * @return the sprints I'm invited or I started, including the proposed ones
     */
    @Override
    public List<RewardSprint> getUserRewardSprints(String user, Date validAt) {
        List<RewardSprint> sprints = sprintDS.getRewardSprints(user, validAt, SprintStatus.ACTIVE);
        augmentRewardSprint(sprints);
        return sprints;
    }

    /**
     * Gets the specified sprints (all with the specified status)
     *
     * @return the sprints
     */
    @Override
    public List<RewardSprint> getRewardSprints(SprintStatus status) {
        List<RewardSprint> rsl = sprintDS.getRewardSprints(status);
        augmentRewardSprint(rsl);
        return rsl;
    }

    /**
     * Adds a sprint
     *
     * @param rs the reward sprint
     * @return the RewardType
     */
    @Override
    public RewardSprint addRewardSprint(RewardSprint rs) {
        rs = sprintDS.addRewardSprint(rs);
        if(rs.getGuests() != null) {
            for(String g : rs.getGuests()) {
                sprintGstDS.addGuest(rs.getId(), g);
            }
        }
        return rs;
    }

    /**
     * Updates a sprint
     *
     * @param rs the reward sprint
     */
    @Override
    public void updateRewardSprint(RewardSprint rs) {
        sprintDS.updateRewardSprint(rs);
        sprintGstDS.removeGuests(rs.getId());
        if(rs.getGuests() != null) {
            for(String g : rs.getGuests()) {
                sprintGstDS.addGuest(rs.getId(), g);
            }
        }
    }

    /**
     * Updates a sprint status
     *
     * @param rs the reward sprint
     * @return the reward sprint
     */
    @Override
    public RewardSprint advanceSprintStatus(RewardSprint rs) {
        SprintStatus newStat = rs.getStatus();
        switch(rs.getStatus()) {
            case PROPOSED:
                newStat = SprintStatus.ACTIVE;
                break;
            case ACTIVE:
                newStat = SprintStatus.PROPOSED;
                break;
            case EXECUTED:
                LOG.warn("You drink once from the Amalthea's horn. That's enough");
                break;
        }
        rs.setStatus(newStat);
        updateRewardSprint(rs);
        return rs;
    }

    /**
     * Adds a guest to the sprint
     *
     * @param rs the reward sprint
     * @return the reward sprint
     */
    @Override
    public RewardSprint addSprintGuest(RewardSprint rs, String guest) {
        if(rs.getGuests() == null) {
            rs.setGuests(new ArrayList<String>());
        }
        rs.getGuests().add(guest);
        updateRewardSprint(rs);
        return rs;
    }

    /**
     * Removes a guest from the sprint. You should not be able to remove a reward
     * linked person.
     *
     * @param rs the reward sprint
     * @return the reward sprint
     */
    @Override
    public RewardSprint removeSprintGuest(RewardSprint rs, String guest) {
        if(rs.getGuests() == null) {
            return rs;
        }
        //::TODO:: check that is not linked to a reward
        rs.getGuests().remove(guest);
        updateRewardSprint(rs);
        return rs;
    }

    private void augmentRewardSprint(List<RewardSprint> list) {
        if(list != null) {
            for(RewardSprint rs : list) {
                augmentRewardSprint(rs);
            }
        }
    }

    private void augmentRewardSprint(RewardSprint rs) {
        if(rs != null) {
            rs.setGuests(sprintGstDS.getGuestsForSprint(rs.getId()));
        }
    }
}
