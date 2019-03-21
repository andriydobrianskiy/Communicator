package com.mainPage;

import com.Utils.GridComp;
import com.Utils.MiniFilterWindow.FilterFunctions;
import com.mainPage.NotFulled.NotFulledQuery;
import javafx.scene.control.TableColumn;
import org.apache.commons.dbutils.QueryRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public abstract class DerbyComp implements FilterFunctions{

    private static Logger log = Logger.getLogger(DerbyComp.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private NotFulledQuery orderQueries = new NotFulledQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";

    private HashMap<String, String> resultFilterMap = new HashMap<>();


    private HashMap<TableColumn, String> mapFilters = new HashMap();

    protected boolean insertOffering(GridComp repairNoteRecord) { return false; }
    protected boolean updateOffering(GridComp repairNoteRecord) { return false; }
    protected boolean deleteOffering(GridComp repairNoteRecord) { return false; }

    @Override
    public void setStringFilter(TableColumn key, String value) {
        mapFilters.put(key, value);
    }

    @Override
    public void setStringFilterMerge(TableColumn column, String value) {
        mapFilters.merge(column, value, (a, b) -> a + "\n" + b);
    }

    @Override
    public String getStringFilter() {
        StringBuilder builder = new StringBuilder("");
        try {
            mapFilters.forEach((k, v) ->
                    builder.append(v));
        } catch (NullPointerException e) {

        }


        return builder.toString();
    }

    @Override
    public void removeStringFilter(TableColumn key) {
        mapFilters.remove(key);
    }

    public void clearHashMapFilter() {
        resultFilterMap.clear();
    }
}