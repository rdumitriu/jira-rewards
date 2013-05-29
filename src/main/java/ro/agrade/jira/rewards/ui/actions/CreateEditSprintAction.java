/**
 * Created with IntelliJ IDEA.
 * Date: 5/29/13
 * Time: 9:49 PM
 */
package ro.agrade.jira.rewards.ui.actions;

import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import org.apache.commons.lang.StringUtils;
import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.*;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Creates/edits a sprint
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class CreateEditSprintAction extends JiraWebActionSupport {

    private static final Log LOG = LogFactory.getLog(CreateEditSprintAction.class);

    private long id;
    private String name;
    private String where;
    private Date when;

    private final RewardAdminService rAdminService;
    private final BetterSoyRenderer soyRenderer;

    /**
     * Constructor
     * @param rAdminService
     */
    public CreateEditSprintAction(RewardAdminService rAdminService,
                                  SoyTemplateRendererProvider soyProvider) {
        this.rAdminService = rAdminService;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
    }


    public String doEdit(){
        if(this.id <= 0){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return INPUT;
        }
        RewardSprint rs = rAdminService.getRewardSprint(this.id);
        if(rs == null){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return INPUT;
        }
        this.name = rs.getName();
        this.where = rs.getWhere();
        this.when = rs.getWhen();

        return INPUT;
    }

    public String doCreate(){
        return INPUT;
    }

    public String doSaveEdit(){
        if(this.id <= 0){
            addErrorMessage(getText("rewards.sprints.empty.name"));
            return INPUT;
        }
        if(StringUtils.isBlank(this.name)){
            addError("name", getText("rewards.sprints.empty.name"));
            return INPUT;
        }
        RewardSprint rs = rAdminService.getRewardSprint(this.id);
        if(rs == null){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return INPUT;
        }
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Updating sprint %s %s %s %s", id, name, where, when));
        }
        rs.setName(this.name);
        rs.setWhere(this.where);
        rs.setWhen(this.when);
        rAdminService.updateRewardSprint(rs);
        return returnComplete(String.format("/secure/BeerSprints.jspa?selectedSprint=%s", this.id));
    }

    public String doAdd(){
        if(StringUtils.isBlank(this.name)){
            addError("name", getText("rewards.sprints.empty.name"));
            return INPUT;
        }
        RewardSprint rs = new RewardSprint(0L, this.name, this.where,
                                           getLoggedInApplicationUser().getKey(),
                                           this.when, SprintStatus.ACTIVE,
                                           null);
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Creating sprint %s %s %s %s", id, name, where, when));
        }
        rs = rAdminService.addRewardSprint(rs);
        return returnComplete(String.format("/secure/BeerSprints.jspa?selectedSprint=%s", rs.getId()));
    }

    public SoyTemplateRenderer getSoyRenderer() {
        return soyRenderer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }
}
