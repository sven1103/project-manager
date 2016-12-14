package life.qbic.portal.database;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;


public class ProjectDatabaseTest {

    private ProjectDatabase projectDatabase;

    @Mock
    SimpleJDBCConnectionPool testPool;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        projectDatabase = new ProjectDatabase("test", "123");
    }


    @Test
    public void test_presence_of_member_variables() throws NoSuchFieldException{
        projectDatabase.getClass().getDeclaredField("tableName");
        projectDatabase.getClass().getDeclaredField("primaryKey");
        projectDatabase.getClass().getDeclaredField("driverName");
        projectDatabase.getClass().getDeclaredField("connectionURI");
        projectDatabase.getClass().getDeclaredField("pool");
        projectDatabase.getClass().getDeclaredField("user");
        projectDatabase.getClass().getDeclaredField("password");
    }

    @Test
    public void connect_to_database_and_succeed() throws IllegalArgumentException, SQLException{
        projectDatabase.connectToDatabase();
    }

    @Test (expected = RuntimeException.class)
    public void test_loading_of_table_data() throws SQLException, RuntimeException{
        projectDatabase.connectToDatabase();
        projectDatabase.loadCompleteTableData();
    }

    public void make_a_successfull_free_form_query() throws SQLException{

    }



}