package com.mainPage.ArchiveFiles.ArchiveFilesRequest;

import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.InTract.InTractRequest.InTractRequest;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyArchiveRequestDAO implements ArchiveRequestDAO {
    private static Logger log = Logger.getLogger(DerbyArchiveRequestDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private ArchiveRequestQuery queries = new ArchiveRequestQuery();
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
    public List<ArchiveFilesRequest> findAll(String requestID) {

        try (Connection connection = DBConnection.getDataSource().getConnection()){
            return dbAccess.query(DBConnection.getDataSource().getConnection(), queries.getArchiveRequestQuery(requestID), new BeanListHandler<ArchiveFilesRequest>(ArchiveFilesRequest.class));
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Column filter exception (findAll): " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return EMPTYLIST;
    }
}
