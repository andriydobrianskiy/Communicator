package com.Utils;

import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import org.google.jhsheets.filtered.operators.IFilterOperator;

public interface DictionaryProperties {

    void createTableColumns();

    void createTableColumnsAll();

    void loadDataFromDatabase();
    void disableFilter(TableColumn column, Pane content);
    void removeFilterFromHbox(TableColumn column);
    void refreshData();
    void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value);
    void loadDataFromDatabaseAll();
}
