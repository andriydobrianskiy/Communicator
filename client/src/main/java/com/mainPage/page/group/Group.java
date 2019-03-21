package com.mainPage.page.group;

import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.createRequest.CreateInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Group implements CreateInterface//DictionaryInterface
 {

    private static Logger log = Logger.getLogger(Group.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private QuerySearchGroup conatctQueries = new QuerySearchGroup();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";

    private String ID;
     private String name;






    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public Group() {
    }







    public String getID() {
        return ID;
    }



    public void setID(String ID) {
this.ID = ID;
    }




     @Override
    public List<Group> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        String whereClause = "";
        String valueClause = "";

        switch ((GroupSearchType) searchType) {
            case NAME:
                valueClause = "%" + value.toString() + "%";
                whereClause = " WHERE [J].[Name] LIKE '%" + value.toString() + "%'";
                break;


        }

        try {
            return dbAccess.query(DBConnection.getDataSource().getConnection(), conatctQueries.getMainGroup(false, 0) + whereClause, new BeanListHandler<Group>(Group.class));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Column filter exception (findAccountByProperty): " + e);  DBConnection database = new DBConnection();
            database.reconnect();
        }

        return EMPTYLIST;
    }

    @Override
    public ObservableList<Group> findAll(boolean pagination, int rowIndex) {
        String query = (pagination ? conatctQueries.getMainGroup(true, rowIndex) : conatctQueries.getMainGroup(false, 0));
        try{
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<Group>(Group.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }

     @Override
    public String toString() {
        return getName();
    }


}
