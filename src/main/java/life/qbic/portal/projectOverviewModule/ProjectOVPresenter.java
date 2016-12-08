package life.qbic.portal.projectOverviewModule;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.Like;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;

import java.util.HashMap;

import life.qbic.portal.MasterPresenter;
import org.apache.commons.logging.Log;

/**
 * This presenter class will connect the UI with the underlying
 * logic. As this module will display the projectmanager database
 * content, the presenter will request data from the model, which handles
 * the SQL connection and contains the information to be shown.
 * Issues and Errors will be directed to the user via a notification message.
 */
public class ProjectOVPresenter{

    private final ProjectContentModel contentModel;

    private final Log log;

    private final ProjectOverviewModule overViewModule;

    private final ObjectProperty<Boolean> overviewModuleChanged = new ObjectProperty<>(true);

    public ProjectOVPresenter(ProjectContentModel model,
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

        overViewModule.getOverviewGrid().setContainerDataSource(contentModel.getTableContent());

        overViewModule.getOverviewGrid().isChanged.addValueChangeListener(this::triggerViewPropertyChanged);

        renderTable();

    }

    private void renderTable(){
        overViewModule.columnList = overViewModule.getOverviewGrid().getColumns();
        overViewModule.columnList.forEach((Grid.Column column) -> {
            String colName = column.getPropertyId().toString();
            if (colName.contains("Date") || overViewModule.columnHide.contains(colName)) {
                overViewModule.getOverviewGrid().removeColumn(colName);
            }
        });

        ColumnFieldTypes.clearFromParents();    // Clear from parent nodes (when reloading page)
        setFieldType("projectStatus", ColumnFieldTypes.PROJECTSTATUS);
        setFieldType("projectRegistered", ColumnFieldTypes.PROJECTREGISTERED);
        setFieldType("barcodeSent", ColumnFieldTypes.BARCODESENT);
        setFieldType("dataProcessed", ColumnFieldTypes.DATAPROCESSED);
        setFieldType("dataAnalyzed", ColumnFieldTypes.DATAANALYZED);
        setFieldType("reportSent", ColumnFieldTypes.REPORTSENT);

        overViewModule.getOverviewGrid().setCellStyleGenerator(cellReference -> {
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
            overViewModule.getOverviewGrid().getColumn(columnID).setEditorField(fieldType);
        } catch (Exception exp){
            log.error(String.format("Could not set editor field %s. Reason: %s", columnID, exp.getMessage()));
        }
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

    void triggerViewPropertyChanged(Property.ValueChangeEvent event){
        this.overviewModuleChanged.setValue(false ? overviewModuleChanged.getValue() : true);
    }

    public ObjectProperty<Boolean> getIsChangedFlag(){
        return this.overviewModuleChanged;
    }


}