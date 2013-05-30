/**
 * Created with IntelliJ IDEA.
 * Date: 5/28/13
 * Time: 9:40 PM
 */
package ro.agrade.jira.rewards.utils;

import ro.agrade.jira.rewards.services.RewardSprint;

import java.util.*;

/**
 * Compares sprints by dates NEWEST FIRST!
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class SprintDateComparator implements Comparator<RewardSprint> {

    @Override
    public int compare(RewardSprint s1, RewardSprint s2) {
        if(s2.getWhen() == null){
            return s1.getWhen() == null ? 0 : 1;
        }
        if(s1.getWhen() == null)  {
            return -1;
        }
        return s1.getWhen().compareTo(s2.getWhen());
    }
}
