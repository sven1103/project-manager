package life.qbic.portal.database;

import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sven on 12/11/16.
 */
public class ProjectDatabase implements ProjectDatabaseConnector {

    private String driverName = "com.mysql.jdbc.Driver";

    private String connectionURI = "jdbc:mysql://portal-testing.am10.uni-tuebingen.de:3306/project_investigator_db";

    private JDBCConnectionPool pool;

    private String user;

    private String password;

    public ProjectDatabase(String user, String password){
        this.user = user;
        this.password = password;
    }

    @Override
    public boolean connectToDatabase() throws IllegalArgumentException, SQLException {
        if (pool == null){
            pool = new SimpleJDBCConnectionPool(driverName, connectionURI, user, password, 5, 10);
            return true;
        }
        return false;

    }

    @Override
    public SQLContainer loadCompleteTableData(String tableName, String primaryKey) throws RuntimeException, SQLException {
        TableQuery query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);
        return tableContent;
    }

    @Override
    public FreeformQuery makeFreeFormQuery(QuerryType type, HashMap arguments, String primaryKey) throws SQLException, WrongArgumentSettingsException{
        //System.out.println(SatusQuerryGenerator.getQuerryFromType(type, arguments));
        return new FreeformQuery(SatusQuerryGenerator.getQuerryFromType(type, arguments), pool, primaryKey);
    }

    @Override
    public JDBCConnectionPool getConnectionPool() {
        return this.pool;
    }

    @Override
    public SQLContainer loadSelectedTableData(String tableName, String primaryKey, String includeValuesFromColumnName, List<String> inclusionValues)
    throws SQLException, RuntimeException{

        Container.Filter qbicIdFilter = new SqlQbicIdFilter(includeValuesFromColumnName, inclusionValues);
        List<Container.Filter> filterList = new ArrayList<>();
        filterList.add(qbicIdFilter);

        TableQuery query = new TableQuery(tableName, pool);
        query.setVersionColumn(primaryKey);
        query.setFilters(filterList);

        SQLContainer tableContent = new SQLContainer(query);
        tableContent.setAutoCommit(true);
        return tableContent;
    }

}
