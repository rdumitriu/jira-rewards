/*
 * Created on 5/13/13
 */
package ro.agrade.jira.rewards.ui.report;

import java.util.*;
import java.io.IOException;
import java.net.URI;
import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.atlassian.jira.template.soy.SoyTemplateRendererProvider;
import com.atlassian.sal.api.auth.LoginUriProvider;
import com.atlassian.sal.api.user.UserManager;
import com.atlassian.soy.renderer.SoyTemplateRenderer;
import com.atlassian.templaterenderer.TemplateRenderer;

import ro.agrade.jira.rewards.context.BetterSoyRenderer;
import ro.agrade.jira.rewards.services.*;
import ro.agrade.jira.rewards.ui.descriptors.SimpleSprintDescriptor;
import ro.agrade.jira.rewards.ui.descriptors.UserDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The report on the screen
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardSprintReport extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(RewardSprintReport.class);

    private final UserManager userManager;
    private final LoginUriProvider loginUriProvider;
    private final TemplateRenderer renderer;
    private final RewardService rService;
    private final RewardAdminService adminService;
    private final SoyTemplateRenderer soyRenderer;

    public RewardSprintReport(UserManager userManager,
                              LoginUriProvider loginUriProvider,
                              TemplateRenderer renderer,
                              SoyTemplateRendererProvider soyProvider,
                              RewardService rService,
                              RewardAdminService adminService) {
        this.userManager = userManager;
        this.loginUriProvider = loginUriProvider;
        this.renderer = renderer;
        this.soyRenderer = new BetterSoyRenderer(soyProvider.getRenderer());
        this.rService = rService;
        this.adminService = adminService;
    }

    private void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.sendRedirect(loginUriProvider.getLoginUri(getUri(request)).toASCIIString());
    }

    private URI getUri(HttpServletRequest request) {
        StringBuffer builder = request.getRequestURL();
        if (request.getQueryString() != null) {
            builder.append("?");
            builder.append(request.getQueryString());
        }
        return URI.create(builder.toString());
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws IOException, ServletException {
        String username = userManager.getRemoteUsername(request);
        if (username == null || !userManager.isSystemAdmin(username)) {
            redirectToLogin(request, response);
            return;
        }

        response.setContentType("text/html");
        Map<String, Object> velocityParams = new HashMap<String, Object>();
        velocityParams.put("soyRenderer", soyRenderer);

        try {
            String currentRewardSprintStr = request.getParameter("currentRewardSprintId");
            long currentRewardSprintId = 0L;
            List<RewardSprint> rewards = getRewardSprints(username);
            //remove me
            rewards.add(new RewardSprint(0, "beer sprint 0", "place", "admin", new Date(), SprintStatus.ACTIVE, new HashSet<String>()));
            rewards.add(new RewardSprint(-1, "beer sprint -1", "place", "admin", new Date(), SprintStatus.ACTIVE, new HashSet<String>()));
            //
            velocityParams.put("rewardSprints", rewards);
            if(currentRewardSprintStr != null) {
                try {
                    currentRewardSprintId = Integer.parseInt(currentRewardSprintStr);
                } catch (NumberFormatException e) {}
            }
            if(currentRewardSprintId > 0) {
                velocityParams.put("currentSprint",
                                   new SimpleSprintDescriptor(adminService.getRewardSprint(currentRewardSprintId)));
                SprintReport report = createReport(currentRewardSprintId);
                velocityParams.put("rewardSprintReportUsers", toDescriptors(report.getUniqueUsers()));
                velocityParams.put("rewardSprintReport", report);
            } else if(currentRewardSprintId >= -1) {
                RewardSprint sprtest = new RewardSprint(-1, "beer sprint " + currentRewardSprintId, "place", "admin", new Date(), SprintStatus.ACTIVE, new HashSet<String>());
                velocityParams.put("currentSprint", new SimpleSprintDescriptor(sprtest));
                SprintReport report = createReport(currentRewardSprintId);
                velocityParams.put("rewardSprintReportUsers", toDescriptors(report.getUniqueUsers()));
                velocityParams.put("rewardSprintReport", report);
            }
            renderer.render("velocity/reportSprint.vm", velocityParams, response.getWriter());
        } catch(Exception e) {
            LOG.error("Got exception while rendering report", e);
            renderer.render("velocity/reportSprintError.vm", velocityParams, response.getWriter());
        }
    }

    private List<UserDescriptor> toDescriptors(String[] uniqueUsers) {
        List<UserDescriptor> descriptors = new ArrayList<UserDescriptor>();
        for(String s : uniqueUsers) {
            descriptors.add(new UserDescriptor(s));
        }
        return descriptors;
    }

    private SprintReport createReport(long currentRewardSprint) {
        List<Reward> list = rService.getRewardsForSprint(currentRewardSprint);
        RewardSprintReportBuilder reportBuilder = createReportBuilder();
        reportBuilder.init();
        reportBuilder.addReward(new Reward(1, 1, 0, 2, new Date(), "Two beers if you shoe my horse", "4 iron shoes", "admin", "user1", "Done", -1));
        reportBuilder.addReward(new Reward(2, 1, 0, 1, new Date(), "One beer if you shoot yourself", "For the sake of beer", "admin", "user1", "Done", -1));
        reportBuilder.addReward(new Reward(3, 1, 0, 1, new Date(), "One beer for nothing", "For the sake of beer", "admin", "user2", "Done", -1));
        reportBuilder.addReward(new Reward(3, 1, 0, 4, new Date(), "Four beers in exchange", "For the sake of beer", "user1", "admin", "Done", -1));

        if(list != null) {
            for(Reward r : list) {
                reportBuilder.addReward(r);
            }
        }
        reportBuilder.postProcess();
        return reportBuilder.getReport();
    }

    private RewardSprintReportBuilder createReportBuilder() {
        return new ReductiveRewardSprintReportBuilder();
    }

    private List<RewardSprint> getRewardSprints(String user) {
        return adminService.getUserRewardSprints(user, new Date());
    }
}
