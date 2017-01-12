package life.qbic.portal.projectOverviewModule;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.database.QuerryType;
import life.qbic.portal.database.WrongArgumentSettingsException;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sven on 11/13/16.
 * This class contains the business logic and is connected with the
 * MySQL database which contains all the information of QBiC projects.
 */
public class ProjectContentModel{

    private HashMap<String, String> queryArguments = new HashMap<>();

    {
        queryArguments.put("table", "projectsoverview");
    }

    private String primaryKey = "projectID";

    private SQLContainer tableContent;

    private final ProjectDatabaseConnector projectDatabaseConnector;

    private HashMap<String, Double> keyFigures;

    public ProjectContentModel(ProjectDatabaseConnector projectDatabaseConnector){
        this.projectDatabaseConnector = projectDatabaseConnector;
    }

    public final void init() throws SQLException, IllegalArgumentException, WrongArgumentSettingsException{
        projectDatabaseConnector.connectToDatabase();
        this.tableContent = projectDatabaseConnector.loadCompleteTableData(queryArguments.get("table"), primaryKey);
        querryKeyFigures();
    }

    /**
     * Getter for the table content
     * @return The table content
     */
    public final SQLContainer getTableContent(){
        return this.tableContent;
    }


    /**
     * Request the counts for the key figures of
     * the projects status 'open', 'in progress', 'closed'
     */
    private void querryKeyFigures() throws SQLException, WrongArgumentSettingsException {
        double projectsWithOpenStatus;
        double projectsWithClosedStatus;
        double projectsWithInProgressStatus;
        HashMap<String, Double> keyFigures = new HashMap<>();

        projectsWithOpenStatus = (double) projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_OPEN, queryArguments, primaryKey).getCount();
        projectsWithClosedStatus = (double) projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_CLOSED, queryArguments, primaryKey).getCount();
        projectsWithInProgressStatus = (double) projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_INPROGRESS, queryArguments, primaryKey).getCount();

        keyFigures.put("closed", projectsWithClosedStatus);
        keyFigures.put("open", projectsWithOpenStatus);
        keyFigures.put("in progress", projectsWithInProgressStatus);
        this.keyFigures = keyFigures;
    }


    public void updateFigure(){
        try{
            querryKeyFigures();
        } catch (Exception exp){
            exp.printStackTrace();
        }
    }

    public void refresh() throws SQLException, WrongArgumentSettingsException{
        this.tableContent = projectDatabaseConnector.loadCompleteTableData(queryArguments.get("table"), primaryKey);
        querryKeyFigures();
    }


    public Map<String, Double> getKeyFigures(){
        return this.keyFigures;
    }

}
