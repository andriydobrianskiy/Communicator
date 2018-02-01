package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import javafx.collections.ObservableList;
import com.Utils.SearchType;

import java.util.List;

public interface ProductSearchInterface {
    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);
  boolean insertAccount(ProductSearch offeringRequest);
    boolean updateAccount(ProductSearch offeringRequest);
    boolean deleteAccount(ProductSearch offeringRequest);

     List findByProperty(Object value, Enum<? extends SearchType> searchType);
  ObservableList<ProductSearch> findAll(boolean pagination, int rowIndex);
}

