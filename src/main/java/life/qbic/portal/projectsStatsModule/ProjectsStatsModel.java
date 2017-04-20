package life.qbic.portal.projectsStatsModule;

import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import life.qbic.portal.database.*;
import life.qbic.portal.projectFollowerModule.ProjectFollowerPresenter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by spaethju on 12.04.17.
 */
public class ProjectsStatsModel {

    private ProjectDatabaseConnector projectDatabase;
    private List<String> followingProjects = new ArrayList<>();
    private Integer overdueProjects = 0;

    public ProjectsStatsModel(ProjectDatabaseConnector projectDatabase) {
        this.projectDatabase = projectDatabase;
    }

    public List loadFollowingProjects(String userID, String primaryKey)
            throws SQLException, WrongArgumentSettingsException {

        projectDatabase.connectToDatabase();

        followingProjects.clear();

        HashMap<String, String> querySettings = new HashMap<>();
        querySettings.put("table", "followingprojects");
        querySettings.put("user_id", userID);

        FreeformQuery query = projectDatabase.makeFreeFormQuery(QuerryType.GET_FOLLOWING_PROJECTS,
                querySettings, primaryKey);

        query.beginTransaction();
        ResultSet followingProjectsQuery = query.getResults(0, 0);
        query.commit();

        followingProjectsQuery.first();

        while (!followingProjectsQuery.isLast()) {
            followingProjects.add(followingProjectsQuery.getString("project_id"));
            followingProjectsQuery.next();
        }
        followingProjects.add(followingProjectsQuery.getString("project_id"));
        return followingProjects;
    }

    /**
     * Request project timeline statistics
     *
     * @return A map containing values for different categories
     */
    public Integer getNumberOfOverdueProjects(String userID) throws SQLException, WrongArgumentSettingsException {

        projectDatabase.connectToDatabase();

        HashMap<String, String> querySettings = new HashMap<>();
        querySettings.put("table", "projectsoverview");
        querySettings.put("user_id", userID);

        overdueProjects = projectDatabase.makeFreeFormQuery(QuerryType.PROJECT_REGISTERED_RAWDATA,
                querySettings, "projectID", followingProjects).getCount();

        return overdueProjects;
    }
}
