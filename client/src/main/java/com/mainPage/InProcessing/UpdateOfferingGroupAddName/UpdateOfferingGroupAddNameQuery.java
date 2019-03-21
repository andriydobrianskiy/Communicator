package com.mainPage.InProcessing.UpdateOfferingGroupAddName;

import com.connectDatabase.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateOfferingGroupAddNameQuery {

    private static Logger log = Logger.getLogger(UpdateOfferingGroupAddNameQuery.class.getName());

    private static StringBuilder query;

    public String getMainPageAdminostration(Boolean top, int rowIndex, int nameElement) {

        query = new StringBuilder();
        query.append("SELECT \n");




        if (top) query.append(" TOP " + rowIndex + "\n");

        query.append(
              " ID, " +
                      " Name " +
                      " from tbl_Contact \n" +
                      " WHERE \n" +
                      " CommunicatorTypeGroup = "+nameElement+""
        );


        return query.toString();
    }




}
