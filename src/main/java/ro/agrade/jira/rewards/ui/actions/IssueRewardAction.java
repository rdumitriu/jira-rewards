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
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.descriptors.SimpleSprintDescriptor;

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

        // TODO create reward and add
        // TODO maybe create constructor without id
        Reward reward = new Reward(0l, typeId, sprintId, quantity,
                                  new Date(), summary, longDescription,
                                  getLoggedInApplicationUser().getKey(),
                                  null, "", getId());
//        rService.addReward(reward);
        return returnComplete();
    }

    public String doSelectReward(){
        // TODO uncomment when sprint planning is done
//        Reward reward = rService.getReward(rwdId);
//        if(reward == null){
//            addErrorMessage(getText("rewards.forms.errors.invalid.reward"));
//        }
        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

        Reward reward = new Reward(0l, 1l, 2l, 12,
                new Date(), "my summary", "really long description",
                getLoggedInApplicationUser().getKey(),
                null, "", 10000);
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

        // TODO maybe create constructor without id
        Reward reward = new Reward(0l, typeId, sprintId, quantity,
                new Date(), summary, longDescription,
                getLoggedInApplicationUser().getKey(),
                null, "", getId());

        if(!reward.getFromUser().equals(getLoggedInApplicationUser().getKey())){
            addErrorMessage(getText("rewards.edit.errors.owner.permission"));
        }
        if(getHasErrors() || getHasErrorMessages()){
            return INPUT;
        }

//        rService.updateReward(reward);
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
                                             getText("rewards.new.offer.label")));
        }
    }

    public List<RewardType> getTypes() {
        // TODO
        return Lists.newArrayList(new RewardType(1, "Beer", "Beers", "Sweet golden nectar of the gods", "/download/resources/ro.agrade.jira.rewards:rewards-resources/images/beer_32x32.png"));
//        return radminService.getRewardTypes();
    }

    public List<SimpleSprintDescriptor> getSprints() {
        // TODO
        RewardSprint sprint = new RewardSprint(1, "Sprint", "location", "admin", new Date(System.currentTimeMillis()), SprintStatus.ACTIVE, new HashSet<String>());
        RewardSprint sprint2 = new RewardSprint(2, "Sprint2", "location2", "admin", new Date(System.currentTimeMillis() + 2000000), SprintStatus.ACTIVE, new HashSet<String>());
//        return radminService.getRewardSprints(SprintStatus.ACTIVE);
        return Lists.newArrayList(new SimpleSprintDescriptor(sprint), new SimpleSprintDescriptor(sprint2));

    }

    public String getBaseurl(){
        return properties.getString("jira.baseurl");
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
