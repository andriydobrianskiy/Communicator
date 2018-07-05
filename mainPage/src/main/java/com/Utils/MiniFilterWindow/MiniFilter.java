package com.Utils.MiniFilterWindow;

import com.Utils.*;
import javafx.application.Platform;
import javafx.scene.control.TableColumn;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class MiniFilter {
    private static Logger log = Logger.getLogger(MiniFilter.class.getName());
    private static final int ADVANCE_FILTER = 2;

    private HashMap<TableColumn, String> hashColumns;
    private FilterFunctions derby;
    private ColumnFilterEvent event;
    private DictionaryProperties window;

    public MiniFilter(DictionaryProperties window,
                      FilterFunctions derby,
                      HashMap<TableColumn, String> hashColumns,
                      ColumnFilterEvent event) {
        this.window = window;
        this.hashColumns = hashColumns;
        this.derby = derby;
        this.event = event;
    }

    public void setFilter() {
        getFilterValue();

        //new FilterTask().execute();
    }

    private void getFilterValue() {
        Object value = null;
        final List<IFilterOperator> filters = event.getFilters();

        try {

            for (IFilterOperator filter : filters) {
                System.out.println(filter.getValue());
                if (filter.getValue() instanceof Date) {
                    try {
                        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(filter.getValue().toString());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = dateFormat.format(date);

                    } catch (ParseException e) { }
                } else {
                    value = filter.getValue();
                }

                if (value == null || filter.getType() == IFilterOperator.Type.NONE) continue;

                try {

                    if (filter.getType() == IFilterOperator.Type.NONE) {
                        derby.removeStringFilter(event.sourceColumn());
                        window.disableFilter(event.sourceColumn(), null);
                        window.refreshData();
                        //disableFilter(event.sourceColumn());
                        return;
                    }

                    if(filters.stream()
                            .filter(line -> !line.getType().equals(IFilterOperator.Type.NONE))
                            .collect(Collectors.toList()).size() == ADVANCE_FILTER)
                    {
                        derby.setStringFilterMerge(event.sourceColumn(), new FilterQuery(hashColumns.get(event.sourceColumn()), filter.getType(), value).getWhereClause());
                    } else {
                        window.removeFilterFromHbox(event.sourceColumn());
                        derby.setStringFilter(event.sourceColumn(), new FilterQuery(hashColumns.get(event.sourceColumn()), filter.getType(), value).getWhereClause());
                    }

                    window.fillHboxFilter(event.sourceColumn(), filter.getType(), value);
                    window.refreshData();
                    //setupFilter(event.sourceColumn(), filter.getType(), value);

                } catch (Exception e) {
                    log.log(Level.SEVERE, " Exception: " + e);
                }
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Exception mini-filter: " + e);
            UsefulUtils.showErrorDialog("Сталась помилка, повідомте на скайп " + Consts.skypeLogin);
        }
    }

    private void disableFilter(TableColumn column) {
        Platform.runLater(() -> {
            derby.removeStringFilter(column);
            window.disableFilter(column, null);
        });

    }

    private void setupFilter(TableColumn column, IFilterOperator.Type type, Object  value) {
        log.log(Level.SEVERE, "EXE 1");
        Platform.runLater(() -> {
            window.fillHboxFilter(column, type, value);
        });
    }

    public class FilterTask extends AsyncTask<Void, Void, Void> {



        public FilterTask() {

        }



        @Override
        public void onPreExecute() {

        }

        @Override
        public Void doInBackground(Void... params) {

            getFilterValue();
            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            window.refreshData();
        }

        @Override
        public void progressCallback(Void... params) {

        }


    }
}
