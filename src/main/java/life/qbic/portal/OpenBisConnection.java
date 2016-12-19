package life.qbic.portal;

import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.Project;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import life.qbic.openbis.openbisclient.OpenBisClient;
import life.qbic.portal.beans.ProjectBean;
import life.qbic.portal.beans.ProjectToProjectBeanConverter;

import java.util.List;

/**
 * Created by sven1103 on 8/12/16.
 */
public class OpenBisConnection {

    private OpenBisClient openBisClient;

    private BeanItemContainer<ProjectBean> projectBeanBeanItemContainer = new BeanItemContainer<ProjectBean>(ProjectBean.class);

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

    public BeanItemContainer<ProjectBean> getListOfProjects(){
        if (this.openBisClient == null){
            return null;
        }
        if (projectBeanBeanItemContainer.size() > 0) {
            projectBeanBeanItemContainer.removeAllItems();
        }
        openBisClient.listProjects().forEach(project -> {
            projectBeanBeanItemContainer.addBean(
                    ProjectToProjectBeanConverter.convertToProjectBean(project));
        });


        return projectBeanBeanItemContainer;
    }

}
