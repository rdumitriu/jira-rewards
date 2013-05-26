/*
 * Created on 5/21/13
 */
package ro.agrade.jira.rewards.ui.report;

import java.util.*;

import ro.agrade.jira.rewards.services.Reward;

/**
 * The cell. Keeps track of each reward and how it relates to the quantity
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class SprintReportCell {
    private long quantity;
    private List<Reward> positiveRewards;
    private List<Reward> negativeRewards;

    /**
     * Creates an empty cell
     */
    public SprintReportCell() {
        this.quantity = 0L;
        this.positiveRewards = new ArrayList<Reward>();
    }

    /**
     * The quantity
     * @return the quantity
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * Gets the positive rewards
     * @return the positive rewards
     */
    public List<Reward> getPositiveRewards() {
        return positiveRewards;
    }

    /**
     * Gets the negative rewards
     * @return the negative rewards
     */
    public List<Reward> getNegativeRewards() {
        return negativeRewards;
    }

    /**
     * Adds a reward
     * @param r the reward
     */
    public void add(Reward r) {
        add(r.getQuantity());
        positiveRewards.add(r);
    }

    /**
     * Substracts a reward
     * @param r the reward
     */
    public void substract(Reward r) {
        substract(r.getQuantity());
        negativeRewards.add(r);
    }

    /**
     * Adds a quantity
     * @param quantity the quantity
     */
    public void add(long quantity) {
        this.quantity += quantity;
    }

    /**
     * Substracts a quantity
     * @param quantity the quantity
     */
    public void substract(long quantity) {
        this.quantity -= quantity;
    }
}
