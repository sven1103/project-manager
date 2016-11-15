package life.qbic.portal;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

    private final ProjectOVPresenter presenter;
    private final ProjectContentModel contentModel;

    private final Button loadButton;
    private final Notification info;
    private final Notification error;
    private final Grid overviewGrid;

    /**
     * Constructor
     * @param model The business logic
     */
    ProjectOverviewModule(ProjectContentModel model){
        this.contentModel = model;
        this.presenter = new ProjectOVPresenter();
        this.loadButton = new Button("Load Table");
        this.overviewGrid = new Grid();
        info = new Notification("", "", Notification.Type.TRAY_NOTIFICATION);
        error = new Notification("", "", Notification.Type.ERROR_MESSAGE);
        this.init();
    }

    /**
     * Make some init settings
     */
    private void init(){
        presenter.init();

        this.addComponents(loadButton, overviewGrid);
        this.setSpacing(true);
        this.setSizeFull();

        overviewGrid.setCaption("Projects overview");


        info.setDelayMsec(1000);
        info.setPosition(Position.TOP_CENTER);

        loadButton.addClickListener(presenter::loadTable);
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
     * This presenter class will connect the UI with the underlying
     * logic. As this module will display the projectmanager database
     * content, the presenter will request data from the model, which handles
     * the SQL connection and contains the information to be shown.
     * Issues and Errors will be directed to the user via a notification message.
     */
    class ProjectOVPresenter{

        /**
         * Call and validate database connection of the business logic.
         */
        void init(){
            if (contentModel == null){
                log.error("The model was not instanciated yet!");
                return;
            }
            if(!contentModel.connectToDB()){
                sendError("Database Error", "Could not connect to database :(");
            } else{
                sendInfo("Good Job", "Successfully connected to database");
            }

            if (!contentModel.loadData()){
                sendError("Damn", "Could not load the data from the database :(");
                loadButton.setEnabled(false);
            }
        }

        /**
         * Static method reference to buttons
         * @param event A button click event
         */
        void loadTable(Button.ClickEvent event){

            overviewGrid.setContainerDataSource(contentModel.getTableContent());

        }

    }

}
