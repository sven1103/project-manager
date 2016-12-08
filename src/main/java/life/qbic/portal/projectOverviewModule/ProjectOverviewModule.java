package life.qbic.portal;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Created by sven on 11/13/16.
 * This class represents the core of the projectmanager
 * module. It will display the database content of different projects
 * and its progress status.
 */
class ProjectOverviewModule extends VerticalLayout{

    /**
     * Get static logger instance
     */
    private final static Log log =
            LogFactory.getLog(ProjectContentModel.class.getName());

    private final ProjectContentModel contentModel;

    private final Notification info;
    private final Notification error;
    final MyGrid overviewGrid;
    List<Column> columnList;

    final HashSet columnHide = new HashSet<String>(){{
        add("projectID");
        add("investigatorID");
        add("instrumentID");
        add("offerID");
        add("invoice");
    }};


    /**
     * Constructor
     * @param model The business logic
     */
    ProjectOverviewModule(ProjectContentModel model){
        this.contentModel = model;
        this.overviewGrid = new MyGrid();
        this.info = new Notification("", "", Notification.Type.TRAY_NOTIFICATION);
        this.error = new Notification("", "", Notification.Type.ERROR_MESSAGE);
    }

    /**
     * Make some init settings
     */
    public void init() throws Exception{

        this.addComponents(overviewGrid);
        this.setSpacing(true);

        overviewGrid.setCaption("Projects overview");
        overviewGrid.setHeight(50, Unit.PERCENTAGE);
        overviewGrid.setWidth(50, Unit.PERCENTAGE);
        overviewGrid.setEditorEnabled(true);
        //fieldGroup.addCommitHandler(presenter.commitChanges());
        info.setDelayMsec(1000);
        info.setPosition(Position.TOP_CENTER);
    }

    /**
     * Sends an info notification message to the user on the screen.
     * @param caption The caption
     * @param message Your message
     */
    void sendInfo(String caption, String message){
        info.setCaption(caption);
        info.setDescription(message);
        info.show(Page.getCurrent());
    }

    /**
     * Sends an error notification message to the user on the screen.
     * @param caption The caption
     * @param message Your message
     */
    void sendError(String caption, String message){
        error.setCaption(caption);
        error.setDescription(message);
        error.show(Page.getCurrent());
    }

    /**
     * Getter for the grid
     * @return
     */
    MyGrid getOverviewGrid(){
        return this.overviewGrid;
    }


}
