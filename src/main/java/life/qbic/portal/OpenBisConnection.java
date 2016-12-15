package life.qbic.portal;

import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.Project;
import life.qbic.openbis.openbisclient.OpenBisClient;

import java.util.List;

/**
 * Created by sven1103 on 8/12/16.
 */
public class OpenBisConnection {

    private OpenBisClient openBisClient;

    public boolean initConnection(OpenBisClient openBisClient) {

        if (this.openBisClient != null){
            this.openBisClient.logout();
        }
        try{
            this.openBisClient = openBisClient;
            this.openBisClient.login();
        } catch (Exception exp){
            return false;
        }
        return true;
    }

    public List<Project> getListOfProjects(){
        if (this.openBisClient == null){
            return null;
        }
        return openBisClient.listProjects();
    }



}
