package com.mainPage.NotFulled.ProductAdd;

import com.Utils.GridComp;
import com.jfoenix.controls.*;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingRequest.InProcessingRequest;
import com.mainPage.InProcessing.InProcessingRequest.InProcessingRequestController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.login.User;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.NotFulled.OfferingRequest.ExampleController;
import com.mainPage.NotFulled.OfferingRequest.OfferingRequest;
import com.mainPage.NotFulled.ProductAdd.ProductSearch.ProductSearch;
import com.mainPage.NotFulled.ProductAdd.ProductSearch.ProductSearchController;
import com.mainPage.InProcessing.Edit.WindowOperation;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductAddController implements Initializable {

    private static Logger log = Logger.getLogger(ProductAddController.class.getName());
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private Connection con = null;
    @FXML
    private ArrayList<? extends Control> listFields;
    @FXML
    private JFXButton closeButton;
    @FXML
    private ProductAddNewController productAddNewViewController;
    @FXML
    public JFXToggleButton btnToggle;
    @FXML
    private HBox buttons;
    @FXML
    private JFXTextField txt_Quentity;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane anchorPaneNew;
    @FXML
    public Label labelTools;
    public static ProductSearch chosenElement = null;
    public static GridComp chosenAccount;
    @FXML
    private JFXButton btn_OK;
    @FXML
    private JFXComboBox cmbMain3;
    @FXML
    private JFXTextArea txtName;
    public NotFulfilled notFulfilled;
    public InProcessing inProcessing;
    @FXML
    ExampleController exampleController;
    @FXML
    private InProcessingRequestController inProcessingRequestController;
    @FXML
    NotFulledController notFulledController;

    private InProcessingRequest offeringInProcessing;
    private static GridComp inputElement = null;

    private static WindowOperation operation = null;

    public ProductAddController() {
    }

    public ProductAddController(WindowOperation edit, GridComp selectedRecord) {
        operation = edit;
        inputElement = selectedRecord;
    }


    public void setExampleController(ExampleController exampleController, NotFulledController notFulledController) {
        this.exampleController = exampleController;
        this.notFulledController = notFulledController;
    }

    public void setInProcessingRequestController(InProcessingRequestController inProcessingRequestController) {
        this.inProcessingRequestController = inProcessingRequestController;
    }

    public void setOfferingInProcessing(InProcessingRequest selectedRecord) {
        this.offeringInProcessing = selectedRecord;
    }

    public ExampleController getExampleController() {
        return exampleController;
    }

    @FXML
    public void btnSearchCounterpart(ActionEvent event) {


        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/ProductSearchView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setMaxHeight(570);
        stage.setMaxWidth(900);
        ProductSearchController productSearchController = new ProductSearchController();


        stage.showAndWait();
        cmbMain3.getItems().add(productSearchController.chosenElement);
        cmbMain3.getSelectionModel().select(productSearchController.chosenElement);

    }

    @FXML
    public void btnCreateOut(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbMain3.setStyle("-fx-text-fill: black;" +
                "-fx-opacity: 1;");
        cmbMain3.setDisable(true);

        productAddNewViewController.init(this);
        anchorPane.setVisible(false);
        ToggleGroup group = new ToggleGroup();

        btnToggle.setToggleGroup(group);


        group.selectedToggleProperty().addListener(event -> {

            if (group.getSelectedToggle() != null) {
                anchorPane.setVisible(true);
                anchorPaneNew.setVisible(false);

            } else if (anchorPaneNew != null) {
                anchorPaneNew.setVisible(true);
                anchorPane.setVisible(false);

            }//anchorPane.setVisible(false);
        });
        Platform.runLater(() -> {
            if (operation.equals(WindowOperation.EDIT)) setFieldsByObject();
        });
        btn_OK.setOnAction(event -> {


            initAccountObject();

        });


    }

    private StringBuilder builderQuery;

    private void setFieldsByObject() { // Заповнення полів вхідним об'єктом контрагента, тільки для EDIT(оновлення)
        try {
            chosenAccount = inputElement;
            ObservableList<ProductSearch> listCountry = FXCollections.observableArrayList(new ProductSearch().findByProperty(chosenAccount.getOfferingID(), DictionarySearchType.ID));
            if (!listCountry.isEmpty()) {
                cmbMain3.getItems().add(listCountry.get(0));
                cmbMain3.getSelectionModel().select(listCountry.get(0));
            } else {
                cmbMain3.setValue(null);
            }
            txt_Quentity.setText(chosenAccount.getQuantity());
            txtName.setText(chosenAccount.getNewDescription());

        } catch (Exception e) {
            log.log(Level.SEVERE, "initMainAccountObjectException : " + e);
            UsefulUtils.showErrorDialog("\n\tВиникла помилка. \n" +
                    "Неможливо заповнити поля!");
            closeWindow();

            return;
        }
    }

    private void initAccountObject() throws NullPointerException {
        try {


            if (operation.equals(WindowOperation.ADD)) {

                chosenAccount = new OfferingRequest();

            } else if (operation.equals(WindowOperation.EDIT)) {
                chosenAccount = inputElement;

            }
            if (UsefulUtils.checkEmptyRequiredFields(listFields)) {
                UsefulUtils.showErrorDialogDown("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
                return;
            }
            if (UsefulUtils.checkEmptyRequiredFields(listFields)) {
                UsefulUtils.showErrorDialogDown("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
                return;
            }
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не можливо записати запит");
        }

        try {
            listFields.forEach(field -> {
                String value = null;
                String objectValue = null;

                try {

                    if (field instanceof TextField) {
                        value = ((TextField) field).getText().isEmpty() ? null : ((TextField) field).getText();
                    } else if (field instanceof TextArea) {
                        value = ((TextArea) field).getText().isEmpty() ? null : ((TextArea) field).getText();
                    } else if (field instanceof DatePicker) {
                        value = ((DatePicker) field).getValue() == null ? null : UsefulUtils.dateTimeConverter((DatePicker) field);
                    } else if (field instanceof ComboBox) {
                        value = ((ComboBox<ProductSearch>) field).getValue() == null ? null : ((ComboBox<ProductSearch>) field).getValue().getID();
                    }

                } catch (NullPointerException npe) {

                }
                System.out.println("START - " + field.getId());

                switch (field.getId()) {
                    case "cmbMain3":
                        chosenAccount.setRequestID(value);
                        break;
                    case "txt_Quentity":
                        chosenAccount.setQuantity(value);
                        break;

                }
            });

        } catch (Exception e) {
            log.log(Level.SEVERE, "initMainAccountObjectException : " + e);
            UsefulUtils.showErrorDialogDown(
                    "Дані введені не правильно!");

            return;
        }
        if (operation.equals(WindowOperation.EDIT)) {
            String query = "Update tbl_OfferingInRequestOffering   " +
                    "SET    " +
                    "ModifiedOn = CURRENT_TIMESTAMP,   " +
                    "ModifiedByID = ?,  " +
                    "OfferingID = ?,  " +
                    "Quantity = ?,    " +
                    "NewDescription = ?  " +

                    "WHERE  " +
                    "RequestID = ? ";
            pst = null;

            try {
                pst = DBConnection.getDataSource().getConnection().prepareStatement(query);

                pst.setString(1, User.getContactID());
                pst.setString(2, chosenAccount.getRequestID());
                pst.setString(3, txt_Quentity.getText());
                pst.setString(4, txtName.getText());
                pst.setString(5, inProcessing.getID());
                pst.execute();

                closeWindow();
                inProcessingRequestController.refreshData();
                UsefulUtils.showSuccessful("Продукт успішно оновлено");
            } catch (SQLException e) {
                e.printStackTrace();
                DBConnection database = new DBConnection();
                database.reconnect();
            }
        } else {
            builderQuery = new StringBuilder();

            String query = ("INSERT INTO [dbo].[tbl_OfferingInRequestOffering] ([ModifiedOn], [ModifiedByID], [OfferingID], [RequestID], [Quantity], [CreatedOn], [CreatedByID], [newDescription])\n" +
                    "VALUES ( CURRENT_TIMESTAMP, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?)");

            pst = null;

            try {
                pst = DBConnection.getDataSource().getConnection().prepareStatement(query);

                pst.setString(1, User.getContactID());
                pst.setString(2, chosenAccount.getRequestID());
                pst.setString(3, notFulfilled.getID());
                pst.setString(4, chosenAccount.getQuantity());
                pst.setString(5, User.getContactID());
                pst.setString(6, txtName.getText());

                pst.execute();

                closeWindow();
                exampleController.refresh();
                UsefulUtils.showSuccessful("Продукт успішно добавлено");
            } catch (SQLException e) {
                e.printStackTrace();
                DBConnection database = new DBConnection();
                database.reconnect();
            }
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setNotFulled(NotFulfilled notFulfilled) {
        this.notFulfilled = notFulfilled;
    }

    public NotFulfilled getNotFulled() {
        return notFulfilled;
    }

    public void setInProcessing(InProcessing inProcessing) {
        this.inProcessing = inProcessing;
    }

    public InProcessing getInProcessing() {
        return inProcessing;
    }
}