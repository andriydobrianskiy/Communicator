package com.Utils.MiniFilterWindow;

public enum IFilter {

    AND(" AND "),
    OR(" OR ");



    private String label;

    IFilter(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }

    public String toString() {
        return label;
    }
}
