package com.Utils.MiniFilterWindow;


import javafx.scene.control.TableColumn;

public interface FilterFunctions {
    void setStringFilter(TableColumn key, String value);
    void setStringFilterMerge(TableColumn column, String value);
    String getStringFilter();
    void removeStringFilter(TableColumn key);
}
