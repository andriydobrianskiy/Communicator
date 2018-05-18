package com.mainPage.InProcessing.Calculator;

import com.Utils.SearchType;
import com.mainPage.createRequest.CreateInterface;
import javafx.collections.ObservableList;

import java.util.List;

public class Calculator implements CreateInterface {
    private String ID;
    private String Name;
    private String OfferingTypeID;

    public Calculator () {}

    public Calculator(String ID, String name) {
        this.ID = ID;
        Name = name;

    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getOfferingTypeID() {
        return OfferingTypeID;
    }

    public void setOfferingTypeID(String offeringTypeID) {
        OfferingTypeID = offeringTypeID;
    }

    @Override
    public List<? extends CreateInterface> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        return null;
    }

    @Override
    public ObservableList<? extends CreateInterface> findAll(boolean pagination, int rowIndex) {
        return null;
    }


    @Override
    public String toString() {
        return getName();
    }
}
