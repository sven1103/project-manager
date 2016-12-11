package life.qbic.portal.database;


import java.sql.SQLException;

/**
 * Created by sven on 12/10/16.
 */
public interface ProjectDatabaseConnector {

    void connectToDatabase() throws IllegalArgumentException, SQLException;

    void setConnectionCredentials(String user, String password);

}
