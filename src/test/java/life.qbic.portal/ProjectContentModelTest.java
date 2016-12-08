package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 6/12/16.
 */
public class ProjectContentModelTest {

    ProjectContentModel projectContentModel = new ProjectContentModel();

    FreeformQuery freeformQueryMock = mock(FreeformQuery.class);

    String testUser = "mariadbuser";

    String testPassword = "dZAmDa9-Ysq_Zv1AGygQ";

    @Before
    public void setUp() {
        // Setup up credentials for test instance
        projectContentModel.setPassword("dZAmDa9-Ysq_Zv1AGygQ");
        projectContentModel.setUser("mariadbuser");
    }

    @Test
    public void setPassword() {
        projectContentModel.setPassword("helloWorld");

    }

    @Test
    public void setUser() throws Exception {
        projectContentModel.setUser("dummy");

        final Field field = projectContentModel.getClass().getDeclaredField("user");
        field.setAccessible(true);
        assertEquals("Passwords don't match", field.get(projectContentModel), "dummy");
    }


    @Test
    public void connectToDB() {
        assertTrue(projectContentModel.connectToDB());
    }

    @Test
    public void loadData() throws SQLException, IllegalAccessException {
        projectContentModel.connectToDB();
        projectContentModel.loadData();

        try {
            Field field = projectContentModel.getClass().getDeclaredField("tableContent");
            field.setAccessible(true);
            assertNotNull(field.get(projectContentModel));
            field.setAccessible(false);
        } catch (NoSuchFieldException exp){
            fail();
        }
    }

    @Test
    public void getTableContent() throws Exception {
        assertNull(projectContentModel.getTableContent());
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