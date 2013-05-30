/*
 * Created on 4/9/13
 */
package ro.agrade.jira.rewards.dao;

import java.sql.Timestamp;
import java.util.*;

import org.ofbiz.core.entity.*;

import ro.agrade.jira.rewards.services.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The implementation for rewards
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardDataServiceImpl implements RewardDataService {
    private static final Log LOG = LogFactory.getLog(RewardDataServiceImpl.class);
    private final GenericDelegator delegator;

    public RewardDataServiceImpl() {
        this.delegator = GenericDelegator.getGenericDelegator("default");
    }

    /**
     * Gets the specified reward
     *
     * @param id the id
     * @return the sprint
     */
    @Override
    public Reward getReward(long id) {
        try {
            GenericValue ret = delegator.findByPrimaryKey(ENTITY, makePk(id));
            return (ret != null ? fromGenericValue(ret) : null);
        } catch(GenericEntityException e) {
            String msg = String.format("Could not load reward (id=%d) ?!?", id);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Gets the specified rewards for the given sprint
     *
     *
     * @param sprintId the id of the sprint
     * @return the list of rewards for the sprint
     */
    @Override
    public List<Reward> getRewardsForSprint(long sprintId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(SPRINTID_FIELD, sprintId);
            List<GenericValue> ret = delegator.findByAnd(ENTITY, map);
            return (ret != null ? fromGenericValue(ret) : null);
        } catch(GenericEntityException e) {
            String msg = String.format("Could not load reward (sprint id=%d) ?!?", sprintId);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Adds the reward
     *
     * @param reward the reward
     * @return the reward
     */
    @Override
    public Reward addReward(Reward reward) {
        try {
            reward.setId(delegator.getNextSeqId(ENTITY));
            delegator.create(toGenericValue(reward));
            return reward;
        } catch (GenericEntityException e) {
            String msg = String.format("Cannot save reward (%d).", reward.getId());
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Updates the reward
     *
     * @param reward the reward
     */
    @Override
    public void updateReward(Reward reward) {
        try {
            delegator.store(toGenericValue(reward));
        } catch (GenericEntityException e) {
            String msg = String.format("Cannot save reward (%d).", reward.getId());
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Removes the reward
     *
     * @param id the reward id
     */
    @Override
    public void deleteReward(long id) {
        try {
            delegator.removeByPrimaryKey(makePk(id));
        } catch (GenericEntityException e) {
            String msg = String.format("Cannot delete reward (%d).", id);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    private GenericPK makePk(long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_FIELD, id);
        return delegator.makePK(ENTITY, map);
    }

    private GenericValue toGenericValue(Reward r) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_FIELD, r.getId());
        map.put(TYPEID_FIELD, r.getTypeId());
        map.put(SPRINTID_FIELD, r.getSprintId());
        map.put(ISSUEID_FIELD, r.getIssueId() > 0 ? r.getIssueId() : null);
        map.put(QUANTITY_FIELD, r.getQuantity());
        map.put(LIMITDATE_FIELD, r.getDateEnds() != null ? new Timestamp(r.getDateEnds().getTime()) : null);
        map.put(SHORTDESC_FIELD, r.getSummary());
        map.put(LONGDESC_FIELD, r.getLongDescription());
        map.put(FROMUSR_FIELD, r.getFromUser());
        map.put(TOUSR_FIELD, r.getToUser());
        map.put(RESOLUTION_FIELD, r.getResolution());

        return delegator.makeValue(ENTITY, map);
    }

    private Reward fromGenericValue(GenericValue genval) {
        if(genval == null) {
            return null;
        }
        long id = (Long)genval.get(ID_FIELD);
        long typeId = (Long)genval.get(TYPEID_FIELD);
        long sprintId = (Long)genval.get(SPRINTID_FIELD);
        long issueId = (Long)genval.get(ISSUEID_FIELD);
        long quantity = (Long)genval.get(QUANTITY_FIELD);
        Timestamp endDate = (Timestamp) genval.get(LIMITDATE_FIELD);
        String summary = (String)genval.get(SHORTDESC_FIELD);
        String longDesc = (String)genval.get(LONGDESC_FIELD);
        String fromUser = (String)genval.get(FROMUSR_FIELD);
        String toUser = (String)genval.get(TOUSR_FIELD);
        String resolution = (String)genval.get(RESOLUTION_FIELD);
        return new Reward(id, typeId, sprintId, quantity, new Date(endDate.getTime()), summary, longDesc, fromUser, toUser, resolution, issueId);
    }

    private List<Reward> fromGenericValue(List<GenericValue> l) {
        List<Reward> result = new ArrayList<Reward>();
        if(l == null) {
            return result;
        }
        for(GenericValue gv : l) {
            result.add(fromGenericValue(gv));
        }
        return result;
    }

    private static final String ENTITY = "RWDREWARDS";
    private static final String ID_FIELD = "r_id";
    private static final String TYPEID_FIELD = "t_id";
    private static final String SPRINTID_FIELD = "s_id";
    private static final String QUANTITY_FIELD = "r_quantity";
    private static final String LIMITDATE_FIELD = "r_limitdate";
    private static final String SHORTDESC_FIELD = "r_shortdesc";
    private static final String LONGDESC_FIELD = "r_longdesc";
    private static final String FROMUSR_FIELD = "r_fromuser";
    private static final String TOUSR_FIELD = "r_touser";
    private static final String RESOLUTION_FIELD = "r_resolution";
    private static final String ISSUEID_FIELD = "r_issueid";
}
