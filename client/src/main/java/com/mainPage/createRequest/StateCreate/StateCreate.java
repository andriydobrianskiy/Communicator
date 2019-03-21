package com.mainPage.createRequest.StateCreate;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.createRequest.CreateInterface;
import com.mainPage.createRequest.searchCounterpart.Counterpart;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StateCreate implements CreateInterface {

    private static Logger log = Logger.getLogger(StateCreate.class.getName());

    private QueryRunner dbAccess = new QueryRunner();

    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";
    public StateCreate (){

    }
    private String ID;
    private String name;

    public StateCreate(String id, String name) {
        this.ID = id;
        this.name = name;
    }

    @Override
    public String getID() {
    return ID;
    }
    @Override
    public String getName () {
    return name;
    }
    @Override
    public void setID (String ID) {
    this.ID = ID;
    }
    @Override
    public void setName (String name) {
    this.name = name;
    }

    @Override
    public List<StateCreate> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        return null;
    }


    @Override
    public ObservableList<Counterpart> findAll(boolean pagination, int rowIndex) {
        String query = (pagination ? getStateCreate(true, rowIndex) : getStateCreate(false, 0));

        try (Connection connection = DBConnection.getDataSource().getConnection()){
      //      return FXCollections.observableArrayList(dbAccess.query(connection, query, new BeanListHandler<StateCreate>(StateCreate.class)));
        } catch (Exception e) {
            DBConnection database = new DBConnection();
            database.reconnect();

            log.log(Level.SEVERE, "StateCreate exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }



    private String getStateCreate(boolean top, int rowIndex) {
        StringBuilder builderQuery = new StringBuilder("SELECT ");
        if(top) builderQuery.append(" TOP " + rowIndex);
        builderQuery.append(" [tbl_RequestOfferingState].[ID] AS [ID],\n" +
                "\t[tbl_RequestOfferingState].[Name] AS [Name]\n" +
                "FROM\n" +
                "\t[dbo].[tbl_RequestOfferingState] AS [tbl_RequestOfferingState]");

        return builderQuery.toString();
    }
    @Override
    public String toString() {
        return getName();
    }
}
