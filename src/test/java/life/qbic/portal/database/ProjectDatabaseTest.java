package life.qbic.portal.database;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;


public class ProjectDatabaseTest {

    private ProjectDatabase projectDatabase;

    @Mock
    SimpleJDBCConnectionPool testPool;

    private final String dummyUser = "testUser";

    private final String dummyPW = "testpw123";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        Mockito.mock(TableQuery.class);
        Mockito.mock(SQLContainer.class);
        projectDatabase = new ProjectDatabase(dummyUser, dummyPW);
    }


    @Test
    public void test_presence_of_member_variables() throws NoSuchFieldException{
        projectDatabase.getClass().getDeclaredField("driverName");
        projectDatabase.getClass().getDeclaredField("connectionURI");
        projectDatabase.getClass().getDeclaredField("pool");
        projectDatabase.getClass().getDeclaredField("user");
        projectDatabase.getClass().getDeclaredField("password");
    }

    @Test
    public void connect_to_database_and_succeed() throws IllegalArgumentException, SQLException{
        projectDatabase = new ProjectDatabase(dummyUser, dummyPW);
        Assert.assertTrue(projectDatabase.connectToDatabase());
        Assert.assertFalse(projectDatabase.connectToDatabase());
    }

    @Test (expected = RuntimeException.class)
    public void test_loading_of_table_data() throws SQLException, RuntimeException{
        projectDatabase.connectToDatabase();
        projectDatabase.loadCompleteTableData("testTable", "testID");
    }

    public void make_a_successfull_free_form_query() throws SQLException{

    }



}