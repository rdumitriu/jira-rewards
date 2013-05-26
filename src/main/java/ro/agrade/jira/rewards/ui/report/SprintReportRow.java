/*
 * Created on 5/21/13
 */
package ro.agrade.jira.rewards.ui.report;

import java.util.*;

import ro.agrade.jira.rewards.services.Reward;

/**
 * This is the reward row in the report
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class SprintReportRow {
    private long total;
    private Map<String, SprintReportCell> cells;

    /**
     * Creates an empty report row (on to user)
     */
    public SprintReportRow() {
        this.total = 0L;
        this.cells = new HashMap<String, SprintReportCell>();
    }

    /**
     * The total
     * @return
     */
    public long getTotal() {
        return total;
    }

    /**
     * Add a reward
     * @param r the reward
     */
    public void add(Reward r) {
        SprintReportCell cell = getRelevantCell(r.getFromUser());
        cell.add(r);
        total += r.getQuantity();
    }

    /**
     * Substracts the reward
     * @param r the reward
     */
    public void substract(Reward r) {
        SprintReportCell cell = getRelevantCell(r.getFromUser());
        cell.substract(r);
        total -= r.getQuantity();
    }

    /**
     * Substracts the reward on the to user
     * @param r the reward
     */
    public void substractInverse(Reward r) {
        SprintReportCell cell = getRelevantCell(r.getToUser());
        cell.substract(r);
        total -= r.getQuantity();
    }

    /**
     * Add a reward without specifying where it came from
     * @param fromUser the from user
     * @param quantity the quantity
     */
    public void add(String fromUser, long quantity) {
        SprintReportCell cell = getRelevantCell(fromUser);
        cell.add(quantity);
        total += quantity;
    }

    /**
     * Substracts a reward without specifying where it came from
     * @param fromUser the from user
     * @param quantity the quantity
     */
    public void substract(String fromUser, long quantity) {
        SprintReportCell cell = getRelevantCell(fromUser);
        cell.substract(quantity);
        total -= quantity;
    }

    /**
     * Gets the quantity a user has to give to the current user
     * @param fromUser the user
     * @return the quantity
     */
    public long getQuantity(String fromUser) {
        SprintReportCell cell = getRelevantCell(fromUser);
        return cell.getQuantity();
    }

    private SprintReportCell getRelevantCell(String user) {
        SprintReportCell cell = cells.get(user);
        if(cell == null) {
            cell = new SprintReportCell();
            cells.put(user, cell);
        }
        return cell;
    }

    public List<Reward> getPositiveRewards(String fromUser) {
        SprintReportCell cell = getRelevantCell(fromUser);
        return cell.getPositiveRewards();
    }

    public List<Reward> getNegativeRewards(String fromUser) {
        SprintReportCell cell = getRelevantCell(fromUser);
        return cell.getNegativeRewards();
    }
}
