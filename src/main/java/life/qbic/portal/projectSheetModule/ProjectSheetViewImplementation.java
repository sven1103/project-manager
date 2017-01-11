package life.qbic.portal.projectSheetModule;


import com.vaadin.ui.*;

import java.util.Date;

/**
 * Created by sven1103 on 9/01/17.
 */
public class ProjectSheetViewImplementation implements ProjectSheetView{

    private Panel projectSheet;

    private String projectCode;

    private Label projectCodeLabel;

    private Date registrationDate;

    private DateField registrationDateField;

    private Button saveButton;


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
        projectCodeLabel = new Label();
        registrationDateField = new PopupDateField();
        registrationDateField.setDateFormat("yyyy-MM-dd");
        saveButton = new Button("Save");

        projectCodeLabel.setCaption("Project Code");
        registrationDateField.setCaption("Registration Date");
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
        projectCodeLabel.setValue(this.projectCode);

        projectLayout.addComponent(projectCodeLabel);
        projectLayout.addComponent(registrationDateField);
        projectLayout.addComponent(saveButton);
        projectLayout.addStyleName("myformlayout-sheetcontent");

        this.projectSheet.setContent(projectLayout);
    }

    @Override
    public void setRegistrationDate(Date date) {
        this.registrationDate = date;
        this.registrationDateField.setValue(date);
    }

    @Override
    public Button getSaveButton() {
        return this.saveButton;
    }

    @Override
    public DateField getRegistrationDateField() {
        return this.registrationDateField;
    }
}
