package com.mainPage.NotFulled;

import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;

import java.util.List;

public interface DictionaryPropertiesNotfulled {

    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);
    void createTableColumns();
    List<NotFulfilled> loadDataFromDatabase();
    void disableFilter(TableColumn column, Pane content);

  //  List<ProductList> loadDataFromDatabaseBottom();

   // void createTableColumnsProduct();
}
