package life.qbic.portal;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import life.qbic.openbis.openbisclient.OpenBisClient;
import life.qbic.portal.database.ProjectDatabase;
import life.qbic.portal.database.ProjectFilter;
import life.qbic.portal.database.WrongArgumentSettingsException;
import life.qbic.portal.projectFollowerModule.ProjectFollowerModel;
import life.qbic.portal.projectFollowerModule.ProjectFollowerPresenter;
import life.qbic.portal.projectFollowerModule.ProjectFollowerView;
import life.qbic.portal.projectFollowerModule.ProjectFollowerViewImpl;
import life.qbic.portal.projectOverviewModule.ProjectContentModel;
import life.qbic.portal.projectOverviewModule.ProjectOVPresenter;
import life.qbic.portal.projectOverviewModule.ProjectOverviewModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.vaadin.sliderpanel.SliderPanel;
import org.vaadin.sliderpanel.SliderPanelBuilder;
import org.vaadin.sliderpanel.SliderPanelStyles;
import org.vaadin.sliderpanel.client.SliderMode;
import org.vaadin.sliderpanel.client.SliderTabPosition;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
@PreserveOnRefresh
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

        final VerticalLayout mainFrame = new VerticalLayout();

        final VerticalLayout sliderFrame = new VerticalLayout();

        final VerticalLayout mainContent = new VerticalLayout();

        final ProjectFilter projectFilter = new ProjectFilter();

        final ProjectDatabase projectDatabase = new ProjectDatabase(credentials.get("sqluser"),
                credentials.get("sqlpassword"),
                projectFilter);
        try{
            projectDatabase.connectToDatabase();
        } catch (SQLException exp){
            System.err.println("Could not connect to SQL project database. Reason: " + exp.getMessage());
        }


        final Properties properties = getPropertiesFromFile("/etc/openbis_production.properties");

        final OpenBisClient openBisClient = new OpenBisClient(properties.getProperty("openbisuser"),
                properties.getProperty("openbispw"), properties.getProperty("openbisURI"));


        final ProjectFollowerModel followerModel = new ProjectFollowerModel(projectDatabase);

        final ProjectFollowerView followerView = new ProjectFollowerViewImpl()
                .setSpaceCaption("Institution")
                .setProjectCaption("Project")
                .build();

        final OpenBisConnection openBisConnection = new OpenBisConnection();

        if(!openBisConnection.initConnection(openBisClient)){
            Notification.show("Could not connect to openBis!");
        }

        final ProjectFollowerPresenter followerPresenter = new ProjectFollowerPresenter(followerView, followerModel, openBisConnection);
        followerPresenter.setUserID("zxmqp08").setSQLTableName("followingprojects").setPrimaryKey("id");

        try{
            followerPresenter.startOrchestration();
        } catch (SQLException exp){
            System.err.println("Could not connect to target SQL database. Reason: " + exp.getMessage());
            exp.printStackTrace();
        } catch (WrongArgumentSettingsException exp){
            System.err.println("You provided the wrong arguments for the openbis connection. Reason:" + exp.getMessage());
        } catch (Exception exp){
            System.err.println("Un enexpected Exception occured.");
            exp.printStackTrace();
        }

        final ProjectContentModel model = new ProjectContentModel(projectDatabase, followerModel.getAllFollowingProjects());

        final PieChartStatusModule pieChartStatusModule = new PieChartStatusModule();

        final ProjectOverviewModule projectOverviewModule = new ProjectOverviewModule();

        final ProjectOVPresenter projectOVPresenter = new ProjectOVPresenter(model, projectOverviewModule, log);


        final MasterPresenter masterPresenter = new MasterPresenter(pieChartStatusModule,
                projectOVPresenter, followerPresenter, projectFilter);


        final SliderPanel sliderPanel = new SliderPanelBuilder(followerView.getUI())
                .caption("FOLLOW PROJECTS")
                .mode(SliderMode.TOP)
                .tabPosition(SliderTabPosition.MIDDLE)
                .style(SliderPanelStyles.COLOR_WHITE)
                .animationDuration(100).build();

        //ChartOptions.get().setTheme(new GridTheme());

        sliderFrame.addComponent(sliderPanel);
        mainContent.addComponent(pieChartStatusModule);
        mainContent.addComponent(projectOverviewModule);
        mainFrame.addComponent(sliderFrame);
        mainFrame.addComponent(mainContent);

        mainFrame.setExpandRatio(mainContent, 1);
        mainFrame.setSizeFull();
        setContent(mainFrame);
    }


    private Map<String, String> getCredentialsFromEnvVariables(){
        final Map<String, String> credentials = new HashMap<>();
        credentials.put("sqluser", System.getProperty("sqluser"));
        credentials.put("sqlpassword", System.getProperty("sqlpassword"));

        if (credentials.get("sqluser") != null && credentials.get("sqlpassword") != null){
            return credentials;
        } else {
            return null;
        }
    }

    private Properties getPropertiesFromFile(String path){
        Properties properties = new Properties();
        try{
            properties.load(Files.newBufferedReader(Paths.get(path)));
        } catch (Exception exp){
            System.err.println(("Could not open or read from properties file!"));
        }

        return properties;
    }


    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = ManagerUI.class, productionMode = false)
    public static class ProjectManagerServlet extends VaadinServlet {
    }
}
