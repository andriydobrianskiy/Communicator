package com.mainPage.page.group;

import com.Utils.SearchType;

public enum GroupSearchType implements SearchType {
    NAME("Назва групи");


    private String label;

    GroupSearchType(String label) {
        this.label = label;
    }

    public String getSearchType() {
        return label;
    }

    public String toString() {
        return label;
    }

}
