package life.qbic.portal;

import javax.servlet.annotation.WebServlet;

import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

import com.vaadin.ui.Notification;

import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import life.qbic.openbis.openbisclient.OpenBisClient;
import life.qbic.portal.database.ProjectDatabase;

import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.projectOverviewModule.ProjectContentModel;
import life.qbic.portal.projectOverviewModule.ProjectOVPresenter;
import life.qbic.portal.projectOverviewModule.ProjectOverviewModule;
import life.qbic.portal.projectSheetModule.ProjectSheetPresenter;
import life.qbic.portal.projectSheetModule.ProjectSheetView;
import life.qbic.portal.projectSheetModule.ProjectSheetViewImplementation;
import life.qbic.portal.projectsTimeLineChart.TimeLineChart;
import life.qbic.portal.projectsTimeLineChart.TimeLineChartPresenter;
import life.qbic.portal.projectsTimeLineChart.TimeLineModel;
import life.qbic.portal.projectsTimeLineChart.TimeLineStats;
import org.apache.commons.collections.map.HashedMap;

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

        final CssLayout statisticsPanel = new CssLayout();

        final ProjectDatabaseConnector projectDatabase = new ProjectDatabase(credentials.get("sqluser"),
                credentials.get("sqlpassword"), projectFilter);

        try{
            projectDatabase.connectToDatabase();
        } catch (SQLException exp){
            System.err.println("Could not connect to SQL project database. Reason: " + exp.getMessage());
        }


        final CssLayout projectDescriptionLayout = new CssLayout();


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

        final ProjectOVPresenter projectOVPresenter = new ProjectOVPresenter(model,
                projectOverviewModule, projectDatabase, openBisConnection, log);


        final ProjectSheetView projectSheetView = new ProjectSheetViewImplementation("Project Sheet");

        final ProjectSheetPresenter projectSheetPresenter = new ProjectSheetPresenter(projectSheetView, projectDatabase, log);

        final TimeLineChart timeLineChart = new TimeLineChart();

        timeLineChart.setTitle("Test statistics");

        final TimeLineStats timeLineModel = new TimeLineModel();

        final TimeLineChartPresenter timeLineChartPresenter = new TimeLineChartPresenter(timeLineModel, timeLineChart);

        final MasterPresenter masterPresenter = new MasterPresenter(pieChartStatusModule,
                projectOVPresenter, projectSheetPresenter, followerPresenter, projectFilter, timeLineChartPresenter);




        projectOverviewModule.setWidth(100, Unit.PERCENTAGE);
        projectOverviewModule.addStyleName("overview-module-style");
        projectDescriptionLayout.setSizeFull();
        projectDescriptionLayout.addComponent(projectOverviewModule);
        projectDescriptionLayout.addComponent(projectSheetView.getProjectSheet());

        projectSheetView.getProjectSheet().setSizeUndefined();

        Responsive.makeResponsive(projectDescriptionLayout);


        final SliderPanel sliderPanel = new SliderPanelBuilder(followerView.getUI())
                .caption("FOLLOW PROJECTS")
                .mode(SliderMode.TOP)
                .tabPosition(SliderTabPosition.MIDDLE)
                .style("slider-format")
                .animationDuration(100).build();


        sliderFrame.addComponent(sliderPanel);
        statisticsPanel.addComponent(pieChartStatusModule);
        statisticsPanel.addComponent(timeLineChart);
        Responsive.makeResponsive(statisticsPanel);

        timeLineChart.setSizeUndefined();
        pieChartStatusModule.setSizeUndefined();

        mainContent.addComponent(statisticsPanel);
        mainContent.addComponent(projectDescriptionLayout);
        mainFrame.addComponent(sliderFrame);
        mainFrame.addComponent(mainContent);

        mainFrame.setExpandRatio(mainContent, 1);
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
