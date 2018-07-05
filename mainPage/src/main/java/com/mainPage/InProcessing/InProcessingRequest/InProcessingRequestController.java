package com.mainPage.InProcessing.InProcessingRequest;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
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

public class InProcessingRequestController extends WorkArea implements Initializable {

    private static Logger log = Logger.getLogger(InProcessingRequestController.class.getName());
    @FXML private FilterableStringTableColumn <InProcessingRequest, String> colIndex;

    @FXML private FilterableStringTableColumn <InProcessingRequest, String>colSkrut;

    @FXML private FilterableStringTableColumn <InProcessingRequest, String>colNewDescription;

    @FXML private FilterableStringTableColumn <InProcessingRequest, String>colOfferingName;

    @FXML private FilterableIntegerTableColumn<InProcessingRequest, Integer> colQuantity;

    @FXML private FilterableStringTableColumn <InProcessingRequest, String>colDefaultOfferingCode;

    @FXML private FilterableStringTableColumn <InProcessingRequest, String>colNewOfferingCode;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<InProcessingRequest> data;
  /*  @FXML
    private AnchorPane anchorPane;*/
    private InProcessing selectedRecord;


    private InProcessingController main;
    private InProcessingRequestQuery queries = new InProcessingRequestQuery();
    private DerbyInProcessingRequestDAO derbyInProcessingRequestDAO = new DerbyInProcessingRequestDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //createTableColumnsProduct();
        // loadDataFromDatabaseBottom();
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();  DBConnection database = new DBConnection();
            database.reconnect();
        }
        setCellTable();
        data = FXCollections.observableArrayList();
        tableView.setTableMenuButtonVisible(true);
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);

    }

    public void init(InProcessingController inProcessingController) {
        main = inProcessingController;
    }

    public void handleTableView(InProcessing value) {
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



            colIndex.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.createTableColumns();

    }

    private StringBuilder query;

    public List<InProcessingRequest> loadDataFromDatabaseBottom() {
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
                data.add(new InProcessingRequest(rs.getString(8), rs.getString(9), rs.getString(14),
                        rs.getString(10), rs.getString(12), rs.getString(11), rs.getString(13)));


            }
            tableView.setItems(data);


        } catch (SQLException e) {
            e.printStackTrace();
        }

        // tableViewRequest.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        return null;
    }


  /*  public void loadDataFromDatabaseBottom() {
        try {

            data = FXCollections.observableArrayList();
            List<InProcessingRequest> listItems = derbyInProcessingRequestDAO.findAll(5, selectedRecord.getID());

            listItems.forEach(item -> data.add(item));

            tableInProcessingProduct.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }



    public void createTableColumnsProduct() {

        try {
            TableColumn<InProcessingRequest, String> index = new TableColumn<InProcessingRequest, String>("Індекс");
            TableColumn<InProcessingRequest, String> skrut = new TableColumn<InProcessingRequest,String>("Скорочення");
            TableColumn<InProcessingRequest, String> offeringName = new TableColumn<InProcessingRequest, String>("Продукт");
            TableColumn<InProcessingRequest, String> quantity = new TableColumn<InProcessingRequest, String>("Кількість");
            TableColumn<InProcessingRequest, String> defaultOfferingCode = new TableColumn<InProcessingRequest, String>("Код товару");



            index.setMinWidth(150);

            //   tableProduct.setVisible(false);
            tableInProcessingProduct.setTableMenuButtonVisible(true);
            tableInProcessingProduct.getColumns().addAll(
                    index,
                    skrut,
                    offeringName,
                    quantity,
                    defaultOfferingCode
            );
            index.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("index"));
            skrut.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("skrut"));
            offeringName.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("offeringName"));
            quantity.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("quantity"));
            defaultOfferingCode.setCellValueFactory(new PropertyValueFactory<InProcessingRequest,String>("defaultOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
   /* public  void setSelectedRecord(OfferingRequest value) {
        this.selectedRecord = value;
        loadDataFromDatabaseBottom();
    }*/


}
