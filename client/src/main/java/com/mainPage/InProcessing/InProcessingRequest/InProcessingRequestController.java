package com.mainPage.InProcessing.InProcessingRequest;

import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.login.User;
import com.mainPage.InProcessing.Edit.WindowOperation;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.NotFulled.Edit.EditController;
import com.mainPage.NotFulled.ProductAdd.ProductAddController;
import com.mainPage.NotFulled.ProductAdd.ProductAddNewController;
import com.mainPage.WorkArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.google.jhsheets.filtered.tablecolumn.FilterableIntegerTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InProcessingRequestController extends WorkArea
        implements Initializable {

    private static Logger log = Logger.getLogger(InProcessingRequestController.class.getName());
    @FXML
    private FilterableStringTableColumn<InProcessingRequest, String> colIndex;

    @FXML
    private FilterableStringTableColumn<InProcessingRequest, String> colSkrut;

    @FXML
    private FilterableStringTableColumn<InProcessingRequest, String> colNewDescription;

    @FXML
    private FilterableStringTableColumn<InProcessingRequest, String> colOfferingName;

    @FXML
    private FilterableIntegerTableColumn<InProcessingRequest, Integer> colQuantity;

    @FXML
    private FilterableStringTableColumn<InProcessingRequest, String> colDefaultOfferingCode;

    @FXML
    private FilterableStringTableColumn<InProcessingRequest, String> colNewOfferingCode;
    @FXML
    public JFXButton editButton;
    @FXML
    public JFXButton colorByOfferingSaleButton;
    @FXML
    public JFXButton cancelColorByOfferingSaleButton;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<InProcessingRequest> data;
    private InProcessing selectedRecord = null;
    private InProcessingController main;
    private InProcessingRequestQuery queries = new InProcessingRequestQuery();
    private DerbyInProcessingRequestDAO derbyInProcessingRequestDAO = new DerbyInProcessingRequestDAO();
    private InProcessingRequest inProcessingRequest = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        colorByOfferingSaleButton.setVisible(false);
        cancelColorByOfferingSaleButton.setVisible(false);
        editButton.setVisible(false);
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        setCellTable();
        data = FXCollections.observableArrayList();
        tableView.setTableMenuButtonVisible(true);
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        UsefulUtils.installCopyPasteHandler(tableView);



        colorByOfferingSaleButton.setOnAction(event -> {
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

        cancelColorByOfferingSaleButton.setOnAction(event -> {
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
                try (Connection connection = DBConnection.getDataSource().getConnection()) {
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


    }


    public void updateColor()  {
        tableView.setRowFactory(new Callback<TableView<InProcessingRequest>, TableRow<InProcessingRequest>>() {
            @Override
            public TableRow<InProcessingRequest> call(TableView<InProcessingRequest> param) {
                return new TableRow<InProcessingRequest>() {
                    @Override
                    protected void updateItem(InProcessingRequest item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            setStyle(" ");
                            if (item.getColorByOfferingSale().equals(0) || item.getColorByOfferingSale().equals(null)) {
                                setStyle(" ");
                            } else if (item.getColorByOfferingSale().equals(1)) {
                                setStyle("-fx-background-color: #75ff96;");
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
        loadDataFromDatabase();
        updateColor();
    }

    private void setCellTable() {
        try {
            hashColumns.put(colIndex, "[tbl_RequestOffering].[Number]");
            hashColumns.put(colSkrut, "[tbl_RequestOffering].[CreatedOn]");
            hashColumns.put(colNewDescription, "[tbl_Contact].[Name]");
            hashColumns.put(colOfferingName, "[tbl_Account].[Name]");
            hashColumns.put(colQuantity, "[tbl_Account].[Code]");
            hashColumns.put(colDefaultOfferingCode, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colNewOfferingCode, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");


            colIndex.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<InProcessingRequest, String>("newOfferingCode"));


        } catch (Exception e) {
            e.printStackTrace();
        }
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                }
            }
        });
        super.createTableColumns();

    }

    private StringBuilder query;

    public void loadDataFromDatabase() {



        try {

            data = FXCollections.observableArrayList();
            System.out.println(selectedRecord.getID() + "Offering Request");
            List<InProcessingRequest> listItems = derbyInProcessingRequestDAO.findAll(10, selectedRecord.getID());

            listItems.forEach(item -> data.add(item));
            super.loadDataFromDatabase();

            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }

    public void refreshData() {
        try {

            tableView.setSelectedRow();
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            updateColor();
            loadDataFromDatabase();

            UsefulUtils.fadeTransition(tableView);
        }

    }

    @FXML
    public void handleButtonEdit(ActionEvent event) {

        try {
            InProcessingRequest selectedRecord1 = null;
            try {
                selectedRecord1 = (InProcessingRequest) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(ProductAddNewController.class.getResource("/views/ProductAddView.fxml"));
            ProductAddController createRequest = loader.getController();
            AnchorPane serverrLayout = null;
            try {
                serverrLayout = loader.load();
            } catch (IOException e) {
                log.log(Level.SEVERE, "getAccount exception: " + e);
                e.printStackTrace();
            }
            Scene scene = new Scene(serverrLayout);
            ProductAddController contactEditWindow =
                    (ProductAddController) loader
                            .getController();
            stage.setScene(scene);
            stage.setMaxHeight(520);
            stage.setMaxWidth(620);
            contactEditWindow.setInProcessing(selectedRecord);
            ProductAddController edit = new ProductAddController(WindowOperation.EDIT, selectedRecord1);
            contactEditWindow.setInProcessingRequestController(this);
            contactEditWindow.setOfferingInProcessing(selectedRecord1);
            contactEditWindow.btnToggle.setVisible(false);
            contactEditWindow.labelTools.setVisible(false);
            stage.show();
            stage.requestFocus();
            refreshData();

        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Неможливо відкрити вікно редагування!");

        }
    }

}
