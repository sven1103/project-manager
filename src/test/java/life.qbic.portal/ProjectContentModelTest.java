package life.qbic.portal;

import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.projectOverviewModule.ProjectContentModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 6/12/16.
 */
public class ProjectContentModelTest {

    @Mock
    ProjectDatabaseConnector projectDatabaseConnector;

    ProjectContentModel projectContentModel;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        projectContentModel = new ProjectContentModel(projectDatabaseConnector);
    }

    @Test (expected = SQLException.class)
    public void connection_to_database_throws_SQLException() throws IllegalArgumentException, SQLException{
        doThrow(new SQLException()).when(projectDatabaseConnector).connectToDatabase();
        projectContentModel.init();
        fail("SQL Exception should have been thrown.");
    }

    @Test (expected = IllegalArgumentException.class)
    public void connection_to_database_throws_IllegalArgumentException() throws  IllegalArgumentException, SQLException{
        doThrow(new IllegalArgumentException()).when(projectDatabaseConnector).connectToDatabase();
        projectContentModel.init();
        fail("SQL Exception should have been thrown.");
    }

    @Test
    public void connection_to_database_was_successful() throws IllegalArgumentException, SQLException{
        projectContentModel.init();
    }



}