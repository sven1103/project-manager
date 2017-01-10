package life.qbic.portal;

import com.vaadin.data.Property;
import life.qbic.portal.projectOverviewModule.ProjectOVPresenter;
import life.qbic.portal.projectSheetModule.ProjectSheetPresenter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is a master presenter class and
 * helps to communicate between the different
 * modules
 */
public class MasterPresenter {

    private final PieChartStatusModule pieChartStatusModule;

    private final ProjectOVPresenter projectOverviewPresenter;

    private final ProjectSheetPresenter projectSheetPresenter;

    private final static Log log =
            LogFactory.getLog(ManagerUI.class.getName());

    MasterPresenter(PieChartStatusModule pieChartStatusModule,
                    ProjectOVPresenter projectOverviewPresenter,
                    ProjectSheetPresenter projectSheetPresenter){
        this.pieChartStatusModule = pieChartStatusModule;
        this.projectOverviewPresenter = projectOverviewPresenter;
        this.projectSheetPresenter = projectSheetPresenter;

        init();
    }

    private void init(){
        try{
            projectOverviewPresenter.init();
            log.info("Init projectoverview module successfully.");
        } catch (Exception exp){
            log.fatal("Init of projectoverview module failed. Reason: " + exp.getMessage());
            projectOverviewPresenter.sendError("Project Overview Module failed.", exp.getMessage());
        }

        projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);

        projectOverviewPresenter.getSelectedProject().addValueChangeListener( event ->
                projectSheetPresenter.showInfoForProject(projectOverviewPresenter.getSelectedProject().getValue()));

        pieChartStatusModule.addPointClickListener(event -> {
                    projectOverviewPresenter.setFilter("projectStatus", pieChartStatusModule.getDataSeriesObject(event));
                });

        projectOverviewPresenter.getIsChangedFlag().addValueChangeListener(this::refreshModuleViews);
    }

    private void refreshModuleViews(Property.ValueChangeEvent event){
        projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);
    }

}
