package life.qbic.portal;

import com.vaadin.data.Property;
import life.qbic.portal.database.ProjectFilter;
import life.qbic.portal.projectFollowerModule.ProjectFollowerPresenter;
import life.qbic.portal.projectOverviewModule.ProjectOVPresenter;
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

    private final ProjectFollowerPresenter projectFollowerPresenter;

    private final ProjectFilter projectFilter;

    private final static Log log =
            LogFactory.getLog(ManagerUI.class.getName());

    MasterPresenter(PieChartStatusModule pieChartStatusModule,
                    ProjectOVPresenter projectOverviewPresenter,
                    ProjectFollowerPresenter projectFollowerPresenter,
                    ProjectFilter projectFilter){
        this.pieChartStatusModule = pieChartStatusModule;
        this.projectOverviewPresenter = projectOverviewPresenter;
        this.projectFollowerPresenter = projectFollowerPresenter;
        this.projectFilter = projectFilter;

        init();
    }

    private void init(){
        makeFilter();

        try{
            projectOverviewPresenter.init();
            log.info("Init projectoverview module successfully.");
        } catch (Exception exp){
            log.fatal("Init of projectoverview module failed. Reason: " + exp.getMessage());
            projectOverviewPresenter.sendError("Project Overview Module failed.", exp.getMessage());
        }

        projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);
        pieChartStatusModule.addPointClickListener(event -> {
                    projectOverviewPresenter.setFilter("projectStatus", pieChartStatusModule.getDataSeriesObject(event));
                });

        projectOverviewPresenter.getIsChangedFlag().addValueChangeListener(this::refreshModuleViews);

        projectFollowerPresenter.getIsChangedFlag().addValueChangeListener(this::refreshModuleViews);

        projectFilter.createFilter("projectID", projectFollowerPresenter.getFollowingProjects());

    }

    private void refreshModuleViews(Property.ValueChangeEvent event){
        makeFilter();
        projectOverviewPresenter.refresh();
        projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);
    }

    private void makeFilter(){
        projectFilter.createFilter("projectID", projectFollowerPresenter.getFollowingProjects());
    }

}
