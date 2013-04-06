/*
 * Created on 1/28/13
 */
package ro.agrade.jira.rewards.dao;

import java.util.*;

import org.ofbiz.core.entity.*;

import ro.agrade.jira.rewards.services.RewardType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * The data service
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardTypeDataServiceImpl implements RewardTypeDataService {
    private static final Log LOG = LogFactory.getLog(RewardTypeDataServiceImpl.class);
    private final GenericDelegator delegator;

    /**
     * Constructor
     */
    public RewardTypeDataServiceImpl() {
        this.delegator = GenericDelegator.getGenericDelegator("default");
    }

    /**
     * Gets the types
     *
     * @param id the id
     * @return the RewardType
     */
    @Override
    public RewardType getRewardType(long id) {
        try {
            GenericValue ret = delegator.findByPrimaryKey(ENTITY, makePk(id));
            return (ret != null ? fromGenericValue(ret) : null);
        } catch(GenericEntityException e) {
            String msg = String.format("Could not load reward type (id=%d) ?!?", id);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Gets all the reward types
     *
     * @return the reward
     */
    @Override
    public List<RewardType> getRewardTypes() {
        try {
            List<GenericValue> list = delegator.findAll(ENTITY);
            return fromGenericValue(list);
        } catch(GenericEntityException e) {
            String msg = "Could not load all rewards types ?!?";
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    /**
     * Creates a reward type
     * @param name the name of the reward, e.g. 'Beer'
     * @param desc the description
     * @return the reward type, created
     */
    @Override
    public RewardType addRewardType(String name, String desc) {
        try {
            long id = delegator.getNextSeqId(ENTITY);
            RewardType ret = new RewardType(id, name, desc);
            delegator.create(toGenericValue(ret));
            return ret;
        } catch(GenericEntityException e) {
            String msg = String.format("Could not create reward type ('%s', '%s') ?!?", name, desc);
            LOG.error(msg);
            throw new OfbizDataException(msg, e);
        }
    }

    private GenericPK makePk(long id) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_FIELD, id);
        return delegator.makePK(ENTITY, map);
    }

    private GenericValue toGenericValue(RewardType rt) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(ID_FIELD, rt.getId());
        map.put(NAME_FIELD, rt.getName());
        map.put(DESC_FIELD, rt.getDescription());
        return delegator.makeValue(ENTITY, map);
    }

    private RewardType fromGenericValue(GenericValue genval) {
        if(genval == null) {
            return null;
        }
        long id = (Long)genval.get(ID_FIELD);
        String name = (String)genval.get(NAME_FIELD);
        String desc = (String)genval.get(DESC_FIELD);

        return new RewardType(id, name, desc);
    }

    private List<RewardType> fromGenericValue(List<GenericValue> l) {
        List<RewardType> result = new ArrayList<RewardType>();
        if(l == null) {
            return result;
        }
        for(GenericValue gv : l) {
            result.add(fromGenericValue(gv));
        }
        return result;
    }

    private static final String ENTITY = "RWDTYPES";
    private static final String ID_FIELD = "t_id";
    private static final String NAME_FIELD = "t_name";
    private static final String DESC_FIELD = "t_desc";
}
