package life.qbic.portal;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class ManagerUI extends UI {

    /**
     * Get static logger instance
     */
    private final static Log log =
            LogFactory.getLog(ManagerUI.class.getName());

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        log.info("Started project-manager.");

        Map<String, String> credentials = getCredentialsFromEnvVariables();

        if (credentials == null){
            System.err.println("Database login credentials missing from environment");
            System.exit(1);
        }

        final VerticalLayout layout = new VerticalLayout();

        final ProjectContentModel model = new ProjectContentModel();
        model.setPassword(credentials.get("sqlpassword"));
        model.setUser(credentials.get("sqluser"));

        final PieChartStatusModule pieChartStatusModule = new PieChartStatusModule();

        final ProjectOverviewModule projectOverviewModule = new ProjectOverviewModule(model);

        final MasterPresenter masterPresenter = new MasterPresenter(pieChartStatusModule,
                projectOverviewModule);

        layout.addComponent(pieChartStatusModule);
        layout.addComponent(projectOverviewModule);

        layout.setMargin(true);
        layout.setSpacing(true);
        setContent(layout);
    }


    private Map<String, String> getCredentialsFromEnvVariables(){
        final Map<String, String> credentials = new HashedMap();
        credentials.put("sqluser", System.getProperty("sqluser"));
        credentials.put("sqlpassword", System.getProperty("sqlpassword"));

        if (credentials.get("sqluser") != null && credentials.get("sqlpassword") != null){
            return credentials;
        } else {
            return null;
        }
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ManagerUI.class, productionMode = false)
    public static class ProjectManagerServlet extends VaadinServlet {
    }
}
