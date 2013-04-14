/**
 * Created with IntelliJ IDEA.
 * Date: 4/14/13
 * Time: 8:39 PM
 */
package ro.agrade.jira.rewards.context;

import com.atlassian.soy.renderer.SoyException;
import com.atlassian.soy.renderer.SoyTemplateRenderer;

import java.util.*;

/**
 * Support for injected data without appender parameter
 *
 * @author Florin Manaila (florin.manaila@gmail.com)
 */
public class BetterSoyRenderer implements SoyTemplateRenderer {

    private SoyTemplateRenderer soyRenderer;

    /**
     * Constructor
     * @param soyRenderer the soy renderer to wrap
     */
    public BetterSoyRenderer(SoyTemplateRenderer soyRenderer){
        this.soyRenderer = soyRenderer;
    }

    @Override
    public String render(String completeModuleKey,
                         String templateName,
                         Map<String, Object> data) throws SoyException {
        return soyRenderer.render(completeModuleKey, templateName, data);
    }

    @Override
    public void render(Appendable appendable,
                       String completeModuleKey,
                       String templateName,
                       Map<String, Object> data) throws SoyException {
        soyRenderer.render(appendable, completeModuleKey, templateName, data);
    }

    @Override
    public void render(Appendable appendable,
                       String completeModuleKey,
                       String templateName,
                       Map<String, Object> data,
                       Map<String, Object> injectedData) throws SoyException {
        soyRenderer.render(appendable, completeModuleKey, templateName, data, injectedData);
    }

    public String render(String completeModuleKey,
                         String templateName,
                         Map<String, Object> data,
                         Map<String, Object> injectedData) throws SoyException {
        // because some like to do half the job
        StringBuilder sb = new StringBuilder();
        render(sb, completeModuleKey, templateName, data, injectedData);
        return sb.toString();
    }
}
