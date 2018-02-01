package com.Utils;

import com.mainPage.InProcessing.InProcessing;

import java.util.List;

public interface InProcessingIntarface {
   /* String getID();
    String getName();

    void setID(String ID);
    void setName(String name);*/

    //  List<? extends InProcessingIntarface> findByProperty1(Object value, Enum<? extends SearchType> searchType);
    List<InProcessing> findAllOne(boolean pagination, int rowIndex, String createdByID, String offeringGroupID);

    List<InProcessing> findAllInProcessing(boolean pagination, int rowIndex);


}
