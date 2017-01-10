package life.qbic.portal.projectSheetModule;

import com.vaadin.ui.Panel;

/**
 * Created by sven1103 on 9/01/17.
 */
public interface ProjectSheetView {

    Panel getProjectSheet();

    void setDefaultContent();

    void setProjectCode(String id);

    void showProjectLayout();
}
