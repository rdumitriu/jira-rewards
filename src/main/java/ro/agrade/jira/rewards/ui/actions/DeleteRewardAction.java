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
import ro.agrade.jira.rewards.ui.UiUtils;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Delete a reward
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class DeleteRewardAction extends JiraWebActionSupport {

    private long rwdId;

    private final RewardService rService;
    private final ApplicationProperties properties;
    private final SoyTemplateRenderer soyRenderer;

    public DeleteRewardAction(RewardService rService,
                              ApplicationProperties properties,
                              SoyTemplateRendererProvider soyProvider){
        this.rService = rService;
        this.properties = properties;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
    }

    public String doDelete(){
        if(!UiUtils.isAdmin(getLoggedInApplicationUser())){
            addErrorMessage(getText("rewards.delete.permissions"));
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

        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        // TODO uncomment when sprint planning is done
//        rService.deleteReward(rwdId);
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

    public SoyTemplateRenderer getSoyRenderer() {
        return soyRenderer;
    }
}
