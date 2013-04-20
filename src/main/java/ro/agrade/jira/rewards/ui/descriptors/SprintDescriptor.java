/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 1:56 PM
 */
package ro.agrade.jira.rewards.ui.descriptors;

import ro.agrade.jira.rewards.services.RewardSprint;

import java.util.*;

/**
 * Sprint descriptor for the UI
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class SprintDescriptor extends SimpleSprintDescriptor{
    private List<UserDescriptor> guests;
    private List<RewardDescriptor> rewards;

    public SprintDescriptor(RewardSprint s,
                            List<UserDescriptor> guests,
                            List<RewardDescriptor> rewards){
        super(s);
        this.guests = guests;
        this.rewards = rewards;
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
