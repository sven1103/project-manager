package life.qbic.portal;

import com.vaadin.data.Property;
import com.vaadin.ui.Upload;

/**
 * This class is a master presenter class and
 * helps to communicate between the different
 * modules
 */
public class MasterPresenter {

    private final PieChartStatusModule pieChartStatusModule;

    private final ProjectOverviewModule projectOverviewModule;


    MasterPresenter(PieChartStatusModule pieChartStatusModule,
                    ProjectOverviewModule projectOverviewModule){
        this.pieChartStatusModule = pieChartStatusModule;
        this.projectOverviewModule = projectOverviewModule;

        init();
    }

    private void init(){
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
