package life.qbic.portal.projectsStatsModule;

import life.qbic.portal.database.ProjectDatabase;
import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.database.QuerryType;
import life.qbic.portal.database.WrongArgumentSettingsException;
import life.qbic.portal.projectOverviewModule.ProjectContentModel;

import java.sql.SQLException;

/**
 * Created by spaethju on 12.04.17.
 */
public class ProjectsStatsModel {

    ProjectDatabaseConnector projectDatabaseConnector;
    double totalProjects, overdueProjects;

    public ProjectsStatsModel(ProjectDatabaseConnector projectDatabaseConnector){
        this.projectDatabaseConnector = projectDatabaseConnector;
    }

    public final void init() {

    }
}
