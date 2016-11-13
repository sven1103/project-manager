package edu.debrains;

import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by sven on 11/13/16.
 */
public class ProjectOverviewModule extends VerticalLayout{

    private final Presenter presenter = new Presenter();
    private final ProjectContentModel contentModel;

    private final Button infoButton;
    private final Label versionDisplay;

    ProjectOverviewModule(ProjectContentModel model){
        this.contentModel = model;
        this.infoButton = new Button("Show Version");
        this.versionDisplay = new Label();
        this.addComponents(infoButton, versionDisplay);
    }

    void init(){
        infoButton.addClickListener(presenter::getModelVersion);
    }

    private class Presenter{

        void getModelVersion(Button.ClickEvent event){
            versionDisplay.setValue(contentModel.version);
        }

    }

}
