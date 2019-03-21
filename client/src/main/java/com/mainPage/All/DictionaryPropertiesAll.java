package com.mainPage.All;

import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;

import java.util.List;

public interface DictionaryPropertiesAll {
    void createTableColumns();
    List<All> loadDataFromDatabase();
    void disableFilter(TableColumn column, Pane content);
}
