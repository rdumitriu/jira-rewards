/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 5:23 PM
 */
package ro.agrade.jira.rewards.context;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.descriptors.RewardDescriptor;
import ro.agrade.jira.rewards.ui.descriptors.SprintDescriptor;

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
    private RewardAdminService rAdminService;
    private RewardService rService;

    /**
     * Constructor
     *
     * @param authenticationContext the auth context
     * @param soyProvider the soy template renderer provider
     */
    public IssueRewardsContextProvider(JiraAuthenticationContext authenticationContext,
                                       SoyTemplateRendererProvider soyProvider,
                                       RewardAdminService rAdminService,
                                       RewardService rService) {
        super(authenticationContext, soyProvider);
        this.rAdminService = rAdminService;
        this.rService = rService;
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
        List<RewardSprint> rewardSprints = rAdminService.getRewardSprints(SprintStatus.ACTIVE);
        List<SprintDescriptor> ret = new ArrayList<SprintDescriptor>();
        if(rewardSprints == null || rewardSprints.size() == 0){
            return ret;
        }
        for(RewardSprint rs : rewardSprints){
            List<Reward> rewards = rService.getRewardsForSprint(rs.getId());
            // only add if we have any rewards
            if(rewards == null || rewards.size() == 0){
                // nope, no rewards
                continue;
            }
            // YAY! BEER!
            List<RewardDescriptor> rewardDescrs = Lists.transform(rewards, new Function<Reward, RewardDescriptor>() {
                @Override
                public RewardDescriptor apply(Reward input) {
                    return new RewardDescriptor(input, rAdminService.getRewardType(input.getTypeId()));
                }
            });
            ret.add(new SprintDescriptor(rs, null, rewardDescrs));
        }
        return ret;
    }
}
