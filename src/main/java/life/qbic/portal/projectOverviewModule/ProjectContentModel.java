package life.qbic.portal.projectOverviewModule;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import life.qbic.portal.database.ProjectDatabaseConnector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by sven on 11/13/16.
 * This class contains the business logic and is connected with the
 * MySQL database which contains all the information of QBiC projects.
 */
public class ProjectContentModel {

    /**
     * Get static logger instance
     */
    private final static Log log =
            LogFactory.getLog(ProjectContentModel.class.getName());

    final private String tableName = "projectsoverview";

    private String primaryKey = "projectID";

    private SQLContainer tableContent;


    private final String queryStatusOpen = String.format("SELECT * FROM %s WHERE projectStatus=\'open\'", tableName);

    private final String queryClosedStatus = String.format("SELECT * FROM %s WHERE projectStatus=\'closed\'", tableName);

    private final String queryProgressStatus = String.format("SELECT * FROM %s WHERE projectStatus=\'in progress\'", tableName);

    private final ProjectDatabaseConnector projectDatabaseConnector;

    public ProjectContentModel(ProjectDatabaseConnector projectDatabaseConnector){
        this.projectDatabaseConnector = projectDatabaseConnector;
    }


    public final void init() throws SQLException, IllegalArgumentException{
        projectDatabaseConnector.connectToDatabase();
        this.tableContent = projectDatabaseConnector.loadCompleteTableData();
    }


    /**
     * Getter for the table content
     * @return The table content
     */
    public final SQLContainer getTableContent(){
        return this.tableContent;
    }

    /**
     * Performs a free form query to the database
     * @param query A SQL query string
     * @return A FreeFormQuery
     * @throws SQLException If the query goes wrong
     */
    private FreeformQuery makeFreeFormQuery(String query) throws SQLException{
        FreeformQuery tmpQuery = new FreeformQuery(query, pool, primaryKey);
        return tmpQuery;
    }

    /**
     * Request the counts for the key figures of
     * the projects status 'open', 'in progress', 'closed'
     * @return A hashmap with key figures and names
     */
    public final HashMap<String, Double> getKeyFigures(){
        double projectsWithOpenStatus;
        double projectsWithClosedStatus;
        double projectsWithInProgressStatus;
        HashMap<String, Double> keyFigures = new HashMap<>();
        try{
            projectsWithOpenStatus = (double) makeFreeFormQuery(this.queryStatusOpen).getCount();
            projectsWithClosedStatus = (double) makeFreeFormQuery(this.queryClosedStatus).getCount();
            projectsWithInProgressStatus = (double) makeFreeFormQuery(this.queryProgressStatus).getCount();
        } catch (SQLException exp){
            log.error(String.format("Could not perform status query. Reason: %s", exp.getMessage()));
            return keyFigures;
        }
        keyFigures.put("closed", projectsWithClosedStatus);
        keyFigures.put("open", projectsWithOpenStatus);
        keyFigures.put("in progress", projectsWithInProgressStatus);
        return keyFigures;
    }

}
