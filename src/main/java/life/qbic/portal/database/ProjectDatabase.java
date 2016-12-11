package life.qbic.portal.database;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

import java.sql.SQLException;

/**
 * Created by sven on 12/11/16.
 */
public class ProjectDatabase implements ProjectDatabaseConnector {

    private final String tableName = "projectsoverview";

    private String primaryKey = "projectID";

    private String driverName = "com.mysql.jdbc.Driver";

    private JDBCConnectionPool pool;

    private ProjectDatabase(){}

    public ProjectDatabase(String user, String password){}

    @Override
    public void connectToDatabase() throws IllegalArgumentException, SQLException {

    }

    @Override
    public SQLContainer loadCompleteTableData() throws SQLException {
        TableQuery query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);
        return tableContent;
    }

    @Override
    public void setConnectionCredentials(String user, String password) {

    }
}
