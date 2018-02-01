package com.mainPage.InTract.InTractRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.InTract.InTract;
import com.mainPage.InTract.InTractController;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class InTractRequestController implements Initializable {
    private static Logger log = Logger.getLogger(InTractRequestController.class.getName());
    private InTractController main;
    @FXML
    private TableView<InTractRequest> tableViewRequest;
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
    private ObservableList<InTractRequest> data;
    private DerbyInTractRequestDAO derbyInTractRequestDAO = new DerbyInTractRequestDAO();
    private InTract selectedRecord;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // createTableColumnsProduct();
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        setCellTable();
        data = FXCollections.observableArrayList();
        // loadDataFromDatabaseBottom();
        tableViewRequest.setTableMenuButtonVisible(true);
        tableViewRequest.getSelectionModel().setCellSelectionEnabled(false);
        tableViewRequest.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableViewRequest);
    }

    public void init(InTractController inTractController) {
        main = inTractController;
    }

    public void handleTableView(InTract value) {
        this.selectedRecord = value;
        data.clear();
        loadDataFromDatabaseBottom();

    }



  /*  public void loadDataFromDatabaseBottom() {

        try {

            data = FXCollections.observableArrayList();
           // System.out.println(DBConnection.getDataSource().getConnection().toString());
            List<InTractRequest> listItems = derbyInTractRequestDAO.findAll(true,1);

            listItems.forEach(item -> data.add(item));

            tableViewRequest.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }



    public void createTableColumnsProduct() {

        try {
            TableColumn<InTractRequest, String> index = new TableColumn<InTractRequest, String>("Індекс");
            TableColumn<InTractRequest, String> skrut = new TableColumn<InTractRequest,String>("Скорочення");
            TableColumn<InTractRequest, String> offeringName = new TableColumn<InTractRequest, String>("Продукт");
            TableColumn<InTractRequest, String> quantity = new TableColumn<InTractRequest, String>("Кількість");
            TableColumn<InTractRequest, String> defaultOfferingCode = new TableColumn<InTractRequest, String>("Код товару");



            index.setMinWidth(150);

            //   tableProduct.setVisible(false);
            tableViewRequest.setTableMenuButtonVisible(true);
            tableViewRequest.getColumns().addAll(
                    index,
                    skrut,
                    offeringName,
                    quantity,
                    defaultOfferingCode
            );
            index.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("Index"));
            skrut.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("Skrut"));
            offeringName.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("OfferingName"));
            quantity.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("Quantity"));
            defaultOfferingCode.setCellValueFactory(new PropertyValueFactory<InTractRequest,String>("DefaultOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

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
        }
        tableViewRequest.setItems(data);
        // tableViewRequest.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        return null;
    }
}
