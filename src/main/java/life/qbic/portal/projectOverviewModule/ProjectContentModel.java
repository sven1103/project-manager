package life.qbic.portal.projectOverviewModule;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.database.QuerryType;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sven on 11/13/16.
 * This class contains the business logic and is connected with the
 * MySQL database which contains all the information of QBiC projects.
 */
public class ProjectContentModel{

    private SQLContainer tableContent;

    private final ProjectDatabaseConnector projectDatabaseConnector;

    private HashMap<String, Double> keyFigures;

    public ProjectContentModel(ProjectDatabaseConnector projectDatabaseConnector){
        this.projectDatabaseConnector = projectDatabaseConnector;
    }

    public final void init() throws SQLException, IllegalArgumentException{
        projectDatabaseConnector.connectToDatabase();
        this.tableContent = projectDatabaseConnector.loadCompleteTableData();
        this.querryKeyFigures();
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
    private void querryKeyFigures() throws SQLException{
        double projectsWithOpenStatus;
        double projectsWithClosedStatus;
        double projectsWithInProgressStatus;
        HashMap<String, Double> keyFigures = new HashMap<>();

        projectsWithOpenStatus = (double) projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_OPEN).getCount();
        projectsWithClosedStatus = (double) projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_CLOSED).getCount();
        projectsWithInProgressStatus = (double) projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_INPROGRESS).getCount();

        keyFigures.put("closed", projectsWithClosedStatus);
        keyFigures.put("open", projectsWithOpenStatus);
        keyFigures.put("in progress", projectsWithInProgressStatus);
        this.keyFigures = keyFigures;
    }


    public Map<String, Double> getKeyFigures(){
        return this.keyFigures;
    }

}
