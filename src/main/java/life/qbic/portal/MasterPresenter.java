package life.qbic.portal;

import com.vaadin.data.Property;
import life.qbic.portal.database.ProjectFilter;
import life.qbic.portal.projectFollowerModule.ProjectFollowerPresenter;
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

    private final ProjectFollowerPresenter projectFollowerPresenter;

    private final ProjectFilter projectFilter;


    private final static Log log =
            LogFactory.getLog(ManagerUI.class.getName());

    MasterPresenter(PieChartStatusModule pieChartStatusModule,
                    ProjectOVPresenter projectOverviewPresenter,
                    ProjectSheetPresenter projectSheetPresenter
                    ProjectFollowerPresenter projectFollowerPresenter,
                    ProjectFilter projectFilter){
        this.pieChartStatusModule = pieChartStatusModule;
        this.projectOverviewPresenter = projectOverviewPresenter;
        this.projectFollowerPresenter = projectFollowerPresenter;
        this.projectSheetPresenter = projectSheetPresenter;
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

        projectOverviewPresenter.getSelectedProject().addValueChangeListener( event ->
                projectSheetPresenter.showInfoForProject(projectOverviewPresenter.getSelectedProjectItem()));

        pieChartStatusModule.addPointClickListener(event -> {
                    projectOverviewPresenter.setFilter("projectStatus", pieChartStatusModule.getDataSeriesObject(event));
                });

        projectOverviewPresenter.getIsChangedFlag().addValueChangeListener(this::refreshModuleViews);

        projectSheetPresenter.getInformationCommittedFlag().addValueChangeListener(this::refreshModuleViews);
      
        projectFollowerPresenter.getIsChangedFlag().addValueChangeListener(this::refreshModuleViews);

        projectFilter.createFilter("projectID", projectFollowerPresenter.getFollowingProjects());

    }

    private void refreshModuleViews(Property.ValueChangeEvent event){

        projectOverviewPresenter.refreshView();
       
        projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);
    }

    private void makeFilter(){
        projectFilter.createFilter("projectID", projectFollowerPresenter.getFollowingProjects());
    }

}
