package com.Utils;

import com.mainPage.createRequest.searchCounterpart.Counterpart;

import java.util.List;

public interface DictionaryInterface {
    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);

    List<Counterpart> findByProperty(Object value, Enum<? extends SearchType> searchType);
    List<Counterpart> findAll(boolean pagination, int rowIndex);
}
