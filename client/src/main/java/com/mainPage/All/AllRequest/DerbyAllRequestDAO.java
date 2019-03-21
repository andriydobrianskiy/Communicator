package com.mainPage.All.AllRequest;

import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.ArchiveFiles.ArchiveFilesRequest.ArchiveFilesRequest;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyAllRequestDAO implements AllRequestDAO {
    private static Logger log = Logger.getLogger(DerbyAllRequestDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private AllRequestQuery queries = new AllRequestQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";




    @Override
    public boolean insertAccount(ArchiveFilesRequest offeringRequest) {
        return false;
    }

    @Override
    public boolean updateAccount(ArchiveFilesRequest offeringRequest) {
        return false;
    }

    @Override
    public boolean deleteAccount(ArchiveFilesRequest offeringRequest) {
        return false;
    }

    @Override
    public List<ArchiveFilesRequest> findByProperty(SearchType searchType, Object value) {
        return null;
    }


    @Override
    public List<AllRequest> findAll(String requestID) {

        try (Connection connection = DBConnection.getDataSource().getConnection()){
            return dbAccess.query(DBConnection.getDataSource().getConnection(), queries.getAllRequestQuery(requestID), new BeanListHandler<AllRequest>(AllRequest.class));
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Column filter exception (findAll): " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return EMPTYLIST;
    }
}
