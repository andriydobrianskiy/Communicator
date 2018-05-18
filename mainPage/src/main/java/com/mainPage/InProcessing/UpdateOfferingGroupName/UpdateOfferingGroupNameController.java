package com.mainPage.InProcessing.UpdateOfferingGroupName;

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

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateOfferingGroupNameController implements Initializable {
    private static Logger log = Logger.getLogger(UpdateOfferingGroupNameController.class.getName());
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
            e.printStackTrace();
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
       // FxUtilTest.autoCompleteComboBoxPlus(updateComboBox, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));
      //  FxUtilTest.getComboBoxValue(updateComboBox);

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
            String queryStatusRequest = "select ID AS ID,\n" +
                    "       Name AS Name \n" +
                    "from \n" +
                    "    tbl_Contact \n" +
                    "\twhere \n" +
                    "\tJobID = 'CCB28AD0-ECAC-43DF-9827-E2F9CEA56A3A' OR ID = '71820A9D-95B6-4D65-A480-4F2C57AE9A4B'\n" +
                    "\t \n" +
                    "         ORDER BY\n" +
                    "                    2 ASC";
            pst = con.prepareStatement(queryStatusRequest);
            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new UpdateOfferingGroupName(rs.getString(1), rs.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
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
                "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                "\t[ModifiedByID] = ?\n" +
                "WHERE([tbl_RequestOffering].[ID] = ?)";
        try {
            pst = con.prepareStatement(query);
            pst.setString(1, inProcessing.getOfferingGroupID());
            pst.setString(2, User.getContactID());
            pst.setString(3, inProcessing.getID());

            pst.executeUpdate();

            closeWindow();
        inProcessingController.refreshData();
        UsefulUtils.showSuccessful("Запит успішно добавлено");
        } catch (SQLException e) {
        e.printStackTrace();
        }
        }
public void closeWindow () {
    Stage stage = (Stage) btn_Close.getScene().getWindow();
    stage.close();
}

}
