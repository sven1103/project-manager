package life.qbic;

import com.vaadin.data.Property;
import life.qbic.database.ProjectFilter;
import life.qbic.projectFollowerModule.ProjectFollowerPresenter;
import life.qbic.projectOverviewModule.ProjectOVPresenter;
import life.qbic.projectSheetModule.ProjectSheetPresenter;
import life.qbic.projectsStatsModule.ProjectsStatsPresenter;
import life.qbic.projectsTimeLineChart.TimeLineChartPresenter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class is a master presenter class and
 * helps to communicate between the different
 * modules
 */
public class MasterPresenter {

    //private final PieChartStatusModule pieChartStatusModule;

    private final ProjectOVPresenter projectOverviewPresenter;

    private final ProjectSheetPresenter projectSheetPresenter;

    private final ProjectFollowerPresenter projectFollowerPresenter;

    private final ProjectFilter projectFilter;

    private final TimeLineChartPresenter timeLineChartPresenter;

    private final ProjectsStatsPresenter projectsStatsPresenter;

    private final static Log log =
            LogFactory.getLog(ManagerUI.class.getName());

    //removed PieChartStatusModule pieChartStatusModule #25
    MasterPresenter(ProjectOVPresenter projectOverviewPresenter,
                    ProjectSheetPresenter projectSheetPresenter,
                    ProjectFollowerPresenter projectFollowerPresenter,
                    ProjectFilter projectFilter,
                    TimeLineChartPresenter timeLineChartPresenter, ProjectsStatsPresenter projectsStatsPresenter) {
        //this.pieChartStatusModule = pieChartStatusModule;
        this.projectOverviewPresenter = projectOverviewPresenter;
        this.projectFollowerPresenter = projectFollowerPresenter;
        this.projectSheetPresenter = projectSheetPresenter;
        this.projectFilter = projectFilter;
        this.timeLineChartPresenter = timeLineChartPresenter;
        this.projectsStatsPresenter = projectsStatsPresenter;

        init();
    }

    private void init() {
        makeFilter();

        try {
            projectOverviewPresenter.init();
            log.info("Init projectoverview module successfully.");
        } catch (Exception exp) {
            log.fatal("Init of projectoverview module failed. Reason: " + exp.getMessage(), exp);
            projectOverviewPresenter.sendError("Project Overview Module failed.", exp.getMessage());
        }

        //projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);

        projectOverviewPresenter.getSelectedProject().addValueChangeListener(event ->
                projectSheetPresenter.showInfoForProject(projectOverviewPresenter.getSelectedProjectItem()));

        //pieChartStatusModule.addPointClickListener(event -> {
        //            projectOverviewPresenter.setFilter("projectStatus", pieChartStatusModule.getDataSeriesObject(event));
        //        });

        projectOverviewPresenter.getIsChangedFlag().addValueChangeListener(this::refreshModuleViews);

        projectSheetPresenter.getInformationCommittedFlag().addValueChangeListener(this::refreshModuleViews);

        projectFilter.createFilter("projectID", projectFollowerPresenter.getFollowingProjects());

        projectFollowerPresenter.getIsChangedFlag().addValueChangeListener(event -> {
            final String selectedProject = projectFollowerPresenter.getCurrentProject();
            boolean doesDBEntryExist = projectOverviewPresenter.isProjectInFollowingTable(selectedProject);
            if (!doesDBEntryExist) {
                projectOverviewPresenter.createNewProjectEntry(selectedProject);
            }
            refreshModuleViews(event);
        });

        timeLineChartPresenter.setCategories(projectOverviewPresenter.getTimeLineStats());

    }

    private void refreshModuleViews(Property.ValueChangeEvent event) {
        makeFilter();
        projectOverviewPresenter.refreshView();
        //projectOverviewPresenter.getStatusKeyFigures().forEach(pieChartStatusModule::update);
        timeLineChartPresenter.updateData(projectOverviewPresenter.getTimeLineStats());
        projectsStatsPresenter.update();
    }

    private void makeFilter() {
        projectFilter.createFilter("projectID", projectFollowerPresenter.getFollowingProjects());
    }
}
