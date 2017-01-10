package life.qbic.portal.projectSheetModule;

import com.vaadin.data.Item;
import com.vaadin.ui.Label;
import life.qbic.portal.database.ColumnTypes;
import life.qbic.portal.database.TableColumns;

/**
 * Created by sven1103 on 10/01/17.
 */
public class ProjectSheetPresenter {

    final private ProjectSheetView projectSheetView;

    public ProjectSheetPresenter(ProjectSheetView projectSheetView){
        this.projectSheetView = projectSheetView;
    }

    public void showInfoForProject(Item project) {

        if (project == null){
            projectSheetView.setDefaultContent();
        } else{
            projectSheetView.getProjectSheet().setContent(new Label(project.getItemProperty(TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.PROJECTID))));
        }

    }
}
