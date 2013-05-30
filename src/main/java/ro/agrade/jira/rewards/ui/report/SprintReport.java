/*
 * Created on 5/21/13
 */
package ro.agrade.jira.rewards.ui.report;

import java.util.*;

import ro.agrade.jira.rewards.services.Reward;
import ro.agrade.jira.rewards.services.RewardType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The sprint report is actually a sparse matrix ...
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class SprintReport {
    private static final Log LOG = LogFactory.getLog(SprintReport.class);
    private Map<String, SprintReportRow> rows;
    private Set<String> uniqueUsers;
    private Set<String> allPossibleUsers;
    private RewardType rewardType;

    /**
     * Constructs an empty report
     */
    public SprintReport(RewardType rewardType) {
        rows = new TreeMap<String, SprintReportRow>();
        uniqueUsers = new TreeSet<String>();
        allPossibleUsers = new TreeSet<String>();
        this.rewardType = rewardType;
    }

    /**
     * Returns the reward type
     * @return the reward type
     */
    public RewardType getRewardType() {
        return rewardType;
    }

    /**
     * Adds an reward
     * @param r the reward
     */
    public void add(Reward r) {
        if(r.getFromUser() == null || r.getToUser() == null) {
            //skip over
            LOG.warn(String.format("Reward %d (to:%s, from:%s) was not added, not completed (to, from)",
                                   r.getId(), r.getToUser(), r.getFromUser()));
            return;
        }
        SprintReportRow row = getRelevantRow(r.getToUser());
        row.add(r);
        addUsers(r.getFromUser(), r.getToUser());
    }

    /**
     * Substract an reward
     * @param r the reward
     */
    public void substract(Reward r) {
        if(r.getFromUser() == null || r.getToUser() == null) {
            //skip over
            LOG.warn(String.format("Reward %d (to:%s, from:%s) was not substracted, not completed (to, from)",
                    r.getId(), r.getToUser(), r.getFromUser()));
            return;
        }
        SprintReportRow row = getRelevantRow(r.getToUser());
        row.substract(r);
        addUsers(r.getFromUser(), r.getToUser());
    }

    /**
     * Substracts but on the 'from' user
     * @param r the reward
     */
    public void substractInverse(Reward r) {
        if(r.getFromUser() == null || r.getToUser() == null) {
            //skip over
            LOG.warn(String.format("Reward %d (to:%s, from:%s) was not substracted-, not completed (to, from)",
                    r.getId(), r.getToUser(), r.getFromUser()));
            return;
        }
        SprintReportRow row = getRelevantRow(r.getFromUser());
        row.substractInverse(r);
        addUsers(r.getFromUser(), r.getToUser());
    }

    /**
     * Adds just the quantity, no reward
     * @param fromUser the from user
     * @param toUser to user
     * @param quantity the quantity
     */
    public void add(String fromUser, String toUser, long quantity) {
        SprintReportRow row = getRelevantRow(toUser);
        row.add(fromUser, quantity);
        addUsers(fromUser, toUser);
    }

    /**
     * Substracts just the quantity, no reward
     * @param fromUser the from user
     * @param toUser to user
     * @param quantity the quantity
     */
    public void substract(String fromUser, String toUser, long quantity) {
        SprintReportRow row = getRelevantRow(toUser);
        row.substract(fromUser, quantity);
        addUsers(fromUser, toUser);
    }

    /**
     * Gets the quantity on the cell depicted by from and to users (used as indices)
     * @param fromUser the from user
     * @param toUser the to user
     * @return the quantity
     */
    public long getQuantity(String fromUser, String toUser) {
        SprintReportRow row = getRelevantRow(toUser);
        return row.getQuantity(fromUser);
    }

    /**
     * Gets the rewards
     * @param fromUser from user
     * @param toUser to user
     * @return the list of rewards
     */
    public List<Reward> getPositiveRewards(String fromUser, String toUser) {
        SprintReportRow row = getRelevantRow(toUser);
        return row.getPositiveRewards(fromUser);
    }

    /**
     * Gets the rewards
     * @param fromUser from user
     * @param toUser to user
     * @return the list of rewards
     */
    public List<Reward> getNegativeRewards(String fromUser, String toUser) {
        SprintReportRow row = getRelevantRow(toUser);
        return row.getNegativeRewards(fromUser);
    }

    /**
     * Gets the total of an user has to receive
     * @param toUser the to user
     * @return the total
     */
    public long getTotal(String toUser) {
        SprintReportRow row = getRelevantRow(toUser);
        return row.getTotal();
    }

    /**
     * Gets the unique users
     * @return the array of users
     */
    public String [] getUniqueUsers() {
        return uniqueUsers.toArray(new String[uniqueUsers.size()]);
    }

    /**
     * Get the users who do not count towards the report
     * @return the array of irrelevant users
     */
    public String [] getIrrelevantUsers() {
        Set<String> irr = new TreeSet<String>();
        for(String s : allPossibleUsers) {
            if(!uniqueUsers.contains(s)) {
                irr.add(s);
            }
        }
        return irr.toArray(new String[irr.size()]);
    }


    /**
     * Collection of all possible users
     * @param allPossibleUsers the set of all users
     */
    public void setAllPossibleUsers(Set<String> allPossibleUsers) {
        this.allPossibleUsers = allPossibleUsers;
    }

    private SprintReportRow getRelevantRow(String toUser) {
        SprintReportRow row = rows.get(toUser);
        if(row == null) {
            row = new SprintReportRow();
            rows.put(toUser, row);
        }
        return row;
    }

    private void addUsers(String ... users) {
        for(String s : users) {
            uniqueUsers.add(s);
        }
    }
}
