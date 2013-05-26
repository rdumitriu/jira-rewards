/*
 * Created on 5/26/13
 */
package ro.agrade.jira.rewards.ui.report;

import ro.agrade.jira.rewards.services.Reward;

/**
 * The first implementation of the report
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class BasicRewardSprintReportBuilder implements RewardSprintReportBuilder {
    private SprintReport report;

    public BasicRewardSprintReportBuilder() {
    }

    /**
     * The initialization
     */
    @Override
    public void init() {
        report = new SprintReport();
    }

    /**
     * Add reward to the report
     *
     * @param r the reward
     */
    @Override
    public void addReward(Reward r) {
        if(r.getFromUser() != null && r.getToUser() != null) {
            report.add(r);
        }
    }

    /**
     * Post-process the report, In this case, does nothing
     */
    @Override
    public void postProcess() {
    }

    /**
     * Gets the finished report
     *
     * @return the report
     */
    @Override
    public SprintReport getReport() {
        return report;
    }
}
