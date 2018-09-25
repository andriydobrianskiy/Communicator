package com.mainPage.InProcessing.InProcessingRequest;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.login.User;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.WorkArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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

    @FXML
    private JFXButton colorByOfferingSaleButton;
    @FXML
    private JFXButton cancelColorByOfferingSaleButton;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<InProcessingRequest> data;
    private InProcessing selectedRecord;
    private InProcessingController main;
    private InProcessingRequestQuery queries = new InProcessingRequestQuery();
    private DerbyInProcessingRequestDAO derbyInProcessingRequestDAO = new DerbyInProcessingRequestDAO();
    private InProcessingRequest inProcessingRequest =null;

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

        updateColor();

        colorByOfferingSaleButton.setOnAction(event ->{
            try {
                inProcessingRequest = (InProcessingRequest) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви підтверджуєте, що продукт потрібно замовляти?") == ButtonType.OK) {

                String query = "Update tbl_OfferingInRequestOffering\n" +
                        "SET\n" +
                        "\n" +
                        "ModifiedByID = ?,\n" +
                        "ModifiedOn = CURRENT_TIMESTAMP,\n" +
                        "ColorByOfferingSale = 1\n" +
                        "WHERE \n" +
                        "ID = ?";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, inProcessingRequest.getID());
                    pst.executeUpdate();

                    refreshData();
                    UsefulUtils.showSuccessful("Продукт " + inProcessingRequest.getSkrut() + " добавлено в продаж");
                } catch (SQLException e) {
                    DBConnection database = new DBConnection();
                    database.reconnect();
                }
            } else return;


        });

        cancelColorByOfferingSaleButton.setOnAction(event ->{
            try {
                inProcessingRequest = (InProcessingRequest) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви підтверджуєте зняття продукту з замовлення?") == ButtonType.OK) {

                String query = "Update tbl_OfferingInRequestOffering\n" +
                        "SET\n" +
                        "\n" +
                        "ModifiedByID = ?,\n" +
                        "ModifiedOn = CURRENT_TIMESTAMP,\n" +
                        "ColorByOfferingSale = 0\n" +
                        "WHERE \n" +
                        "ID = ?";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, inProcessingRequest.getID());
                    pst.executeUpdate();

               refreshData();
                    UsefulUtils.showSuccessful("Продукт " + inProcessingRequest.getSkrut() + " знято з продажу");
                } catch (SQLException e) {
                    DBConnection database = new DBConnection();
                    database.reconnect();
                }
            } else return;
        });
        //tableView.getSelectionModel().getSelectedCells().get(0);


    }



    public void updateColor (){
        tableView.setRowFactory(new Callback<TableView<InProcessingRequest>, TableRow<InProcessingRequest>>() {
            @Override
            public TableRow<InProcessingRequest> call(TableView<InProcessingRequest> param) {
                return new TableRow<InProcessingRequest>() {
                    @Override
                    protected void updateItem(InProcessingRequest item, boolean empty) {
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
                        "\tWHERE([tbl_OfferingAnalogue].[OfferingID] = [tbl_OfferingInRequestOffering].[OfferingID])) AS [IsRoot],\n" +
                        "\t [tbl_OfferingInRequestOffering].[ColorByOfferingSale] AS [ColorByOfferingSale] \n" +
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
                        rs.getString(10), rs.getString(12), rs.getString(11), rs.getString(13), rs.getInt(16), rs.getString(1)));


            }
            tableView.setItems(data);


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void refreshData() {
        try {


            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            updateColor();
            loadDataFromDatabaseBottom();

            UsefulUtils.fadeTransition(tableView);
        }

    }
}
