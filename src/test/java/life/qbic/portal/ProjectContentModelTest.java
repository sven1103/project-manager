package life.qbic.portal;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.database.ProjectFilter;
import life.qbic.portal.database.QuerryType;
import life.qbic.portal.database.WrongArgumentSettingsException;
import life.qbic.portal.projectOverviewModule.ProjectContentModel;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.ArrayList;
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

    HashMap<String, String> queryArguments = new HashMap<>();

    @Mock
    FreeformQuery testQuerry;


    @Before
    public void setUp() throws SQLException, WrongArgumentSettingsException {
        MockitoAnnotations.initMocks(this);
        ArrayList filter = new ArrayList();
        projectContentModel = new ProjectContentModel(projectDatabaseConnector, filter);

        queryArguments.put("table", "projectsoverview");
        when(testQuerry.getCount()).thenReturn(1);
        when(projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_OPEN, queryArguments, "projectID")).thenReturn(testQuerry);
        when(projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_INPROGRESS, queryArguments, "projectID")).thenReturn(testQuerry);
        when(projectDatabaseConnector.makeFreeFormQuery(QuerryType.PROJECTSTATUS_CLOSED, queryArguments, "projectID")).thenReturn(testQuerry);

    }

    @Test (expected = SQLException.class)
    public void connection_to_database_throws_SQLException() throws IllegalArgumentException, SQLException, WrongArgumentSettingsException{
        doThrow(new SQLException()).when(projectDatabaseConnector).connectToDatabase();
        projectContentModel.init();
        verify(projectDatabaseConnector).connectToDatabase();
        fail("SQL Exception should have been thrown.");
    }

    @Test (expected = IllegalArgumentException.class)
    public void connection_to_database_throws_IllegalArgumentException() throws  IllegalArgumentException, SQLException, WrongArgumentSettingsException{
        doThrow(new IllegalArgumentException()).when(projectDatabaseConnector).connectToDatabase();
        projectContentModel.init();
        verify(projectDatabaseConnector).connectToDatabase();
        fail("SQL Exception should have been thrown.");
    }

    @Test
    public void connection_to_database_was_successful() throws IllegalArgumentException, SQLException, NullPointerException, WrongArgumentSettingsException{
        projectContentModel.init();
    }

    @Test
    public void load_data_from_SQL_database_was_successful() throws SQLException, NullPointerException, WrongArgumentSettingsException{
        projectContentModel.init();
        verify(projectDatabaseConnector).connectToDatabase();
    }

    @Test (expected = SQLException.class)
    public void load_data_from_SQL_database_and_receive_SQLException() throws SQLException, NullPointerException, WrongArgumentSettingsException{
        doThrow(new SQLException()).when(projectDatabaseConnector).loadCompleteTableData("projectsoverview", "projectID");
        projectContentModel.init();
        fail("SQL connection should have been thrown here.");
        verify(projectDatabaseConnector).loadCompleteTableData("projectsoverview", "projectID");
    }

    @Test
    public void load_data_and_receive_SQLcontainer_object() throws  SQLException, NullPointerException, WrongArgumentSettingsException{
        SQLContainer testContainer = mock(SQLContainer.class);
        when(projectDatabaseConnector.loadCompleteTableData("projectsoverview", "projectID")).thenReturn(testContainer);
        projectContentModel.init();
        Assert.assertNotNull(projectContentModel.getTableContent());
        verify(projectDatabaseConnector).loadCompleteTableData("projectsoverview", "projectID");
    }

    @Test
    public void perform_free_form_query_and_succeed() throws SQLException, WrongArgumentSettingsException{

        projectContentModel.init();
        Assert.assertEquals("The count of the query ", Double.valueOf(1.0), projectContentModel.getKeyFigures().get("in progress"));
        verify(projectDatabaseConnector).makeFreeFormQuery(QuerryType.PROJECTSTATUS_CLOSED, queryArguments, "projectID");
        verify(projectDatabaseConnector).makeFreeFormQuery(QuerryType.PROJECTSTATUS_INPROGRESS, queryArguments, "projectID");
        verify(projectDatabaseConnector).makeFreeFormQuery(QuerryType.PROJECTSTATUS_OPEN, queryArguments, "projectID");
    }



}