package com.mainPage.Statistic;

import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXDatePicker;
import com.mainPage.page.MainPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StatisticController implements Initializable{
    private MainPageController mainPageController;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs= null;
    private ObservableList<Statistic> data;
    @FXML
    private JFXDatePicker fromDate;
    @FXML
    private JFXDatePicker toDate;
    @FXML
    private TableView <Statistic> tableView;
    @FXML
    private TableColumn <?, ?> column_Manager;
    @FXML
    private TableColumn <?, ?> column_InProcessing;
    @FXML
    private TableColumn <?, ?> column_Tract;
    @FXML
    private TableColumn <?, ?> column_Completed;
    @FXML
    private TableColumn <?, ?> column_ReturnDelete;
    @FXML
    private TableColumn <?, ?> column_All;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setCellTable();

        data = FXCollections.observableArrayList();



        this.fromDate.setValue(LocalDate.now());
        this.toDate.setValue(LocalDate.now());


        loadDataFromDatabaseBottom(this.toDate.getValue(),this.fromDate.getValue());

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
                        "WHERE([RO].[CreatedOn] BETWEEN CONVERT(DATETIME, '"+fromDate+"', 121) AND CONVERT(DATETIME, '"+toDate+"', 121))\n" +
                        "GROUP BY\n" +
                        "\t[C].[Name],\n" +
                        "\t[RO].[CreatedByID]\n" +
                        "ORDER BY\n" +
                        "\t1 DESC");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            /*pst.setString(1, String.valueOf(Date.valueOf(this.fromDate.getValue())));
            pst.setString(2, String.valueOf(Date.valueOf(this.toDate.getValue())));
            pst.setString(3, String.valueOf((Date.valueOf(this.fromDate.getValue()))));
            pst.setString(4, String.valueOf(Date.valueOf(this.toDate.getValue())));
            pst.setString(5, String.valueOf((Date.valueOf(this.fromDate.getValue()))));
            pst.setString(6, String.valueOf(Date.valueOf(this.toDate.getValue())));
            pst.setString(7, String.valueOf((Date.valueOf(this.fromDate.getValue()))));
            pst.setString(8, String.valueOf(Date.valueOf(this.toDate.getValue())));
            pst.setString(9, String.valueOf(fromDate));
            pst.setString(10, String.valueOf(Date.valueOf(this.toDate.getValue())));*/
            rs = pst.executeQuery();
            while (rs.next()){
                data.add(new Statistic(rs.getString(2),rs.getString(3), rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(1)));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(data);
        // tableViewRequest.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));

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
}
