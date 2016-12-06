package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import org.junit.Test;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 6/12/16.
 */
public class ProjectContentModelTest {

    SimpleJDBCConnectionPool mockedConnectionPool = mock(SimpleJDBCConnectionPool.class);

    ProjectContentModel projectContentModel = new ProjectContentModel();

    @Test
    public void connectToDB() throws Exception {
        projectContentModel.connectToDB();
    }

    @Test
    public void loadData() throws Exception {

    }

    @Test
    public void getTableContent() throws Exception {

    }

    @Test
    public void getKeyFigures() throws Exception {

    }

}