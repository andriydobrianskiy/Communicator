package com.mainPage.InTract;

import java.util.List;

public interface InTractInterface {
    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);

    // List<? extends InProcessingIntarface> findByProperty1(Object value, Enum<? extends SearchType> searchType);
    List<InTract> findInTract(boolean pagination, int rowIndex, String createdByID, String offeringGroupID,String filterSorted );
    List<InTract> findInTractAll(boolean pagination, int rowIndex,String filterSorted );
}
