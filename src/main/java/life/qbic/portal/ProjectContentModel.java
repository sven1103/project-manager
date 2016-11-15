package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;

/**
 * Created by sven on 11/13/16.
 * This class contains the business logic and is connected with the
 * MySQL database which contains all the information of QBiC projects.
 */
class ProjectContentModel {

    /**
     * Get static logger instance
     */
    private final static Log log =
            LogFactory.getLog(ProjectContentModel.class.getName());

    final String version = "Version 0.1b";
    private JDBCConnectionPool pool;

    final String queryALL = "SELECT * FROM projectsoverview";
    final String primaryKey = "projectID";

    private FreeformQuery query;

    private SQLContainer tableContent;

    public boolean connectToDB(){
        try{
            pool = new SimpleJDBCConnectionPool(
                    "com.mysql.jdbc.Driver",
                    "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/project_investigator_db", "mariadbuser", "dZAmDa9-Ysq_Zv1AGygQ", 2, 5
            );
        } catch (Exception e){
            log.fatal("SQL Connection to database failed!", e);
            return false;
        }
        return true;
    }

    /**
     * Load the complete data from the projectoverview table.
     */
    public boolean loadData(){
        boolean loadingSuccessful = true;

        query = new FreeformQuery(
                queryALL, pool, primaryKey);
        try{
            tableContent = new SQLContainer(query);
            log.info("SQL container successfully loaded.");
        } catch (SQLException e){
            log.error("Could not perform query");
            tableContent = null;
            loadingSuccessful = false;
        } catch (Exception e){
            log.error("Another exception occured", e);
            tableContent = null;
            loadingSuccessful = false;
        }

        return loadingSuccessful;
    }

    public SQLContainer getTableContent(){
        return this.tableContent;
    }









}
