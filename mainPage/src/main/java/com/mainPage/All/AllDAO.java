package com.mainPage.All;

import java.util.List;

public interface AllDAO {

    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);

    // List<? extends InProcessingIntarface> findByProperty1(Object value, Enum<? extends SearchType> searchType);
    List<All> findAll(boolean pagination, int rowIndex, String createdByID, String offeringGroupID, String filterSorted);
     List<All> findAllAll(boolean pagination, int rowIndex, String filterSorted);
}
