package com.Utils;


import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.NotFulled.NotFulledController;

import java.util.ArrayList;
import java.util.List;

public class TableObjects {

    public static List<Object> getDictionaryMap() { // Initialization listview labels
        List<Object> list = new ArrayList<Object>();


        list.add(new NotFulledController()); // Контрагенти
       // list.add(new ProductNotFulfilledController()); // Послуги
        list.add(new InProcessingController());



        return list;
    }
}