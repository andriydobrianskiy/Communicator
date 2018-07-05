package com.mainPage.All.AllRequest;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.All.All;
import com.mainPage.All.AllController;
import com.mainPage.WorkArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import org.google.jhsheets.filtered.tablecolumn.FilterableIntegerTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class AllRequestController extends WorkArea implements Initializable {



    @FXML
    private AllController allController;

    @FXML private FilterableStringTableColumn<AllRequest, String> colIndex;

    @FXML private FilterableStringTableColumn <AllRequest, String>colSkrut;

    @FXML private FilterableStringTableColumn <AllRequest, String>colNewDescription;

    @FXML private FilterableStringTableColumn <AllRequest, String>colOfferingName;

    @FXML private FilterableIntegerTableColumn<AllRequest, Integer> colQuantity;

    @FXML private FilterableStringTableColumn <AllRequest, String>colDefaultOfferingCode;

    @FXML private FilterableStringTableColumn <AllRequest, String>colNewOfferingCode;

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs= null;
    private ObservableList<AllRequest> data;
    private All selectedRecord;


public void init (AllController allController){
    this.allController = allController;
}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setTableMenuButtonVisible(true);
        setCellTable();
        data = FXCollections.observableArrayList();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);
    }

    public void handleTableView(All value) {
        this.selectedRecord = value;
        data.clear();
        loadDataFromDatabaseBottom();
    }
    private void setCellTable(){
        try {
            hashColumns.put(colIndex, "[tbl_RequestOffering].[Number]");
            hashColumns.put(colSkrut, "[tbl_RequestOffering].[CreatedOn]");
            hashColumns.put(colNewDescription, "[tbl_Contact].[Name]");
            hashColumns.put(colOfferingName, "[tbl_Account].[Name]");
            hashColumns.put( colQuantity, "[tbl_Account].[Code]");
            hashColumns.put(colDefaultOfferingCode, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colNewOfferingCode, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");



            colIndex.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<AllRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<AllRequest,Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<AllRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.createTableColumns();
    }
    private StringBuilder query;
    public List<AllRequest> loadDataFromDatabaseBottom(){
        try {




            try {
                pst = con.prepareStatement("SELECT [tbl_OfferingInRequestOffering].[ID] AS [ID],\n" +
                        "\t[tbl_OfferingInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                        "\t[tbl_OfferingInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                        "\t[tbl_OfferingInRequestOffering].[ModifiedOn] AS [ModifiedOn],\n" +
                        "\t[tbl_OfferingInRequestOffering].[ModifiedByID] AS [ModifiedByID],\n" +
                        "\t[tbl_OfferingInRequestOffering].[OfferingID] AS [OfferingID],\n" +
                        "\t[tbl_OfferingInRequestOffering].[RequestID] AS [RequestID],\n" +
                        "\t[tbl_Offering].[Index] AS [Index],\n" +
                        "\t[tbl_Offering].[Skrut] AS [Skrut],\n" +
                        "\t[tbl_Offering].[Name] AS [OfferingName],\n" +
                        "\t[tbl_Offering].[DefaultOfferingCode] AS [DefaultOfferingCode],\n" +
                        "\t[tbl_OfferingInRequestOffering].[Quantity] AS [Quantity],\n" +
                        "\t[tbl_OfferingInRequestOffering].[NewOfferingCode] AS [NewOfferingCode],\n" +
                        "\t[tbl_OfferingInRequestOffering].[NewDescription] AS [NewDescription],\n" +
                        "\t(SELECT TOP 1\n" +
                        "\t\t'-' AS [Exist]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_OfferingAnalogue] AS [tbl_OfferingAnalogue]\n" +
                        "\tWHERE([tbl_OfferingAnalogue].[OfferingID] = [tbl_OfferingInRequestOffering].[OfferingID])) AS [IsRoot]\n" +
                        "FROM\n" +
                        "\t[dbo].[tbl_OfferingInRequestOffering] AS [tbl_OfferingInRequestOffering]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_Offering] AS [tbl_Offering] ON [tbl_Offering].[ID] = [tbl_OfferingInRequestOffering].[OfferingID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_OfferingInRequestOffering].[RequestID]\n" +
                        "WHERE [tbl_OfferingInRequestOffering].[RequestID] = ? ");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pst.setString(1, selectedRecord.getID());
            rs = pst.executeQuery();
            while (rs.next()){
                data.add(new AllRequest(rs.getString(8),rs.getString(9), rs.getString(14),
                        rs.getString(10), rs.getString(12), rs.getString(11), rs.getString(13)));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(data);
        // tableViewRequest.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        return null;
    }



}
