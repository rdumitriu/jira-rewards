/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 1:56 PM
 */
package ro.agrade.jira.rewards.ui.descriptors;

import ro.agrade.jira.rewards.services.RewardSprint;
import ro.agrade.jira.rewards.ui.UiUtils;

import java.util.*;

/**
 * Sprint descriptor for the UI
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class SprintDescriptor {
    private long id;
    private String name;
    private String where;
    private UserDescriptor owner;
    private String when;
    private String status;
    private List<UserDescriptor> guests;
    private List<RewardDescriptor> rewards;

    public SprintDescriptor(RewardSprint s,
                            List<UserDescriptor> guests,
                            List<RewardDescriptor> rewards){
        this.id = s.getId();
        this.name = s.getName();
        this.where = s.getWhere();
        this.when = UiUtils.formatDate(s.getWhen());
        this.owner = new UserDescriptor(s.getOwner());
        this.status = s.getStatus().name();
        this.guests = guests;
        this.rewards = rewards;
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

    public UserDescriptor getOwner() {
        return owner;
    }

    public void setOwner(UserDescriptor owner) {
        this.owner = owner;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<UserDescriptor> getGuests() {
        return guests;
    }

    public void setGuests(List<UserDescriptor> guests) {
        this.guests = guests;
    }

    public List<RewardDescriptor> getRewards() {
        return rewards;
    }

    public void setRewards(List<RewardDescriptor> rewards) {
        this.rewards = rewards;
    }
}
