package com.mainPage.All;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.connectDatabase.DBConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DerbyAllDAO implements AllDAO{
    private static Logger log = Logger.getLogger(DerbyAllDAO.class.getName());
    private QueryRunner dbAccess = new QueryRunner();
    private AllQuery accountQueries1 = new AllQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";
    private HashMap<TableColumn, String> mapFilters = new HashMap();

    public static List<Object> getDictionaryMap() { // Initialization listview labels
        List<Object> list = new ArrayList<Object>();


        list.add(new AllController());
        //  list.add(new ServicesTable());
        //list.add(new OfferingsTable());
        // list.add(new AccountVehicleTable());

        return list;

    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setID(String ID) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public List<All> findAll(boolean pagination, int rowIndex, String createdByID, String offeringGroupID) {
        String query = (pagination ? accountQueries1.getMainAll(true, rowIndex, createdByID, offeringGroupID) : accountQueries1.getMainAll(false, 0, createdByID, offeringGroupID));
        try {
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<All>(All.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }


    public void setStringFilter(TableColumn column, String value) {
        mapFilters.put(column, value);
    }

    public String getStringFilter() {
        StringBuilder builder = new StringBuilder("");
        try {
            mapFilters.forEach((k, v) ->
                    builder.append(v));
        } catch (NullPointerException e) {

        }

        System.out.println(builder.toString());
        return builder.toString();
    }

    public void removeStringFilter(TableColumn key) {
        mapFilters.remove(key);

    }
}
