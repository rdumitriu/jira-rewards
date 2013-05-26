/*
 * Created on 5/26/13
 */
package ro.agrade.jira.rewards.ui.report;

import ro.agrade.jira.rewards.services.Reward;

/**
 * We want to be able to offer (maybe!) multiple reports. So while we all know that this is a
 * very simple process, we might want to enable some flexibility here
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public interface RewardSprintReportBuilder {

    /**
     * The initialization
     */
    public abstract void init();

    /**
     * Add reward to the report
     * @param r the reward
     */
    public abstract void addReward(Reward r);

    /**
     * Post-process the report
     */
    public abstract void postProcess();

    /**
     * Gets the finished report
     * @return the report
     */
    public abstract SprintReport getReport();
}
