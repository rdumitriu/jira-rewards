/**
 * Created with IntelliJ IDEA.
 * Date: 4/20/13
 * Time: 11:59 AM
 */
package ro.agrade.jira.rewards.ui.actions;

import com.atlassian.jira.config.SubTaskManager;
import com.atlassian.jira.config.properties.ApplicationProperties;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.web.action.issue.AbstractIssueSelectAction;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.descriptors.SimpleSprintDescriptor;
import ro.agrade.jira.rewards.utils.JIRAUtils;

import javax.annotation.Nullable;
import java.util.*;

/**
 * Add a new reward
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class IssueRewardAction extends AbstractIssueSelectAction {
    // extending AbstractIssueSelectAction to enable
    // the use of "issueaction" decorator

    private long rwdId;
    private long typeId;
    private long sprintId;
    private long quantity;
    private Date dateEnds; // ??
    private String summary;
    private String longDescription;
    private String fromUser;

    private final RewardAdminService radminService;
    private final RewardService rService;
    private final ApplicationProperties properties;
    private final SoyTemplateRenderer soyRenderer;

    public IssueRewardAction(RewardAdminService radminService,
                             RewardService rService,
                             ApplicationProperties properties,
                             SoyTemplateRendererProvider soyProvider,
                             SubTaskManager subTaskManager){
        super(subTaskManager);
        this.radminService = radminService;
        this.rService = rService;
        this.properties = properties;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
    }

    public String doCreate(){
        checkFields();

        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        Reward reward = new Reward(0, typeId, sprintId, quantity,
                                  new Date(), summary, longDescription,
                                  getLoggedInApplicationUser().getKey(),
                                  null, "", getId());
        rService.addReward(reward);
        return returnComplete();
    }

    public String doSelectReward(){
        Reward reward = rService.getReward(rwdId);
        if(reward == null){
            addErrorMessage(getText("rewards.forms.errors.invalid.reward"));
        }
        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        this.typeId = reward.getTypeId();
        this.sprintId = reward.getSprintId();
        this.quantity = reward.getQuantity();
        this.dateEnds = reward.getDateEnds();
        this.summary = reward.getSummary();
        this.longDescription = reward.getLongDescription();
        this.fromUser = reward.getFromUser();
        setId(reward.getIssueId());
        return INPUT;
    }

    public String doEdit(){
        if(rwdId <= 0){
            addErrorMessage(getText("rewards.forms.errors.invalid.reward"));
        }
        checkFields();

        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        Reward reward = rService.getReward(rwdId);
        if(reward == null){
            addErrorMessage(getText("rewards.forms.errors.invalid.reward"));
        }

        if(!reward.getFromUser().equals(getLoggedInApplicationUser().getKey())){
            addErrorMessage(getText("rewards.edit.errors.owner.permission"));
        }
        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        reward.setLongDescription(this.longDescription);
        reward.setQuantity(this.quantity);
        reward.setSummary(this.summary);
        reward.setTypeId(this.typeId);
        reward.setSprintId(this.sprintId);

        rService.updateReward(reward);
        return returnComplete();
    }

    private void checkFields() {
        if(StringUtils.isBlank(summary)){
            getErrors().put("summary", getText("rewards.forms.errors.required",
                                               getText("rewards.new.summary.label")));
        }
        if(quantity <= 0) {
            getErrors().put("offer", getText("rewards.forms.errors.invalid",
                                             getText("rewards.new.offer.label")));
        }
        if(typeId <= 0) {
            getErrors().put("offer", getText("rewards.forms.errors.invalid",
                                             getText("rewards.new.offer.label")));
        }
        if(sprintId <= 0) {
            getErrors().put("sprint", getText("rewards.forms.errors.invalid",
                                             getText("rewards.new.sprint.label")));
        }
        RewardSprint rs = radminService.getRewardSprint(sprintId);
        if(rs == null){
            getErrors().put("sprint", getText("rewards.forms.errors.invalid",
                                              getText("rewards.new.sprint.label")));
        } else if(!rs.getStatus().equals(SprintStatus.ACTIVE)) {
            getErrors().put("sprint", getText("rewards.forms.errors.invalid.sprint.status"));
        } else if(rs.getWhen() != null && rs.getWhen().compareTo(new Date()) <= 0) {
            getErrors().put("sprint", getText("rewards.forms.errors.sprint.overdue"));
        }
    }

    public List<RewardType> getTypes() {
        return radminService.getRewardTypes();
    }

    public List<SimpleSprintDescriptor> getSprints() {
        List<RewardSprint> rewardSprints = radminService.getRewardSprints(SprintStatus.ACTIVE);
        if(rewardSprints == null || rewardSprints.size() == 0){
            return new ArrayList<SimpleSprintDescriptor>();
        }
        return Lists.transform(rewardSprints, new Function<RewardSprint, SimpleSprintDescriptor>() {
            @Override
            public SimpleSprintDescriptor apply(@Nullable RewardSprint input) {
                return new SimpleSprintDescriptor(input);
            }
        });
    }

    public String getBaseurl(){
        return JIRAUtils.getRelativeJIRAPath(properties);
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public long getSprintId() {
        return sprintId;
    }

    public void setSprintId(long sprintId) {
        this.sprintId = sprintId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Date getDateEnds() {
        return dateEnds;
    }

    public void setDateEnds(Date dateEnds) {
        this.dateEnds = dateEnds;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
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
