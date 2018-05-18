package com.Utils.Services;

import com.Utils.SearchType;


public enum OfferingsSearchType implements SearchType{
    ID(""),
    NAME("Назва продукту"),
    INDEX("Індекс"),
    SKRUT("Скорочення");



    private String label;

    OfferingsSearchType(String label) {
        this.label = label;
    }

    public String getSearchType() {
        return label;
    }

    public String toString() {
        return label;
    }
}
