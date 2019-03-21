package com.mainPage.Statistic;

import com.Utils.MiniFilterWindow.FilterFunctions;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXDatePicker;
import com.mainPage.WorkArea;
import com.mainPage.page.MainPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableIntegerTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.UUID;

public class StatisticController extends WorkArea implements MiniFilterFunction, Initializable,FilterFunctions {
    private MainPageController mainPageController;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs= null;
    private ObservableList<Statistic> data;
    @FXML
    private JFXDatePicker fromDate;
    @FXML
    private JFXDatePicker toDate;


    @FXML private FilterableStringTableColumn column_Manager;

    @FXML private FilterableIntegerTableColumn column_InProcessing;

    @FXML private FilterableIntegerTableColumn column_Tract;

    @FXML private FilterableIntegerTableColumn column_Completed;

    @FXML private FilterableIntegerTableColumn column_ReturnDelete;

    @FXML private FilterableIntegerTableColumn column_All;

    private HashMap<String, String> resultFilterMap = new HashMap<>();
    private UUID uniqueID;

    private HashMap<TableColumn, String> mapFilters = new HashMap();
    public ObservableList observableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();  DBConnection database = new DBConnection();
            database.reconnect();
        }
        setCellTable();

        data = FXCollections.observableArrayList();



        this.fromDate.setValue(LocalDate.now());
        this.toDate.setValue(LocalDate.now());


        loadDataFromDatabaseBottom(this.toDate.getValue(),this.fromDate.getValue());
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(StatisticController.this, StatisticController.this, hashColumns,event).setFilter();
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        UsefulUtils.copySelectedCell(tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borderPane.setCenter(tableView);
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        // customizeScene();
        //    tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.setOnKeyReleased(eventKey -> {
            UsefulUtils.searchCombination(eventKey, tableView);
        });
        //   });




    }
    private void setCellTable(){
        column_Manager.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column_InProcessing.setCellValueFactory(new PropertyValueFactory<>("Processing"));
        column_Tract.setCellValueFactory(new PropertyValueFactory<>("Trakt"));
        column_Completed.setCellValueFactory(new PropertyValueFactory<>("Completed"));
        column_ReturnDelete.setCellValueFactory(new PropertyValueFactory<>("Canceled"));
        column_All.setCellValueFactory(new PropertyValueFactory<>("ID"));
    }

    public void loadDataFromDatabaseBottom(LocalDate toDate, LocalDate fromDate){

        //  Date fromDate = Date.valueOf(this.fromDate.getValue());
        //  Date toDate = Date.valueOf(this.toDate.getValue());

        try {

            try {
                pst = con.prepareStatement(  "SELECT\n" +
                        "\tCOUNT(CAST([RO].[ID] AS VARCHAR(38))) AS [ID],\n" +
                        "\t[C].[Name] AS [Name],\n" +
                        "\t(SELECT\n" +
                        "\t\tCOUNT(CAST([RO1].[ID] AS VARCHAR(38))) AS [ID]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_RequestOffering] AS [RO1]\n" +
                        "\tWHERE([RO1].[CreatedByID] = [RO].[CreatedByID] AND\n" +
                        "\t\t[RO1].[StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}' AND\n" +
                        "\t\t[RO1].[CreatedOn] BETWEEN CONVERT(DATETIME, '"+fromDate+"', 121) AND CONVERT(DATETIME, '"+toDate+"', 121))) AS [Processing],\n" +
                        "\t(SELECT\n" +
                        "\t\tCOUNT(CAST([RO2].[ID] AS VARCHAR(38))) AS [ID]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_RequestOffering] AS [RO2]\n" +
                        "\tWHERE([RO2].[CreatedByID] = [RO].[CreatedByID] AND\n" +
                        "\t\t[RO2].[StatusID] = '{3B552198-B239-4801-819C-7033AA118B65}' AND\n" +
                        "\t\t[RO2].[CreatedOn] BETWEEN CONVERT(DATETIME, '"+fromDate+"', 121) AND CONVERT(DATETIME, '"+toDate+"', 121))) AS [Trakt],\n" +
                        "\t(SELECT\n" +
                        "\t\tCOUNT(CAST([RO3].[ID] AS VARCHAR(38))) AS [ID]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_RequestOffering] AS [RO3]\n" +
                        "\tWHERE([RO3].[CreatedByID] = [RO].[CreatedByID] AND\n" +
                        "\t\t[RO3].[StatusID] = '{6F784E48-1474-4CB9-B28D-A4B580FB346C}' AND\n" +
                        "\t\t[RO3].[CreatedOn] BETWEEN CONVERT(DATETIME, '"+fromDate+"', 121) AND CONVERT(DATETIME, '"+toDate+"', 121))) AS [Completed],\n" +
                        "\t(SELECT\n" +
                        "\t\tCOUNT(CAST([RO4].[ID] AS VARCHAR(38))) AS [ID]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_RequestOffering] AS [RO4]\n" +
                        "\tWHERE([RO4].[CreatedByID] = [RO].[CreatedByID] AND\n" +
                        "\t\t[RO4].[StatusID] = '{3E7F90E2-335C-41B2-828A-576A06375B8C}' AND\n" +
                        "\t\t[RO4].[CreatedOn] BETWEEN CONVERT(DATETIME, '"+fromDate+"', 121) AND CONVERT(DATETIME, '"+toDate+"', 121))) AS [Canceled],\n" +
                        "\t[RO].[CreatedByID] AS [CreatedByID]\n" +
                        "FROM\n" +
                        "\t[dbo].[tbl_RequestOffering] AS [RO]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_Contact] AS [C] ON [C].[ID] = [RO].[CreatedByID]\n" +
                        "WHERE([RO].[CreatedOn] BETWEEN CONVERT(DATETIME, '"+fromDate+"', 121) AND CONVERT(DATETIME, '"+toDate+"', 121))\n" + getStringFilter()+
                        "GROUP BY\n" +
                        "\t[C].[Name],\n" +
                        "\t[RO].[CreatedByID]\n" );
                System.out.println(pst);
            } catch (SQLException e) {
                e.printStackTrace();
                DBConnection database = new DBConnection();
                database.reconnect();
            }
            rs = pst.executeQuery();
            while (rs.next()){
                data.add(new Statistic(rs.getString(2),rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(1)));


            }

        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        tableView.setItems(data);
    }

    @FXML
    private void actiontoDate (ActionEvent e) {
        data.clear();
        loadDataFromDatabaseBottom(this.toDate.getValue(),this.fromDate.getValue());
    }
    @FXML
    private void actionfromDate (ActionEvent event) {
        data.clear();
        loadDataFromDatabaseBottom(this.toDate.getValue(),this.fromDate.getValue());

    }

    public void init(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }


    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            loadDataFromDatabaseBottom(this.toDate.getValue(),this.fromDate.getValue());
        }

        UsefulUtils.fadeTransition(tableView);
    }

    @Override
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

            hboxFilter.setMargin(content, new Insets(0, 0, 0, 10));

            scrollPaneFilter.setFitToHeight(true);

            System.out.println("SETTTED");
            //content.getStylesheets().add("sample/ua/ucountry/MainTables/Account/MainDictionaryTable.css");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    @Override
    public void setStringFilter(TableColumn column, String value) {
        mapFilters.put(column, value);
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
    @Override
    public void disableFilter(TableColumn column, Pane content) {
        this.removeStringFilter(column);
        refreshData();

        //column.filteredProperty().setValue(true);

        if(content == null) {
            removeFilterFromHbox(column);
            return;
        }

        hboxFilter.getChildren().remove(content);


        System.out.println("DISABLED");
    }
}
