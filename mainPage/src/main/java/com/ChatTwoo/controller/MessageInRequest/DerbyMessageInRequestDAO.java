package com.ChatTwoo.controller.MessageInRequest;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyMessageInRequestDAO implements MessageInRequestDAO {
    private static Logger log = Logger.getLogger(DerbyMessageInRequestDAO.class.getName());

    private QueryRunner queryRunner = new QueryRunner();
    private MessageInRequestQuery queries = new MessageInRequestQuery();

    @Override
    public boolean insertAccount(MessageInRequest offeringRequest) {
        return false;
    }

    @Override
    public boolean updateAccount(MessageInRequest offeringRequest) {
        return false;
    }

    @Override
    public boolean deleteAccount(MessageInRequest offeringRequest) {
        return false;
    }

    @Override
    public List<MessageInRequest> findByProperty(SearchType searchType, Object value) {
        return null;
    }

    @Override
    public List<MessageInRequest> findAll() {
        try {
            return queryRunner.query(DBConnection.getDataSource().getConnection(), queries.getMessageInRequestQuery(), new BeanListHandler<>(MessageInRequest.class));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception :" + e);
        }


        return new ArrayList<>();
    }
}
