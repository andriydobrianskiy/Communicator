package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import com.Utils.SearchType;

import java.util.List;

public interface ProductInterface {
   ///String getID();
   // String getOfferingName();

   // void setID(String ID);
   // void setName(String name);
  // List<ProductSearch> findByProperty(Object value,SearchType searchType );
    List<ProductSearch> findByProperty(Object value, Enum<? extends SearchType> searchType);
    List<ProductSearch> findAll(boolean pagination, int rowIndex);
}
