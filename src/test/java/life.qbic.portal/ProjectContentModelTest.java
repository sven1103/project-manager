package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 6/12/16.
 */
public class ProjectContentModelTest {

    SimpleJDBCConnectionPool mockedConnectionPool = mock(SimpleJDBCConnectionPool.class);

    ProjectContentModel projectContentModel = new ProjectContentModel();

    FreeformQuery freeformQueryMock = mock(FreeformQuery.class);



    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void connectToDB() {
        assertFalse(projectContentModel.connectToDB());
        projectContentModel.setUser("dummy");
        projectContentModel.setPassword("password");
        assertTrue(projectContentModel.connectToDB());
    }

    @Test
    public void loadData() throws Exception {
        projectContentModel.setUser("dummy");
        projectContentModel.setPassword("password");
        projectContentModel.connectToDB();
        assertFalse(projectContentModel.loadData());

    }

    @Test
    public void getTableContent() throws Exception {

    }

    @Test
    public void getKeyFigures() throws Exception {
        projectContentModel.setUser("dummy");
        projectContentModel.setPassword("password");
        projectContentModel.connectToDB();
        when(freeformQueryMock.getCount()).thenReturn(10);
        assertTrue(projectContentModel.getKeyFigures().isEmpty());
    }

}