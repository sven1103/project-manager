package life.qbic.portal.projectFollowerModule;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import life.qbic.portal.MyGrid;

/**
 * Created by sven1103 on 19/12/16.
 */
public class ProjectFollowerViewImpl implements ProjectFollowerView{

    private String spaceCaption;
    private String projectCaption;

    private ComboBox spaceBox;
    private ComboBox projectBox;

    private VerticalLayout mainContent;

    private HorizontalLayout boxWrapper;


    public ProjectFollowerViewImpl() {
        spaceCaption = "ExampleCaption1";
        projectCaption = "ExampleCaption2";
    }

    @Override
    public MyGrid getProjectGrid() {
        return null;
    }

    @Override
    public ComboBox getSpaceComboBox() {
        return this.spaceBox;
    }

    @Override
    public ComboBox getProjectComboBox() {
        return this.projectBox;
    }

    @Override
    public ProjectFollowerView setSpaceCaption(String caption) {
        this.spaceCaption = caption;
        return this;
    }

    @Override
    public ProjectFollowerView setProjectCaption(String caption) {
        this.projectCaption = caption;
        return this;
    }

    @Override
    public ProjectFollowerView build(){
        this.mainContent = new VerticalLayout();

        this.spaceBox = new ComboBox(spaceCaption);
        this.projectBox = new ComboBox(projectCaption);
        this.boxWrapper = new HorizontalLayout();

        HorizontalLayout leftContainer = new HorizontalLayout();
        leftContainer.addComponent(spaceBox);

        HorizontalLayout rightContainer = new HorizontalLayout();
        rightContainer.addComponent(projectBox);

        boxWrapper.addComponents(leftContainer, rightContainer);
        mainContent.addComponents(boxWrapper);
        mainContent.setComponentAlignment(boxWrapper, Alignment.BOTTOM_CENTER);
        mainContent.setSpacing(true);
        mainContent.setMargin(true);
        boxWrapper.setMargin(true);
        boxWrapper.setSpacing(true);
        return this;
    }

    @Override
    public VerticalLayout getUI() {
        return this.mainContent;
    }
}
