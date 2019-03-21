package com.mainPage.createRequest;

import javafx.collections.ObservableList;
import com.Utils.SearchType;

import java.util.List;

public interface CreateInterface {

   String getID();
    String getName();

  void setID(String ID);
  void setName(String Name);

    List<? extends CreateInterface> findByProperty(Object value, Enum<? extends SearchType> searchType);



    ObservableList<? extends CreateInterface> findAll(boolean pagination, int rowIndex);

}
