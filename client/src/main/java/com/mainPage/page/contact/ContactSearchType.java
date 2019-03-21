package com.mainPage.page.contact;

import com.Utils.SearchType;

public enum ContactSearchType implements SearchType {
    NAMECONTACT("П.І.Б"),
    NAMEJOB("Посада");

    private String label;

    ContactSearchType(String label) {
        this.label = label;
    }

    public String getSearchType() {
        return label;
    }

    public String toString() {
        return label;
    }

}
