package com.mainPage;

import com.Utils.*;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.mainPage.tableutils.CustomTableView;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.google.jhsheets.filtered.operators.IFilterOperator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public abstract class WorkArea extends BorderPane implements MiniFilterFunction, DictionaryProperties {
    protected static Logger log = Logger.getLogger(WorkArea.class.getName());

    private final static String VISIBILITY_PREFERENCE = ":visibility";
    private final static String RESIZE_PREFERENCE = ":resize";
    private final static Integer ROWS_PER_PAGE = 10;

    protected DerbyComp derbyOrderDAO;
    protected ObservableList<GridComp> data;
    public GridComp offeringRequest;

    protected Preferences windowPreference = Preferences.userNodeForPackage(this.getClass());

    @FXML
    protected Pagination pagination;
    @FXML public CustomTableView tableView;

    @FXML
    private TextArea textArea;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    public int pageCount = 5;
    public int itemsPerPage = 40;
    public int currentPageIndex = 0;

    @FXML protected BorderPane borderPaneTV;
    @FXML protected BorderPane borderPane;
    @FXML protected HBox hboxFilter;
    @FXML protected ScrollPane scrollPaneFilter;
    @FXML protected BorderPane anchorPane;

    protected SummaryTable summaryTable;

    protected long fromIndex;
    protected long toIndex;

    protected HashMap<TableColumn, String> hashColumns = new HashMap<>();
    protected HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();

    protected void initComponents() {}

    public void createTableColumns() {
        try {
            invokePreferenceHandler();

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }


    protected void fillWindowComponents() {

    }

    private void invokePreferenceHandler() {
        try {
            startTableViewPropertyListeners();
            setTableViewPropertyListeners();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }

    public void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/MiniFilter.fxml"));

        Pane content;
        try {
            content = fxmlLoader.load();

            MiniFilterController miniC = fxmlLoader.getController();

            miniC.setWindow(this, content);
            miniC.setFilter(column, type, value);

            hboxFilter.getChildren().add(content);

            hashMiniFilter.put(column, content);

            hboxFilter.setMargin(content, new Insets(0,0,0,10));

            scrollPaneFilter.setFitToHeight(true);

            log.log(Level.INFO, "SETTED");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    protected synchronized BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * ROWS_PER_PAGE;
            toIndex = Math.min(fromIndex + ROWS_PER_PAGE, data.size());

            loadDataFromDatabase();

            tableView.setTableItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }



        return borderPane;
    }










    public synchronized void loadDataFromDatabase() {
        try {
            tableView.setTableItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }


    protected synchronized void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {

            UsefulUtils.searchCombination(eventKey, tableView);


            if(eventKey.getCode().equals(KeyCode.DOWN) || eventKey.getCode().equals(KeyCode.UP)) { // arrow up down
                fixSelectedRecord();

            }
        });

        UsefulUtils.copySelectedCell(tableView);

    }
    public void updateColor (){
        tableView.setRowFactory(new Callback<TableView<GridComp>, TableRow<GridComp>>() {
            @Override
            public TableRow<GridComp> call(TableView<GridComp> param) {
                return new TableRow<GridComp>() {
                    @Override
                    protected void updateItem(GridComp item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            setStyle(" ");
                            if(item.getColorByOfferingSale().equals(0) || item.getColorByOfferingSale().equals(null)) {
                                setStyle(" ");
                            }else if (item.getColorByOfferingSale().equals(1)) {
                                setStyle("-fx-background-color: #2eff3b;");
                            }

                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }


                    }
                };
            }
        });
    }


    protected void fixSelectedRecord() {}
    protected void fixSelectedRecordAll() {}

    protected void showEditWindow() {}

    public synchronized void refreshData() {
        //new RefreshTask(this, borderPaneTV, tableView, data).execute(); //TODO
        try {
            tableView.setSelectedRow();
            data.clear();
        } catch (NullPointerException ex) {
        } finally {

            loadDataFromDatabase();
            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
            UsefulUtils.fadeTransition(tableView);

        }
        UsefulUtils.fadeTransition(tableView);
    }

    public void setTableViewPropertyListeners() {

        tableView.getColumns().forEach(item -> {
            ((TableColumn) item).widthProperty().addListener((ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) -> {
                if(windowPreference.get(((TableColumn) item).getId()+ RESIZE_PREFERENCE, null) != null)
                    windowPreference.remove(((TableColumn) item).getId()+ RESIZE_PREFERENCE);
                windowPreference.putDouble(((TableColumn) item).getId()+ RESIZE_PREFERENCE, newWidth.doubleValue());
            });

            ((TableColumn) item).visibleProperty().addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
                if(windowPreference.get(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE, null) != null)
                    windowPreference.remove(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE);
                windowPreference.putBoolean(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE, newValue);
            });
        });
    }

    public void startTableViewPropertyListeners() {

        tableView.getColumns()
                .forEach(item -> {
                    ((TableColumn) item).setPrefWidth(windowPreference.getDouble(((TableColumn) item).getId()+ RESIZE_PREFERENCE, ((TableColumn) item).getWidth()));
                    ((TableColumn) item).setVisible(windowPreference.getBoolean(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE, ((TableColumn) item).isVisible()));
                });
    }



    protected void setWindowPreference(Preferences preference) {
        this.windowPreference = preference;
    }

    protected void executeTask(TableType type) {
        new PreLoader(this, type).execute();
    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {

    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }


    public class PreLoader extends AsyncTask<Void, Void, Void> {

        private WorkArea area;
        private TableType type;


        public PreLoader(WorkArea area, TableType type) {
            this.area = area;
            this.type = type;

            borderPaneTV.setCenter(UsefulUtils.getProgressIndicator());
            if(pagination != null) getPagination();
        }


        @Override
        public void onPreExecute() {
            area.fillWindowComponents();
        }

        @Override
        public Void doInBackground(Void... params) {
            area.createTableColumns();
            area.initComponents();

            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            switch (type) {
                case REGULAR_TABLE:
                    borderPaneTV.setCenter(tableView);
                    break;
                case SUMMARY_TABLE:
                    borderPaneTV.setCenter(summaryTable.setSummaryTable());
                    break;
            }

        }

        @Override
        public void progressCallback(Void... params) {

        }

        private synchronized void getPagination() {
            try {
                CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination);
                pagination.setSkin(pageSkin);
                pagination.setPageFactory(num -> area.createPage(num));

            } catch (Exception e) {
                log.log(Level.SEVERE, "Exception: " + e);
            }
        }


    }

    public class RefreshTask extends AsyncTask<Void, Void, Void> {
        private WorkArea area;
        private BorderPane borderPaneTV;
        private TableView tableView;
        private ObservableList<GridComp> data;

        public RefreshTask(WorkArea area, BorderPane borderPaneTV, TableView tableView, ObservableList data) {
            this.area = area;
            this.borderPaneTV = borderPaneTV;
            this.tableView = tableView;
            this.data = data;

            borderPaneTV.setCenter(UsefulUtils.getProgressIndicator());

        }


        @Override
        public void onPreExecute() {
            try {
                data.clear();
            } catch (NullPointerException ex) {
                // ignored
            }
        }

        @Override
        public Void doInBackground(Void... params) {
            if(pagination == null) area.loadDataFromDatabase();

            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            UsefulUtils.fadeTransition(tableView);
            borderPaneTV.setCenter(tableView);
        }

        @Override
        public void progressCallback(Void... params) {

        }


    }

    public void setTablePaginationSorted (){

    }
    protected int getPageCount(int totalCount, int itemsPerPage) {
        float floatCount = Float.valueOf(totalCount) / Float.valueOf(itemsPerPage);
        int intCount = totalCount / itemsPerPage;
        System.out.println("floatCount=" + floatCount + ", intCount=" + intCount);
        return ((floatCount > intCount) ? ++intCount : intCount);
    }



}

