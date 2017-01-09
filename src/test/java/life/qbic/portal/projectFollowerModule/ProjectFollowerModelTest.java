package life.qbic.portal.projectFollowerModule;

import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import life.qbic.portal.database.ProjectDatabase;
import life.qbic.portal.database.QuerryType;
import life.qbic.portal.database.WrongArgumentSettingsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * Created by sven on 12/17/16.
 */
public class ProjectFollowerModelTest {

    @Mock
    private ProjectDatabase projectDatabase;

    private ProjectFollowerModel projectFollowerModel;


    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        projectFollowerModel = new ProjectFollowerModel(projectDatabase);
    }

    @Test
    public void get_all_following_projects_after_initiation(){
        projectFollowerModel = new ProjectFollowerModel(projectDatabase);
        assertEquals(0, projectFollowerModel.getAllFollowingProjects().size());
    }

    @Test
    public void make_SQL_query_and_fetch_all_following_projects() throws SQLException, WrongArgumentSettingsException{
        projectFollowerModel = new ProjectFollowerModel(projectDatabase);
        HashMap<String, String> arguments = new HashMap<>();
        arguments.put("table", "followertable");
        arguments.put("user_id", "zxmqp08");

        //Stubs
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        FreeformQuery testFreeFormQuerry = Mockito.mock(FreeformQuery.class);

        //Conditional return values of stubs
        Mockito.when(testFreeFormQuerry.getResults(0,0)).thenReturn(resultSet);
        Mockito.when(resultSet.isLast()).thenReturn(true);
        Mockito.when(
                projectDatabase.makeFreeFormQuery(QuerryType.GET_FOLLOWING_PROJECTS, arguments, "primary_id"))
                .thenReturn(testFreeFormQuerry);

        projectFollowerModel.loadFollowingProjects("followertable", "zxmqp08", "primary_id");
        assertEquals(1, projectFollowerModel.getAllFollowingProjects().size());
    }


}
