package com.ChatTwoo.controller;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyColumnMessageDAO  implements ColumnMessageDAO {

    private static Logger log = Logger.getLogger(DerbyColumnMessageDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private QueryClientChat accountQueries = new QueryClientChat();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";

    @Override
    public List<ColumnMessage> findAll() {
        try {
            return dbAccess.query(DBConnection.getDataSource().getConnection(), accountQueries.getMainAccount(), new BeanListHandler<>(ColumnMessage.class));
        } catch (Exception e) {


            log.log(Level.SEVERE, "Column filter exception (findAll): " + e);
        }

        return EMPTYLIST;
    }

    @Override
    public boolean insertAccount(ColumnMessage columnMessage) {
        return false;
    }

    @Override
    public boolean updateAccount(ColumnMessage columnMessage) {
        return false;
    }

    @Override
    public boolean deleteAccount(ColumnMessage columnMessage) {
        return false;
    }

    @Override
    public List<ColumnMessage> findByProperty(SearchType searchType, Object value) {
        return null;
    }


}
