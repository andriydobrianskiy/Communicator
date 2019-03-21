package com.mainPage.InProcessing.UpdateOfferingGroupNameNotPrice;

import com.Utils.SearchType;
import com.mainPage.createRequest.CreateInterface;
import javafx.collections.ObservableList;

import java.util.List;

public class UpdateOfferingGroupNameNotPrice implements CreateInterface {
    private String ID;
    private String Name;
    public UpdateOfferingGroupNameNotPrice() {

    }
    public UpdateOfferingGroupNameNotPrice(String id, String name) {
this.ID = id;
        this.Name = name;
    }
    public void setID (String ID) {
        this.ID = ID;
    }
    public String getID () {
        return ID;
    }
    public void setName (String name) {
        this.Name = name;
    }

    @Override
    public List<? extends CreateInterface> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        return null;
    }

    @Override
    public ObservableList<? extends CreateInterface> findAll(boolean pagination, int rowIndex) {
        return null;
    }


    public String getName () {
        return Name;
    }

    @Override
    public String toString() {
        return getName();
    }

 }
