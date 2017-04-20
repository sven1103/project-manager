package life.qbic.portal.projectsStatsModule;

import com.vaadin.data.util.BeanItemContainer;
import life.qbic.portal.OpenBisConnection;
import life.qbic.portal.beans.ProjectBean;
import life.qbic.portal.database.WrongArgumentSettingsException;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by spaethju on 12.04.17.
 */
public class ProjectsStatsPresenter {

    private ProjectsStatsModel model;
    private ProjectsStatsView view;
    private String sqlTableName;
    private String userID;
    private String primaryKey;
    private List<String> followingProjects;
    private Integer overdueProjects;
    private OpenBisConnection connection;

    public ProjectsStatsPresenter(ProjectsStatsModel model, ProjectsStatsView view, OpenBisConnection connection) {
        this.model = model;
        this.view = view;
        this.connection = connection;
    }

    public void init() throws SQLException, WrongArgumentSettingsException {
        followingProjects = model.loadFollowingProjects(userID, primaryKey);
        overdueProjects = model.getNumberOfOverdueProjects(userID);
        view.setNumberOfTotalProjects(followingProjects.size());
        view.setNumberOfOverdueProjects(overdueProjects);


    }

    private void refreshProjects() throws SQLException, WrongArgumentSettingsException {
        this.followingProjects = model.loadFollowingProjects(userID, primaryKey);
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
}
