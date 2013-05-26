/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 5:23 PM
 */
package ro.agrade.jira.rewards.context;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.security.PermissionManager;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.google.common.collect.Lists;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.descriptors.*;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Provides the rewards for the issue in the context
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class IssueRewardsContextProvider extends SoyContextProvider {

    private static final Log LOG = LogFactory.getLog(SoyContextProvider.class);

    /**
     * Constructor
     *
     * @param authenticationContext the auth context
     * @param soyProvider the soy template renderer provider
     */
    public IssueRewardsContextProvider(JiraAuthenticationContext authenticationContext,
                                       SoyTemplateRendererProvider soyProvider) {
        super(authenticationContext, soyProvider);
    }

    @Override
    public Map<String, Object> getContextMap(Map<String, Object> context) {
        Map<String, Object> ctxMap = super.getContextMap(context);
        Issue iss = (Issue) ctxMap.get("issue");
        if (iss == null) {
            LOG.debug("No issue provided in super context");
            ctxMap.put("sprints", new ArrayList<SprintDescriptor>());
            return ctxMap;
        }
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Adding sprints to context for issue %s", iss.getKey()));
        }
        ctxMap.put("sprints", getSprints());
        return ctxMap;
    }

    public List<SprintDescriptor> getSprints() {
        RewardSprint sprint = new RewardSprint(1, "Sprint", "location", "admin", new Date(System.currentTimeMillis()), SprintStatus.ACTIVE, new HashSet<String>());
        RewardSprint sprint2 = new RewardSprint(2, "Sprint2", "location2", "admin", new Date(System.currentTimeMillis() + 200000000), SprintStatus.ACTIVE, new HashSet<String>());
        RewardType type = new RewardType(1, "Beer", "Beers", "Sweet golden nectar of the gods", "/download/resources/ro.agrade.jira.rewards:rewards-resources/images/beer_32x32.png");
        Reward r1 = new Reward(1, 1, 1, 1, new Date(System.currentTimeMillis()), "rwd summary", "long description", "admin", null, "resolution", 10000);
        Reward r2 = new Reward(2, 1, 1, 12, new Date(System.currentTimeMillis()), "rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd " +
                                                                                 "summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary ",
                                                                                 "long description", "admin", "admin", "resolution", 10000);
        Reward r3 = new Reward(3, 1, 1, 999, new Date(System.currentTimeMillis()), "rwd summary", "long description", "admin", null, "resolution", 10000);
        Reward r4 = new Reward(4, 1, 2, 5, new Date(System.currentTimeMillis()), "rwd summary", "long description", "admin", null, "resolution", 10000);
        Reward r5 = new Reward(5, 1, 2, 100, new Date(System.currentTimeMillis()), "rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd " +
                                                                                   "summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary " +
                                                                                   "rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summary rwd summ",
                                                                                   "long description", "admin", null, "resolution", 10000);

        List<UserDescriptor> ud = new ArrayList<UserDescriptor>();
        ud.add(new UserDescriptor("admin"));

        List<RewardDescriptor> rd1 = new ArrayList<RewardDescriptor>();
        rd1.add(new RewardDescriptor(r1, type));
        rd1.add(new RewardDescriptor(r2, type));
        rd1.add(new RewardDescriptor(r3, type));

        List<RewardDescriptor> rd2 = new ArrayList<RewardDescriptor>();
        rd2.add(new RewardDescriptor(r4, type));
        rd2.add(new RewardDescriptor(r5, type));

        List<SprintDescriptor> sd = new ArrayList<SprintDescriptor>();
        sd.add(new SprintDescriptor(sprint, ud, rd1));
        sd.add(new SprintDescriptor(sprint2, ud, rd2));
        return sd;
    }
}
