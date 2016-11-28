package life.qbic.portal;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitHandler;
import com.vaadin.server.Page;
import com.vaadin.server.UserError;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.DateRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sven on 11/13/16.
 * This class represents the core of the projectmanager
 * module. It will display the database content of different projects
 * and its progress status.
 */
class ProjectOverviewModule extends VerticalLayout{

    /**
     * Get static logger instance
     */
    private final static Log log =
            LogFactory.getLog(ProjectContentModel.class.getName());

    private final ProjectOVPresenter presenter;
    private final ProjectContentModel contentModel;

    private final Button loadButton;
    private final Notification info;
    private final Notification error;
    private final MyGrid overviewGrid;
    private List<Column> columnList;

    /**
     * Constructor
     * @param model The business logic
     */
    ProjectOverviewModule(ProjectContentModel model){
        this.contentModel = model;
        this.loadButton = new Button("Load Table");
        this.overviewGrid = new MyGrid();
        this.info = new Notification("", "", Notification.Type.TRAY_NOTIFICATION);
        this.error = new Notification("", "", Notification.Type.ERROR_MESSAGE);
        this.presenter = new ProjectOVPresenter();
        init();
    }

    /**
     * Make some init settings
     */
    private void init(){

        this.addComponents(loadButton, overviewGrid);
        this.setSpacing(true);

        overviewGrid.setCaption("Projects overview");
        overviewGrid.setSizeFull();
        overviewGrid.setEditorEnabled(true);
        //fieldGroup.addCommitHandler(presenter.commitChanges());
        info.setDelayMsec(1000);
        info.setPosition(Position.TOP_CENTER);

        loadButton.addClickListener(presenter::loadTable);
    }

    /**
     * Sends an info notification message to the user on the screen.
     * @param caption The caption
     * @param message Your message
     */
    void sendInfo(String caption, String message){
        info.setCaption(caption);
        info.setDescription(message);
        info.show(Page.getCurrent());
    }

    /**
     * Sends an error notification message to the user on the screen.
     * @param caption The caption
     * @param message Your message
     */
    void sendError(String caption, String message){
        error.setCaption(caption);
        error.setDescription(message);
        error.show(Page.getCurrent());
    }

    /**
     * This presenter class will connect the UI with the underlying
     * logic. As this module will display the projectmanager database
     * content, the presenter will request data from the model, which handles
     * the SQL connection and contains the information to be shown.
     * Issues and Errors will be directed to the user via a notification message.
     */
    class ProjectOVPresenter{

        ProjectOVPresenter(){
            init();
        }

        /**
         * Call and validate database connection of the business logic.
         */
        void init(){

            overviewGrid.getEditorFieldGroup().addCommitHandler(new CommitHandler() {
                private static final long serialVersionUID = -8378742499490422335L;

                @Override
                public void preCommit(FieldGroup.CommitEvent commitEvent)
                        throws FieldGroup.CommitException {
                    //
                }

                @Override
                public void postCommit(FieldGroup.CommitEvent commitEvent)
                        throws FieldGroup.CommitException {
                    Notification.show("Saved successfully");
                    overviewGrid.setComponentError(new UserError("This is user error could be empty"));
                }
            });

            if (contentModel == null){
                log.error("The model was not instantiated yet!");
                return;
            }
            if(!contentModel.connectToDB()){
                sendError("Database Error", "Could not connect to database :(");
            } else{
                sendInfo("Good Job", "Successfully connected to database");
            }

            if (!contentModel.loadData()){
                sendError("Sorry", "Could not load the data from the database :(");
                loadButton.setEnabled(false);
            }
        }

        /**
         * Static method reference to buttons
         * @param event A button click event
         */
        void loadTable(Button.ClickEvent event){
            overviewGrid.setContainerDataSource(contentModel.getTableContent());
            renderTable();
        }

        private void renderTable(){
            columnList = overviewGrid.getColumns();
            columnList.forEach((Column column) -> {
                String colName = column.getPropertyId().toString();
                if (colName.contains("Date")) {
                    overviewGrid.removeColumn(colName);
                }
            });
            ColumnFieldTypes.clearFromParents();    // Clear from parent nodes (when reloading page)
            setFieldType("projectStatus", ColumnFieldTypes.PROJECTSTATUS);
            setFieldType("projectRegistered", ColumnFieldTypes.PROJECTREGISTERED);
            setFieldType("barcodeSent", ColumnFieldTypes.BARCODESENT);
            setFieldType("dataProcessed", ColumnFieldTypes.DATAPROCESSED);
            setFieldType("dataAnalyzed", ColumnFieldTypes.DATAANALYZED);
            setFieldType("reportSent", ColumnFieldTypes.REPORTSENT);
        }


        private void setFieldType(String columnID, Field fieldType){
            try{
                overviewGrid.getColumn(columnID).setEditorField(fieldType);
            } catch (Exception exp){
                log.error(String.format("Could not set editor field %s. Reason: %s", columnID, exp.getMessage()));
            }
        }

        private void renderToDate(Column col){
            col.setRenderer(new DateRenderer(new SimpleDateFormat("EEE, MMM d, YYYY")));
        }
    }

}
