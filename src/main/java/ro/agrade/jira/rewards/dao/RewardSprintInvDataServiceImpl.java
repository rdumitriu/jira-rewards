/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.dao;

import java.util.*;

import org.ofbiz.core.entity.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Guests, implementation
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardSprintInvDataServiceImpl implements RewardSprintInvDataService {
    private static final Log LOG = LogFactory.getLog(RewardSprintInvDataServiceImpl.class);
    private final GenericDelegator delegator;

    /**
     * Constructor
     */
    public RewardSprintInvDataServiceImpl() {
        this.delegator = GenericDelegator.getGenericDelegator("default");
    }

    /**
     * Adds a guest
     * @param sprintId the sprint id
     * @param guestName the name of the guest
     */
    @Override
    public void addGuest(long sprintId, String guestName) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ID_FIELD, sprintId);
            map.put(GUEST_FIELD, guestName);
            GenericValue gv = delegator.makeValue(ENTITY, map);
            delegator.create(gv);
        } catch (GenericEntityException e) {
            String msg = String.format("Could not create reward sprint guests (id=%d) ?!?", sprintId);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Removes all guests from the sprint
     * @param sprintId the sprint id
     */
    @Override
    public void removeGuests(long sprintId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ID_FIELD, sprintId);
            delegator.removeByAnd(ENTITY, map);
        } catch (GenericEntityException e) {
            String msg = String.format("Could not create reward sprint guests (id=%d) ?!?", sprintId);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Gets all the guests for the given sprint
     * @param sprintId the sprint
     * @return the list of guests
     */
    @Override
    public List<String> getGuestsForSprint(long sprintId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(ID_FIELD, sprintId);
            List<GenericValue> list = delegator.findByAnd(ENTITY, map);
            List<String> ret = new ArrayList<String>();
            if(list != null) {
                for(GenericValue gv : list) {
                    ret.add((String)gv.get(GUEST_FIELD));
                }
            }
            return ret;
        } catch (GenericEntityException e) {
            String msg = String.format("Could not load reward sprint guests (id=%d) ?!?", sprintId);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    private static final String ENTITY = "RWDSPRINTGST";
    private static final String ID_FIELD = "s_id";
    private static final String GUEST_FIELD = "i_who";
}
