/**
 * Created with IntelliJ IDEA.
 * Date: 4/20/13
 * Time: 11:59 AM
 */
package ro.agrade.jira.rewards.ui.actions;

import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.UiUtils;

/**
 * Delete a reward
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class DeleteSprintAction extends JiraWebActionSupport {

    private long sprintId;

    private final RewardService rService;
    private RewardAdminService rAdminService;
    private final ApplicationProperties properties;
    private final SoyTemplateRenderer soyRenderer;

    public DeleteSprintAction(RewardService rService,
                              RewardAdminService rAdminService,
                              ApplicationProperties properties,
                              SoyTemplateRendererProvider soyProvider){
        this.rService = rService;
        this.rAdminService = rAdminService;
        this.properties = properties;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
    }

    public String doDelete(){
        if(!UiUtils.isAdmin(getLoggedInApplicationUser())){
            addErrorMessage(getText("rewards.delete.permissions"));
        }

        RewardSprint sprint = rAdminService.getRewardSprint(sprintId);
        if(sprintId <= 0 || sprint == null){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
        }

        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        rAdminService.deleteRewardSprint(sprintId);
        return returnComplete();
    }

    public String getBaseurl(){
        return properties.getString("jira.baseurl");
    }

    public long getSprintId() {
        return sprintId;
    }

    public void setSprintId(long sprintId) {
        this.sprintId = sprintId;
    }

    public SoyTemplateRenderer getSoyRenderer() {
        return soyRenderer;
    }
}
