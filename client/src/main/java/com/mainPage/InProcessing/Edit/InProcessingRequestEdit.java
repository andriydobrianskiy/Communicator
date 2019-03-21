package com.mainPage.InProcessing.Edit;

import com.Utils.DictionaryInterface;
import com.Utils.UsefulUtils;
import com.jfoenix.controls.*;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingRequest.InProcessingRequest;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.NotFulled.OfferingRequest.ExampleController;
import com.mainPage.NotFulled.OfferingRequest.OfferingRequest;
import com.mainPage.NotFulled.ProductAdd.ProductAddNewController;
import com.mainPage.NotFulled.ProductAdd.ProductSearch.ProductSearch;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTabPane;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InProcessingRequestEdit implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private static Logger log = Logger.getLogger(InProcessingRequestEdit.class.getName());

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
    public InProcessingRequest chosenAccount;
    @FXML
    private JFXButton btn_OK;
    @FXML
    private JFXComboBox cmbMain3;
    public NotFulfilled notFulfilled;
    @FXML
    ExampleController exampleController;
    @FXML
    NotFulledController notFulledController;


//    private InProcessingRequest chosenAccount;
        private static InProcessingRequest inputAccount = null;

        private static WindowOperation operation = null;



        // Lookup fields


        public InProcessingRequestEdit() {

        }

        public InProcessingRequestEdit(WindowOperation oper, InProcessingRequest inputAccount) {
            this.inputAccount = inputAccount;
            this.operation = oper;
        }

  /*  private void getOrderByOOIO() {
        try {
            List<InProcessingRequest> listItems = FXCollections.observableArrayList();

            listItems.addAll(new DerbyOrderDAO().findOfferingByProperty(OrderSearchType.ID, inputOrder.getOutputOrderID()));
            orderFromOOIO = listItems.get(0);
        } catch (Exception e) {
            UsefulUtils.showTrayErrorDialog("Неможливо знайти рахунок");
        }

    }*/


  /*  private void setComboBoxDefaultValues() { // -> статичні значення за замовчуванням
        listComboBoxes.forEach(box -> {
            ObservableList<? extends DictionaryInterface> list = FXCollections.observableArrayList();
            switch(box.getId()) {
                case "cmbMain3":list = new PaymentType().findAll(false, 0);  box.setItems(list);        break;
            }
        });

    }*/





