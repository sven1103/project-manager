package life.qbic.portal.projectFollowerModule;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import life.qbic.portal.MyGrid;

/**
 * Created by sven on 12/18/16.
 */
public interface ProjectFollowerView {

    MyGrid getProjectGrid();

    ComboBox getSpaceComboBox();

    ComboBox getProjectComboBox();

    ProjectFollowerView setSpaceCaption(String caption);

    ProjectFollowerView setProjectCaption(String caption);

    ProjectFollowerView build();

    VerticalLayout getUI();
}
