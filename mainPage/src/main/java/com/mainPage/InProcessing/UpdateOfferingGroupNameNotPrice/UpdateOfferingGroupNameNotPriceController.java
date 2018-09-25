package com.mainPage.InProcessing.UpdateOfferingGroupNameNotPrice;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.login.User;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.createRequest.CreateInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateOfferingGroupNameNotPriceController implements Initializable {
    private static Logger log = Logger.getLogger(UpdateOfferingGroupNameNotPriceController.class.getName());
    @FXML
    private JFXComboBox updateComboBox;
    @FXML
    private JFXButton btn_Close;
    @FXML
    private JFXButton btn_Ok;
    @FXML
    private ArrayList<? extends Control> listFields;
    ObservableList data = FXCollections.observableArrayList();
    ComboBox choiceBoxDeliveryCity = new ComboBox(data);
    public InProcessingController inProcessingController;
    public InProcessing inProcessing;
    private StringBuilder builderQuery;
   // public InProcessing chosenElement;

    private PreparedStatement pst = null;
    private Connection con = null;
    private ResultSet rs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        data = FXCollections.observableArrayList();
        StatusRequest();
       // updateComboBox.getSelectionModel().select(data.get(1));
        try {

            btn_Ok.setOnAction(action -> {
                initAccountObject();

            });

        } catch (Exception e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }


    }
    public void setOfferingRequest(InProcessingController oper, InProcessing inProcessing) {
        this.inProcessingController = oper;
        this.inProcessing = inProcessing;
    }

    @FXML
    public void btnClose (ActionEvent e){
        closeWindow();
    }

    public void StatusRequest() {

        try {
            String query = "select C.ID, C.Name from tbl_Contact AS C\n" +
                    "Left Join\n" +
                    "tbl_Job AS J ON J.ID= C.JobID\n" +
                    "WHERE\n" +
                    "C.JobID = 'FE067F6D-62A3-4F2D-A39F-7202FEC30C57'\n" +
                    "Order By 2 DESC";
            pst = con.prepareStatement(query);
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new UpdateOfferingGroupNameNotPrice(rs.getString(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        updateComboBox.setItems(data);
    }

    private void initAccountObject() throws NullPointerException{

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
                value = ((ComboBox<? extends CreateInterface>) field).getValue() == null ? null : ((ComboBox<? extends CreateInterface>) field).getValue().getID();
            }




        } catch (NullPointerException npe) { // ignore NullPointerException

        }

        switch (field.getId()) {

            case "updateComboBox":
                inProcessing.setOfferingGroupID(value);
                break;


        }

    });


        builderQuery = new StringBuilder();

        String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                "\tSET  [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                "[OfferingGroupID] = ?,\n" +
                "[OriginalGroupID] = ?,\n"+
                "[GroupChangedByID] = ?,\n"+
                "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                "\t[ModifiedByID] = ?\n" +
                "WHERE([tbl_RequestOffering].[ID] = ?)";
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, inProcessing.getOfferingGroupID());
            pst.setString(2, User.getContactID());
            pst.setString(3, User.getContactID());
            pst.setString(4, User.getContactID());
            pst.setString(5, inProcessing.getID());

            pst.executeUpdate();
            inProcessingController.tableView.setRowFactory(new Callback<TableView<InProcessing>, TableRow<InProcessing>>() {
                @Override
                public TableRow<InProcessing> call(TableView<InProcessing> param) {
                    return new TableRow<InProcessing>() {
                        @Override
                        protected void updateItem(InProcessing item, boolean empty) {
                            super.updateItem(item, empty);
                            setStyle("-fx-background-color: #e3ff28");

                        }
                    };
                }
            });

            closeWindow();
        inProcessingController.refreshData();
        UsefulUtils.showSuccessful("Запит успішно добавлено");
        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        }
public void closeWindow () {
    Stage stage = (Stage) btn_Close.getScene().getWindow();
    stage.close();
}

}
