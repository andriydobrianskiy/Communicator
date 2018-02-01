package com.mainPage.NotFulled.ProductAdd;

public interface ObservableNF {
    void add(ObserverNF value);
    void remove(ObserverNF value);
    void changeExists();
}
