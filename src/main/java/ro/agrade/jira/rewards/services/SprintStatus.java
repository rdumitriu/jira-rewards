/*
 * Created on 4/7/13
 */
package ro.agrade.jira.rewards.services;

/**
 * That's the sprint status
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public enum SprintStatus {
    /** A proposed sprint is used just for management, nu rewards on it */
    PROPOSED,
    /** Active sprints gather rewards */
    ACTIVE,
    /** Executed, no rewards will be added anymore and current rewards are nullified */
    EXECUTED
}
