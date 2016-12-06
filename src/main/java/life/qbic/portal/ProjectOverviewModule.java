package life.qbic.portal;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Like;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.DateRenderer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.*;

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

    private final Notification info;
    private final Notification error;
    private final MyGrid overviewGrid;
    private List<Column> columnList;
    private HashSet columnHide = new HashSet<String>(){{
        add("projectID");
        add("investigatorID");
        add("instrumentID");
        add("offerID");
        add("invoice");
    }};


    /**
     * Constructor
     * @param model The business logic
     */
    ProjectOverviewModule(ProjectContentModel model){
        this.contentModel = model;
        this.overviewGrid = new MyGrid();
        this.info = new Notification("", "", Notification.Type.TRAY_NOTIFICATION);
        this.error = new Notification("", "", Notification.Type.ERROR_MESSAGE);
        this.presenter = new ProjectOVPresenter();
    }

    /**
     * Make some init settings
     */
    public void init() throws Exception{
        this.presenter.init();

        this.addComponents(overviewGrid);
        this.setSpacing(true);

        overviewGrid.setCaption("Projects overview");
        overviewGrid.setHeight(50, Unit.PERCENTAGE);
        overviewGrid.setWidth(50, Unit.PERCENTAGE);
        overviewGrid.setEditorEnabled(true);
        //fieldGroup.addCommitHandler(presenter.commitChanges());
        info.setDelayMsec(1000);
        info.setPosition(Position.TOP_CENTER);
    }

    public ProjectOVPresenter getPresenter(){
        return presenter;
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
     * Getter for the grid
     * @return
     */
    MyGrid getOverviewGrid(){
        return this.overviewGrid;
    }

    /**
     * This presenter class will connect the UI with the underlying
     * logic. As this module will display the projectmanager database
     * content, the presenter will request data from the model, which handles
     * the SQL connection and contains the information to be shown.
     * Issues and Errors will be directed to the user via a notification message.
     */
    class ProjectOVPresenter{

        private final int timeout = 5000;

        ProjectOVPresenter(){
        }

        /**
         * Call and validate database connection of the business logic.
         */
        public void init() throws Exception{
            if (contentModel == null){
                log.error("The model was not instantiated yet!");
                return;
            }
            if(!contentModel.connectToDB()){
                sendError("Database Error", "Could not connect to database :(");
                return;
            } else{
                sendInfo("Good Job", "Successfully connected to database");
            }

            contentModel.loadData();

            overviewGrid.setContainerDataSource(contentModel.getTableContent());
            renderTable();

        }


        private void renderTable(){
            columnList = overviewGrid.getColumns();
            columnList.forEach((Column column) -> {
                String colName = column.getPropertyId().toString();
                if (colName.contains("Date") || columnHide.contains(colName)) {
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

            overviewGrid.setCellStyleGenerator(cellReference -> {
                if ("no".equals(cellReference.getValue())){
                    return "v-grid-cell-no";
                }
                if ("in progress".equals(cellReference.getValue())){
                    return "v-grid-cell-progress";
                }
                if ("done".equals(cellReference.getValue())){
                    return "v-grid-cell-done";
                }
                return "v-grid-cell-normal";
            });
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

        public HashMap<String, Double> getStatusKeyFigures(){
            return contentModel.getKeyFigures();
        }

        public void setFilter(String column, String filter){
            Container.Filter tmpFilter = new Like (column, filter);
            if(!contentModel.getTableContent().getContainerFilters().contains(tmpFilter)){
                contentModel.getTableContent().removeAllContainerFilters();
                contentModel.getTableContent().addContainerFilter(new Like(column, filter));
            } else{
                contentModel.getTableContent().removeContainerFilter(tmpFilter);
            }

        }

    }

}
