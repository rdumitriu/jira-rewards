/*
 * Created on 5/21/13
 */
package ro.agrade.jira.rewards.ui.report;

import java.util.*;

import ro.agrade.jira.rewards.services.Reward;

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

    /**
     * Constructs an empty report
     */
    public SprintReport() {
        rows = new TreeMap<String, SprintReportRow>();
        uniqueUsers = new TreeSet<String>();
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

    private SprintReportRow getRelevantRow(String toUser) {
        SprintReportRow row = rows.get(toUser);
        if(row == null) {
            row = new SprintReportRow();
            rows.put(toUser, row);
        }
        return row;
    }
}
