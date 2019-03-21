package com.mainPage.page.group;

import com.connectDatabase.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QuerySearchGroup//implements Queries
 {
    private static Logger log = Logger.getLogger(QuerySearchGroup.class.getName());

    private static StringBuilder query;

  public String getMainGroup(Boolean top, int rowIndex) {

   query = new StringBuilder();
   query.append("SELECT \n");




     if (top) query.append(" TOP " + rowIndex + "\n");

   query.append("\t\tJ.ID AS ID,\n" +
           "\t\tJ.Name AS Name\n" +
           "FROM\n" +
           "\ttbl_Job AS J "
   );


   return query.toString();
  }
     public long getMainGroupCount() {

         query = new StringBuilder();
         long count = 0;
         try {
             query.append("SELECT \n");


             query.append("\t\tCOUNT(J.ID) AS [rowsCount]\n" +
                     "FROM\n" +
                     "\ttbl_Job AS J"
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
