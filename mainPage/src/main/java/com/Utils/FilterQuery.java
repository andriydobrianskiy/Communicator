package com.Utils;

import com.Utils.MiniFilterWindow.IFilter;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.operators.NumberOperator;

import java.util.LinkedList;
import java.util.List;

public class FilterQuery {
    private String columnName;
    private IFilterOperator.Type type;
    private Object value = null;
    private LinkedList listValues = null;

    private String query;

    private StringBuilder resultQuery;

    private IFilter andOr = null;

    public FilterQuery(String columnName, NumberOperator.Type type, Object value) {
        this.columnName = columnName;
        this.type = type;
        this.value = value;

        createSQLQuery();
    }

    public FilterQuery(String columnName, NumberOperator.Type type, List listValues) {
        this.columnName = columnName;
        this.type = type;
        this.listValues = new LinkedList(listValues);

        createSQLQuery();
    }

    public FilterQuery(String columnName, IFilter andOr, NumberOperator.Type type, Object value) {
        this.andOr = andOr;
        this.columnName = columnName;
        this.type = type;
        this.value = value;

        createSQLQuery();
    }

    public FilterQuery(String columnName, IFilter andOr, NumberOperator.Type type, List listValues) {
        this.andOr = andOr;
        this.columnName = columnName;
        this.type = type;
        this.listValues = new LinkedList(listValues);

        createSQLQuery();
    }

    public String getWhereClause() {
        return resultQuery.toString();
    }

    private void createSQLQuery() {
        resultQuery = new StringBuilder();

        if(andOr == null) {
            resultQuery.append("  AND ");
        } else  {
            resultQuery.append(andOr.getValue());
        }
        if(columnName != null) {
            resultQuery.append(columnName);
        }
        resultQuery.append(getValueByType() + "\n");

    }

    private String getValueByType() {
        String sqlValue = "";
        try {
            if(type == null) return value.toString();
            sqlValue = UsefulUtils.createSQLString(value.toString());
        } catch (NullPointerException ex) {

        }

        switch (type) {
            case EQUALS:
                return " = " + sqlValue;

            case NOTEQUALS:
                return " <> " + sqlValue;


            case AFTERON:
            case GREATERTHAN:
                return " > " + sqlValue;

            case AFTER:
            case GREATERTHANEQUALS:
                return " >= " + sqlValue;

            case BEFOREON:
            case LESSTHAN:
                return " < " + sqlValue;

            case NOTCONTAINS:
                return " NOT LIKE " + UsefulUtils.createSQLString("%" + value + "%");

            case BEFORE:
            case LESSTHANEQUALS:
                return " <= " + sqlValue;



            case CONTAINS:
                return " LIKE " + UsefulUtils.createSQLString("%" + value + "%");

            case STARTSWITH:
                return " LIKE " + UsefulUtils.createSQLString(value + "%");

            case ENDSWITH:
                return " LIKE " + UsefulUtils.createSQLString("%" + value);



            case TRUE:
                return " = 1";

            case FALSE:
                return " = 0";
            case IN:
                StringBuilder inQuery = new StringBuilder();
                inQuery.append(" IN (");
                if(value == null) {
                    listValues.forEach(item -> {
                        if(item.equals(listValues.getLast())) {
                            inQuery.append(UsefulUtils.createSQLString(item.toString()));
                        } else inQuery.append(UsefulUtils.createSQLString(item.toString()) + ",");
                    });
                } else
                    inQuery.append(sqlValue);

                inQuery.append(")");

                return inQuery.toString();

        }
        return "";
    }
}
