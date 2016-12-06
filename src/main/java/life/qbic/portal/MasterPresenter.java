package life.qbic.portal;

import com.vaadin.data.Property;
import com.vaadin.ui.Upload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is a master presenter class and
 * helps to communicate between the different
 * modules
 */
public class MasterPresenter {

    private final PieChartStatusModule pieChartStatusModule;

    private final ProjectOverviewModule projectOverviewModule;

    private final static Log log =
            LogFactory.getLog(ManagerUI.class.getName());

    MasterPresenter(PieChartStatusModule pieChartStatusModule,
                    ProjectOverviewModule projectOverviewModule){
        this.pieChartStatusModule = pieChartStatusModule;
        this.projectOverviewModule = projectOverviewModule;

        init();
    }

    private void init(){
        try{
            projectOverviewModule.init();
            log.info("Init projectoverview module successfully.");
        } catch (Exception exp){
            log.fatal("Init of projectoverview module failed. Reeason: " + exp.getMessage());
            projectOverviewModule.sendError("Project Overview Module failed.", exp.getMessage());
        }

        projectOverviewModule.getPresenter().getStatusKeyFigures().forEach(pieChartStatusModule::update);
        pieChartStatusModule.addPointClickListener(event -> {
                projectOverviewModule.getPresenter().setFilter("projectStatus", pieChartStatusModule.getDataSeriesObject(event));

        });
        projectOverviewModule.getOverviewGrid().isChanged.addValueChangeListener(this::refreshModuleViews);
    }

    public void refreshModuleViews(Property.ValueChangeEvent event){
        projectOverviewModule.getPresenter().getStatusKeyFigures().forEach(pieChartStatusModule::update);
    }

}
