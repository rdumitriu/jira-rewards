/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

/**
 * This is the reward.
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class Reward {
    //<field name="r_id" type="numeric"/>
    private long id;
    //<field name="t_id" type="numeric"/>
    private long typeId;
    //<field name="s_id" type="numeric"/>
    private long sprintId;
    //<field name="r_quantity" type="numeric"/>
    private long quantity;
    //<field name="r_limitdate" type="date"/>
    private Date dateEnds;
    //<field name="r_shortdesc" type="long-varchar"/>
    private String summary;
    //<field name="r_longdesc" type="extremely-long"/>
    private String longDescription;
    //<field name="r_fromuser" type="long-varchar"/>
    private String fromUser;
    //<field name="r_touser" type="long-varchar"/>
    private String toUser;
    //<field name="r_resolution" type="extremely-long"/>
    private String resolution;
    //<field name="r_issueid" type="numeric"/>
    private long issueId;

    public Reward(long id, long typeId, long sprintId, long quantity, Date dateEnds, String summary, String longDescription, String fromUser, String toUser, String resolution, long issueId) {
        this.id = id;
        this.typeId = typeId;
        this.sprintId = sprintId;
        this.quantity = quantity;
        this.dateEnds = dateEnds;
        this.summary = summary;
        this.longDescription = longDescription;
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.resolution = resolution;
        this.issueId = issueId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public long getIssueId() {
        return issueId;
    }

    public void setIssueId(long issueId) {
        this.issueId = issueId;
    }
}
