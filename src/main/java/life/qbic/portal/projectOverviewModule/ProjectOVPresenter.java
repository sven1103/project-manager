package life.qbic.portal.projectOverviewModule;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.filter.Like;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.Grid;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.renderers.DateRenderer;
import com.vaadin.ui.themes.ValoTheme;
import life.qbic.portal.database.ColumnTypes;
import life.qbic.portal.database.TableColumns;
import life.qbic.portal.database.WrongArgumentSettingsException;
import org.apache.commons.logging.Log;
import org.vaadin.gridutil.GridUtil;
import org.vaadin.gridutil.cell.GridCellFilter;

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

    private final ObjectProperty<String> selectedProject = new ObjectProperty<>("");

    private Item selectedProjectItem = null;

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
        try{
            contentModel.init();
        } catch (SQLException exp) {
            log.error(exp);
            overViewModule.sendError("Database Error", "Could not connect to database :(");
            return;
        } catch (WrongArgumentSettingsException exp){
            log.error(exp);
            overViewModule.sendError("Database Error", "Could not connect to database");
            return;
        }

        log.info("Successfully connected to database");

        overViewModule.getOverviewGrid().setContainerDataSource(contentModel.getTableContent());

        overViewModule.getOverviewGrid().isChanged.addValueChangeListener(this::triggerViewPropertyChanged);

        overViewModule.getOverviewGrid().addItemClickListener(event -> {
            this.selectedProjectItem = event.getItem();
            this.selectedProject.setValue((String) event.getItem().getItemProperty(TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.PROJECTID)).getValue());
            System.out.println("Selected project changed to: " + this.selectedProject.getValue());
        });

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
        setFieldType("rawDataRegistered", ColumnFieldTypes.RAWDATAREGISTERED);

        overViewModule.getOverviewGrid().getColumn("rawDataRegistered").
                setRenderer(new DateRenderer(new SimpleDateFormat("yyyy-MM-dd")));
        overViewModule.getOverviewGrid().getColumn("rawDataRegistered").setMaximumWidth(250d);
        overViewModule.getOverviewGrid().getColumn("projectID").setMaximumWidth(120d);

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
            if (cellReference.getPropertyId().equals("rawDataRegistered")){
                return GridUtil.ALIGN_CELL_CENTER;
            }
            return "v-grid-cell-normal";
        });

        final GridCellFilter filter = new GridCellFilter(overViewModule.getOverviewGrid());
        initExtraHeaderRow(overViewModule.getOverviewGrid(), filter);
        filter.setTextFilter("projectID", true, true);
        filter.setDateFilter("rawDataRegistered", new SimpleDateFormat("yyyy-MM-dd"), true);
    }

    private void initExtraHeaderRow(final Grid grid, final GridCellFilter filter){
        Grid.HeaderRow firstHeaderRow = grid.prependHeaderRow();
        firstHeaderRow.join("projectID", "offerID", "projectStatus", "barcodeSent", "rawDataRegistered",
                "dataProcessed", "dataAnalyzed", "reportSent", "invoice");
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setSpacing(true);
        firstHeaderRow.getCell("projectID").setComponent(buttonLayout);
        Button clearAllFilters = new Button("clearAllFilters", (Button.ClickListener) clickEvent ->
            filter.clearAllFilters());
        clearAllFilters.setIcon(FontAwesome.TIMES);
        clearAllFilters.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
        buttonLayout.addComponent(clearAllFilters);
    }


    private void setFieldType(String columnID, Field fieldType){
        try{
            overViewModule.getOverviewGrid().getColumn(columnID).setEditorField(fieldType);
        } catch (Exception exp){
            log.error(String.format("Could not set editor field %s. Reason: %s", columnID, exp.getMessage()));
        }
    }


    public Map<String, Double> getStatusKeyFigures(){
        return contentModel.getKeyFigures();
    }

    public void setFilter(String column, String filter){
        Container.Filter tmpFilter = new Like(column, filter);
        if(!contentModel.getTableContent().getContainerFilters().contains(tmpFilter)){
            contentModel.getTableContent().removeContainerFilters("projectStatus");
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

    private void triggerViewPropertyChanged(Property.ValueChangeEvent event){
        this.contentModel.updateFigure();
        this.overviewModuleChanged.setValue(!overviewModuleChanged.getValue());

    }

    public ObjectProperty<Boolean> getIsChangedFlag(){
        return this.overviewModuleChanged;
    }

    public ObjectProperty<String> getSelectedProject() {return this.selectedProject;}

    public Item getSelectedProjectItem() {
        return this.selectedProjectItem;
    }

    /**
     * Refreshes the grid
     */
    public void refreshView() {
        try{
            // First, refresh the model (new SQL query!)
            this.contentModel.refresh();

            int timer = 0;

            /*
            If a content change happens, the editor is active.
            Since we are doing autocommit to the backend database,
            we have to wait until this is finished.
             */
            while(true){
                try{
                    this.overViewModule.getOverviewGrid().cancelEditor();
                } catch (Exception exp){

                }
                if (timer == 5){
                    break;
                } else if(!this.overViewModule.getOverviewGrid().isEditorActive()){
                    break;
                }
                TimeUnit.MILLISECONDS.sleep(500);
                timer++;
            }

            // Second, update the grid
            /*
            The order of the next two lines is crucial!
            Do not change it, otherwise the grid will not
            be refreshed properly
             */
            this.overViewModule.init();
            this.overViewModule.getOverviewGrid().setContainerDataSource(this.contentModel.getTableContent());
        } catch (Exception exc){
            log.error("Could not refresh the project overview model.", exc);
        }

    }
}