package com.mainPage.InTract;

import com.Utils.CustomPaginationSkin;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.ListinerTract;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.login.User;
import com.mainPage.InTract.InTractRequest.InTractRequestController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InTractController extends BorderPane implements Initializable, DictionaryPropertiesInTract, ObserverNF {

    private static Logger log = Logger.getLogger(InTractController.class.getName());
    private MainPageController main;
    private PreparedStatement pst = null;
    private Connection con = null;
    private ResultSet rs = null;
    @FXML
    private InTractRequestController inTractRequestViewController;
    @FXML
    private TextField searchingField;
    @FXML
    public TableView tableView;
    @FXML
    private Pagination pagination;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnConfirmation;
    @FXML
    private JFXButton btn_Delete;
    @FXML
    private JFXButton btn_СancelRequest;
    @FXML
    private JFXButton btn_Create;
    @FXML
    private JFXButton btn_Refresh;
    @FXML
    private ChatController chatViewController;
    @FXML
    private SplitPane splitPane1;
    @FXML
    private SplitPane splitPane2;
    @FXML
    public TableView tableviewAll;
    @FXML
    private JFXToggleButton btn_ButtonAll;
    public  ChatController conn;
    private Scene scene;
    /*@FXML
    private InTractRequestController inTractRequestController;*/
    private ObservableList<InTract> data;
    private InTract account = new InTract();
    public static InTract chosenAccount = null;
    private long fromIndex;
    private long toIndex;
    private InTractQuery accountQueries = new InTractQuery();


   /* public InTractController (){
        refreshData();
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getStylesheets().add
                (InTractController.class.getResource("/styles/TableStyle.css").toExternalForm());
        btnConfirmation.setVisible(true);
        btn_Delete.setVisible(false);
        btn_СancelRequest.setVisible(true);
        btn_Create.setVisible(false);
        btn_Refresh.setVisible(false);
        splitPane1.setDividerPositions(0.75);

        inTractRequestViewController.init(this);
        chatViewController.init(this);

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // ToggleButtons TableView
        tableView.setVisible(true);
        tableviewAll.setVisible(false);
        btn_ButtonAll.setVisible(false);
      //  datePicker.setVisible(false);
      //  dateLabel.setVisible(false);

        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle() != null) {

                //    data.clear();
                //  loadDataFromDatabase();

                tableView.setVisible(true);
                tableviewAll.setVisible(false);
                //datePicker.setVisible(true);
              //  dateLabel.setVisible(true);
               // btn_UpdateStatus.setVisible(false);
                searchingField.setOnAction(event1 -> {
                    String value = searchingField.getText();

                    if (value.equals("")) {
                        refreshData();
                    } else
                        try {
                            findByProperty(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });

            } else if (tableviewAll != null) {
                tableviewAll.setVisible(true);
                tableView.setVisible(false);
           //     datePicker.setVisible(false);
//                dateLabel.setVisible(false);

                //  dataAll.clear();
                //   loadDataFromDatabaseAll();

             //   btn_UpdateStatus.setVisible(true);
                searchingField.setOnAction(event1 -> {
                    String value = searchingField.getText();

                    if (value.equals("")) {
                        refreshData();
                    } else
                        try {
                          //  findByPropertyAll(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });
            }
        });
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    //closeWindow();
                }
            }
        });
        btnConfirmation.setOnAction(event -> {

            try {

                chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                System.out.println(chosenAccount.getID() + "8888888888888888888888888885555555555555555555555555555566666666666666666666666");
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви підтверджуєте, що запчастини прибули на стан і бажаєте завершити замовлення?") == ButtonType.OK) {
                String query = ("UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET [StatusID] = '{6F784E48-1474-4CB9-B28D-A4B580FB346C}',\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)");
                try {
                    pst = con.prepareStatement(query);
                    // pst.setString(1, User.getContactID());
                    pst.setString(1, User.getContactID());
                    pst.setString(2, chosenAccount.getID());
                    pst.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " завершено");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });


        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 1) {
                    try {
                        chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

                        //   NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);
                        loginButtonAction(chosenAccount);
                        //  ClientController.recordInTract = chosenAccount;
                        //     System.out.println(chosenAccount.getID() + "5649848949849845316546854684896");
                        //     ClientController clientController = new ClientController(chosenAccount, false);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });
//loadDataFromDatabase();
        createTableColumns();
        createTableColumnsAll();
        tableViewHandles();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableviewAll.getSelectionModel().setCellSelectionEnabled(false);
        tableviewAll.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination

        pagination.setSkin(pageSkin);
        pagination.setPageFactory(this::createPage);
        pagination.setPageFactory(this::createPageAll);
        searchingField.setOnAction(event1 -> {
            String value = searchingField.getText();

            if (value.equals("")) {
                refreshData();
            } else
                try {
                    findByProperty(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
        //    searchCode();
        // Button Cancel
        btn_СancelRequest.setOnAction(event -> {

            try {

                chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте анулювати запит?") == ButtonType.OK) {
                try {
                    //  con = DBConnection.getDataSource().getConnection();
                    CallableStatement callProc = con.prepareCall("{call dbo.tsp_ROActionNullifyRequest (?,?)}");
                    callProc.setString(1, chosenAccount.getID());
                    callProc.setString(2, User.getContactID());
                    callProc.executeUpdate();

                    tableView.setRowFactory(new Callback<TableView<InTract>, TableRow<InTract>>() {
                        @Override
                        public TableRow<InTract> call(TableView<InTract> param) {
                            return new TableRow<InTract>() {
                                @Override
                                protected void updateItem(InTract item, boolean empty) {
                                    super.updateItem(item, empty);

                                    setStyle("-fx-background-color: #ff0700");

                                }
                            };
                        }
                    });

                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " анульовано");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });

        tableView.setRowFactory(new Callback<TableView<InTract>, TableRow<InTract>>() {
            @Override
            public TableRow<InTract> call(TableView<InTract> param) {
                return new TableRow<InTract>() {
                    @Override
                    protected void updateItem(InTract item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            if (item == null || item.getIsReadMeassage() == null || item.getIsReadMeassage().equals(0)) {
                                setStyle("");
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2)){
                                    setStyle("-fx-background-color: #FF0000;");

                                }
                            } else {
                                setStyle("-fx-font-weight: bold");
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2))
                                    setStyle("-fx-background-color: #FF0000;" +
                                            "-fx-font-weight: bold");

                            }
                        } catch (NullPointerException ex) {

                        }
                        //    if (item.getJointAnnulment().equals(2) || item.getJointAnnulment().equals(1)) {
                        //      setStyle("-fx-background-color: #FF0000");
                        //    }

                        //      } else {
                        //
                        // if (!item.getJointAnnulment().equals(3) || !item.getJointAnnulment().equals(0)) {
                        //   setStyle("-fx-background-color: #FF0000;" +
                        //           "-fx-font-weight: bold");
                        //       }


                    }
                };
            }
        });

        try {
            UpdateNotificationAllTable();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    String Skrut = null;
    public void findByProperty(String value) {
        data.clear();

        try {
            pst = con.prepareStatement ("SELECT RequestID " +
                            "\t\t\t\t\tFROM dbo.tbl_OfferingInRequestOffering \n" +
                            " WHERE [tbl_OfferingInRequestOffering].[OfferingID] IN (SELECT ID\n" +
                            "        FROM [tbl_Offering] WHERE [tbl_Offering].Skrut LIKE '"+value.toString()+"%' OR [tbl_Offering].[Index] LIKE '"+value.toString()+"%')"//+
                    //  "FOR XML PATH('')"
            );
            rs = pst.executeQuery();
            while (rs.next()) {
                Skrut = rs.getString(1);
                //  options.add(rs.getString(1));
                List<InTract> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));



            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /// Skrut = (String) options.get(0);


        tableView.setItems(data);



    }
    public void loginButtonAction(InTract chosenAccount) throws IOException {
        String hostname = "192.168.10.144";
        int port = 9001;
        String username = User.getContactName();
        String picture = "Default";


        //FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));

       // Parent window = (Pane) fmxlLoader.load();
      //  chatViewController = fmxlLoader.<ChatController>getController();
        //  instance = fmxlLoader.<InProcessingController>getController();
        chatViewController.setOfferingTract(chosenAccount);
        chatViewController.setInTract(this);
        chatViewController.buttonSend.setVisible(false);
        chatViewController.setUsernameLabel(User.getContactName());
        ListinerTract listener = new ListinerTract(hostname, port, username, picture, chatViewController, this, chosenAccount);
        Thread x = new Thread(listener);
        x.start();

        //this.scene = new Scene(window);


    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            // (Stage) hostnameTextfield.getScene().getWindow();
            //   stage.getScene();
//stage.showAndWait();
            stage.setResizable(true);
            stage.setWidth(1040);
            stage.setHeight(620);

            //        stage.setOnCloseRequest((WindowEvent e) -> {
            //           Platform.exit();
            //       System.exit(0);
            //    });
            stage.setScene(this.scene);

            //  stage.setMinWidth(800);
            //   stage.setMinHeight(300);

            ResizeHelper.addResizeListener(stage);
            //      stage.showAndWait();
            System.out.println("chatFive");

            conn.setUsernameLabel(User.getContactName());
            conn.setImageLabel("Default");
          //  conn.setIdTextFild(chosenAccount.getID());
            stage.showAndWait();
        });
    }
    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }


    private void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> fixSelectedRecord());
    }

    private void tableViewHandlesAll() {
        tableviewAll.setOnMouseClicked(mouseEvent -> fixSelectedRecordAll());
        tableviewAll.setOnKeyReleased(eventKey -> fixSelectedRecordAll());
    }
    private void fixSelectedRecord() {
        InTract record = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println(record);
        inTractRequestViewController.handleTableView(record);
        chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        try {
            loginButtonAction(chosenAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fixSelectedRecordAll() {
        InTract record = (InTract) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());

        System.out.println(record);
        inTractRequestViewController.handleTableView(record);
        chosenAccount = (InTract) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());
        try {
            loginButtonAction(chosenAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTableColumns() {

        try {
            TableColumn<InTract, String> number = new TableColumn<InTract, String>("Номер запиту");
            TableColumn<InTract, String> createdOn = new TableColumn<InTract, String>("Дата");
            TableColumn<InTract, String> createdBy = new TableColumn<InTract, String>("Створив");
            TableColumn<InTract, String> accountCode = new TableColumn<InTract, String>("Код контрагента");
            TableColumn<InTract, String> accountName = new TableColumn<InTract, String>("Контрагент");
            TableColumn<InTract, String> accountSaldo = new TableColumn<InTract, String>("Сальдо");
            TableColumn<InTract, String> accountIsSolid = new TableColumn<InTract, String>("Солідність");
            TableColumn<InTract, String> storeCity = new TableColumn<InTract, String>("Місто поставки");
            TableColumn<InTract, String> status = new TableColumn<InTract, String>("Статус");
            TableColumn<InTract, String> groupChangedBy = new TableColumn<InTract, String>("Змінив групу");
            TableColumn<InTract, String> specialMarginTypeName = new TableColumn<InTract, String>("Тип спец націнки");
            TableColumn<InTract, String> stateName = new TableColumn<InTract, String>("Статус запиту");
            TableColumn<InTract, String> offeringGroupName = new TableColumn<InTract, String>("Група товарів");
            TableColumn<InTract, String> originalGroupName = new TableColumn<InTract, String>("Початкова група");


           // number.setVisible(false);
            tableView.setTableMenuButtonVisible(true);

            number.setMinWidth(150);

            //    id.setVisible(false);

            tableView.getColumns().addAll(
                    number,
                    createdOn,
                    createdBy,
                    accountCode,
                    accountName,
                    accountSaldo,
                    accountIsSolid,
                    storeCity,
                    status,
                    groupChangedBy,
                    specialMarginTypeName,
                    stateName,
                    offeringGroupName,
                    originalGroupName);
            number.setCellValueFactory(new PropertyValueFactory<InTract, String>("Number"));
            createdOn.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedOn"));
            createdBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedBy"));
            accountCode.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountCode"));
            accountName.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountName"));
            accountSaldo.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountSaldo"));
            accountIsSolid.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountIsSolid"));
            storeCity.setCellValueFactory(new PropertyValueFactory<InTract, String>("StoreCity"));
            status.setCellValueFactory(new PropertyValueFactory<InTract, String>("Status"));
            groupChangedBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("GroupChangedBy"));
            specialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InTract, String>("SpecialMarginTypeName"));
            stateName.setCellValueFactory(new PropertyValueFactory<InTract, String>("StateName"));
            offeringGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OfferingGroupName"));
            originalGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OriginalGroupName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }

    @Override
    public void createTableColumnsAll() {
        try {
            TableColumn<InTract, String> number = new TableColumn<InTract, String>("Номер запиту");
            TableColumn<InTract, String> createdOn = new TableColumn<InTract, String>("Дата");
            TableColumn<InTract, String> createdBy = new TableColumn<InTract, String>("Створив");
            TableColumn<InTract, String> accountCode = new TableColumn<InTract, String>("Код контрагента");
            TableColumn<InTract, String> accountName = new TableColumn<InTract, String>("Контрагент");
            TableColumn<InTract, String> accountSaldo = new TableColumn<InTract, String>("Сальдо");
            TableColumn<InTract, String> accountIsSolid = new TableColumn<InTract, String>("Солідність");
            TableColumn<InTract, String> storeCity = new TableColumn<InTract, String>("Місто поставки");
            TableColumn<InTract, String> status = new TableColumn<InTract, String>("Статус");
            TableColumn<InTract, String> groupChangedBy = new TableColumn<InTract, String>("Змінив групу");
            TableColumn<InTract, String> specialMarginTypeName = new TableColumn<InTract, String>("Тип спец націнки");
            TableColumn<InTract, String> stateName = new TableColumn<InTract, String>("Статус запиту");
            TableColumn<InTract, String> offeringGroupName = new TableColumn<InTract, String>("Група товарів");
            TableColumn<InTract, String> originalGroupName = new TableColumn<InTract, String>("Початкова група");


            // number.setVisible(false);
            tableviewAll.setTableMenuButtonVisible(true);

            number.setMinWidth(150);

            //    id.setVisible(false);

            tableviewAll.getColumns().addAll(
                    number,
                    createdOn,
                    createdBy,
                    accountCode,
                    accountName,
                    accountSaldo,
                    accountIsSolid,
                    storeCity,
                    status,
                    groupChangedBy,
                    specialMarginTypeName,
                    stateName,
                    offeringGroupName,
                    originalGroupName);
            number.setCellValueFactory(new PropertyValueFactory<InTract, String>("Number"));
            createdOn.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedOn"));
            createdBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedBy"));
            accountCode.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountCode"));
            accountName.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountName"));
            accountSaldo.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountSaldo"));
            accountIsSolid.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountIsSolid"));
            storeCity.setCellValueFactory(new PropertyValueFactory<InTract, String>("StoreCity"));
            status.setCellValueFactory(new PropertyValueFactory<InTract, String>("Status"));
            groupChangedBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("GroupChangedBy"));
            specialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InTract, String>("SpecialMarginTypeName"));
            stateName.setCellValueFactory(new PropertyValueFactory<InTract, String>("StateName"));
            offeringGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OfferingGroupName"));
            originalGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OriginalGroupName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
    }
    @Override
    public List<InTract> loadDataFromDatabase() {
        // System.out.println(chosenAccount.getOfferingGroupID()+ "7899999999999999999999999999989854645613256131651156" + User.getContactID());
        try {
            List<InTract> listItems = account.findInTract(true, (int) toIndex, User.getContactID(), User.getContactID());

            listItems.forEach(item -> data.add(item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        return null;
    }


    @Override
    public List<InTract> loadDataFromDatabaseAll() {
        // System.out.println(chosenAccount.getOfferingGroupID()+ "7899999999999999999999999999989854645613256131651156" + User.getContactID());
        try {
            List<InTract> listItems = account.findInTractAll(true, (int) toIndex);

            listItems.forEach(item -> data.add(item));

            tableviewAll.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        return null;
    }



    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainInTractCount());


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }
    public BorderPane createPageAll(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainInTractCount());


            loadDataFromDatabaseAll();


            tableviewAll.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }

    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            loadDataFromDatabase();
            loadDataFromDatabaseAll();
        }

        UsefulUtils.fadeTransition(tableView);



    }
    private void searchCode() {
        FilteredList<InTract> filteredData = new FilteredList<>(data, e -> true);
        searchingField.setOnKeyReleased(e -> {
            searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super InTract>) user -> {
                    if (newValue == null || newValue.isEmpty()) {

                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getAccountCode().contains(newValue)) {
                        return true;
                    } else if (user.getOfferingGroupName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;

                });
            });
            SortedList<InTract> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);

        });
    }


    public void UpdateNotificationAllTable() throws IOException {
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            if (chosenAccount != null) {
                if (event.getCode() == KeyCode.SPACE) {

                    if (chosenAccount.getIsReadMeassage() == null || chosenAccount.getIsReadMeassage().equals(0)) {
                        String queryUpdate = "UPDATE tbl_RequestOffering \n" +
                                "\t\t\t\t\tSET \n" +
                                "\t\t\t\t\tIsReadMeassage = '1'\n" +
                                "\t\t\t\t\tWHERE ID = ?";
                        try {
                            pst = DBConnection.getDataSource().getConnection().prepareStatement(queryUpdate);

                            pst.setString(1, chosenAccount.getID());
                            pst.executeUpdate();
                            main.changeExists();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String query = "UPDATE tbl_RequestOffering \n" +
                                "\t\t\t\t\tSET \n" +
                                "\t\t\t\t\tIsReadMeassage = '0'\n" +
                                "\t\t\t\t\tWHERE ID = ?";
                        try {
                            pst = DBConnection.getDataSource().getConnection().prepareStatement(query);

                            pst.setString(1, chosenAccount.getID());
                            pst.executeUpdate();
                            main.changeExists();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void update() {
        refreshData();
    }
}
