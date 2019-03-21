package com.mainPage.InTract.InTractRequest;

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

public class DerbyInTractRequestDAO implements InTractRequestDAO{
    private static Logger log = Logger.getLogger(DerbyInTractRequestDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private InTractRequestQuery queries = new InTractRequestQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";


    @Override
    public boolean insertAccount(InTractRequest inTractRequest) {
        return false;
    }

    @Override
    public boolean updateAccount(InTractRequest inTractRequest) {
        return false;
    }

    @Override
    public boolean deleteAccount(InTractRequest inTractRequest) {
        return false;
    }

    @Override
    public List<InTractRequest> findByProperty(SearchType searchType, Object value) {
        return null;
    }

    @Override
    public List<InTractRequest> findAll(String requestID) {

        try (Connection connection = DBConnection.getDataSource().getConnection()){


            return dbAccess.query(DBConnection.getDataSource().getConnection(), queries.getInTractRequestQuery(requestID), new BeanListHandler<InTractRequest>(InTractRequest.class));

        } catch (SQLException e) {
            log.log(Level.SEVERE, "Column filter exception (findAll): " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return EMPTYLIST;
    }
}
