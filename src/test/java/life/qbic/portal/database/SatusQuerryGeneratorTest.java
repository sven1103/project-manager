package life.qbic.portal.database;

import org.junit.Assert;
import org.junit.Test;


/**
 * Created by sven1103 on 14/12/16.
 */
public class SatusQuerryGeneratorTest {

    @Test
    public void querry_Strings_from_querry_type() throws NoSuchFieldException{
        SatusQuerryGenerator.class.getDeclaredField("querryMap");
        Assert.assertEquals("SELECT * FROM test WHERE projectStatus=\'open\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.PROJECTSTATUS_OPEN, "test"));
        Assert.assertEquals("SELECT * FROM test WHERE projectStatus=\'closed\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.PROJECTSTATUS_CLOSED, "test"));
        Assert.assertEquals("SELECT * FROM test WHERE projectStatus=\'in progress\'", SatusQuerryGenerator.getQuerryFromType(QuerryType.PROJECTSTATUS_INPROGRESS, "test"));
    }


}