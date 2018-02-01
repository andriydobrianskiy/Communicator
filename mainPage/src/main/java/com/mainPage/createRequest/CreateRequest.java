package com.mainPage.createRequest;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.login.User;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.createRequest.StateCreate.DeliveryCity;
import com.mainPage.createRequest.StateCreate.GroupPeople;
import com.mainPage.createRequest.StateCreate.StateCreate;
import com.mainPage.createRequest.StateCreate.Status;
import com.mainPage.createRequest.searchCounterpart.Counterpart;
import com.mainPage.createRequest.searchCounterpart.SearchCounterpart;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateRequest implements Initializable {
    private static Logger log = Logger.getLogger(CreateRequest.class.getName());
    private PreparedStatement pstStatusRequest = null;
    private PreparedStatement pstDeliveryCity = null;
    private PreparedStatement pstStatus = null;
    private PreparedStatement pstGroupPeople;
    private Connection conGroupPeople = null;
    private ResultSet rsGroupPeople = null;
    private Connection conStatusRequest = null;
    private Connection conDeliveryCity = null;
    private Connection conStatus = null;
    private Connection conGroup = null;
    private ResultSet rsStatusRequest = null;
    private ResultSet rsDeliveryCity = null;
    private ResultSet rsStatus = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private Connection con = null;
    @FXML
    private ArrayList<? extends Control> listFields;
    @FXML
    private ArrayList<? extends Control> listComboBox;

    @FXML
    private JFXButton closeButton;
    @FXML
    private JFXButton btnOK;

    @FXML
    private JFXTextField edtSkrut;
    @FXML
    AnchorPane anchorPane;
    @FXML
    private JFXComboBox Status;
    @FXML
    private JFXComboBox<NotFulfilled> cmbMain3;
    private ObservableList optioncmbMain3 = FXCollections.observableArrayList();
    @FXML
    private JFXButton SearchCounterpart;
    private ObservableList optionsStatus = FXCollections.observableArrayList();
    ComboBox choiceBoxStatus = new ComboBox(optionsStatus);
    @FXML
    private JFXComboBox DeliveryCity;
    private ObservableList optionsDeliveryCity = FXCollections.observableArrayList();
    ComboBox choiceBoxDeliveryCity = new ComboBox(optionsDeliveryCity);
    @FXML
    private JFXComboBox StatusRequest;
    private ObservableList optionsStatusRequest = FXCollections.observableArrayList();
    ComboBox choiceBoxStatusRequest = new ComboBox(optionsStatusRequest);

    public NotFulfilled chosenElement;
    // public static Counterpart chosenAccount = null;
    private static NotFulfilled input = null;
    private static NotFulfilled inputAccount = null;
    @FXML
    private ChoiceBox comboBoxGroup;
    private ObservableList optionsComboBoxGroup = FXCollections.observableArrayList();

    @FXML
    private JFXComboBox GroupPeople;
    private ObservableList optionsGroupPeople = FXCollections.observableArrayList();
    JFXComboBox choiceBoxGroupPeople = new JFXComboBox(optionsGroupPeople);
    @FXML
    NotFulledController notFulledController;

    private static WindowOperation operation = null;


    private ObservableList options = FXCollections.observableArrayList();
    ComboBox choiceBox = new ComboBox(options);
    public Counterpart counterpart = new Counterpart();

    public void setOfferingRequest(NotFulledController oper) {
        this.notFulledController = oper;
    }
    public NotFulledController getOfferingRequest() {
        return this.notFulledController;
    }

    HashMap<String, String> map = new HashMap<>();

    public void GroupRequest(String value) {
        System.out.println("54656");
        String uniqueID = UUID.randomUUID().toString();
        System.out.println("54656");
        try {
            conGroup = DBConnection.getDataSource().getConnection();
            CallableStatement callProc = conGroup.prepareCall("{call dbo.tsp_RODefineOfferingGroup (?,?)}");
            System.out.println(1);
            callProc.setString(1, value);
            callProc.registerOutParameter(2, Types.LONGNVARCHAR);
            callProc.execute();

            map.put("113C960D-CD99-4161-AA8C-575D1E5BDDCE", "Група 1");/* Add GroupOffering*/
            map.put("E92CEF11-EA8B-4BBC-A244-7F82FA5F9BAC", "Група 2");
            map.put("4869AFC3-E32B-4732-B370-ECDAFA9EF185", "Група 3");
            map.put("1FA8112D-A9B4-47F3-A89F-0C101BE07709", "Група 4");
            map.put("DE516376-8A47-44DA-9EBB-5137F928EA05", "Група 5");
            map.put("D3B12AE2-3445-489E-BA7B-E731DE67ABB0", "Група 6");
            map.put("C2A6935C-0F10-4827-A789-9D733EDDFFB4", "Група 7");
            map.put("480ABDAD-8006-4D34-8862-803924357598", "Група 8");
            map.put("71820A9D-95B6-4D65-A480-4F2C57AE9A4B", "Група 9");
            map.put("FA7F12EE-E5FC-4DA3-92C2-51C93FFF6355", "Група 10");
            map.put("92C6E142-0BBA-40CE-8C99-861E9257AA90", "Група 11");
            map.put("DDC21C62-4289-4EF0-88FA-D2D2FE8F9934", "Група 12");
            map.put("67569753-AFC7-493C-8291-EECA91B285D4", "Група 13");
            String all = callProc.getString(2);
            if (all != null) {
                String key = all;
                options.add(map.get(key));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        comboBoxGroup.setItems(options);
    }

//StatusRequest
    public void StatusRequest() {

        try {
            String queryStatusRequest = "SELECT\n" +
                    "\t[tbl_RequestOfferingState].[ID] AS [ID],\n" +
                    "\t[tbl_RequestOfferingState].[Name] AS [Name]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_RequestOfferingState] AS [tbl_RequestOfferingState]";
            pstStatusRequest = conStatusRequest.prepareStatement(queryStatusRequest);
            rsStatusRequest = pstStatusRequest.executeQuery();
            while (rsStatusRequest.next()) {
                optionsStatus.add(new StateCreate(rsStatusRequest.getString(1), rsStatusRequest.getString(2)));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        StatusRequest.setItems(optionsStatus);
    }

    //DeliveryCity

    private ObservableList optionsStatusRequest1 = FXCollections.observableArrayList();

    public void StatusRequest1() {

        try {
            String queryStatusRequest = "SELECT\n" +
                    "\t[tbl_RequestOfferingState].[ID] AS [ID],\n" +
                    "\t[tbl_RequestOfferingState].[Name] AS [Name]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_RequestOfferingState] AS [tbl_RequestOfferingState]";
            pstStatusRequest = conStatusRequest.prepareStatement(queryStatusRequest);
            rsStatusRequest = pstStatusRequest.executeQuery();
            while (rsStatusRequest.next()) {
                optionsStatusRequest1.add(rsStatusRequest.getString(1));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void DeliveryCity() {

        try {
            String queryDelivery = "SELECT\n" +
                    "                   [tbl_StoreCity].[ID] AS [ID],\n" +
                    "                   [tbl_StoreCity].[Name] AS [Name]\n" +
                    "                    FROM\n" +
                    "                    [dbo].[tbl_StoreCity] AS [tbl_StoreCity]\n" +
                    "\t\t\t\t\tWHERE tbl_StoreCity.Name = 'Мукачево' \n" +
                    "        OR  tbl_StoreCity.Name = 'Іршава'\n" +
                    "        OR  tbl_StoreCity.Name ='Ужгород'\n" +
                    "        OR  tbl_StoreCity.Name ='Львів 1'\n" +
                    "        OR  tbl_StoreCity.Name ='Львів 2'\n" +
                    "        OR  tbl_StoreCity.Name ='Гурти'\n" +
                    "        OR  tbl_StoreCity.Name ='Цс Львів'\n" +
                    "        OR  tbl_StoreCity.Name ='Цс київ'\n" +
                    "        OR  tbl_StoreCity.Name ='Сто черляни'\n" +
                    "        OR  tbl_StoreCity.Name ='Кпп Черляни'\n" +
                    "        OR  tbl_StoreCity.Name ='Городок'\n" +
                    "        OR  tbl_StoreCity.Name ='Луцьк'\n" +
                    "        OR  tbl_StoreCity.Name ='Рівне'\n" +
                    "        OR  tbl_StoreCity.Name ='Житомир'\n" +
                    "        OR  tbl_StoreCity.Name ='Київ'\n" +
                    "        OR  tbl_StoreCity.Name ='Київ 2'\n" +
                    "        OR  tbl_StoreCity.Name ='Харків'\n" +
                    "        OR  tbl_StoreCity.Name ='Дніпро'\n" +
                    "        OR  tbl_StoreCity.Name ='Вінниця'\n" +
                    "        OR  tbl_StoreCity.Name ='Вінниця 2'\n" +
                    "        OR  tbl_StoreCity.Name ='Одеса'\n" +
                    "        OR  tbl_StoreCity.Name ='Хмельницький'\n" +
                    "        OR  tbl_StoreCity.Name ='Тернопіль'\n" +
                    "        OR  tbl_StoreCity.Name ='Чернівці'\n" +
                    "        OR  tbl_StoreCity.Name ='КПП Київ'\n" +
                    "\t\tOR  tbl_StoreCity.Name LIKE 'Розборка%'\n" +
                    "\t\tOR  tbl_StoreCity.Name LIKE 'ГУРТОВІ%'\n" +
                    "        ORDER BY\n" +
                    "        2 ASC";
            pstDeliveryCity = conDeliveryCity.prepareStatement(queryDelivery);

            rsDeliveryCity = pstDeliveryCity.executeQuery();

            while (rsDeliveryCity.next()) {
                optionsDeliveryCity.add(new DeliveryCity(rsDeliveryCity.getString(1), rsDeliveryCity.getString(2)));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        DeliveryCity.setItems(optionsDeliveryCity);

    }

    //Status
    public void Status() {

        try {
            String queryStatus = "SELECT\n" +
                    "\t[tbl_RequestOfferingStatus].[ID] AS [ID],\n" +
                    "\t[tbl_RequestOfferingStatus].[Name] AS [Name]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_RequestOfferingStatus] AS [tbl_RequestOfferingStatus]";

            pstStatus = conStatus.prepareStatement(queryStatus);

            rsStatus = pstStatus.executeQuery();
            while (rsStatus.next()) {
                optionsStatus.add(new Status(rsStatus.getString(1), rsStatus.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Status.setItems(optionsStatus);
        System.out.println(Status.getPromptText().toString() + "6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
    }

    public void GroupPeople() {

        try {
            String queryGroupPeople = "SELECT [tbl_Contact].ID, [tbl_Contact].[Name] FROM [tbl_Contact] AS [tbl_Contact] WHERE [tbl_Contact].JobID = '7B756193-B6CF-469A-92E0-C9C964CBB8F4'";

            pstGroupPeople = conGroupPeople.prepareStatement(queryGroupPeople);

            rsGroupPeople = pstGroupPeople.executeQuery();
            while (rsGroupPeople.next()) {
                optionsGroupPeople.add(new GroupPeople(rsGroupPeople.getString(1), rsGroupPeople.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        GroupPeople.setItems(optionsGroupPeople);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            conStatusRequest = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        optionsStatusRequest = FXCollections.observableArrayList();
        StatusRequest();
        try {
            conDeliveryCity = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        optionsDeliveryCity = FXCollections.observableArrayList();
        DeliveryCity();
        try {
            conStatus = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        optionsStatus = FXCollections.observableArrayList();
        Status();
        try {
            conGroup = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        options = FXCollections.observableArrayList();
        try {
            conGroupPeople = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }
        optionsGroupPeople = FXCollections.observableArrayList();
        GroupPeople();
        Status.getSelectionModel().select(optionsStatus.get(0));
        try {

            btnOK.setOnAction(action -> {
                initAccountObject();

            });

        } catch (Exception e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }

        edtSkrut.setOnKeyReleased((KeyEvent keyEvent) -> {
                    GroupRequest(edtSkrut.getText());
                    comboBoxGroup.getSelectionModel().clearSelection();


                }
        );
    }


    private StringBuilder builderQuery;

    private void initAccountObject() throws NullPointerException {
        try {

            if (WindowOperation.ADD.equals(WindowOperation.ADD)) {
                //  chosenElement.setID(UUID.randomUUID().toString());
                chosenElement = new NotFulfilled();
                //
            }else if (getOfferingRequest().equals(WindowOperation.EDIT)) {
                chosenElement = input;
            }

            if (WindowOperation.ADD.equals(WindowOperation.ADD) && UsefulUtils.checkEmptyRequiredFields(listComboBox)) {
                UsefulUtils.showErrorDialogDown("Обов'язкові поля не заповнені!\nЗараз вони позначені червоним");
                return;
            }

            if (UsefulUtils.checkEmptyRequiredFields(listComboBox)) {
                UsefulUtils.showErrorDialogDown("Обов'язкові поля не заповнені!\nЗараз вони позначені червоним");
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
                        value = ((ComboBox<? extends CreateInterface>) field).getValue() == null ? null : ((ComboBox<? extends CreateInterface>) field).getValue().getID();
                    } else if (field instanceof ChoiceBox) {
                        objectValue = (String) ((ChoiceBox) field).getValue();
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (objectValue.equals(entry.getValue())) {
                                value = entry.getKey();
                                break;
                            }
                        }

                    }

                } catch (NullPointerException npe) { // ignore NullPointerException

                }

                switch (field.getId()) {

                    case "StatusRequest":
                        chosenElement.setStateID(value);
                        break;

                    case "cmbMain3":
                        chosenElement.setAccountID(value);
                        break;

                    case "DeliveryCity":
                        chosenElement.setStoreCityID(value);
                        break;

                    case "Status":
                        chosenElement.setStatusID(value);

                        break;
                    case "comboBoxGroup":
                        chosenElement.setOriginalGroupID(value);
                        break;

                    case "GroupPeople":
                        chosenElement.setOfferingGroupID(value);
                        break;
                }

            });
        } catch (Exception e) {
            log.log(Level.SEVERE, "initMainAccountObjectException : " + e);
            UsefulUtils.showErrorDialogDown(
                    "Дані введені не правильно!");

            return;
        }

        builderQuery = new StringBuilder();

        String query = ("INSERT INTO [dbo].[tbl_RequestOffering] (CreatedOn, CreatedByID, ModifiedOn, ModifiedByID, [AccountID], StoreCityID, [StatusID], [OfferingGroupID], OriginalGroupID, [StateID])\n" +
                "                      VALUES (CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?,  ?, ?, ?, ?, ?, ? )");

        pst = null;

        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement(query);
            pst.setString(1, User.getContactID());
            pst.setString(2, User.getContactID());
            pst.setString(3, chosenElement.getAccountID());
            pst.setString(4, chosenElement.getStoreCityID());
            pst.setString(5, chosenElement.getStatusID());
            pst.setString(6, chosenElement.getOfferingGroupID());
            pst.setString(7, chosenElement.getOriginalGroupID());
            pst.setString(8, chosenElement.getStateID());
            pst.execute();

            closeWindow();
            notFulledController.handleTableView();
            UsefulUtils.showSuccessful("Запит успішно добавлено");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void btnSearchCounterpart(ActionEvent event) {


        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/SearchCounterpart.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setMaxHeight(570);
        stage.setMaxWidth(900);


        stage.showAndWait();
        com.mainPage.createRequest.searchCounterpart.SearchCounterpart searchCounterpart = new SearchCounterpart();

        cmbMain3.getItems().add(searchCounterpart.chosenElement);
        cmbMain3.getSelectionModel().select(searchCounterpart.chosenElement);
    }

    @FXML
    public void btnCreateOut(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


}
