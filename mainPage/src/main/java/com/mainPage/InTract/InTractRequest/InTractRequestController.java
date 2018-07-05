package com.mainPage.InTract.InTractRequest;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.InTract.InTract;
import com.mainPage.InTract.InTractController;
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
import java.util.logging.Logger;

public class InTractRequestController extends WorkArea implements Initializable {
    private static Logger log = Logger.getLogger(InTractRequestController.class.getName());
    private InTractController main;
    @FXML private FilterableStringTableColumn<InTractRequest, String> colIndex;

    @FXML private FilterableStringTableColumn <InTractRequest, String>colSkrut;

    @FXML private FilterableStringTableColumn <InTractRequest, String>colNewDescription;

    @FXML private FilterableStringTableColumn <InTractRequest, String>colOfferingName;

    @FXML private FilterableIntegerTableColumn<InTractRequest, Integer> colQuantity;

    @FXML private FilterableStringTableColumn <InTractRequest, String>colDefaultOfferingCode;

    @FXML private FilterableStringTableColumn <InTractRequest, String>colNewOfferingCode;

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<InTractRequest> data;
    private DerbyInTractRequestDAO derbyInTractRequestDAO = new DerbyInTractRequestDAO();
    private InTract selectedRecord;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // createTableColumnsProduct();
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        setCellTable();
        data = FXCollections.observableArrayList();
        // loadDataFromDatabaseBottom();
        tableView.setTableMenuButtonVisible(true);
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);
    }

    public void init(InTractController inTractController) {
        main = inTractController;
    }

    public void handleTableView(InTract value) {
        this.selectedRecord = value;
        data.clear();
        loadDataFromDatabaseBottom();

    }





    private void setCellTable() {

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



            colIndex.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<InTractRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<InTractRequest,Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<InTractRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.createTableColumns();
    }

    private StringBuilder query;

    public List<InTractRequest> loadDataFromDatabaseBottom() {
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
            while (rs.next()) {
                data.add(new InTractRequest(rs.getString(8), rs.getString(9), rs.getString(14),
                        rs.getString(10), rs.getString(12), rs.getString(11), rs.getString(13)));


            }

        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        tableView.setItems(data);
        // tableViewRequest.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        return null;
    }


}
