package life.qbic.portal;

/**
 * Created by sven1103 on 8/12/16.
 */

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.commons.logging.Log;

/**
 * This presenter class will connect the UI with the underlying
 * logic. As this module will display the projectmanager database
 * content, the presenter will request data from the model, which handles
 * the SQL connection and contains the information to be shown.
 * Issues and Errors will be directed to the user via a notification message.
 */
class ProjectOVPresenter{

    private final int timeout = 5000;

    private final ProjectContentModel contentModel;

    private final Log log;

    private final ProjectOverviewModule overViewModule;

    ProjectOVPresenter(ProjectContentModel model,
                       ProjectOverviewModule overViewModule,
                       Log log){
        this.contentModel = model;
        this.log = log;
        this.overViewModule = overViewModule;
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
            overViewModule.sendError("Database Error", "Could not connect to database :(");
            return;
        } else{
            overViewModule.sendInfo("Good Job", "Successfully connected to database");
        }

        contentModel.loadData();

        overViewModule.overviewGrid.setContainerDataSource(contentModel.getTableContent());
        renderTable();

    }


    private void renderTable(){
        overViewModule.columnList = overViewModule.overviewGrid.getColumns();
        overViewModule.columnList.forEach((Grid.Column column) -> {
            String colName = column.getPropertyId().toString();
            if (colName.contains("Date") || overViewModule.columnHide.contains(colName)) {
                overViewModule.overviewGrid.removeColumn(colName);
            }
        });

        ColumnFieldTypes.clearFromParents();    // Clear from parent nodes (when reloading page)
        setFieldType("projectStatus", ColumnFieldTypes.PROJECTSTATUS);
        setFieldType("projectRegistered", ColumnFieldTypes.PROJECTREGISTERED);
        setFieldType("barcodeSent", ColumnFieldTypes.BARCODESENT);
        setFieldType("dataProcessed", ColumnFieldTypes.DATAPROCESSED);
        setFieldType("dataAnalyzed", ColumnFieldTypes.DATAANALYZED);
        setFieldType("reportSent", ColumnFieldTypes.REPORTSENT);

        overViewModule.overviewGrid.setCellStyleGenerator(cellReference -> {
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
            overViewModule.overviewGrid.getColumn(columnID).setEditorField(fieldType);
        } catch (Exception exp){
            log.error(String.format("Could not set editor field %s. Reason: %s", columnID, exp.getMessage()));
        }
    }

    private void renderToDate(Grid.Column col){
        col.setRenderer(new DateRenderer(new SimpleDateFormat("EEE, MMM d, YYYY")));
    }

    public HashMap<String, Double> getStatusKeyFigures(){
        return contentModel.getKeyFigures();
    }

    public void setFilter(String column, String filter){
        Container.Filter tmpFilter = new Like(column, filter);
        if(!contentModel.getTableContent().getContainerFilters().contains(tmpFilter)){
            contentModel.getTableContent().removeAllContainerFilters();
            contentModel.getTableContent().addContainerFilter(new Like(column, filter));
        } else{
            contentModel.getTableContent().removeContainerFilter(tmpFilter);
        }

    }

    public void sendError(String caption, String message){
        overViewModule.sendError(caption, message);
    }

    public void sendInfo(String caption, String message){
        overViewModule.sendInfo(caption, message);
    }

}