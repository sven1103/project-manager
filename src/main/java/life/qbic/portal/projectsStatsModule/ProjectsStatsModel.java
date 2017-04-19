package life.qbic.portal.projectsStatsModule;

import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import life.qbic.portal.database.*;
import life.qbic.portal.projectFollowerModule.ProjectFollowerModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by spaethju on 12.04.17.
 */
public class ProjectsStatsModel {

    private ProjectDatabaseConnector projectDatabase;
    private Integer totalProjects, overdueProjects;
    private HashMap<String, String> querySettings = new HashMap<>();
    private HashMap<String, String> queryArguments = new HashMap<>();
    private List<String> followingProjects = new ArrayList<>();

    private SQLContainer tableContent;

    private String primaryKey = "projectID";

    public ProjectsStatsModel(ProjectDatabaseConnector projectDatabase){
        this.projectDatabase = projectDatabase;


    }

    public List loadFollowingProjects(String sqlTable, String userID, String primaryKey)
            throws SQLException, WrongArgumentSettingsException{

        projectDatabase.connectToDatabase();

        followingProjects.clear();

        querySettings.put("table", sqlTable);
        querySettings.put("user_id", userID);

        FreeformQuery query = projectDatabase.makeFreeFormQuery(QuerryType.GET_FOLLOWING_PROJECTS,
                querySettings, primaryKey);


        query.beginTransaction();
        ResultSet followingProjectsQuery = query.getResults(0,0);
        query.commit();

        followingProjectsQuery.first();

        while(!followingProjectsQuery.isLast()){
            followingProjects.add(followingProjectsQuery.getString("project_id"));
            followingProjectsQuery.next();
        }
        followingProjects.add(followingProjectsQuery.getString("project_id"));

        return followingProjects;


    }

}
