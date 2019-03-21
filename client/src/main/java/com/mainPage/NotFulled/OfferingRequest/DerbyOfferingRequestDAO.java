package com.mainPage.NotFulled.OfferingRequest;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyOfferingRequestDAO implements OfferingRequestDAO{
    private static Logger log = Logger.getLogger(DerbyOfferingRequestDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private OfferingRequestQuery queries = new OfferingRequestQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";


    @Override
    public String getID() {
        return getID();
    }

    @Override
    public String getName() {
        return getName();
    }

    @Override
    public void setID(String ID) {

    }

    @Override
    public void setName(String Name) {

    }

    @Override
    public boolean insertAccount(OfferingRequest offeringRequest) {
        return false;
    }

    @Override
    public boolean updateAccount(OfferingRequest offeringRequest) {
        return false;
    }
    private OfferingRequestQuery offeringsQueries = new OfferingRequestQuery();
    @Override
    public boolean deleteAccount(OfferingRequest offeringRequest) {
        try( Connection connection = DBConnection.getDataSource().getConnection()) {

            dbAccess.update(connection, offeringsQueries.deleteQuery(), offeringRequest.getID());

            return true;
        } catch (SQLException e) {

            log.log(Level.SEVERE, "Delete offering row exception: " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        return false;
    }


    @Override
    public List<OfferingRequest> findByProperty(SearchType searchType, Object value) {
        return null;
    }

    @Override
    public List<OfferingRequest> findAll(int rowIndex, String requestID) {
        try {
            return dbAccess.query(DBConnection.getDataSource().getConnection(), queries.getOfferingRequestQuery(true,100,requestID ), new BeanListHandler<>(OfferingRequest.class));

        } catch (Exception e) {


            log.log(Level.SEVERE, "Column filter exception (findAll): " + e);  DBConnection database = new DBConnection();
            database.reconnect();
        }

        return EMPTYLIST;
    }
}
