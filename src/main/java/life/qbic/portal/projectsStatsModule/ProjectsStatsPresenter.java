package life.qbic.portal.projectsStatsModule;

/**
 * Created by spaethju on 12.04.17.
 */
public class ProjectsStatsPresenter {

    ProjectsStatsModel model;
    ProjectsStatsView view;

    public ProjectsStatsPresenter(ProjectsStatsModel model, ProjectsStatsView view) {
        this.model = model;
        this.view = view;
    }
}
