package com.mainPage.NotFulled;

import org.apache.commons.dbutils.QueryRunner;
import com.connectDatabase.DBConnection;
import com.mainPage.createRequest.Querys.Query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyNotFulledDAO implements MainNotFulledQuery {

    private static Logger log = Logger.getLogger(Query.class.getName());

    @Override
    public void showData() {

    }

    @Override
    public boolean insertOffering(NotFulfilled account) {
        return false;
    }

    @Override
    public boolean updateOffering(NotFulfilled account) {
        return false;
    }

    private QueryRunner dbAccess = new QueryRunner();
    private NotFulledQuery offeringsQueries = new NotFulledQuery();

    @Override
    public boolean deleteOffering(NotFulfilled notFulfilled) {
        try( Connection connection = DBConnection.getDataSource().getConnection()) {

            dbAccess.update(connection, offeringsQueries.deleteQuery(), notFulfilled.getID());

            return true;
        } catch (SQLException e) {

            log.log(Level.SEVERE, "Delete offering row exception: " + e);  DBConnection database = new DBConnection();
            database.reconnect();
        }
        return false;
    }
}
