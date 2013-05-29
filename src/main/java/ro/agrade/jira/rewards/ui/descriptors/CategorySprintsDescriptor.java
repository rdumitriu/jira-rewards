/**
 * Created with IntelliJ IDEA.
 * Date: 5/29/13
 * Time: 12:00 AM
 */
package ro.agrade.jira.rewards.ui.descriptors;

import java.util.*;

/**
 * A set of sprints under the same category
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class CategorySprintsDescriptor {

    public String category;
    public List<SimpleSprintDescriptor> sprints;

    /**
     * Constructor
     * @param category category for all the sprints in here
     * @param sprints the sprints that fall into this category
     */
    public CategorySprintsDescriptor(String category, List<SimpleSprintDescriptor> sprints) {
        this.category = category;
        this.sprints = sprints;
    }
}
