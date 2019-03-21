package com.mainPage.createRequest.StateCreate;

import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import com.Utils.SearchType;
import com.mainPage.createRequest.CreateInterface;
import com.mainPage.createRequest.searchCounterpart.Counterpart;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class GroupPeople  implements CreateInterface {

    private static Logger log = Logger.getLogger(StateCreate.class.getName());

    private QueryRunner dbAccess = new QueryRunner();

    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";
    public GroupPeople (){

    }
    private String ID;
    private String name;

    public GroupPeople(String id, String name) {
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
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }
}
