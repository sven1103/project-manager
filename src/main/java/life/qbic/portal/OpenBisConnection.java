package life.qbic.portal;

import life.qbic.openbis.openbisclient.OpenBisClient;

/**
 * Created by sven1103 on 8/12/16.
 */
public class OpenBisConnection {

    private OpenBisClient openBisClient;

    public boolean initConnection(String user, String password, String uri) {

        if (openBisClient != null){
            openBisClient.logout();
        } else{
            try{
                openBisClient = new OpenBisClient(user, password, uri);
            } catch (Exception exp){

                return false;
            }
        }
        return true;
    }
}
