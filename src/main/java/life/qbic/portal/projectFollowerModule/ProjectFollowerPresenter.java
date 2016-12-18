package life.qbic.portal.projectFollowerModule;

import com.vaadin.data.util.BeanItemContainer;
import life.qbic.portal.MyGrid;
import life.qbic.portal.OpenBisConnection;
import life.qbic.portal.beans.ProjectBean;
import life.qbic.portal.database.WrongArgumentSettingsException;

import java.sql.SQLException;
import java.util.Set;

/**
 * Created by sven on 12/18/16.
 */
public class ProjectFollowerPresenter {

    private final ProjectFollowerView view;
    private final ProjectFollowerModel model;
    private final OpenBisConnection connection;
    private String sqlTableName;
    private String userID;
    private String primaryKey;

    public ProjectFollowerPresenter(ProjectFollowerView view, ProjectFollowerModel model,
                                    OpenBisConnection openBisConnection){
        this.view = view;
        this.model = model;
        this.connection = openBisConnection;
    }


    public void startOrchestration() throws NullPointerException, SQLException, WrongArgumentSettingsException{

        MyGrid projectGrid = view.getProjectGrid();

        BeanItemContainer<ProjectBean> projectBeanBeanItemContainer = connection.getListOfProjects();

        Set<String> followingProjects = model.loadFollowingProjects(sqlTableName, userID, primaryKey).getAllFollowingProjects();

        if (projectBeanBeanItemContainer.size() > 0){
            projectGrid.setContainerDataSource(projectBeanBeanItemContainer);
        }

    }

    public ProjectFollowerPresenter setSQLTableName(String tableName){
        this.sqlTableName = tableName;
        return this;
    }

    public ProjectFollowerPresenter setUserID(String userID){
        this.userID = userID;
        return this;
    }

    public ProjectFollowerPresenter setPrimaryKey(String primaryKey){
        this.primaryKey = primaryKey;
        return this;
    }

}
