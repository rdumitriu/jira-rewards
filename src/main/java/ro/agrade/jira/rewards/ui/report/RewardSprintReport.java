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
            String hideSprintChooser = request.getParameter("hidelist");

            if(hideSprintChooser == null) {
                List<RewardSprint> rewards = getRewardSprints(username);
                velocityParams.put("rewardSprints", rewards);
            }

            long currentRewardSprintId = 0L;
            if(currentRewardSprintStr != null) {
                try {
                    currentRewardSprintId = Integer.parseInt(currentRewardSprintStr);
                } catch (NumberFormatException e) {}
            }
            if(currentRewardSprintId > 0) {
                RewardSprint sprint = adminService.getRewardSprint(currentRewardSprintId);
                velocityParams.put("currentSprint", new SimpleSprintDescriptor(sprint));
                SprintReport report = createReport(sprint);
                velocityParams.put("rewardSprintReportUsers", toDescriptors(report.getUniqueUsers()));
                velocityParams.put("rewardSprintReportOtherUsers", toDescriptors(report.getIrrelevantUsers()));
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

    private SprintReport createReport(RewardSprint sprint) {
        List<Reward> list = rService.getRewardsForSprint(sprint.getId());
        RewardType rType = getTheOneAndOnlyRewardType();
        RewardSprintReportBuilder reportBuilder = createReportBuilder(rType);
        reportBuilder.init();

        if(list != null) {
            for(Reward r : list) {
                reportBuilder.addReward(r);
            }
        }
        reportBuilder.postProcess(sprint);
        return reportBuilder.getReport();
    }

    private RewardType getTheOneAndOnlyRewardType() {
        List<RewardType> list = adminService.getRewardTypes();
        if(list == null || list.size() == 0) {
            String msg = "No reward types ?!? Plugin not configured!";
            LOG.error(msg);
            throw new RewardException(msg);
        }
        return list.get(0);
    }

    private RewardSprintReportBuilder createReportBuilder(RewardType rewardType) {
        return new ReductiveRewardSprintReportBuilder(rewardType);
    }

    private List<RewardSprint> getRewardSprints(String user) {
        return adminService.getUserRewardSprints(user, new Date());
    }
}
