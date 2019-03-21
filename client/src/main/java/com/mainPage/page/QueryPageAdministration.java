package com.mainPage.page;

import com.connectDatabase.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryPageAdministration {

    private static Logger log = Logger.getLogger(QueryPageAdministration.class.getName());

    private static StringBuilder query;

    public String getMainPageAdminostration(Boolean top, int rowIndex, String nameElement) {

        query = new StringBuilder();
        query.append("SELECT \n");




        if (top) query.append(" TOP " + rowIndex + "\n");

        query.append(
                "\t\t\t\t\t\tAA.ID AS ID,\n" +
                "\t\t\t\t\t\tAA.CreatedOn AS CreatedOn,\n" +
                "\t\t\t\t\t\tAA.CreatedByID AS CreatedByID,\n" +
                "\t\t\t\t\t\tAA.ModifiedOn AS ModifiedOn,\n" +
                "\t\t\t\t\t\tAA.ModifiedByID AS ModifiedByID,\n" +
                "\t\t\t\t\t\tAA.ContactID AS ContactID,\n" +
                "\t\t\t\t\t\tC.Name AS ContactName,\n" +
                        "AA.JobID AS JobID, \n" +
                        "J.Name AS JobName, \n" +
                "\t\t\t\t\t\tAA.NameElements AS NameElements,\n" +
                "\t\t\t\t\t\tAA.Access AS Access,\n" +
                        "AA.[Type] AS [Type]\n" +
                "\t\t\t\t from tbl_AdministrationAccess AS AA\n" +
                "\t\t\t\tLeft JOIN\n" +
                "\t\t\t\ttbl_Contact AS C ON C.ID = AA.ContactID\n" +
                        "LEFT JOIN\n" +
                        "tbl_Job AS J ON J.ID = AA.JobID\n" +
                "\t\t\t\tWHERE\n" +
                "\t\t\t\tAA.NameElements = '"+nameElement+"'"
        );


        return query.toString();
    }

    public String getMainPageAdminostrationControl( String contactID, String jobID) {

        query = new StringBuilder();
        query.append("SELECT \n");



        query.append(
                "\t\t\t\t\t\tAA.ID AS ID,\n" +
                        "\t\t\t\t\t\tAA.CreatedOn AS CreatedOn,\n" +
                        "\t\t\t\t\t\tAA.CreatedByID AS CreatedByID,\n" +
                        "\t\t\t\t\t\tAA.ModifiedOn AS ModifiedOn,\n" +
                        "\t\t\t\t\t\tAA.ModifiedByID AS ModifiedByID,\n" +
                        "\t\t\t\t\t\tAA.ContactID AS ContactID,\n" +
                        "\t\t\t\t\t\tC.Name AS ContactName,\n" +
                        "AA.JobID AS JobID, \n" +
                        "J.Name AS JobName, \n" +
                        "\t\t\t\t\t\tAA.NameElements AS NameElements,\n" +
                        "\t\t\t\t\t\tAA.Access AS Access,\n" +
                        "AA.[Type] AS [Type]\n" +
                        "\t\t\t\t from tbl_AdministrationAccess AS AA\n" +
                        "\t\t\t\tLeft JOIN\n" +
                        "\t\t\t\ttbl_Contact AS C ON C.ID = AA.ContactID\n" +
                        "LEFT JOIN\n" +
                        "tbl_Job AS J ON J.ID = AA.JobID\n" +
                        "\t\t\t\tWHERE\n" +
                        "\t\t\t\tAA.ContactID = '"+contactID+"' OR \n" +
                        "CAST(AA.JobID AS NVARCHAR(50)) = '"+jobID+"'"
        );


        return query.toString();
    }

    public long getMainPageAdminostrationCount(String nameElement) {

        query = new StringBuilder();
        long count = 0;
        try {
            query.append("SELECT \n");


            query.append(
                    "\t\t\t\t\t\tCOUNT(AA.ID) AS [rowsCount]\n" +
                    "\t\t\t\t from tbl_AdministrationAccess AS AA\n" +
                    "\t\t\t\tLeft JOIN\n" +
                    "\t\t\t\ttbl_Contact AS C ON C.ID = AA.ContactID\n" +
                    "\t\t\t\tWHERE\n" +
                    "\t\t\t\tAA.NameElements = '"+nameElement+"'"
            );

            ResultSet rs = DBConnection.getDataSource().getConnection().createStatement().executeQuery(query.toString());

            while (rs.next()) {
                count = rs.getLong("rowsCount");
            }

        }catch (SQLException e) {
            log.log(Level.SEVERE, "Pages count exception: " + e);  DBConnection database = new DBConnection();
            database.reconnect();
        }

        return count;

    }


}
