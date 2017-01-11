package life.qbic.portal.projectSheetModule;

import com.vaadin.data.Item;
import life.qbic.portal.database.ColumnTypes;
import life.qbic.portal.database.ProjectDatabaseConnector;
import life.qbic.portal.database.TableColumns;
import org.apache.commons.logging.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sven1103 on 10/01/17.
 */
public class ProjectSheetPresenter {

    final private ProjectSheetView projectSheetView;

    final private ProjectDatabaseConnector dbConnector;

    final private String sqlCommitCommand = String.format("UPDATE projectsoverview SET %s = ? " +
            "WHERE %s = ?",
            TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.REGISTRATIONDATE),
            TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.ID));

    private Item currentItem;

    private final Log log;

    public ProjectSheetPresenter(ProjectSheetView projectSheetView, ProjectDatabaseConnector dbConnector, Log log){
        this.projectSheetView = projectSheetView;
        this.dbConnector = dbConnector;
        this.log = log;
        init();
    }

    private void init() {
        projectSheetView.getSaveButton().addClickListener(event -> {
            try {
                commitChangesToDataBase();
            } catch (SQLException exc) {
                log.fatal("Could not update saved changes to database", exc);
            }
        });
    }

    public void showInfoForProject(Item project) {

        if (project == null){
            projectSheetView.setDefaultContent();
        } else{
            currentItem = project;
            fillInContentFromItem();
            projectSheetView.showProjectLayout();
        }

    }

    private void fillInContentFromItem(){
        String projectCode = (String) currentItem.getItemProperty(
                TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.PROJECTID)).getValue();
        Date projectRegistered = (Date) currentItem.getItemProperty(
                TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.REGISTRATIONDATE)).getValue();
        projectSheetView.setProjectCode(projectCode);
        projectSheetView.setRegistrationDate(projectRegistered);
    }

    private void commitChangesToDataBase() throws SQLException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String id = currentItem.getItemProperty(TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.ID)).getValue().toString();
        String projectRegisteredDate = dateFormat.format(projectSheetView.getRegistrationDateField().getValue());
        String project = (String) currentItem.getItemProperty(TableColumns.PROJECTOVERVIEWTABLE.get(ColumnTypes.PROJECTID)).getValue();

        Connection con = dbConnector.getConnectionPool().reserveConnection();

        if (con == null){
            throw new SQLException("Could not reserve any connection to the database.");
        }

        PreparedStatement ps = con.prepareStatement(sqlCommitCommand);

        ps.setString(1, projectRegisteredDate);
        ps.setString(2, id);

        ps.execute();
        con.commit();

        ps.close();
        con.close();
        log.info(String.format("Changes for project %s successfully updated.", project));
    }


}
