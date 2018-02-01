package com.mainPage.InProcessing;

import org.apache.commons.dbutils.QueryRunner;
import com.connectDatabase.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyInProcessingDAO implements MainInProcessingQuery {

    private static Logger log = Logger.getLogger(DerbyInProcessingDAO.class.getName());
    @Override
    public void showData() {
    }

    @Override
    public boolean insertOffering(InProcessing account) {
        return false;
    }

    @Override
    public boolean updateOffering(InProcessing account) {
        return false;
    }

    private QueryRunner dbAccess = new QueryRunner();
    private InProcessingQuery offeringsQueries = new InProcessingQuery();

    @Override
    public boolean deleteOffering(InProcessing notFulfilled) {
        try( Connection connection = DBConnection.getDataSource().getConnection()) {

            dbAccess.update(connection, offeringsQueries.deleteQuery(), notFulfilled.getID());

            return true;
        } catch (SQLException e) {

            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        return false;
    }
}
