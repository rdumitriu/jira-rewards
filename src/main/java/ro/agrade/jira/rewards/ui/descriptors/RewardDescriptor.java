/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 2:05 PM
 */
package ro.agrade.jira.rewards.ui.descriptors;

import ro.agrade.jira.rewards.services.Reward;
import ro.agrade.jira.rewards.services.RewardType;
import ro.agrade.jira.rewards.ui.UiUtils;

/**
 * Reward representation for the UI
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class RewardDescriptor {
    private long id;
    private String name;
    private String namePluralForm;
    private String typeDescription;
    private String iconURL;
    private long sprintId;
    private long quantity;
    private String dateEnds;
    private String summary;
    private String longDescription;
    private UserDescriptor fromUser;
    private UserDescriptor toUser;
    private String resolution;

    public RewardDescriptor(Reward r, RewardType t){
        this.id = r.getId();
        this.name = t.getName();
        this.namePluralForm = t.getNamePluralForm();
        this.typeDescription = t.getDescription();
        this.iconURL = t.getIconURL();
        this.sprintId = r.getSprintId();
        this.quantity = r.getQuantity();
        this.dateEnds = UiUtils.formatDate(r.getDateEnds());
        this.summary = r.getSummary();
        this.longDescription = r.getLongDescription();
        this.fromUser = new UserDescriptor(r.getFromUser());
        if(r.getToUser() != null){
            this.toUser = new UserDescriptor(r.getToUser());
        }
        this.resolution = r.getResolution();
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

    public String getNamePluralForm() {
        return namePluralForm;
    }

    public void setNamePluralForm(String namePluralForm) {
        this.namePluralForm = namePluralForm;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
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

    public String getDateEnds() {
        return dateEnds;
    }

    public void setDateEnds(String dateEnds) {
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

    public UserDescriptor getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserDescriptor fromUser) {
        this.fromUser = fromUser;
    }

    public UserDescriptor getToUser() {
        return toUser;
    }

    public void setToUser(UserDescriptor toUser) {
        this.toUser = toUser;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}
