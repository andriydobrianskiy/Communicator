package com.mainPage.InTract;

import org.apache.commons.dbutils.QueryRunner;
import com.connectDatabase.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyInTractDAO implements MainInTractQuery {

    private static Logger log = Logger.getLogger(DerbyInTractDAO.class.getName());

    @Override
    public boolean insertOffering(InTract account) {
        return false;
    }

    @Override
    public boolean updateOffering(InTract account) {
        return false;
    }

    private QueryRunner dbAccess = new QueryRunner();
    private InTractQuery offeringsQueries = new InTractQuery();

    @Override
    public boolean deleteOffering(InTract account) {
        try( Connection connection = DBConnection.getDataSource().getConnection()) {

            dbAccess.update(connection, offeringsQueries.deleteQuery(), account.getID());

            return true;
        } catch (SQLException e) {

            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        return false;
    }
}
