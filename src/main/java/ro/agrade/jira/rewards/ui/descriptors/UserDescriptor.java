/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 2:00 PM
 */
package ro.agrade.jira.rewards.ui.descriptors;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.user.ApplicationUser;
import ro.agrade.jira.rewards.ui.UiUtils;

/**
 * User descriptor for the UI
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class UserDescriptor{
    private String key;
    private String username;
    private String displayName;
    private String avatarUrl;

    public UserDescriptor(ApplicationUser user){
        this.key = user.getKey();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.avatarUrl = UiUtils.getAvatarUrl(user);
    }

    public UserDescriptor(String userKey){
        ApplicationUser user = ComponentAccessor.getUserManager().getUserByKey(userKey);
        if(user == null){
            return;
        }
        this.key = user.getKey();
        this.username = user.getUsername();
        this.displayName = user.getDisplayName();
        this.avatarUrl = UiUtils.getAvatarUrl(user);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
