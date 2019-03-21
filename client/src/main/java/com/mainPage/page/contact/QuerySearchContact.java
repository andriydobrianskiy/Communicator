package com.mainPage.page.contact;

import com.connectDatabase.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuerySearchContact//implements Queries
 {
    private static Logger log = Logger.getLogger(QuerySearchContact.class.getName());

    private static StringBuilder query;

  public String getMainContact(Boolean top, int rowIndex) {

   query = new StringBuilder();
   query.append("SELECT \n");




     if (top) query.append(" TOP " + rowIndex + "\n");

   query.append("\t\t\t\t\t\tC.ID AS ID,\n" +
           "\t\t\t\t\t\tC.CreatedOn AS CreatedOn,\n" +
           "\t\t\t\t\t\tC.Name AS ContactName,\n" +
           "\t\t\t\t\t\tJ.Name AS JobName\n" +
           "\n" +
           "\t\t\t\tFROM tbl_Contact AS C\n" +
           "\t\t\t\tLeft JOIN \n" +
           "\t\t\t\ttbl_Job AS J ON J.ID = C.JobID\n" +
           "\t\t\t\twhere\n" +
           "\t\t\t\tC.ContactTypeID = '47CBC6BB-D74E-4872-A488-75CC01122136' "
   );


   return query.toString();
  }
     public long getMainContactCount() {

         query = new StringBuilder();
         long count = 0;
         try {
             query.append("SELECT \n");


             query.append("\t\t\t\t\t\tCOUNT(C.ID) AS [rowsCount]\n" +

                     "\t\t\t\tFROM tbl_Contact AS C\n" +
                     "\t\t\t\tLeft JOIN \n" +
                     "\t\t\t\ttbl_Job AS J ON J.ID = C.JobID\n" +
                     "\t\t\t\twhere\n" +
                     "\t\t\t\tC.ContactTypeID = '47CBC6BB-D74E-4872-A488-75CC01122136'"
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
