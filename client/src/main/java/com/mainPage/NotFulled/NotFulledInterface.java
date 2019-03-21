package com.mainPage.NotFulled;

import com.Utils.SearchType;

import java.util.List;

public interface NotFulledInterface {
    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);

      List<? extends NotFulledInterface> findByProperty1(Object value, Enum<? extends SearchType> searchType);
    List<NotFulfilled> findAllNotFulled(boolean pagination, int rowIndex, String createdByID, String offeringGroupID, Integer pricingType);
}
