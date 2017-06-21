package life.qbic.projectOverviewModule;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import life.qbic.MyGrid;

import java.util.HashSet;
import java.util.List;

/**
 * Created by sven on 11/13/16.
 * This class represents the core of the projectmanager
 * module. It will display the database content of different projects
 * and its progress status.
 */
public class ProjectOverviewModule extends VerticalLayout implements ProjectOverviewI {


    private final Notification info;
    private final Notification error;
    private final MyGrid overviewGrid;
    List<Column> columnList;

    public final HashSet columnHide = new HashSet<String>() {{
        add("id");
        add("investigatorID");
        add("instrumentID");
        add("projectRegistered");
        add("barcodeSent");
    }};


    public ProjectOverviewModule() {
        this.overviewGrid = new MyGrid();
        this.info = new Notification("", "", Notification.Type.TRAY_NOTIFICATION);
        this.error = new Notification("", "", Notification.Type.ERROR_MESSAGE);
        init();
    }

    /**
     * Make some init settings
     */
    public void init() {

        this.addComponents(overviewGrid);
        this.setSpacing(true);

        overviewGrid.setWidth(100, Unit.PERCENTAGE);
        overviewGrid.setHeight(100, Unit.PERCENTAGE);

        overviewGrid.setEditorEnabled(true);
        //fieldGroup.addCommitHandler(presenter.commitChanges());
        info.setDelayMsec(1000);
        info.setPosition(Position.TOP_CENTER);
    }

    /**
     * Sends an info notification message to the user on the screen.
     *
     * @param caption The caption
     * @param message Your message
     */
    void sendInfo(String caption, String message) {
        info.setCaption(caption);
        info.setDescription(message);
        info.show(Page.getCurrent());
    }

    /**
     * Sends an error notification message to the user on the screen.
     *
     * @param caption The caption
     * @param message Your message
     */
    void sendError(String caption, String message) {
        error.setCaption(caption);
        error.setDescription(message);
        error.show(Page.getCurrent());
    }


    @Override
    public MyGrid getOverviewGrid() {
        return this.overviewGrid;
    }

    @Override
    public List<Column> getColumnList() {
        return this.columnList;
    }


}
