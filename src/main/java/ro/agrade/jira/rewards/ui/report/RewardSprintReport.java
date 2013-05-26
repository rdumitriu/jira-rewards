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

/**
 * The report on the screen
 *
 * @author Radu Dumitriu (rdumitriu@gmail.com)
 * @since 1.0
 */
public class RewardSprintReport extends HttpServlet {
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

        String currentRewardSprintStr = request.getParameter("currentRewardSprint");
        long currentRewardSprint = 0L;

        response.setContentType("text/html;charset=utf-8");
        Map<String, Object> velocityParams = new HashMap<String, Object>();
        velocityParams.put("soyRenderer", soyRenderer);
        velocityParams.put("rewardSprints", getRewardSprints(username));
        if(currentRewardSprintStr != null) {
            try {
                currentRewardSprint = Integer.parseInt(currentRewardSprintStr);
            } catch (NumberFormatException e) {}
        }
        if(currentRewardSprint > 0) {
            velocityParams.put("rewardSprintReport", createReport(currentRewardSprint));
        }
        renderer.render("velocity/reportSprint.vm", velocityParams, response.getWriter());
    }

    private SprintReport createReport(long currentRewardSprint) {
        List<Reward> list = rService.getRewardsForSprint(currentRewardSprint);
        RewardSprintReportBuilder reportBuilder = createReportBuilder();
        reportBuilder.init();
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
