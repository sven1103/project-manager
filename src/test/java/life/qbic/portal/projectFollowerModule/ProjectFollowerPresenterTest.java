package life.qbic.portal.projectFollowerModule;

import com.vaadin.data.util.BeanItemContainer;
import life.qbic.portal.OpenBisConnection;
import life.qbic.portal.beans.ProjectBean;
import life.qbic.portal.database.WrongArgumentSettingsException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.HashSet;

/**
 * Created by sven on 12/18/16.
 */
public class ProjectFollowerPresenterTest {

    @Mock
    private ProjectFollowerView testView;
    @Mock
    private ProjectFollowerModel testModel;
    @Mock
    private OpenBisConnection openBisConnection;

    private ProjectFollowerPresenter projectFollowerPresenter;

    private String userID = "test123";

    private String testTable = "testTable";

    private String primaryKey = "primkey";

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        projectFollowerPresenter = new ProjectFollowerPresenter(testView, testModel, openBisConnection);
    }

    @Test (expected = SQLException.class)
    public void start_orchestration_and_fail_due_to_model() throws SQLException, WrongArgumentSettingsException{
        Mockito.when(testModel.loadFollowingProjects(testTable, userID, primaryKey)).thenReturn(testModel);
        Mockito.doThrow(new SQLException()).when(testModel).loadFollowingProjects(testTable, userID, primaryKey);
        projectFollowerPresenter.setSQLTableName(testTable).setUserID(userID).setPrimaryKey(primaryKey);
        projectFollowerPresenter.startOrchestration();
    }

    @Test
    public void start_orchestration_and_get_empty_project_bean_container() throws SQLException, WrongArgumentSettingsException{

        HashSet<String> testProjectSet = new HashSet<>();
        testProjectSet.add("QHIPP");

        Mockito.when(openBisConnection.getListOfProjects()).thenReturn(new BeanItemContainer<>(ProjectBean.class));
        Mockito.when(testModel.loadFollowingProjects(testTable, userID, primaryKey)).thenReturn(testModel);
        Mockito.when(testModel.getAllFollowingProjects()).thenReturn(testProjectSet);
        projectFollowerPresenter.setSQLTableName(testTable).setUserID(userID).setPrimaryKey(primaryKey);
        projectFollowerPresenter.startOrchestration();
        Mockito.verify(testView).getProjectGrid();
    }


}
