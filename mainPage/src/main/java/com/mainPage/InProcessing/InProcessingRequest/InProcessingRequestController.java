package com.mainPage.InProcessing.InProcessingRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class InProcessingRequestController implements Initializable {

    private static Logger log = Logger.getLogger(InProcessingRequestController.class.getName());
    @FXML
    private TableView<InProcessingRequest> tableInProcessingProduct;
    @FXML
    private TableColumn<?, ?> columnIndex;
    @FXML
    private TableColumn<?, ?> columnSkrut;
    @FXML
    private TableColumn<?, ?> columnOfferingName;
    @FXML
    private TableColumn<?, ?> columnQuantity;
    @FXML
    private TableColumn<?, ?> columnDefaultOfferingCode;
    @FXML
    private TableColumn<?, ?> columnNewOfferingCode;
    @FXML
    private TableColumn<?, ?> columnNewDescription;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<InProcessingRequest> data;
    @FXML
    private AnchorPane anchorPane;
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
            e.printStackTrace();
        }
        setCellTable();
        data = FXCollections.observableArrayList();
        tableInProcessingProduct.setTableMenuButtonVisible(true);
        tableInProcessingProduct.getSelectionModel().setCellSelectionEnabled(false);
        tableInProcessingProduct.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableInProcessingProduct);

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
        columnIndex.setCellValueFactory(new PropertyValueFactory<>("Index"));
        columnSkrut.setCellValueFactory(new PropertyValueFactory<>("Skrut"));
        columnNewDescription.setCellValueFactory(new PropertyValueFactory<>("newDescription"));
        columnOfferingName.setCellValueFactory(new PropertyValueFactory<>("OfferingName"));
        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        columnDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<>("DefaultOfferingCode"));
        columnNewOfferingCode.setCellValueFactory(new PropertyValueFactory<>("newOfferingCode"));

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
            tableInProcessingProduct.setItems(data);


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
