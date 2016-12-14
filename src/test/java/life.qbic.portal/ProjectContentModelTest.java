package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.database.QuerryType;
import life.qbic.portal.projectOverviewModule.ProjectContentModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.HashMap;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 6/12/16.
 */
public class ProjectContentModelTest {

    @Mock
    ProjectDatabaseConnector projectDatabaseConnector;

    ProjectContentModel projectContentModel;

    HashMap<QuerryType, String> dummyKeyFigures = new HashMap<>();

    @Mock
    FreeformQuery testQuerry;


    @Before
    public void setUp() throws SQLException{
        MockitoAnnotations.initMocks(this);
        projectContentModel = new ProjectContentModel(projectDatabaseConnector);
        when(testQuerry.getCount()).thenReturn(1);
        when(projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_OPEN)).thenReturn(testQuerry);
        when(projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_INPROGRESS)).thenReturn(testQuerry);
        when(projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_CLOSED)).thenReturn(testQuerry);

    }

    @Test (expected = SQLException.class)
    public void connection_to_database_throws_SQLException() throws IllegalArgumentException, SQLException{
        doThrow(new SQLException()).when(projectDatabaseConnector).connectToDatabase();
        projectContentModel.init();
        verify(projectDatabaseConnector).connectToDatabase();
        fail("SQL Exception should have been thrown.");
    }

    @Test (expected = IllegalArgumentException.class)
    public void connection_to_database_throws_IllegalArgumentException() throws  IllegalArgumentException, SQLException{
        doThrow(new IllegalArgumentException()).when(projectDatabaseConnector).connectToDatabase();
        projectContentModel.init();
        verify(projectDatabaseConnector).connectToDatabase();
        fail("SQL Exception should have been thrown.");
    }

    @Test
    public void connection_to_database_was_successful() throws IllegalArgumentException, SQLException{
        projectContentModel.init();
    }

    @Test
    public void load_data_from_SQL_database_was_successful() throws SQLException{
        projectContentModel.init();
        verify(projectDatabaseConnector).connectToDatabase();
    }

    @Test (expected = SQLException.class)
    public void load_data_from_SQL_database_and_receive_SQLException() throws SQLException{
        doThrow(new SQLException()).when(projectDatabaseConnector).loadCompleteTableData();
        projectContentModel.init();
        fail("SQL connection should have been thrown here.");
        verify(projectDatabaseConnector).loadCompleteTableData();
    }

    @Test
    public void load_data_and_receive_SQLcontainer_object() throws  SQLException{
        SQLContainer testContainer = mock(SQLContainer.class);
        when(projectDatabaseConnector.loadCompleteTableData()).thenReturn(testContainer);
        projectContentModel.init();
        Assert.assertNotNull(projectContentModel.getTableContent());
        verify(projectDatabaseConnector).loadCompleteTableData();
    }

    @Test
    public void perform_free_form_query_and_succeed() throws SQLException{
        projectContentModel.init();
        Assert.assertEquals("The count of the query ", Double.valueOf(1.0), projectContentModel.getKeyFigures().get("in progress"));
        verify(projectDatabaseConnector).makeFreeFormQuery(QuerryType.PROJECTSTATUS_CLOSED);
        verify(projectDatabaseConnector).makeFreeFormQuery(QuerryType.PROJECTSTATUS_INPROGRESS);
        verify(projectDatabaseConnector).makeFreeFormQuery(QuerryType.PROJECTSTATUS_OPEN);
    }



}