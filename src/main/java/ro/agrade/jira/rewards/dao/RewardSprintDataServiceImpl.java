/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.dao;

import java.util.*;

import org.ofbiz.core.entity.*;

import ro.agrade.jira.rewards.services.RewardSprint;
import ro.agrade.jira.rewards.services.SprintStatus;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The guests added to the sprint
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardSprintDataServiceImpl implements RewardSprintDataService {
    private static final Log LOG = LogFactory.getLog(RewardSprintDataServiceImpl.class);
    private final GenericDelegator delegator;

    /**
     * Constructor
     */
    public RewardSprintDataServiceImpl() {
        this.delegator = GenericDelegator.getGenericDelegator("default");
    }

    /**
     * Gets the specified sprint
     *
     * @param id the id
     * @return the sprint
     */
    @Override
    public RewardSprint getRewardSprint(long id) {
        try {
            GenericValue ret = delegator.findByPrimaryKey(ENTITY, makePk(id));
            return (ret != null ? fromGenericValue(ret) : null);
        } catch(GenericEntityException e) {
            String msg = String.format("Could not load reward sprint (id=%d) ?!?", id);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Gets a user reward sprint
     *
     * @return the sprints added by an user
     */
    @Override
    public List<RewardSprint> getRewardSprints(String user, Date validAt, SprintStatus status) {
        try {
            List<EntityExpr> conds = new ArrayList<EntityExpr>();
            conds.add(new EntityExpr(OWNER_FIELD, EntityOperator.EQUALS, user));
            if(status != null) {
                conds.add(new EntityExpr(STATUS_FILED, EntityOperator.EQUALS, status.ordinal()));
            }
            if(validAt != null) {
                conds.add(new EntityExpr(WHEN_FILED, EntityOperator.LESS_THAN, validAt));
            }
            List<GenericValue> ret = delegator.findByCondition(ENTITY, new EntityExprList(conds, EntityOperator.AND), null, null);
            return (ret != null ? fromGenericValue(ret) : null);
        } catch(GenericEntityException e) {
            String msg = String.format("Could not load reward sprints (user=%s) ?!?", user);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Gets the specified sprints (all with the specified status)
     *
     * @return the sprints
     */
    @Override
    public List<RewardSprint> getRewardSprints(SprintStatus status) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put(OWNER_FIELD, status.ordinal());
            List<GenericValue> ret = delegator.findByAnd(ENTITY, map);
            return (ret != null ? fromGenericValue(ret) : null);
        } catch(GenericEntityException e) {
            String msg = String.format("Could not load reward sprints (status=%s) ?!?", status);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Adds a sprint
     *
     * @param rs the reward sprint
     * @return the RewardType
     */
    @Override
    public RewardSprint addRewardSprint(RewardSprint rs) {
        try {
            rs.setId(delegator.getNextSeqId(ENTITY));
            delegator.create(toGenericValue(rs));
            return rs;
        } catch (GenericEntityException e) {
            String msg = String.format("Cannot save reward sprint (%d).", rs.getId());
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Updates a sprint
     *
     * @param rs the reward sprint
     */
    @Override
    public void updateRewardSprint(RewardSprint rs) {
        try {
            delegator.store(toGenericValue(rs));
        } catch (GenericEntityException e) {
            String msg = String.format("Cannot save reward sprint (%d).", rs.getId());
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Removes a sprint
     *
     * @param id the reward sprint id
     */
    @Override
    public void deleteRewardSprint(long id) {
        try {
            delegator.removeByPrimaryKey(makePk(id));
        } catch (GenericEntityException e) {
            String msg = String.format("Cannot delete reward sprint (%d). Still references ?!?", id);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    private GenericPK makePk(long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_FIELD, id);
        return delegator.makePK(ENTITY, map);
    }

    private GenericValue toGenericValue(RewardSprint rs) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_FIELD, rs.getId());
        map.put(NAME_FIELD, rs.getName());
        map.put(WHERE_FIELD, rs.getWhere());
        map.put(OWNER_FIELD, rs.getOwner());
        map.put(WHEN_FILED, rs.getWhen());
        map.put(STATUS_FILED, rs.getStatus().ordinal());
        return delegator.makeValue(ENTITY, map);
    }

    private RewardSprint fromGenericValue(GenericValue genval) {
        if(genval == null) {
            return null;
        }
        long id = (Long)genval.get(ID_FIELD);
        String name = (String)genval.get(NAME_FIELD);
        String where = (String) genval.get(WHERE_FIELD);
        Date when = (Date) genval.get(WHEN_FILED);
        String owner = (String) genval.get(OWNER_FIELD);
        int status = (Integer) genval.get(STATUS_FILED);

        return new RewardSprint(id, name, where, owner, when, SprintStatus.values()[status], null);
    }

    private List<RewardSprint> fromGenericValue(List<GenericValue> l) {
        List<RewardSprint> result = new ArrayList<RewardSprint>();
        if(l == null) {
            return result;
        }
        for(GenericValue gv : l) {
            result.add(fromGenericValue(gv));
        }
        return result;
    }

    private static final String ENTITY = "RWDSPRINTS";
    private static final String ID_FIELD = "s_id";
    private static final String NAME_FIELD = "s_name";
    private static final String WHERE_FIELD = "s_where";
    private static final String OWNER_FIELD = "s_owner";
    private static final String WHEN_FILED = "s_when";
    private static final String STATUS_FILED = "s_status";
}
