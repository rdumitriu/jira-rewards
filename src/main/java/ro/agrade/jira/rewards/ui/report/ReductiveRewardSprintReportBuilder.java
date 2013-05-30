/*
 * Created on 5/26/13
 */
package ro.agrade.jira.rewards.ui.report;

import java.util.*;

import ro.agrade.jira.rewards.services.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Reduces the amount of beers to drink ...
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class ReductiveRewardSprintReportBuilder extends BasicRewardSprintReportBuilder {
    private static final Log LOG = LogFactory.getLog(ReductiveRewardSprintReportBuilder.class);

    /**
     * Creates the builder
     */
    public ReductiveRewardSprintReportBuilder(RewardType type) {
        super(type);
    }

    /**
     * Post-process the report, In this case, does nothing
     * @param sprint the sprint
     */
    @Override
    public void postProcess(RewardSprint sprint) {
        super.postProcess(sprint);
        //create the reductions
        SprintReport report = getReport();

        String [] uniqueUsers = report.getUniqueUsers();
        //Now we can process them.
        for(int i = 0; i < uniqueUsers.length; i++) {
            String user1 = uniqueUsers[i];
            for(int j = i + 1; j < uniqueUsers.length; j++) {
                //we can consider from on i, to on j, and we exclude the diagonal
                String user2 = uniqueUsers[j];
                long q1 = report.getQuantity(user1, user2); //from user 1 to user 2
                //we try to see
                long q2 = report.getQuantity(user2, user1); //from user 2 to user 1
                List<Reward> rwdsToReconsider = null;
                if(q1 < q2 || (q1 == q2 && q1 > 0)) {
                    //user 2 has more to receive than user 1, so:
                    rwdsToReconsider = report.getPositiveRewards(user1, user2);
                } else if(q1 > q2) {
                    //user 1 has more to receive than user 2, so:
                    rwdsToReconsider = report.getPositiveRewards(user2, user1);
                }
                if(rwdsToReconsider != null) {
                    for(Reward r : rwdsToReconsider) {
                        report.substract(r);
                        report.substractInverse(r);
                    }
                }
            }
        }
    }
}
