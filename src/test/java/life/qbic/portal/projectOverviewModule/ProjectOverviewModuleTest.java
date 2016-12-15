package life.qbic.portal.projectOverviewModule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by sven1103 on 14/12/16.
 */
public class ProjectOverviewModuleTest {

    private ProjectOverviewModule projectOverviewModule;

    @Before
    public void setUp(){
        projectOverviewModule = new ProjectOverviewModule();
    }

    @Test
    public void test_content_of_column_hide_set(){
        Assert.assertTrue(projectOverviewModule.columnHide.contains("projectID"));
        Assert.assertTrue(projectOverviewModule.columnHide.contains("investigatorID"));
        Assert.assertTrue(projectOverviewModule.columnHide.contains("instrumentID"));
        Assert.assertTrue(projectOverviewModule.columnHide.contains("offerID"));
        Assert.assertTrue(projectOverviewModule.columnHide.contains("invoice"));
    }

    @Test
    public void test_contructor_variable_set_up(){
        Assert.assertEquals(projectOverviewModule.getComponentCount(), 1);
        Assert.assertNotNull(projectOverviewModule.getOverviewGrid());
        Assert.assertNull(projectOverviewModule.getColumnList());
    }


}