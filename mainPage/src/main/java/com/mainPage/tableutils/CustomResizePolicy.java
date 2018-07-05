package com.mainPage.tableutils;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class CustomResizePolicy implements Callback<TableView.ResizeFeatures, Boolean> {
    static final double DEFAULT_MIN_WIDTH = 10.0F, DEFAULT_MAX_WIDTH = 5000.0F;
    @Override
    public Boolean call(TableView.ResizeFeatures feature) {
        //boolean result = TableView.CONSTRAINED_RESIZE_POLICY.call(feature);
        final TableColumn<?,?> c = feature.getColumn();
        c.setMinWidth(DEFAULT_MIN_WIDTH);
        c.setMaxWidth(DEFAULT_MAX_WIDTH);
        boolean result = TableView.UNCONSTRAINED_RESIZE_POLICY.call(feature);




        return result;
    }
}