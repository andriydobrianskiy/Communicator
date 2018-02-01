package com.mainPage.NotFulled.ProductAdd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import com.mainPage.createRequest.WindowOperation;

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
    private JFXButton SearchCounterpart;
    @FXML
    private JFXButton closeButton;
    @FXML
    private ProductAddNewController productAddNewViewController;
    @FXML
    private JFXToggleButton btnToggle;
    @FXML
    private HBox buttons;
    @FXML
    private JFXTextField txt_Quentity;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane anchorPaneNew;
    public static ProductSearch chosenElement = null;
    public OfferingRequest chosenAccount;
    @FXML
    private JFXButton btn_OK;
    @FXML
    private JFXComboBox cmbMain3;
    public NotFulfilled notFulfilled;
    @FXML
    ExampleController exampleController;
    @FXML
    NotFulledController notFulledController;

    private static WindowOperation operation = null;


    public void setExampleController(ExampleController exampleController, NotFulledController notFulledController) {
        this.exampleController = exampleController;
        this.notFulledController = notFulledController;
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

        btn_OK.setOnAction(event -> {

            initAccountObject();

        });



    }

    private StringBuilder builderQuery;

    /* @FXML
     private ArrayList<? extends Control> requiredFields;*/
    private void initAccountObject() throws NullPointerException {
        //  System.out.println(chosenElement + "777777777777777777777777777777777777777777777777777777");
        try {


            if (WindowOperation.ADD.equals(WindowOperation.ADD)) {
                //  chosenElement.setID(UUID.randomUUID().toString());
                chosenAccount = new OfferingRequest();
                //
                //  } else if (getOfferingRequest().equals(WindowOperation.EDIT)) {
                //  chosenElement = input;
            }
           if (UsefulUtils.checkEmptyRequiredFields(listFields)) {
                UsefulUtils.showErrorDialogDown("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
                return;
            }
            if(UsefulUtils.checkEmptyRequiredFields(listFields)) {
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
                        //  if (field == cmbMain3 || field == StatusRequest) {
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


  /*     MainNotFulledQuery insert = (MainNotFulledQuery) new CreateRequestDAO();
       if (mainOperation()) closeWindow();
        else UsefulUtils.showErrorDialog("Неможливо виконати операцію!");*/
        builderQuery = new StringBuilder();

        String query = ("INSERT INTO [dbo].[tbl_OfferingInRequestOffering] ([ModifiedOn], [ModifiedByID], [OfferingID], [RequestID], [Quantity], [CreatedOn], [CreatedByID])\n" +
                "VALUES ( CURRENT_TIMESTAMP, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)");

        pst = null;

        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement(query);

            pst.setString(1, User.getContactID());
            pst.setString(2, chosenAccount.getRequestID());
            pst.setString(3, notFulfilled.getID());
            pst.setString(4, chosenAccount.getQuantity());
            pst.setString(5, User.getContactID());
    ///UsefulUtils.showConfirmDialogDown("Не можливо записати більше 1 продукту");
if(exampleController.data.size() == 0) {
    pst.execute();

    closeWindow();
    exampleController.refresh();
    UsefulUtils.showSuccessful("Продукт успішно добавлено");
}else{
   UsefulUtils.showErrorDialogDown("Не можливо записати більше одного продукту");
}

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void setNotFulled(NotFulfilled notFulfilled) {
        System.out.println("222");
        this.notFulfilled = notFulfilled;
    }

    public NotFulfilled getNotFulled() {
        return notFulfilled;
    }
}
