package life.qbic.portal;

import life.qbic.openbis.openbisclient.OpenBisClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;

/**
 * Created by sven1103 on 8/12/16.
 */
public class OpenBisConnectionTest{

    final Properties properties = new Properties();

    @Mock
    private OpenBisClient openBisClient;

    private OpenBisConnection openBisConnection;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        openBisConnection = new OpenBisConnection();
        try{
            properties.load(Files.newBufferedReader(Paths.get("/etc/openbis_production.properties")));
        } catch (Exception exp){
            fail("Could not open or read from properties file!");
        }
        Assert.assertNotNull("openbisuser was null", properties.getProperty("openbisuser"));
        Assert.assertNotNull("openbispw was null", properties.getProperty("openbispw"));
        Assert.assertNotNull("openbisURI was null", properties.getProperty("openbisURI"));

    }

    @Test
    public void when_openBis_connection_successful_return_true(){
        assertTrue(openBisConnection.initConnection(openBisClient));
    }

    @Test
    public void when_connection_is_already_there_logout_first(){
        openBisConnection = new OpenBisConnection();
        openBisConnection.initConnection(openBisClient);
        Mockito.verify(openBisClient).login();
        openBisConnection.initConnection(openBisClient);
        Mockito.verify(openBisClient).logout();
    }

    @Test
    public void when_openBis_connection_failed_return_false(){
        doThrow(new NullPointerException()).when(openBisClient).login();
        assertFalse(openBisConnection.initConnection(openBisClient));
    }

    @Test
    public void get_list_of_available_projects(){
        openBisConnection = new OpenBisConnection();
        Assert.assertNull(openBisConnection.getListOfProjects());
        openBisConnection.initConnection(openBisClient);
        Assert.assertEquals(0, openBisConnection.getListOfProjects().size());
        openBisConnection.getListOfProjects();
    }

}
