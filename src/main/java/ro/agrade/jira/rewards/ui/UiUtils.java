/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 5:50 PM
 */
package ro.agrade.jira.rewards.ui;

import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.datetime.*;
import com.atlassian.jira.user.ApplicationUser;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Utils for the UI
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class UiUtils {

    /**
     * Get a pretty formatter for the current user
     * @return the formatter
     */
    public static DateTimeFormatter getDateFormatter(DateTimeStyle style){
        if(style == null){
            style = DateTimeStyle.RELATIVE;
        }
        return ComponentAccessor.getComponentOfType(DateTimeFormatterFactory.class)
                                .formatter()
                                .forLoggedInUser()
                                .withStyle(style);
    }

    /**
     * Get a pretty formatter for the current user
     * @return the formatter
     */
    public static DateTimeFormatter getDateFormatter(){
        return getDateFormatter(null);
    }

    /**
     * Formats a date
     * @param d the date
     * @return the formatted date
     */
    public static String formatDate(Date d){
        if(d == null){
            return "";
        }
        return new SimpleDateFormat("dd-MMM-yyyy").format(d);
    }


    /**
     * Gets the user's avatar
     * @param user the user
     * @return the avatar URL
     */
    public static String getAvatarUrl(ApplicationUser user){
        if(user == null){
            return "";
        }
        return ComponentAccessor.getAvatarService()
                                .getAvatarURL(ComponentAccessor.getJiraAuthenticationContext().getUser(),
                                              user)
                                .toString();
    }
}
