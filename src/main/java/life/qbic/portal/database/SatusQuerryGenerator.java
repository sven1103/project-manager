package life.qbic.portal.database;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sven on 12/11/16.
 */
public class SatusQuerryGenerator {

    private static final Map <QuerryType, String> querryMap = new HashMap<>();

    static{
        querryMap.put(QuerryType.PROJECTSTATUS_OPEN, "SELECT * FROM %s WHERE projectStatus=\'open\'");
        querryMap.put(QuerryType.PROJECTSTATUS_INPROGRESS, "SELECT * FROM %s WHERE projectStatus=\'in progress\'");
        querryMap.put(QuerryType.PROJECTSTATUS_CLOSED, "SELECT * FROM %s WHERE projectStatus=\'closed\'");
    }

    public static final String getQuerryFromType(QuerryType type){
        return querryMap.get(type);
    }

}
