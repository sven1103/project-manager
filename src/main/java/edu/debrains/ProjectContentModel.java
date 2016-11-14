package edu.debrains;

import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

import java.sql.SQLException;

/**
 * Created by sven on 11/13/16.
 * This class contains the business logic and is connected with the
 * MySQL database which contains all the information of QBiC projects.
 */
class ProjectContentModel {

    final String version = "Version 0.1b";
    private JDBCConnectionPool pool;

    final String queryALL = "SELECT * FROM projectsoverview";
    final String primaryKey = "projectID";

    private FreeformQuery query;

    private SQLContainer tableContent;

    void connectToDB() throws SQLException{
        pool = new SimpleJDBCConnectionPool(
                "com.mysql.jdbc.Driver",
                "jdbc:mysql://localhost:3306/qbicprojects", "root", "yL8GOiWAQ1jvsj6cvc8R", 2, 5
        );
        loadData();
    }

    void loadData(){
        query = new FreeformQuery(
                queryALL, pool, primaryKey);
        try{
            tableContent = new SQLContainer(query);
            System.out.println("SQL container successfully loaded.");
        } catch (SQLException e){
            System.err.println("Could not perform query");
            tableContent = null;
        }
        System.out.println(tableContent.getContainerPropertyIds());
    }









}
