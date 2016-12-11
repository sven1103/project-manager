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

    final String version = "Version 0.1b";
    private JDBCConnectionPool pool;

    final private String tableName = "projectsoverview";

    private TableQuery query;

    private String primaryKey = "projectID";

    private SQLContainer tableContent;

    private String driverName = "com.mysql.jdbc.Driver";

    private String connectionURI = "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/project_investigator_db";

    private String password;

    private String user;

    private final String queryStatusOpen = String.format("SELECT * FROM %s WHERE projectStatus=\'open\'", tableName);

    private final String queryClosedStatus = String.format("SELECT * FROM %s WHERE projectStatus=\'closed\'", tableName);

    private final String queryProgressStatus = String.format("SELECT * FROM %s WHERE projectStatus=\'in progress\'", tableName);

    private final ProjectDatabaseConnector projectDatabaseConnector;

    public ProjectContentModel(ProjectDatabaseConnector projectDatabaseConnector){
        this.projectDatabaseConnector = projectDatabaseConnector;
    }

    // TODO Remove this constructor once the databaseconnector works
    public ProjectContentModel(String sqluser, String sqlpassword) {
        this.user = sqluser;
        this.password = sqlpassword;
        this.projectDatabaseConnector = null;
    }

    public final void init() throws SQLException, IllegalArgumentException{
        projectDatabaseConnector.connectToDatabase();
    }

    public final void setPassword(String password){
        this.password = password;
    }

    public final void setUser(String user){
        this.user = user;
    }


    /**
     * Init database connection
     * @return True for success, false for failure
     *//*
    public final boolean connectToDB(){
        try{
            pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 2, 5);
        } catch (Exception e){
            log.fatal("SQL Connection to database failed!", e);
            return false;
        }
        return true;
    }
    */

    public final void connectToDB() throws SQLException{

    }

    /**
     * Load the complete data from the projectoverview table.
     */
     public final void loadData() throws SQLException{

        query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);

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

    public final void setDriverName(String driverName){
        this.driverName = driverName;
    }

    public final void setConnectionURI(String connectionURI){
        this.connectionURI = connectionURI;
    }

}
