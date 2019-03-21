package com.mainPage.InProcessing.UpdateOfferingGroupAddName;

import com.Utils.GridComp;
import com.connectDatabase.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateOfferingGroupAddName  {
    private static Logger log = Logger.getLogger(UpdateOfferingGroupAddName.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private UpdateOfferingGroupAddNameQuery queryPageAdministration = new UpdateOfferingGroupAddNameQuery();
    private static final List EMPTYLIST = new ArrayList<>();


    private String ID;

    private String name;


    public UpdateOfferingGroupAddName (){}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public ObservableList<UpdateOfferingGroupAddName> findAll(boolean pagination, int rowIndex, int nameElements) {
        String query = (pagination ? queryPageAdministration.getMainPageAdminostration(true, 50, nameElements) : queryPageAdministration.getMainPageAdminostration(false, 0, nameElements));
        try{
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<UpdateOfferingGroupAddName>(UpdateOfferingGroupAddName.class)));
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
