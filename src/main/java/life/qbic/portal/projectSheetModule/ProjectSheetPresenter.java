package life.qbic.portal.projectSheetModule;

import com.vaadin.ui.Label;

/**
 * Created by sven1103 on 10/01/17.
 */
public class ProjectSheetPresenter {

    final private ProjectSheetView projectSheetView;

    public ProjectSheetPresenter(ProjectSheetView projectSheetView){
        this.projectSheetView = projectSheetView;
    }

    public void showInfoForProject(String project) {

        projectSheetView.getProjectSheet().setContent(new Label(project));

    }
}
