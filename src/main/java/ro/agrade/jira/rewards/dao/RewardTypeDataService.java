/*
 * Created on 1/28/13
 */
package ro.agrade.jira.rewards.dao;

import ro.agrade.jira.rewards.services.RewardType;

import java.util.List;

/**
 * The reward type data service
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardTypeDataService {

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
     * @param name the name of the reward, e.g. 'Beer'
     * @param desc the description
     * @return the reward type, created
     */
    public abstract RewardType addRewardType(String name, String desc);
}