/*    private void setComboBoxValues() { // ініціалізація полів, зараз майже всі disable, неактуально їх заповнювати

    }

    private void setFieldsByObject() { // Заповнення полів вхідним об'єктом , тільки для EDIT(оновлення)


        try {
            listFields.forEach(field -> {
                ObservableList<? extends DictionaryInterface> list = FXCollections.observableArrayList();

                switch (field.getId()) {
                    case "cmbMain1":
                        ObservableList<Store> listStore = FXCollections.observableArrayList(new Store().findByProperty(orderFromOOIO.getStoreID(), DictionarySearchType.ID));
                        if(!listStore.isEmpty()) {
                            ((ComboBox) field).getItems().add( listStore.get(0));
                            ((ComboBox) field).getSelectionModel().select(listStore.get(0));
                        } else {
                            ((ComboBox) field).setValue(null);
                        }
                        break;
                    case "cmbMain2":
                        ObservableList<AccountCashbox> listACI = FXCollections.observableArrayList(new AccountCashbox().findByProperty(orderFromOOIO.getAccountCashboxIdentifierID(), DictionarySearchType.ID));
                        if(!listACI.isEmpty()) {
                            ((ComboBox) field).getItems().add( listACI.get(0));
                            ((ComboBox) field).getSelectionModel().select(listACI.get(0));
                        } else {
                            ((ComboBox) field).setValue(null);
                        }
                        break;
                    case "cmbMain3":
                        list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                        ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(orderFromOOIO.getPaymentTypeID(), list));
                        break;


                }
            });
        } catch (Exception e ) {
            log.log(Level.SEVERE, "initMainServicesObjectException : " + e);
            UsefulUtils.showErrorDialog("\n\tВиникла помилка. \n" +
                    "Неможливо заповнити поля!");
            closeWindow();

            return;
        }



    }


    private void initOfferingObject() { // створення результуючого об'єкту




        if(UsefulUtils.checkEmptyRequiredFields(requiredFields)) {
            UsefulUtils.showErrorDialog("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
            return;
        }

        try {
            listOfferingInOrderFields.forEach(field -> {
                String stringValue = null;
                Object objectValue = null;

                try {

                    if (field instanceof TextField) {
                        stringValue = ((TextField) field).getText().isEmpty() ? null : ((TextField) field).getText();
                    } else if (field instanceof TextArea) {
                        stringValue = ((TextArea) field).getText().isEmpty() ? null : ((TextArea) field).getText();
                    } else if (field instanceof DatePicker) {
                        stringValue = ((DatePicker) field).getValue() == null ? null : UsefulUtils.dateConverter((DatePicker) field);
                    } else if (field instanceof ComboBox) {

                        objectValue = ((ComboBox) field).getValue();

                    } else if (field instanceof CheckBox){
                        stringValue = String.valueOf(((CheckBox)field).isSelected() ? 1 : 0);
                    } else {
                        stringValue = null;
                    }
                } catch (NullPointerException npe) { // ігнорування NullPointerException

                }

                switch (field.getId()) {

                    case "cmbMain1":
                        orderFromOOIO.setStoreID(objectValue == null ? null : ((Store)objectValue).getID());
                        break;
                    case "cmbMain2":
                        orderFromOOIO.setAccountCashboxIdentifierID(objectValue == null ? null : ((AccountCashbox)objectValue).getID());
                        break;
                    case "cmbMain3":
                        orderFromOOIO.setPaymentTypeID(objectValue == null ? null : ((PaymentType)objectValue).getID());
                        break;

                    /////////////////////////////////////

                }

            });



        } catch (Exception e ) {
            log.log(Level.SEVERE, "initServicesObjectException : " + e);
            UsefulUtils.showErrorDialog("\n\tВиникла помилка. \n" +
                    "Перевірте правильність введених даних!");

            return;
        }


        //checkRequired();

        if(mainOperation()) closeWindow();
        else UsefulUtils.showTrayErrorDialog("Неможливо виконати операцію!");
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }*/

   /* private boolean mainOperation() { // Основні функції (Оновлення/Додавання)

        try {
            DerbyOrderDAO orderDAO = new DerbyOrderDAO();

            orderDAO.updateOrderOfferingInOrder(orderFromOOIO);
            return true;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Main operation exception: " + e);
        }*/



       // return false;
    }
