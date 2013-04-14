/**
 * Created with IntelliJ IDEA.
 * Date: 4/7/13
 * Time: 10:41 PM
 */
package ro.agrade.jira.rewards.context;

import com.atlassian.jira.avatar.Avatar;
import com.atlassian.jira.avatar.AvatarService;
import com.atlassian.jira.plugin.userformat.DefaultUserFormatManager;
import com.atlassian.jira.plugin.userformat.UserFormatter;
import com.atlassian.jira.plugin.userformat.UserFormatterImpl;
import com.atlassian.jira.plugin.webfragment.contextproviders.DefaultVelocityContextProvider;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.jira.user.util.UserUtilImpl;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * Adds the SoyTemplateRenderer into the context
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class SoyContextProvider extends DefaultVelocityContextProvider{

    private static final Log LOG = LogFactory.getLog(SoyContextProvider.class);

    protected final BetterSoyRenderer soyRenderer;
    private JiraAuthenticationContext authenticationContext;

    /**
     * Constructor
     * @param authenticationContext
     * @param soyProvider
     */
    public SoyContextProvider(JiraAuthenticationContext authenticationContext,
                              SoyTemplateRendererProvider soyProvider) {
        super(authenticationContext);
        this.authenticationContext = authenticationContext;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
    }

    @Override
    public Map<String, Object> getContextMap(Map<String, Object> context) {
        Map<String, Object> ctxMap = super.getContextMap(context);
        ctxMap.put("soyRenderer", soyRenderer);
        ctxMap.put("user", authenticationContext.getUser());
        if(LOG.isDebugEnabled()){
            LOG.debug("Passing to context:");
            LOG.debug("================================");
            for(Map.Entry<String,Object> me : ctxMap.entrySet()){
                LOG.debug(String.format("%s : %s", me.getKey(), me.getValue()));
            }
        }
        return ctxMap;
    }
}
