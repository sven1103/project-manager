package life.qbic.portal.projectSheetModule;


import com.vaadin.ui.Panel;

/**
 * Created by sven1103 on 9/01/17.
 */
public class ProjectSheetViewImplementation implements ProjectSheetView{

    private Panel projectSheet;

    public ProjectSheetViewImplementation(){
        this.projectSheet = new Panel();
    }

    public ProjectSheetViewImplementation(String caption){
        this.projectSheet = new Panel(caption);
    }

    @Override
    public Panel getProjectSheet() {
        return projectSheet;
    }
}