/*

        @Override
        public synchronized void initialize(URL location, ResourceBundle resources) {

            try {

                Platform.runLater(() -> {
                    setComboBoxValues();
                    if(operation.equals(WindowOperation.EDIT)) setFieldsByObject();
                });


                UsefulUtils.setCustomControlFields(listControlFields);

                if(operation.equals(WindowOperation.EDIT))
                    requiredLabels.removeAll(requiredLabelsEdit);

                UsefulUtils.setRequiredFields(requiredLabels);
            } catch (Exception e) {
                log.log(Level.SEVERE, "EXCEPTION - " + e);
            }


            edtLookupButton13.setOnMouseClicked(event -> {
                OwnerDictionary ownerDictionary = new OwnerDictionary();
                ownerDictionary.initWindow();
                if(ownerDictionary.getChosenElement() != null) {
                    edtDetail13.getItems().add(ownerDictionary.getChosenElement());
                    edtDetail13.getSelectionModel().select(ownerDictionary.getChosenElement());
                }
            });

            // Main Buttons

            btn_OK.setOnAction( event -> {
                initAccountObject();
            } );

            btnCancel.setOnAction( event -> closeWindow() );

            ////
        }

        private void setComboBoxDefaultValues() { // -> статичні значення за замовчуванням
            try {
                listComboBoxes.forEach(box -> {
                    ObservableList<? extends DictionaryInterface> list = FXCollections.observableArrayList();
                    switch (box.getId()) {

                        case "cmbMain3":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("61ED4385-E8C0-4955-A503-9EEEFF936EC5", list)); // -> Клієнт СТО
                            break;

                        case "cmbMain4":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("58681A15-6715-40F3-91C6-7EC24C337627", list)); // -> Платник ПДВ - ні
                            break;

                        case "cmbMain6":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("315E58BF-D8E6-41B4-8B0B-1219F12DA54E", list)); // -> Готівка
                            break;

                        case "cmbMain7":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("5627B0A3-5062-47D4-8ADD-61DFB21DB3E5", list)); // -> Оподаткування - не є платником податків
                            break;

                        case "cmbMain12":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("226AA174-3B94-4A20-B70D-B342B57ADBE4", list)); // -> Без спец.націнки
                            break;

                        case "cmbMain17":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("06E78B6D-1A6B-4AB2-A8EF-E6967775209F", list)); // -> Без медалі
                            break;

                        case "cmbContact1":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("DBCB6A43-D99F-45AE-9B41-037DE595242E", list));// -> Телефон
                            break;
                        case "cmbContact2":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("FA08FC2A-9D55-40C9-9576-0017EAED3E49", list)); // -> Мобільний
                            break;
                        case "cmbContact3":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("82696D8B-71AE-4BA4-94FD-3F77474D74E7", list)); // -> Факс
                            break;
                        case "cmbContact4":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("7A628D16-D7D0-4979-B8BA-B64EF54A0366", list)); // -> E-mail
                            break;
                        case "cmbContact5":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("7B77F07B-9976-47D6-95AA-D161FF369D6D", list)); // -> Web
                            break;
                        case "cmbContact11":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("FF16BD59-D381-4B43-A5C6-4DD9D5A60E41", list)); // -> Тел. СМС розсилки
                            break;
                        case "cmbContact12":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("0C0B87E1-7DF7-4E2F-A254-E3533D9E459B", list)); // -> Тел. СМС розсилки(дод.)
                            break;
                        case "cmbContact13":
                            list = ((ComboBox) box).getItems();
                            ((ComboBox) box).getSelectionModel().select(UsefulUtils.filter("8BF58DA9-18C7-4407-9B7A-74470AFA4A28", list)); // -> Тел. СМС розсилки(дод.2)
                            break;
                        case "edtDetail14":
                            list = new Owner().findByProperty(User.getContactID(), DictionarySearchType.ID);
                            ((ComboBox) box).setItems(list);
                            ((ComboBox) box).getSelectionModel().select(list.get(0));
                            break;
                        case "edtDetail13":
                            list = new Owner().findByProperty("7CE60BAE-471A-4EB2-BF6E-FD44087797BD", DictionarySearchType.ID); // -> Стадник
                            ((ComboBox) box).setItems(list);
                            ((ComboBox) box).getSelectionModel().select(list.get(0));
                            break;
                        case "edtContact7":
                        case "edtContact10":
                            ((TextField) box).setPromptText("Не вказано");

                            break;
                    }
                });

            } catch (Exception e) {
                log.log(Level.SEVERE, "DEFAULT VALUE EXCEPTION : " + e);
            }


        }





        private void setComboBoxValues() { // ініціалізація полів



            listComboBoxes.forEach(box -> {
                ObservableList<? extends DictionaryInterface> list = FXCollections.observableArrayList();
                switch(box.getId()) {
                    case "cmbMain3":list = new AccountType().findAll(false, 0);  ((ComboBox)box).setItems(list);        break;
                    case "cmbMain4":list = new VATPayer().findAll(false, 0);   ((ComboBox)box).setItems(list);          break;
                    case "cmbMain6":list = new PaymentType().findAll(false, 0);  ((ComboBox)box).setItems(list);        break;
                    case "cmbMain7":list = new Taxation().findAll(false, 0);  ((ComboBox)box).setItems(list);           break;
                    case "cmbMain11":list = new AccountProblem().findAll(false, 0); ((ComboBox)box).setItems(list);     break;
                    case "cmbMain12":list = new SpecialMarginType().findAll(false, 0); ((ComboBox)box).setItems(list);  break;
                    case "cmbMain17":
                        list = new AccountMedal().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        ((ComboBox)box).setValue(null);
                        break;
                    case "cmbDetail9":
                    case "cmbDetail10":
                    case "cmbDetail11":
                    case "cmbDetail12":list = new ContractReceived().findAll(false, 0); ((ComboBox)box).setItems(list); break;

                    case "cmbContact1":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact2":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact3":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact4":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact5":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact11":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact12":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                    case "cmbContact13":
                        list = new CommunicationType().findAll(false, 0);
                        ((ComboBox)box).setItems(list);
                        break;
                }


            });
            if(operation.equals(WindowOperation.ADD)) {
                setComboBoxDefaultValues();
            }


        }

    /*
        Щоб додати нове поле:
            - Додаєм в AccountQueries(insert, mainquery, update, delete)
            - Додаєм в DerbyAccountDAO строго в залежності від положення "?" в update/insert запиті
            - Додаєм в Account
            - Додаєм в AccountEdit.fxml  !!!{ Назва останнього поля +1 edt/cmb/date{назва Tab'y}{останнє поле+1)
            - Додаєм в fx:define ArrayList
            - Якщо Lookup то створюємо класс в AccountUtils за зразком всіх інших класів в ньому.
            - Пробігаємось по потрібним нам методам і властивостям, переважно switch
     */

