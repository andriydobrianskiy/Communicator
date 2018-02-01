package com.Utils;

import com.mainPage.InProcessing.InProcessing;

import java.util.List;

public interface DictionaryProperties {
    void createTableColumns();
    void createTableColumnsAll();
    List<InProcessing> loadDataFromDatabase();
    List<InProcessing> loadDataFromDatabaseAll();


}
