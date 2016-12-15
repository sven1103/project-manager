package life.qbic.portal;

import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Created by sven1103 on 8/12/16.
 */
public class OpenBisConnectionTest{

    final Properties properties = new Properties();

    @Before
    public void setUp() {
        try{
            properties.load(Files.newBufferedReader(Paths.get("/etc/openbis.properties")));
        } catch (Exception exp){
            fail("Could not open or read from properties file!");
        }
    }

    @Test
    public void when_openBis_connection_successful_return_true(){
        OpenBisConnection openBisConnection = new OpenBisConnection();
        assertTrue(openBisConnection.initConnection(properties.getProperty("openbisuser"),
                properties.getProperty("testPassword"), properties.getProperty("openbisURI")));
    }

}
