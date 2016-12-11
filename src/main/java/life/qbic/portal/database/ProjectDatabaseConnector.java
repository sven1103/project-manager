package life.qbic.portal.database;


import com.vaadin.data.util.sqlcontainer.SQLContainer;

import java.sql.SQLException;

/**
 * Created by sven on 12/10/16.
 */
public interface ProjectDatabaseConnector {

    void connectToDatabase() throws IllegalArgumentException, SQLException;

    SQLContainer loadCompleteTableData() throws SQLException;

    void setConnectionCredentials(String user, String password);

}
