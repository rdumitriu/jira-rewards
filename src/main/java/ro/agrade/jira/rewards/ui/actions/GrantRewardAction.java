/**
 * Created with IntelliJ IDEA.
 * Date: 4/20/13
 * Time: 11:59 AM
 */
package ro.agrade.jira.rewards.ui.actions;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import org.apache.commons.lang.StringUtils;
import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.Reward;
import ro.agrade.jira.rewards.services.RewardService;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Grant a reward
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class GrantRewardAction extends JiraWebActionSupport {

    private static final Log LOG = LogFactory.getLog(GrantRewardAction.class);

    private long rwdId;
    private String toUser;
    private String resolution;

    private final RewardService rService;
    private final ApplicationProperties properties;
    private final UserManager userManager;
    private final SoyTemplateRenderer soyRenderer;

    public GrantRewardAction(RewardService rService,
                             ApplicationProperties properties,
                             SoyTemplateRendererProvider soyProvider,
                             UserManager userManager){
        this.rService = rService;
        this.properties = properties;
        this.userManager = userManager;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
    }

    public String doGrant(){
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Granting reward %s to %s because %s", rwdId, toUser, resolution));
        }

        if(StringUtils.isBlank(toUser)){
            getErrors().put("toUser", getText("rewards.forms.errors.required",
                                               getText("rewards.grant.toUser.label")));
        }

        // TODO uncomment when sprint planning is done
//        Reward reward = rService.getReward(rwdId);
        Reward reward = new Reward(0l, 1l, 2l, 12,
                new Date(), "my summary", "really long description",
                getLoggedInApplicationUser().getKey(),
                null, "", 10000);
        if(rwdId <= 0 || reward == null){
            addErrorMessage(getText("rewards.forms.errors.invalid.reward"));
        }

        if(userManager.getUserByKey(toUser) == null){
            getErrors().put("toUser", getText("rewards.forms.errors.invalid",
                                              getText("rewards.grant.toUser.label")));
        }

        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        // TODO uncomment when sprint planning is done
//        rService.grantRewardTo(reward, toUser, resolution);
        return returnComplete();
    }

    public String getBaseurl(){
        return properties.getString("jira.baseurl");
    }

    public long getRwdId() {
        return rwdId;
    }

    public void setRwdId(long rwdId) {
        this.rwdId = rwdId;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public SoyTemplateRenderer getSoyRenderer() {
        return soyRenderer;
    }
}
