package life.qbic.portal.projectFollowerModule;

import life.qbic.portal.OpenBisConnection;
import life.qbic.portal.database.ProjectDatabase;
import life.qbic.portal.database.QuerryType;
import life.qbic.portal.database.WrongArgumentSettingsException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sven on 12/17/16.
 */
public class ProjectFollowerModel {

    private final ProjectDatabase projectDatabase;

    private final Set<String> allFollingProjects = new HashSet<>();
    private final HashMap<String, String> querySettings = new HashMap<>();


    public ProjectFollowerModel(ProjectDatabase projectDatabase){
        this.projectDatabase = projectDatabase;
    }


    public Set<String> getAllFollowingProjects(){
        return allFollingProjects;
    }


    public ProjectFollowerModel loadFollowingProjects(String sqlTable, String userID, String primaryKey)
            throws SQLException, WrongArgumentSettingsException{
        projectDatabase.connectToDatabase();

        querySettings.put("table", sqlTable);
        querySettings.put("user_id", userID);

        ResultSet followingProjectsQuery = projectDatabase.makeFreeFormQuery(QuerryType.GET_FOLLOWING_PROJECTS,
                querySettings, primaryKey).getResults(0,0);

        while(!followingProjectsQuery.isLast()){
            allFollingProjects.add(followingProjectsQuery.getString("project_id"));
            followingProjectsQuery.next();
        }
        allFollingProjects.add(followingProjectsQuery.getString("project_id"));

        return this;
    }

}
