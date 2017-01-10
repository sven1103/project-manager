package life.qbic.portal.projectSheetModule;


import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;

/**
 * Created by sven1103 on 9/01/17.
 */
public class ProjectSheetViewImplementation implements ProjectSheetView{

    private Panel projectSheet;

    private String projectCode;


    public ProjectSheetViewImplementation(){
        this.projectSheet = new Panel();
        init();
    }

    public ProjectSheetViewImplementation(String caption){
        this.projectSheet = new Panel(caption);
        init();
    }

    private void init(){
        setDefaultContent();
        projectCode = "";
    }

    @Override
    public Panel getProjectSheet() {
        return projectSheet;
    }

    @Override
    public void setDefaultContent() {
        Label defaultLabel = new Label("Click a project in the " +
                "table to get detailed content here!");
        projectSheet.setContent(defaultLabel);
    }

    @Override
    public void setProjectCode(String id) {
        this.projectCode = id;
    }

    @Override
    public void showProjectLayout() {
        final FormLayout projectLayout = new FormLayout();
        final Label codeLabel = new Label(this.projectCode);

        projectLayout.addComponent(codeLabel);

        this.projectSheet.setContent(projectLayout);
    }
}
