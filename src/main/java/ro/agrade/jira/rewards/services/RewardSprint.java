/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.services;

import java.util.*;

/**
 * The sprint (meeting or closing time), invitees added as well.
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardSprint {
    //<field name="s_id" type="numeric"/>
    private long id;
    //<field name="s_name" type="long-varchar"/>
    private String name;
    //<field name="s_where" type="extremely-long"/>
    private String where;
    //<field name="s_who" type="long-varchar"/>
    private String who;
    //<field name="s_when" type="date"/>
    private Date when;
    //<field name="s_status" type="numeric"/>
    private SprintStatus status;
    //Link to invitees
    private List<String> invitees;

    public RewardSprint(long id, String name, String where, String who, Date when, SprintStatus status, List<String> invitees) {
        this.id = id;
        this.name = name;
        this.where = where;
        this.who = who;
        this.when = when;
        this.status = status;
        this.invitees = invitees;
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

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public List<String> getInvitees() {
        return invitees;
    }

    public void setInvitees(List<String> invitees) {
        this.invitees = invitees;
    }

    public SprintStatus getStatus() {
        return status;
    }

    public void setStatus(SprintStatus status) {
        this.status = status;
    }
}
