package life.qbic.portal.beans;

import ch.systemsx.cisd.openbis.generic.shared.api.v1.dto.Project;

/**
 * Created by sven on 12/18/16.
 */
public class ProjectToProjectBeanConverter {

    public static ProjectBean convertToProjectBean(Project project){
        ProjectBean newProject = new ProjectBean();

        newProject.setSpace(project.getSpaceCode());
        newProject.setCode(project.getCode());

        return newProject;
    }

}
