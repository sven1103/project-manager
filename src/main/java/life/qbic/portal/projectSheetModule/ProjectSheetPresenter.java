package life.qbic.portal.projectSheetModule;

import com.vaadin.data.Item;
import life.qbic.portal.database.ColumnTypes;
import life.qbic.portal.database.TableColumns;

import java.util.Date;

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
            fillInContentFromItem(project);
            projectSheetView.showProjectLayout();
        }

    }

    private void fillInContentFromItem(Item item){
        String projectCode = (String) item.getItemProperty(
                TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.PROJECTID)).getValue();
        Date projectRegistered = (Date) item.getItemProperty(
                TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.REGISTRATIONDATE)).getValue();
        projectSheetView.setProjectCode(projectCode);
        projectSheetView.setRegistrationDate(projectRegistered);
    }
}
