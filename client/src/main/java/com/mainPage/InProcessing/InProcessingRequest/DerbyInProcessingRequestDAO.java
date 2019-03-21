package com.mainPage.InProcessing.InProcessingRequest;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.NotFulled.OfferingRequest.DerbyOfferingRequestDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyInProcessingRequestDAO implements InProcessingRequestDAO {
    private static Logger log = Logger.getLogger(DerbyOfferingRequestDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private InProcessingRequestQuery queries = new InProcessingRequestQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";


    @Override
    public boolean insertAccount(InProcessingRequest inProcessingRequest) {
        return false;
    }

    @Override
    public boolean updateAccount(InProcessingRequest inProcessingRequest) {
        return false;
    }

    @Override
    public boolean deleteAccount(InProcessingRequest inProcessingRequest) {
        return false;
    }

    @Override
    public List<InProcessingRequest> findByProperty(SearchType searchType, Object value) {
        return null;
    }

    @Override
    public List<InProcessingRequest> findAll(int rowIndex,String requestID) {

        try (Connection connection = DBConnection.getDataSource().getConnection()){

            return dbAccess.query(DBConnection.getDataSource().getConnection(), queries.getInProcessingRequestQuery(true,1000, requestID), new BeanListHandler<InProcessingRequest>(InProcessingRequest.class));


        } catch (SQLException e) {


            log.log(Level.SEVERE, "Column filter exception (findAll): " + e);
            DBConnection dbConnection = new DBConnection();
            dbConnection.reconnect();
        }

        return EMPTYLIST;
    }
}
