package com.mainPage.NotFulled.Edit;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.login.User;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.createRequest.CreateInterface;
import com.mainPage.createRequest.StateCreate.DeliveryCity;
import com.mainPage.createRequest.StateCreate.GroupPeople;
import com.mainPage.createRequest.StateCreate.StateCreate;
import com.mainPage.createRequest.StateCreate.Status;
import com.mainPage.createRequest.WindowOperation;
import com.mainPage.createRequest.searchCounterpart.Counterpart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditController implements Initializable {
    private static Logger log = Logger.getLogger(EditController.class.getName());
    //  private PreparedStatement pst = null;
    private PreparedStatement pstStatusRequest = null;
    private PreparedStatement pstDeliveryCity = null;
    private PreparedStatement pstStatus = null;
    private PreparedStatement pstGroupPeople;
    // private PreparedStatement pstGroup = null;
    // private Connection con = null;
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
    private JFXButton closeButton;
    @FXML
    private JFXButton btnOK;

    /* @FXML
     private JFXTextField edtSkrut;*/
    /* @FXML
     private JFXButton addbtn;*/
    @FXML
    AnchorPane anchorPane;
    @FXML
    private JFXComboBox Status;
    private ObservableList status = FXCollections.observableArrayList();
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
    /* @FXML
     JFXButton addButton;*/
  /*  @FXML
    private ChoiceBox comboBoxGroup;
    private ObservableList optionsComboBoxGroup = FXCollections.observableArrayList();*/

    @FXML
    private JFXComboBox GroupPeople;
    private ObservableList optionsGroupPeople = FXCollections.observableArrayList();
    JFXComboBox choiceBoxGroupPeople = new JFXComboBox(optionsGroupPeople);
    @FXML
    private JFXTextField txt_Number;
    @FXML
    private JFXTextField txt_Created;

    //   public RequestQuery requestQuery = new RequestQuery();

    //   public OfferingsGroup offeringsGroup = new OfferingsGroup();
    private static WindowOperation operation = null;

    static NotFulfilled notFulfilled;

    private ObservableList options = FXCollections.observableArrayList();
    ComboBox choiceBox = new ComboBox(options);
    public Counterpart counterpart = new Counterpart();

 /*   public void setOfferingRequest(NotFulfilled oper) {
        // this.input = inputAccount;
        this.notFulfilled = oper;
    }

    public NotFulfilled getOfferingRequest() {
        return this.notFulfilled;
    }*/

/*    public CreateRequest(WindowOperation oper, NotFulfilled inputAccount
    ) {
        this.input = inputAccount;
        this.operation = oper;
    }*/


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
            //  System.out.println(callProc.getString(2));
            String all = callProc.getString(2);
            // System.out.println(all);
            //  for (String key : map.keySet()) {

            //  options.add(map.get(key));

            //   System.out.println(key);

            if (all != null) {
                String key = all;

                // System.out.println(all);
                options.add(map.get(key));
            }
            //}
            //  System.out.println("3");


            // callProc.close();
            conGroup.close();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        //  comboBoxGroup.setItems(options);
        // comboBoxGroup.getItems().clear();


    }

    // NotFulfilled notFulfilled = new NotFulfilled();

 /*   @FXML
    private void addToTable(EventHandler ev) {
        /*String query = "INSERT INTO [dbo].[tbl_RequestOffering] ([AccountID], [StatusID], [StoreCityID], [OfferingGroupID], [IsReadMeassage], [StateID], [CreatedOn], [CreatedByID], [ModifiedOn], [ModifiedByID])\n" +
                "VALUES ( ?, ?, ?, ?, NULL, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)\n";
    pst = null;

    try {
        pst = DBConnection.getDataSource().getConnection().prepareStatement(query);
        pst.setString(1, notFulfilled.getAccountID());
        pst.setString(2, notFulfilled.getStatusID());
        pst.setString(3, notFulfilled.getStoreCityID());
        pst.setString(4, notFulfilled.getOfferingGroupID());
        pst.setString(5, notFulfilled.getStateID());
        pst.setString(6, User.getContactID());
        pst.setString(7, User.getContactID());
        pst.execute();

} catch (SQLException e) {
        e.printStackTrace();
    }*/

    //   }

    //private PreparedStatement preparedStatement = null;
    /*@FXML
    public void actionAddButton() throws SQLException {

        StatusRequest.setItems(optionsStatusRequest);
        StatusRequest.getSelectionModel().select(1);
         cmbMain3.setItems(optioncmbMain3);
         cmbMain3.getSelectionModel().select(1);
         DeliveryCity.setItems(optionsDeliveryCity);
         DeliveryCity.getSelectionModel().select(1);
        Status.setItems(optionsStatus);
        Status.getSelectionModel().select(1);
 comboBoxGroup.setItems(optionsComboBoxGroup);
 comboBoxGroup.getSelectionModel().select(1);

        String query = "INSERT INTO [dbo].[tbl_RequestOffering] (  [StateID],[AccountID], [StoreCityID], [StatusID],   [CreatedOn], [CreatedByID], [ModifiedOn], [ModifiedByID],[OfferingGroupID])\n" +
                "VALUES ( ?, ?, ?, ?, CURRENT_TIMESTAMP , ?, CURRENT_TIMESTAMP , ?, ?)";
        preparedStatement = null;
        try {
            preparedStatement = conStatus.prepareStatement(query);
            preparedStatement.setString(1, String.valueOf(StatusRequest));
            preparedStatement.setString(2, String.valueOf(cmbMain3));
            preparedStatement.setString(3, String.valueOf(DeliveryCity));
            preparedStatement.setString(4, String.valueOf(Status));
            preparedStatement.setString(5,sample.login.User.getContactID());
            preparedStatement.setString(6,sample.login.User.getContactID());
            preparedStatement.setString(7, String.valueOf(comboBoxGroup));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {

              //  preparedStatement.execute();
                //preparedStatement.close();

        }
        notFulfilledController.loadDataFromDatabase();
       System.out.println(notFulfilledController.loadDataFromDatabase() + "33333333333333333333333333333333");
    }
    @FXML
    private NotFulledController notFulfilledController;


   /* @FXML
        public void addButtonAction(){
        addButton.setOnAction(e-> {if (valid)} );
    }*/
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
            StatusRequest.setItems(optionsStatus);
            conStatusRequest.close();
            rsStatusRequest.close();
            pstStatusRequest.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

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
            conStatusRequest.close();
            pstStatusRequest.close();
            rsStatusRequest.close();
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
            DeliveryCity.setItems(optionsDeliveryCity);
            conDeliveryCity.close();
            rsDeliveryCity.close();
            pstDeliveryCity.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }


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
            Status.setItems(optionsStatus);
            conStatus.close();
            pstStatus.close();
            rsStatus.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    //    System.out.println(Status.getPromptText().toString() + "6666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666");
    }

    public void GroupPeople() {

        try {
            String queryGroupPeople = "SELECT [tbl_Contact].ID, [tbl_Contact].[Name] FROM [tbl_Contact] AS [tbl_Contact] WHERE [tbl_Contact].JobID = 'CCB28AD0-ECAC-43DF-9827-E2F9CEA56A3A' OR ID = '71820A9D-95B6-4D65-A480-4F2C57AE9A4B'\n";

            pstGroupPeople = conGroupPeople.prepareStatement(queryGroupPeople);

            rsGroupPeople = pstGroupPeople.executeQuery();
            while (rsGroupPeople.next()) {
                optionsGroupPeople.add(new GroupPeople(rsGroupPeople.getString(1), rsGroupPeople.getString(2)));
            }
            GroupPeople.setItems(optionsGroupPeople);
            conGroupPeople.close();
            pstGroupPeople.close();
            rsGroupPeople.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txt_Number.setText(notFulfilled.getNumber());
        txt_Created.setText(notFulfilled.getCreatedBy());

        setFieldsByObject();

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


        //System.out.println(optionsStatusRequest1 + "97897897854626132656546");

     /*   try {

            Task<Void> task = new Task<Void>() {
                @Override
                public Void call() throws Exception {
                    Thread.sleep(0);
                    return null;
                }
            };


            task.setOnSucceeded(event -> {

               // setComboBoxValues();
                //  if(operation.equals(WindowOperation.EDIT)) setFieldsByObject();

            });



            /*Image image = new Image(input); // in future get photo from database
            ImageView imageView = new ImageView(image);
            edtLookupButton13.setMinHeight(35);
            edtLookupButton13.setMinWidth(45);
            imageView.setFitHeight(35);
            imageView.setFitWidth(45);
            edtLookupButton13.setGraphic(imageView);*/


          /*  new Thread(task).run();


            //   UsefulUtils.setCustomControlFields(listControlFields);
            //  UsefulUtils.setRequiredFields(requiredLabels);
        } catch (Exception e) {
            log.log(Level.SEVERE, "EXCEPTION - " + e);
        }
*/
        //  txt_Number.setText(notFulfilled.getNumber());
        // txt_Created.setText(notFulfilled.getCreatedBy());


        try {

            btnOK.setOnAction(action -> {
                initAccountObject();

            });

        } catch (Exception e) {
            log.log(Level.SEVERE, "Delete offering row exception: " + e);
        }

     /*   edtSkrut.setOnKeyReleased((KeyEvent keyEvent) -> {
                    GroupRequest(edtSkrut.getText());
                    comboBoxGroup.getSelectionModel().clearSelection();


                }
        );*/


    }

    @FXML
    NotFulledController notFulledController;

    @FXML
    private ArrayList<? extends Control> listFields;
    private StringBuilder builderQuery;

    private void setFieldsByObject() { // Заповнення полів вхідним об'єктом , тільки для EDIT(оновлення)
        String value = null;
        StatusRequest.setPromptText(notFulfilled.getStateName());
        cmbMain3.setPromptText(notFulfilled.getAccountName());
        DeliveryCity.setPromptText(notFulfilled.getStoreCity());
        Status.setPromptText(notFulfilled.getStatus());
   /*     comboBoxGroup.setAccessibleText(notFulfilled.getOriginalGroupName());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (notFulfilled.getOriginalGroupID().equals(entry.getValue())) {
                value = entry.getKey();
                break;
            }*/
        GroupPeople.setPromptText(notFulfilled.getOfferingGroupName());
        // }
    }

    @FXML
    private ArrayList<? extends Control> listComboBox;

    private void initAccountObject() throws NullPointerException {
        //  System.out.println(chosenElement + "777777777777777777777777777777777777777777777777777777");
        try {


            if (WindowOperation.EDIT.equals(WindowOperation.EDIT)) {
                //  chosenElement.setID(UUID.randomUUID().toString());
                chosenElement = new NotFulfilled();
                //
                //  } else if (getOfferingRequest().equals(WindowOperation.EDIT)) {
                //  chosenElement = input;
            }
         /*   if (UsefulUtils.checkEmptyRequiredFields(listComboBox)) {
                UsefulUtils.showInformationDialog("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
                return;
            }*/
       /*     if(UsefulUtils.checkEmptyRequiredFields(requiredFields)) {
                UsefulUtils.showInformationDialog("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
                return;
            }*/
        } catch (Exception e) {
            UsefulUtils.showInformationDialog("Не можливо записати запит");
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
                        value = ((ComboBox<? extends CreateInterface>) field).getValue() == null ? null : ((ComboBox<? extends CreateInterface>) field).getValue().getID();
                        // } else {
                        //       objectValue = (String) ((ComboBox) field).getValue();

                        //  }// == null ? null : ((ComboBox) optionsStatusRequest).getValue().;
                        //  objectValue = String.valueOf(((ComboBox)field).isSelected() ? 1 : 0);
                        //GroupRequest(objectValue);
                    } else if (field instanceof ChoiceBox) {
                        //  value = ((ChoiceBox<? extends CreateInterface>) field).getValue() == null ? null : ((ChoiceBox<? extends CreateInterface>) field).getValue().getID();
                        objectValue = (String) ((ChoiceBox) field).getValue();

                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            if (objectValue.equals(entry.getValue())) {
                                value = entry.getKey();
                                break;
                            }
                        }

                    }

                } catch (NullPointerException npe) { // ігнорування NullPointerException

                }
                System.out.println("START - " + field.getId());

                switch (field.getId()) {


                    case "StatusRequest":
                        chosenElement.setStateID(value);
                        // chosenElement.setStateName(((ComboBox<? extends NotFulfilled>) field).getValue().getName());
                        System.out.println(StatusRequest + "8888888888888888888888888888888888888888888888888888888888888888888888888888");
                        break;
                    case "cmbMain3":
                        chosenElement.setAccountID(value);

                        //  chosenElement.setAccountName(((ComboBox<? extends NotFulfilled>) field).getValue().getName());

                        break;
                    case "DeliveryCity":

                        chosenElement.setStoreCityID(value);
                        //    chosenElement.setStoreCity(((ComboBox<? extends NotFulfilled>) field).getValue().getName());

                        break;
                    case "Status":

                        chosenElement.setStatusID(value);
                        //     chosenElement.setStatus(((ComboBox<? extends NotFulfilled>) field).getValue().getName());


                        break;
                    case "comboBoxGroup":
                        //chosenElement.setOriginalGroupName(((ComboBox<? extends DictionaryInterface>) field).getValue().getName());
                        chosenElement.setOriginalGroupID(value);
                        //    chosenElement.setOriginalGroupID(((ChoiceBox<? extends NotFulfilled>) field).getValue().getName());

                        break;
                    case "GroupPeople":
                        chosenElement.setOfferingGroupID(value);
                        //  chosenElement.setOfferingGroupName(((ComboBox<? extends NotFulfilled>) field).getValue().getName());
                        break;


                }


            });
        } catch (Exception e) {
            log.log(Level.SEVERE, "initMainAccountObjectException : " + e);
            UsefulUtils.showErrorDialog(
                    "Дані введені не правильно!");

            return;
        }


        // MainNotFulledQuery insert = (MainNotFulledQuery) new CreateRequestDAO();
        //    if (mainOperation()) closeWindow();
        // else UsefulUtils.showErrorDialog("Неможливо виконати операцію!");
        builderQuery = new StringBuilder();

        String query = ("UPDATE [dbo].[tbl_RequestOffering]\n" +
                "\tSET [AccountID] = ?,\n" +
                "\t[StatusID] = ?,\n" +
                "\t[StoreCityID] = ?,\n" +
                "\t[OfferingGroupID] = ?,\n" +
                "\t[StateID] = ?,\n" +
                "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                "\t[ModifiedByID] = ?\n" +
                "WHERE([tbl_RequestOffering].[ID] =  ?)"
        );

        pst = null;

        try {
            con = DBConnection.getDataSource().getConnection();
            pst = con.prepareStatement(query);
            pst.setString(1, chosenElement.getAccountID());
            pst.setString(2, chosenElement.getStatusID());
            pst.setString(3, chosenElement.getStoreCityID());
            pst.setString(4, chosenElement.getOfferingGroupID());
            pst.setString(5, chosenElement.getStateID());
            pst.setString(6, User.getContactID());
            pst.setString(7, notFulfilled.getID());
            //    pst.setString(8, chosenElement.getStateID());
            pst.executeUpdate();

            closeWindow();
            notFulledController.handleTableView();
            notFulledController.refreshData();
            con.close();
            pst.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    public EditController() {

    }

    public EditController(NotFulfilled offeringRequest, boolean status) {
        this.notFulfilled = offeringRequest;
        showWindow();


    }

    @FXML
    private ArrayList<? extends Control> listServicesFields;

    public void showWindow() {
        try {

            Stage primaryStage = new Stage();
            primaryStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader();

            //  ClientController con = loader.getController();

            //loader.setRoot(this);
            //loader.setController(this);

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/EditView.fxml"));

            Parent root = fxml.load();


            //  con.setOfferingRequest(offeringRequest);


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        com.mainPage.createRequest.searchCounterpart.SearchCounterpart searchCounterpart = new com.mainPage.createRequest.searchCounterpart.SearchCounterpart();

        cmbMain3.getItems().add(searchCounterpart.chosenElement);
        cmbMain3.getSelectionModel().select(searchCounterpart.chosenElement);
        System.out.println(searchCounterpart.chosenElement + "  222222222222222222");


    }

    @FXML
    public void btnCreateOut(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
