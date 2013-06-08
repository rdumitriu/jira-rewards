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
import ro.agrade.jira.rewards.ui.UiUtils;

import java.text.ParseException;
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
    private Date date;
    private String when;

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
        this.date = rs.getWhen();

        return INPUT;
    }

    public String doClose(){
        changeStatus(SprintStatus.EXECUTED);
        if(getHasErrorMessages()){
            LOG.error(String.format("There were errors closing the sprint: %s", getErrorMessages()));
        }
        return returnComplete("/secure/BeerSprints.jspa?selectedSprint="+this.id);
    }

    public String doReopen(){
        changeStatus(SprintStatus.ACTIVE);
        if(getHasErrorMessages()){
            LOG.error(String.format("There were errors reopening the sprint: %s", getErrorMessages()));
        }
        return returnComplete("/secure/BeerSprints.jspa?selectedSprint="+this.id);
    }


    public String doCreate(){
        return INPUT;
    }

    public String doSaveEdit(){
        if(this.id <= 0){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return INPUT;
        }
        if(StringUtils.isBlank(this.name)){
            addError("name", getText("rewards.sprints.empty.name"));
            return INPUT;
        }
        if(date != null && date.before(new Date(System.currentTimeMillis()))){
            addError("when", getText("rewards.sprints.date.in.the.past"));
            return INPUT;
        }
        RewardSprint rs = rAdminService.getRewardSprint(this.id);
        if(rs == null){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return INPUT;
        }
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Updating sprint %s %s %s %s", id, name, where, date));
        }
        rs.setName(this.name);
        rs.setWhere(this.where);
        rs.setWhen(this.date);
        rAdminService.updateRewardSprint(rs);
        setReturnUrl(String.format("/secure/BeerSprints.jspa?selectedSprint=%s", this.id));
        return returnComplete();
    }

    public String doAdd(){
        if(StringUtils.isBlank(this.name)){
            addError("name", getText("rewards.sprints.empty.name"));
            return INPUT;
        }
        if(date != null && date.before(new Date(System.currentTimeMillis()))){
            addError("when", getText("rewards.sprints.date.in.the.past"));
            return INPUT;
        }
        RewardSprint rs = new RewardSprint(0L, this.name, this.where,
                                           getLoggedInApplicationUser().getKey(),
                                           this.date, SprintStatus.ACTIVE,
                                           null);
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Creating sprint %s %s %s %s", id, name, where, when));
        }
        rs = rAdminService.addRewardSprint(rs);
        setReturnUrl(String.format("/secure/BeerSprints.jspa?selectedSprint=%s", rs.getId()));
        return returnComplete();
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getWhen() {
        return date != null
                ? UiUtils.getDateTimePickerFormat().format(date)
                : null;
    }

    public void setWhen(String when) {
        if(StringUtils.isBlank(when)){
            this.date = null;
        } else {
            try {
                // TODO getTimezone() does: return null
                this.date = UiUtils.getDateTimePickerFormat(getTimezone()).parse(when);
            } catch (ParseException ex){
                if(LOG.isDebugEnabled()){
                    LOG.debug(String.format("Invalid date %s format", when));
                }
                this.date = null;
            }
        }
        this.when = when;
    }

    private void changeStatus(SprintStatus status){
        if(this.id <= 0){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return;
        }
        RewardSprint rs = rAdminService.getRewardSprint(this.id);
        if(rs == null){
            addErrorMessage(getText("rewards.sprints.invalid.id"));
            return;
        }
        rs.setStatus(status);
        rAdminService.updateRewardSprint(rs);
    }
}
