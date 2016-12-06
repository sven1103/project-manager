package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 6/12/16.
 */
public class ProjectContentModelTest {

    ProjectContentModel projectContentModel = new ProjectContentModel();

    FreeformQuery freeformQueryMock = mock(FreeformQuery.class);

    @Test
    public void setPassword() throws NoSuchFieldException, IllegalAccessException {
        projectContentModel.setPassword("helloWorld");

        final Field field = projectContentModel.getClass().getDeclaredField("password");
        field.setAccessible(true);
        assertEquals("Passwords don't match", field.get(projectContentModel), "helloWorld");
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