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
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.UiUtils;
import ro.agrade.jira.rewards.ui.descriptors.*;
import ro.agrade.jira.rewards.utils.SprintDateComparator;

import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Support for the sprints page
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class BeerSprintsAction extends JiraWebActionSupport {

    private static final Log LOG = LogFactory.getLog(BeerSprintsAction.class);

    private final RewardService rService;
    private final RewardAdminService rAdminService;
    private final ApplicationProperties properties;
    private final SoyTemplateRenderer soyRenderer;
    private boolean showActive;
    private boolean showClosed;
    private SimpleDateFormat categoryExtractor;
    private long selectedSprint;
    private SprintDescriptor selectedSprintObj;
    private List<CategorySprintsDescriptor> sprints;

    public BeerSprintsAction(RewardService rService,
                             RewardAdminService rAdminService,
                             ApplicationProperties properties,
                             SoyTemplateRendererProvider soyProvider){
        this.rService = rService;
        this.rAdminService = rAdminService;
        this.properties = properties;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
        this.categoryExtractor = new SimpleDateFormat("MMMMM yyyy");
        this.showClosed = false;
        this.showActive = true;
    }

    public String getBaseurl(){
        return properties.getString("jira.baseurl");
    }

    public SoyTemplateRenderer getSoyRenderer() {
        return soyRenderer;
    }

    @Override
    public String doExecute() throws Exception {
        super.doExecute();
        return doDefault();
    }

    @Override
    public String doDefault() throws Exception {
        if(selectedSprint <= 0){
            if(LOG.isDebugEnabled()){
                LOG.debug("No selected sprint. Getting the first one available.");
            }
            List<CategorySprintsDescriptor> spr = getSprints();
            if(spr != null && spr.size() > 0){
                // can't be NPE here since the category was created from the sprint
                selectedSprint = spr.get(0).sprints.get(0).id;
                if(LOG.isDebugEnabled()){
                    LOG.debug(String.format("Selected first sprint %s", selectedSprint));
                }
            }
        }
        RewardSprint ss = rAdminService.getRewardSprint(selectedSprint);
        if(ss != null){
            if(LOG.isDebugEnabled()){
                LOG.debug(String.format("Selected sprint is %s(%s)", ss.getId(), ss.getName()));
            }
            List<UserDescriptor> guests = new ArrayList<UserDescriptor>();
            if(ss.getGuests() != null){
                for(String guestKey : ss.getGuests()){
                    guests.add(new UserDescriptor(guestKey));
                }
            }
            List<Reward> rewards = rService.getRewardsForSprint(selectedSprint);
            List<RewardDescriptor> rewardDescrs = null;
            if(rewards != null || rewards.size() == 0){
                rewardDescrs = Lists.transform(rewards, new Function<Reward, RewardDescriptor>() {
                    @Override
                    public RewardDescriptor apply(Reward input) {
                        return new RewardDescriptor(input, rAdminService.getRewardType(input.getTypeId()));
                    }
                });
            }
            this.selectedSprintObj = new SprintDescriptor(ss, guests, rewardDescrs);
        } else {
            LOG.debug("No sprint to select");
            selectedSprint = 0;
            this.selectedSprintObj = null;
        }
        return super.doDefault();
    }

    public List<CategorySprintsDescriptor> getSprints() {
        if(sprints == null) {
            List<RewardSprint> rewardSprints = new ArrayList<RewardSprint>();
            if(showActive){
                LOG.debug("Showing active sprints");
                rewardSprints.addAll(rAdminService.getRewardSprints(SprintStatus.ACTIVE));
            }
            if(showClosed){
                LOG.debug("Showing closed sprints");
                rewardSprints.addAll(rAdminService.getRewardSprints(SprintStatus.EXECUTED));
            }
            Collections.sort(rewardSprints, new SprintDateComparator());

            sprints = categorizeSprints(rewardSprints);
        }
        return sprints;
    }

    public SprintDescriptor getSelectedSprintObj(){
        return selectedSprintObj;
    }

    public boolean isShowActive() {
        return showActive;
    }

    public void setShowActive(boolean showActive) {
        this.showActive = showActive;
    }

    public boolean isShowClosed() {
        return showClosed;
    }

    public void setShowClosed(boolean showClosed) {
        this.showClosed = showClosed;
    }

    public long getSelectedSprint() {
        return selectedSprint;
    }

    public void setSelectedSprint(long selectedSprint) {
        this.selectedSprint = selectedSprint;
    }

    private List<CategorySprintsDescriptor> categorizeSprints(List<RewardSprint> rewardSprints) {
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Categorizing %s sprints",
                    rewardSprints != null ? rewardSprints.size() : 0));
        }
        List<CategorySprintsDescriptor> ret = new ArrayList<CategorySprintsDescriptor>();

        String currentCategory = null;
        List<SimpleSprintDescriptor> sprintsInCategory = new ArrayList<SimpleSprintDescriptor>();
        for(RewardSprint rs : rewardSprints){
            // categories will be date-based
            // e.g unscheduled, January 2013, February 2013, etc
            String sprintCategory = extractCategory(rs);
            if(currentCategory == null){
                // first iteration
                if(LOG.isDebugEnabled()){
                    LOG.debug(String.format("Initial category is %s", sprintCategory));
                }
                currentCategory = sprintCategory;
            }
            // we can do this because the sprints are sorted by date so
            // they'll come in blocks by category
            // it's important that we maintain the order of the sprints AND categories
            if(currentCategory.equals(sprintCategory)){
                // if it's the same category as previous sprint, add to the list
                if(LOG.isDebugEnabled()){
                    LOG.debug(String.format("Adding sprint %s to category %s", rs.getName(), currentCategory));
                }
                sprintsInCategory.add(new SimpleSprintDescriptor(rs));
            } else {
                // otherwise add the current category to the list and start a new one
                ret.add(new CategorySprintsDescriptor(currentCategory, sprintsInCategory));
                if(LOG.isDebugEnabled()){
                    LOG.debug(String.format("Starting new category %s", sprintCategory));
                }
                currentCategory = sprintCategory;
                sprintsInCategory = Lists.newArrayList(new SimpleSprintDescriptor(rs));
            }
        }
        if(sprintsInCategory.size() > 0){
            // last one
            ret.add(new CategorySprintsDescriptor(currentCategory, sprintsInCategory));
        }
        if(LOG.isDebugEnabled()){
            LOG.debug(String.format("Got %s categories", ret != null ? ret.size() : 0));
        }
        return ret;
    }

    public boolean getIsAdmin(){
        return UiUtils.isAdmin(getLoggedInApplicationUser());
    }

    private String extractCategory(RewardSprint rs) {
        if(rs.getWhen() == null) {
            return getText("rewards.sprints.category.unscheduled");
        }
        return categoryExtractor.format(rs.getWhen());
    }
}