/*        private void setFieldsByObject() { // Заповнення полів вхідним об'єктом контрагента, тільки для EDIT(оновлення)
            try {
                listAccountFields.forEach(field -> {
                    ObservableList<? extends DictionaryInterface> list = FXCollections.observableArrayList();

                    switch (field.getId()) {

                        case "edtMain1":
                            ((TextField) field).setText(inputAccount.getName());
                            break;
                        case "edtMain2":
                            ((TextField) field).setText(inputAccount.getOfficialAccountName());
                            break;
                        case "cmbMain3":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getAccountTypeID(), list));
                            break;
                        case "cmbMain4":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getVATPayerID(), list));
                            break;
                        case "cmbMain6":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getPaymentTypeID(), list));
                            break;
                        case "cmbMain7":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getTaxationID(), list));
                            break;
                        case "edtMain8":
                            ((TextField) field).setText(inputAccount.getBranchCode());
                            break;
                        case "edtMain9":
                            ((TextField) field).setText(inputAccount.getEvidenceRegistryNumber());
                            break;
                        case "edtMain10":
                            ((TextField) field).setText(inputAccount.getIndividualOrdinalNumber());
                            break;
                        case "cmbMain11":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getAccountProblemID(), list));
                            break;
                        case "cmbMain12":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getSpecialMarginTypeID(), list));
                            break;
                        case "edtMain13":
                            ((TextField) field).setText(inputAccount.getEvidenceDocument());
                            break;
                        case "edtMain14":
                            ((TextField) field).setText(inputAccount.getBankName());
                            break;
                        case "edtMain15":
                            ((TextField) field).setText(inputAccount.getBankInvoice());
                            break;
                        case "dateMain16":
                            ((DatePicker) field).setValue(UsefulUtils.stringToDateTimeConverter(inputAccount.getAccountProblemDate()));
                            break;
                        case "cmbMain17":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getAccountMedalID(), list));
                            break;
                        case "edtMain18":
                            ((TextField) field).setText(String.valueOf(inputAccount.getAccountDiscount()));
                            break;
                        case "edtMain19":
                            ((TextField) field).setText(String.valueOf(inputAccount.getPromiceSel()));
                            break;
                        case "edtMain20":
                            ((TextField) field).setText(String.valueOf(inputAccount.getDelay()));
                            break;
                        case "edtMain21":
                            ((TextField) field).setText(String.valueOf(inputAccount.getCreditSel()));
                            break;
                        case "edtMain22":
                            ((TextField) field).setText(String.valueOf(inputAccount.getLimit()));
                            break;
                        case "edtMain23":
                            ((TextField) field).setText(String.valueOf(inputAccount.getSaldoSel()));
                            break;
                        case "edtMain24":
                            ((TextArea) field).setText(inputAccount.getRemark());
                            break;
                        case "dateDetail1":
                            ((DatePicker) field).setValue(UsefulUtils.stringToDateTimeConverter(inputAccount.getContractFOPRepairFrom()));
                            break;
                        case "dateDetail2":
                            ((DatePicker) field).setValue(UsefulUtils.stringToDateTimeConverter(inputAccount.getContractFOPFrom()));
                            break;
                        case "dateDetail3":
                            ((DatePicker) field).setValue(UsefulUtils.stringToDateTimeConverter(inputAccount.getContractLLCFrom()));
                            break;
                        case "dateDetail4":
                            ((DatePicker) field).setValue(UsefulUtils.stringToDateTimeConverter(inputAccount.getContractLLCTTFrom()));
                            break;
                        case "edtDetail5":
                            ((TextField) field).setText(String.valueOf(inputAccount.getContractFOPRepairNumber()));
                            break;
                        case "edtDetail6":
                            ((TextField) field).setText(String.valueOf(inputAccount.getContractFOPNumber()));
                            break;
                        case "edtDetail7":
                            ((TextField) field).setText(String.valueOf(inputAccount.getContractLLCNumber()));
                            break;
                        case "edtDetail8":
                            ((TextField) field).setText(String.valueOf(inputAccount.getContractLLCTTNumber()));
                            break;
                        case "cmbDetail9":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getContractFOPRepairReceivedID(), list));
                            break;
                        case "cmbDetail10":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getContractFOPReceivedID(), list));
                            break;
                        case "cmbDetail11":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getContractLLCReceivedID(), list));
                            break;
                        case "cmbDetail12":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getContractLLCTTReceivedID(), list));
                            break;
                        case "edtDetail13":
                            list = new OwnerManager().findByProperty(inputAccount.getOwnerManagerID(), DictionarySearchType.ID);
                            if(!list.isEmpty()) {
                                ((ComboBox) field).getItems().add(list.get(0));
                                ((ComboBox) field).getSelectionModel().selectFirst();
                            } else {
                                ((ComboBox) field).setValue(null);
                            }
                            break;
                        case "edtDetail14":
                            list = new Owner().findByProperty(inputAccount.getOwnerID(), DictionarySearchType.ID);
                            if(!list.isEmpty()) {
                                ((ComboBox) field).getItems().add(list.get(0));
                                ((ComboBox) field).getSelectionModel().selectFirst();
                            } else {
                                ((ComboBox) field).setValue(null);
                            }
                            break;
                        case "edtDetail15":
                            ((TextField) field).setText(String.valueOf(inputAccount.getDiscountSTO()));
                            break;
                        case "edtDetail16":
                            ((TextField) field).setText(inputAccount.getAgent());
                            break;
                        case "cmbContact1":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getCommunication1TypeID(), list));
                            break;
                        case "cmbContact2":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getCommunication2TypeID(), list));
                            break;
                        case "cmbContact3":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getCommunication3TypeID(), list));
                            break;
                        case "cmbContact4":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getCommunication4TypeID(), list));
                            break;
                        case "cmbContact5":
                            list = ((ComboBox<? extends DictionaryInterface>) field).getItems();
                            ((ComboBox)field).getSelectionModel().select(UsefulUtils.filter(inputAccount.getCommunication5TypeID(), list));
                            break;
                        case "edtContact1":
                            ((TextField) field).setText(inputAccount.getCommunication1());
                            break;
                        case "edtContact2":
                            ((TextField) field).setText(inputAccount.getCommunication2());
                            break;
                        case "edtContact3":
                            ((TextField) field).setText(inputAccount.getCommunication3());
                            break;
                        case "edtContact4":
                            ((TextField) field).setText(inputAccount.getCommunication4());
                            break;
                        case "edtContact5":
                            ((TextField) field).setText(inputAccount.getCommunication5());
                            break;
                        case "edtContact6":
                            ((TextField) field).setText(inputAccount.getStreet());
                            break;
                        case "edtContact7":
                            ((TextField) field).setText(inputAccount.getLocality());
                            break;
                        case "edtContact8":
                            ((TextField) field).setText(inputAccount.getDistrict());
                            break;
                        case "edtContact9":
                            ((TextField) field).setText(inputAccount.getRegion());
                            break;
                        case "edtContact10":
                            ((TextField) field).setText(inputAccount.getZIPCode());
                            break;
                    }
                });
            } catch (Exception e ) {
                log.log(Level.SEVERE, "initMainAccountObjectException : " + e);
                UsefulUtils.showErrorDialog("\n\tВиникла помилка. \n");
                closeWindow();

                return;
            }

        }


        private void initAccountObject() throws NullPointerException{ // створення результуючого об'єкту
            if(operation.equals(WindowOperation.ADD)) // Якщо додаємо контрагента, тоді створюємо новий об'єкт
                chosenAccount = new InProcessingRequest();
            else if(operation.equals(WindowOperation.EDIT)) // Присвоюємо вхідному об'єкту
                chosenAccount = inputAccount;


            if(operation.equals(WindowOperation.EDIT))
                requiredFields.removeAll(requiredFieldsEdit);

            if(UsefulUtils.checkEmptyRequiredFields(requiredFields)) {
                UsefulUtils.showInformationDialog("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
                return;
            }


            try {
                listAccountFields.forEach(field -> {
                    String value = null;

                    try {

                        if (field instanceof TextField) {
                            value = ((TextField) field).getText().isEmpty() ? null : ((TextField) field).getText();
                        } else if (field instanceof TextArea) {
                            value = ((TextArea) field).getText().isEmpty() ? null : ((TextArea) field).getText();
                        } else if (field instanceof DatePicker) {
                            value = ((DatePicker) field).getValue() == null ? null : UsefulUtils.dateConverter((DatePicker) field);
                        } else if (field instanceof ComboBox) {
                            value = ((ComboBox<? extends DictionaryInterface>) field).getValue() == null ? null : ((ComboBox<? extends DictionaryInterface>) field).getValue().getID();
                        } else {
                            value = null;
                        }
                    } catch (NullPointerException npe) { // ігнорування NullPointerException

                    }

                    switch (field.getId()) {

                        case "edtMain1":
                            chosenAccount.setName(value);
                            break;
                        case "edtMain2":
                            chosenAccount.setOfficialAccountName((String)value);
                            break;
                        case "cmbMain3":
                            chosenAccount.setAccountTypeID(value);
                            break;
                        case "cmbMain4":
                            chosenAccount.setVATPayerID(value);
                            break;
                        case "cmbMain6":
                            chosenAccount.setPaymentTypeID(value);
                            break;
                        case "cmbMain7":
                            chosenAccount.setTaxationID(value);
                            break;
                        case "edtMain8":
                            chosenAccount.setBranchCode((String)value);
                            break;
                        case "edtMain9":
                            chosenAccount.setEvidenceRegistryNumber((String)value);
                            break;
                        case "edtMain10":
                            chosenAccount.setIndividualOrdinalNumber((String)value);
                            break;
                        case "cmbMain11":
                            chosenAccount.setAccountProblemID((String)value);
                            break;
                        case "cmbMain12":
                            chosenAccount.setSpecialMarginTypeID((String)value);
                            break;
                        case "edtMain13":
                            chosenAccount.setEvidenceDocument((String)value);
                            break;
                        case "edtMain14":
                            chosenAccount.setBankName((String)value);
                            break;
                        case "edtMain15":
                            chosenAccount.setBankInvoice((String)value);
                            break;
                        case "dateMain16":
                            chosenAccount.setAccountProblemDate(value);
                            break;
                        case "cmbMain17":
                            chosenAccount.setAccountMedalID((String)value);
                            break;
                        case "edtMain18":
                            chosenAccount.setAccountDiscount(Integer.parseInt((value == null ? "0" : value)));
                            break;
                        case "edtMain19":
                            chosenAccount.setPromiceSel(Double.parseDouble((value == null ? "0" : value)));
                            break;
                        case "edtMain20":
                            chosenAccount.setDelay(Integer.parseInt((value == null ? "0" : value)));
                            break;
                        case "edtMain21":
                            chosenAccount.setCreditSel(Double.parseDouble((value == null ? "0" : value)));
                            break;
                        case "edtMain22":
                            chosenAccount.setLimit(Double.parseDouble((value == null ? "0" : value)));
                            break;
                        case "edtMain23":
                            chosenAccount.setSaldoSel(Double.parseDouble((value == null ? "0" : value)));
                            break;
                        case "edtMain24":
                            chosenAccount.setRemark(value);
                            break;
                        case "dateDetail1":
                            chosenAccount.setContractFOPRepairFrom(value);
                            break;
                        case "dateDetail2":
                            chosenAccount.setContractFOPFrom(value);
                            break;
                        case "dateDetail3":
                            chosenAccount.setContractLLCFrom(value);
                            break;
                        case "dateDetail4":
                            chosenAccount.setContractLLCTTFrom(value);
                            break;
                        case "edtDetail5":
                            chosenAccount.setContractFOPRepairNumber(value);
                            break;
                        case "edtDetail6":
                            chosenAccount.setContractFOPNumber(value);
                            break;
                        case "edtDetail7":
                            chosenAccount.setContractLLCNumber(value);
                            break;
                        case "edtDetail8":
                            chosenAccount.setContractLLCTTNumber(value);
                            break;
                        case "cmbDetail9":
                            chosenAccount.setContractFOPRepairReceivedID(value);
                            break;
                        case "cmbDetail10":
                            chosenAccount.setContractFOPReceivedID(value);
                            break;
                        case "cmbDetail11":
                            chosenAccount.setContractLLCReceivedID(value);
                            break;
                        case "cmbDetail12":
                            chosenAccount.setContractLLCTTReceivedID(value);
                            break;
                        case "edtDetail13":
                            chosenAccount.setOwnerManagerID(value);
                            break;
                        case "edtDetail14":
                            chosenAccount.setOwnerID(value);
                            break;
                        case "edtDetail15":
                            chosenAccount.setDiscountSTO(Double.parseDouble((value == null ? "0" : value)));
                            break;
                        case "edtDetail16":
                            chosenAccount.setAgent(value);
                            break;

                        case "cmbContact1":
                            chosenAccount.setCommunication1TypeID(value);
                            chosenAccount.setCommunication1TypeName(((ComboBox<? extends DictionaryInterface>) field).getValue().getName());
                            break;
                        case "cmbContact2":
                            chosenAccount.setCommunication2TypeID(value);
                            break;
                        case "cmbContact3":
                            chosenAccount.setCommunication3TypeID(value);
                            break;
                        case "cmbContact4":
                            chosenAccount.setCommunication4TypeID(value);
                            break;
                        case "cmbContact5":
                            chosenAccount.setCommunication5TypeID(value);
                            break;
                        case "edtContact1":
                            chosenAccount.setCommunication1(value);
                            break;
                        case "edtContact2":
                            chosenAccount.setCommunication2(value);
                            break;
                        case "edtContact3":
                            chosenAccount.setCommunication3(value);
                            break;
                        case "edtContact4":
                            chosenAccount.setCommunication4(value);
                            break;
                        case "edtContact5":
                            chosenAccount.setCommunication5(value);
                            break;
                        case "edtContact6":
                            chosenAccount.setStreet(value);
                            break;
                        case "edtContact7":
                            chosenAccount.setLocality(value == null ? ((TextField)field).getPromptText() : value);
                            break;
                        case "edtContact8":
                            chosenAccount.setDistrict(value);
                            break;
                        case "edtContact9":
                            chosenAccount.setRegion(value);
                            break;
                        case "edtContact10":
                            chosenAccount.setZIPCode(value == null ? ((TextField)field).getPromptText() : value);
                            break;
                    }

                });


            } catch (Exception e ) {
                log.log(Level.SEVERE, "initMainAccountObjectException : " + e);
                UsefulUtils.showErrorDialog("\n\tВиникла помилка. \n" +
                        "Перевірте правильність введених даних!");

                return;
            }



            AccountDAO insert = new DerbyAccountDAO();

            if(mainOperation()) closeWindow();
            else UsefulUtils.showTrayErrorDialog("Неможливо виконати операцію!");
        }

        private void closeWindow() {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.close();
        }

        private boolean mainOperation() { // Основні функції (Оновлення/Додавання)
            AccountDAO query = new DerbyAccountDAO();

            switch (operation) {
                case ADD:
                    return (query.insertAccount(chosenAccount) ? true : false);
                case EDIT:
                    return (query.updateAccount(chosenAccount) ? true : false);
                default:
                    log.log(Level.SEVERE, "Can't resolve this window operation");
                    return false;
            }
        }

    }*/

//}
