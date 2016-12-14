package life.qbic.portal.database;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

import java.sql.SQLException;

/**
 * Created by sven on 12/11/16.
 */
public class ProjectDatabase implements ProjectDatabaseConnector {

    private final String tableName = "projectsoverview";

    private String primaryKey = "projectID";

    private String driverName = "com.mysql.jdbc.Driver";

    private String connectionURI = "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/project_investigator_db";

    private JDBCConnectionPool pool;

    private String user;

    private String password;

    private ProjectDatabase(){}

    public ProjectDatabase(String user, String password){
        this.user = user;
        this.password = password;
    }

    @Override
    public void connectToDatabase() throws IllegalArgumentException, SQLException {
        pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 2, 5);
    }

    @Override
    public SQLContainer loadCompleteTableData() throws RuntimeException, SQLException {
        TableQuery query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);
        return tableContent;
    }

    @Override
    public FreeformQuery makeFreeFormQuery(QuerryType type) throws SQLException {
        return new FreeformQuery(SatusQuerryGenerator.getQuerryFromType(type), pool, primaryKey);
    }

}
