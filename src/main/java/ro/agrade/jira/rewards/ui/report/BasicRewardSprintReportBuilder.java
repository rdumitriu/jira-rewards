/*
 * Created on 5/26/13
 */
package ro.agrade.jira.rewards.ui.report;

import ro.agrade.jira.rewards.services.*;

/**
 * The first implementation of the report
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class BasicRewardSprintReportBuilder implements RewardSprintReportBuilder {
    private SprintReport report;
    private RewardType rewardType;

    public BasicRewardSprintReportBuilder(RewardType rType) {
        this.rewardType = rType;
    }

    /**
     * The initialization
     */
    @Override
    public void init() {
        report = new SprintReport(rewardType);
    }

    /**
     * Add reward to the report
     *
     * @param r the reward
     */
    @Override
    public void addReward(Reward r) {
        if(r.getTypeId() != rewardType.getId()) {
            return;
        }
        if(r.getFromUser() != null && r.getToUser() != null) {
            report.add(r);
        }
    }

    /**
     * Post-process the report, In this case, does almost nothing
     * @param sprint the sprint
     */
    @Override
    public void postProcess(RewardSprint sprint) {
        report.setAllPossibleUsers(sprint.getGuests());
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
