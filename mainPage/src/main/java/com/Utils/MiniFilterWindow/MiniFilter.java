package com.Utils.MiniFilterWindow;

import javafx.scene.control.TableColumn;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import com.Utils.DictionaryProperties;
import com.Utils.Consts;
import com.Utils.FilterQuery;
import com.Utils.UsefulUtils;

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
        try {
            Object value = null;
            final List<IFilterOperator> filters = event.getFilters();

            if(filters.size() < 2) {

            }

            for (IFilterOperator filter : filters) {
                if (filter.getValue() instanceof Date) {
                    try {
                        Date date = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy").parse(filter.getValue().toString());
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        value = dateFormat.format(date);

                    } catch (ParseException e) {

                    }
                } else {

                    value = filter.getValue();
                }

                if (value == null || filter.getType() == IFilterOperator.Type.NONE) continue;
                System.out.println("  Type=" + filter.getType() + ", Value=" + value);

                try {

                    if (filter.getType() == IFilterOperator.Type.NONE) {
                        derby.removeStringFilter(event.sourceColumn());
                        window.disableFilter(event.sourceColumn(), null);
                        window.refreshData();
                        return;
                    }


                    if(filters.stream()
                            .filter(line -> !line.getType().equals(IFilterOperator.Type.NONE))
                            .collect(Collectors.toList()).size() == ADVANCE_FILTER)
                    {
                        derby.setStringFilterMerge(event.sourceColumn(), new FilterQuery(hashColumns.get(event.sourceColumn()), filter.getType(), value).getWhereClause());
                    } else {
                        //window.disableFilter(event.sourceColumn(),null);
                        window.removeFilterFromHbox(event.sourceColumn());
                        derby.setStringFilter(event.sourceColumn(), new FilterQuery(hashColumns.get(event.sourceColumn()), filter.getType(), value).getWhereClause());
                    }

                    window.fillHboxFilter(event.sourceColumn(), filter.getType(), value);


                    window.refreshData();
                } catch (Exception e) {
                    log.log(Level.SEVERE, " Exception: " + e);
                }
            }
        } catch (Exception e) {
            log.log(Level.WARNING, "Exception mini-filter: " + e);
            UsefulUtils.showErrorDialog("Сталась помилка, повідомте на скайп " + Consts.skypeLogin);
        }
    }
}
