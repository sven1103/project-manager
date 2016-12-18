package life.qbic.portal.database;

import ch.systemsx.cisd.openbis.generic.shared.basic.dto.Null;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;


/**
 * Created by sven1103 on 14/12/16.
 */
public class SatusQuerryGeneratorTest {



    @Test
    public void querry_Strings_from_querry_type() throws NoSuchFieldException, WrongArgumentSettingsException{

        SatusQuerryGenerator.class.getDeclaredField("querryMap");
        SatusQuerryGenerator.class.getDeclaredField("followingProjectMap");

        HashMap<String, String> testArguments = new HashMap<>();
        testArguments.put("table", "testTable");
        testArguments.put("user_id", "testUser");

        Assert.assertEquals("SELECT * FROM testTable WHERE projectStatus=\'open\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.PROJECTSTATUS_OPEN, testArguments));
        Assert.assertEquals("SELECT * FROM testTable WHERE projectStatus=\'closed\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.PROJECTSTATUS_CLOSED, testArguments));
        Assert.assertEquals("SELECT * FROM testTable WHERE projectStatus=\'in progress\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.PROJECTSTATUS_INPROGRESS, testArguments));
        Assert.assertEquals("SELECT * FROM testTable WHERE user_id=\'testUser\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.GET_FOLLOWING_PROJECTS, testArguments));
    }

    @Test (expected = WrongArgumentSettingsException.class)
    public void make_errorness_querry_and_get_NullPointerException() throws WrongArgumentSettingsException{
        SatusQuerryGenerator.getQuerryFromType(QuerryType.GET_FOLLOWING_PROJECTS, null);
    }

    @Test (expected = WrongArgumentSettingsException.class)
    public void give_empty_arguments_to_query_and_get_WrongArgumentSettingsException() throws WrongArgumentSettingsException{
        SatusQuerryGenerator.getQuerryFromType(QuerryType.GET_FOLLOWING_PROJECTS, new HashMap());
    }

    @Test (expected = WrongArgumentSettingsException.class)
    public void give_wrong_arguments_to_querry_and_get_WrongArgumentSettingsException() throws WrongArgumentSettingsException{
        HashMap<String, String> testArguments = new HashMap<>();
        testArguments.put("project", "testTable");
        testArguments.put("user_id", "testUser");

        SatusQuerryGenerator.getQuerryFromType(QuerryType.GET_FOLLOWING_PROJECTS, testArguments);

    }
}