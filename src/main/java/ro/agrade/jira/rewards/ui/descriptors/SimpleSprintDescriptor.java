package ro.agrade.jira.rewards.ui.descriptors;

import ro.agrade.jira.rewards.services.RewardSprint;
import ro.agrade.jira.rewards.ui.UiUtils;

/**
 * Simple sprint descriptor
 */
public class SimpleSprintDescriptor {
    public long id;
    public String name;
    public String where;
    public UserDescriptor owner;
    public String when;
    public String status;

    public SimpleSprintDescriptor(RewardSprint s) {
        this.id = s.getId();
        this.name = s.getName();
        this.where = s.getWhere();
        this.owner = new UserDescriptor(s.getOwner());
        this.when = s.getWhen() != null ? UiUtils.getDateFormatter().format(s.getWhen()) : "";
        this.status = s.getStatus().name();
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
}