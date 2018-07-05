package com.mainPage.InProcessing;


import com.ChatTwoo.controller.ColumnMessage;
import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.login.User;
import com.mainPage.InProcessing.Calculator.CalculatorController;
import com.mainPage.InProcessing.InProcessingRequest.InProcessingRequestController;
import com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController;
import com.mainPage.InProcessing.UpdateOfferingGroupName.UpdateOfferingGroupNameController;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.WorkArea;
import com.mainPage.createRequest.searchCounterpart.SearchCounterpart;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableDateTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableDoubleTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InProcessingController extends WorkArea implements MiniFilterFunction, DictionaryProperties, Initializable, ObserverNF {

    @FXML
    private JFXButton btn_Server;
    @FXML
    private JFXButton tbn_CreateRequest;
    @FXML
    private JFXButton btn_changeRequest;
    @FXML
    private JFXButton btn_ConfirmRequest;
    @FXML
    private JFXButton btn_СancelRequest;
    @FXML
    private BorderPane anchorPane;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btn_IntoProcessing;
    @FXML
    private JFXButton btn_UpdateStatus;
    @FXML
    private JFXButton btnConfirmation;
    @FXML
    private JFXToggleButton btn_ButtonAll;
    @FXML
    public TableView tableView;
    @FXML
    private TextField searchingField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label dateLabel;
    @FXML
    private Button btn_ReturnOwner;
    @FXML
    private Button btn_Calculator;
    private PreparedStatement pst = null;
    private Connection con = null;
    private ResultSet rs = null;
    private static Logger log = Logger.getLogger(SearchCounterpart.class.getName());
    private ColumnMessage columnMessage = new ColumnMessage();
    private ObservableList<InProcessing> data;
    private MainPageController main;
    InProcessing selectedOffering = null;
    private InProcessingQuery accountQueries = new InProcessingQuery();
    private long fromIndex;
    private long toIndex;
    public ChatController conn;

    private double xOffset;
    private double yOffset;
    private Scene scene;
    private Boolean queryAll = false;
    private static InProcessingController instance;
    private InProcessing account = new InProcessing();

    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();


    @FXML
    private FilterableStringTableColumn<InProcessing, String> colNumber;
    @FXML
    private FilterableDateTableColumn<InProcessing, String> colCreatedOn;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colCreatedBy;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colAccountCode;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colAccountName;
    @FXML
    private FilterableDoubleTableColumn<InProcessing, Double> colAccountSaldo;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colAccountIsSolid;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colStoreCity;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colStatus;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colOfferingGroupName;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colOriginalGroupName;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colGroupChangedBy;
    @FXML
    private FilterableStringTableColumn<InProcessing, String> colSpecialMarginTypeName;
    @FXML
    private Pagination pagination;
    public static InProcessing chosenAccount = null;
    public InProcessing chosenElement = null;
    @FXML
    private InProcessingRequestController inProcessingRequestViewController;
    @FXML
    private ChatController chatViewController;

    private IntegerProperty index = new SimpleIntegerProperty();
    private Button id = new Button();
    private Button Number = new Button();
    private Button Solid = new Button();
    private Button Store = new Button();
    boolean order = true;
    private ImageView upImg = new ImageView(new Image("/images/Sort_Top.png"));
    private ImageView downImg = new ImageView(new Image("/images/Sort_Bottom.png"));
    int pageCount = 5;
    int itemsPerPage = 40;
    int currentPageIndex = 0;


    @FXML
    public NotFulledController notFulledController;

    public InProcessingController() {

    }

    public void handleTableView(NotFulledController value) {
        notFulledController = value;
        refreshData();
    }

    public void setNotFulledController(MainPageController mainPageController) {
        this.main = mainPageController;
    }

    public MainPageController getNotFulledController() {
        return main;
    }


    public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {
            UsefulUtils.searchCombination(eventKey, tableView);
            fixSelectedRecord();});
    }

    protected void fixSelectedRecord() {
        InProcessing record = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        inProcessingRequestViewController.handleTableView(record);
        try {
            chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

            loginButtonAction(chosenAccount);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        chatViewController.init(this);
        tableView.getStylesheets().add(InProcessingController.class.getResource("/styles/TableStyle.css").toExternalForm());
        inProcessingRequestViewController.init(this);
        btnConfirmation.setVisible(true);  // Замовити
        btn_UpdateStatus.setVisible(true);  // Взяти в роботу
        tbn_CreateRequest.setVisible(false); // Створити запит
        btn_changeRequest.setVisible(false); // Змынити запит
        btn_ConfirmRequest.setVisible(false); // Пыдтвердити
        btn_IntoProcessing.setVisible(false); // Повернути в обробку
        btn_Server.setVisible(false);   // Сервер
        btnDelete.setVisible(false);    // видалити запит
        btn_СancelRequest.setVisible(true); // анулювати запит
        btn_ReturnOwner.setVisible(true); // Змынити выдповыдального
        btn_Calculator.setVisible(true); // калькулятор

        // Connection Database
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        //Button Update
        btn_UpdateStatus.setOnAction(event -> {
            try {
                chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте взяти в роботу?") == ButtonType.OK) {

                String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET  [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                        "[OfferingGroupID] = ?,\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, User.getContactID());
                    pst.setString(3, chosenAccount.getID());

                    pst.executeUpdate();
                    refreshData();
                    UsefulUtils.showSuccessful("Запит в обробці " + User.getContactName());

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else return;
        });


        // Buttons Confirmation
        btnConfirmation.setOnAction(event -> {
            try {
                chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви підтверджуєте своє замовлення і дату поставки?") == ButtonType.OK) {

                String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET [StatusID] = '{3B552198-B239-4801-819C-7033AA118B65}',\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, String.valueOf(chosenAccount));
                    pst.executeUpdate();
                    AddDate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " переведено в тракт");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else return;
        });

        // Button Cancel
        btn_СancelRequest.setOnAction(event -> {

            try {

                chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте анулювати запит?") == ButtonType.OK) {
                try {
                    CallableStatement callProc = con.prepareCall("{call dbo.tsp_ROActionNullifyRequest (?,?)}");
                    callProc.setString(1, chosenAccount.getID());
                    callProc.setString(2, User.getContactID());
                    callProc.executeUpdate();

                    tableView.setRowFactory(new Callback<TableView<InProcessing>, TableRow<InProcessing>>() {
                        @Override
                        public TableRow<InProcessing> call(TableView<InProcessing> param) {
                            return new TableRow<InProcessing>() {
                                @Override
                                protected void updateItem(InProcessing item, boolean empty) {
                                    super.updateItem(item, empty);
                                    setStyle("-fx-background-color: #ffb599");

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


        // Active Chat TableView

        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                        NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);

                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });




        // ToggleButtons TableView
        datePicker.setValue(LocalDate.now());
        btn_ButtonAll.setVisible(true);
        datePicker.setVisible(false);
        dateLabel.setVisible(false);
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
        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle() != null) {
                queryAll = true;
                datePicker.setVisible(true);
                dateLabel.setVisible(true);
                btn_UpdateStatus.setVisible(false);
                refreshData();
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

            } else
            {
                queryAll = false;
                datePicker.setVisible(false);
                dateLabel.setVisible(false);
               refreshData();
                btn_UpdateStatus.setVisible(true);
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
            }
        });


        NotificationBUProduct();
        //Create Columns
        createTableColumns();
        tableViewHandles();
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(InProcessingController.this, account, hashColumns,event).setFilter();
        });
        UsefulUtils.copySelectedCell(tableView);
        anchorPane.setCenter(tableView);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Create Paginations
        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
            pagination.setSkin(pageSkin);

            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);

            System.out.println("pageCount=" + pageCount);
            pagination.setPageCount(pageCount);
            //   sort();
            initializeTable();

            pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                    currentPageIndex = newValue.intValue();
                    updatePersonView();
                }
            });


            SortButton(Number);
            SortButton(id);
            SortButton(Solid);
            SortButton(Store);
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вдається відкрити сторінки");

        }

        //Buttons Icons
        Image imageDecline = new Image(getClass().getResourceAsStream("/images/CreateRequest.png"));
        tbn_CreateRequest.setGraphic(new ImageView(imageDecline));


        Image imageChange = new Image(getClass().getResourceAsStream("/images/ChangeRequest.png"));
        btn_changeRequest.setGraphic(new ImageView(imageChange));

        Image imageConfirm = new Image(getClass().getResourceAsStream("/images/ConfirmRequest.png"));
        btn_ConfirmRequest.setGraphic(new ImageView(imageConfirm));

        Image imageCancel = new Image(getClass().getResourceAsStream("/images/СancelRequest.png"));
        btn_СancelRequest.setGraphic(new ImageView(imageCancel));

        Image imageDelete = new Image(getClass().getResourceAsStream("/images/DeleteRequest.png"));
        btnDelete.setGraphic(new ImageView(imageDelete));

        Image imageIntoProcessing = new Image(getClass().getResourceAsStream("/images/IntoProcessing.png"));
        btn_IntoProcessing.setGraphic(new ImageView(imageIntoProcessing));

        Image imageServerInProcessing = new Image(getClass().getResourceAsStream("/images/ServerInProcessing.png"));
        btn_Server.setGraphic(new ImageView(imageServerInProcessing));
        btnDelete.setOnAction(event -> {
            try {
                selectedOffering = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
                new DerbyInProcessingDAO().deleteOffering(selectedOffering);
                main.changeExists();
                UsefulUtils.showSuccessful("Запит успішно видалено");


            } else return;

        });

        //hostnameTextfield.setText("localhost");
        int numberOfSquares = 30;
        while (numberOfSquares > 0) {
            //  generateAnimation();
            numberOfSquares--;
        }
        UpdateNotificationTable();



        tableView.setRowFactory(new Callback<TableView<InProcessing>, TableRow<InProcessing>>() {
            @Override
            public TableRow<InProcessing> call(TableView<InProcessing> param) {
                return new TableRow<InProcessing>() {
                    @Override
                    protected void updateItem(InProcessing item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            if (item == null || item.getIsReadMeassage() == null || item.getIsReadMeassage().equals(0)) {
                                setStyle("");
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2)) {
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



                    }
                };
            }
        });



        tableView.getSelectionModel().select(0);
    }

    private void NotificationBUProduct() {

            tableView.setRowFactory(new Callback<TableView<InProcessing>, TableRow<InProcessing>>() {
                @Override
                public TableRow<InProcessing> call(TableView<InProcessing> param) {
                    return new TableRow<InProcessing>() {
                        @Override
                        protected void updateItem(InProcessing item, boolean empty) {
                            super.updateItem(item, empty);
                            try {
                                if ( item.getOfferingGroupID() == "1B5F2F6A-133B-4026-BED3-914C8AC491D9" && item.getIsReadMeassage().equals(1) && User.getContactID() == "1B5F2F6A-133B-4026-BED3-914C8AC491D9") {
                                   UsefulUtils.showConfirmDialog("Новий запит "+ item.getNumber());
                                    Media hit = new Media(getClass().getClassLoader().getResource("sounds/not-bad.mp3").toString());
                                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
                                    mediaPlayer.play();
                                }
                            } catch (NullPointerException ex) {

                            }



                        }
                    };
                }
            });
    }

    private void SortButton(Button button) {
        button.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent t) {
                pagination.setCurrentPageIndex(0);
                sort(button);
                if (order) {
                    Collections.reverse(data);
                }
                order = !order;
                button.setGraphic((order) ? upImg : downImg);
                updatePersonView();
            }
        });
    }

    private void sort(Button notFulfilled) {
        if (notFulfilled == id) {
            data.sort((InProcessing p1, InProcessing p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()));
        } else if (notFulfilled == Number) {
            data.sort((InProcessing p1, InProcessing p2) -> p1.getNumber().compareTo(p2.getNumber()));
        }else if (notFulfilled == Solid) {
            data.sort((InProcessing p1, InProcessing p2) -> p1.getAccountIsSolid().compareTo(p2.getAccountIsSolid()));
        } else if (notFulfilled == Store) {
            data.sort((InProcessing p1, InProcessing p2) -> p1.getNumber().compareTo(p2.getStoreCity()));
        } else {
            UsefulUtils.showErrorDialogDown("Помилка сортування");
        }
    }

    private void initializeTable() {


        colCreatedOn.setGraphic(id);
        colCreatedOn.setSortable(false);
        colNumber.setGraphic(Number);
        colNumber.setSortable(false);
        colAccountIsSolid.setGraphic(Solid);
        colAccountIsSolid.setSortable(false);
        colStoreCity.setGraphic(Store);
        colStoreCity.setSortable(false);

    }

    public void updatePersonView() {
        fromIndex = currentPageIndex * itemsPerPage;
        toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
    }

    String Skrut = null;

    public void findByProperty(String value) {
        data.clear();
        try {
            pst = con.prepareStatement("SELECT RequestID " +
                            "\t\t\t\t\tFROM dbo.tbl_OfferingInRequestOffering \n" +
                            " WHERE [tbl_OfferingInRequestOffering].[OfferingID] IN (SELECT ID\n" +
                            "        FROM [tbl_Offering] WHERE [tbl_Offering].Skrut LIKE '" + value.toString() + "%' OR [tbl_Offering].[Index] LIKE '" + value.toString() + "%')"//+
                    //  "FOR XML PATH('')"
            );
            rs = pst.executeQuery();
            while (rs.next()) {
                Skrut = rs.getString(1);
                //  options.add(rs.getString(1));
                List<InProcessing> listItems = account.findAllOneSearch(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));


            }
            tableView.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public void AddDate() {

        Date date = Date.valueOf(datePicker.getValue());
        System.out.println(date + "datePostavki");
        String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
                "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
        pst = null;


        try {
            pst = con.prepareStatement(query);

            pst.setString(1, com.login.User.getContactID());
            pst.setString(2, columnMessage.getRecipientID());
            pst.setString(3, chosenAccount.getID());
            pst.setString(4, "Дата поставки: " + date);
            pst.execute();


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }




    public void loginButtonAction(InProcessing chosenAccount) throws IOException {
        String hostname = "192.168.10.144";
        System.out.println(hostname);
        int port = 9001;
        String username = User.getContactName();
        String picture = "Default";
        chatViewController.setOfferingRequest(chosenAccount);
        try {
            chatViewController.loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        chatViewController.buttonSendTract.setVisible(false);
        main.setChatController(chatViewController);
        main.messageSend();


        Listener listener = new Listener(hostname, port, username, picture, chatViewController, this, chosenAccount);
        Thread x = new Thread(listener);
        x.start();

        chatViewController.setUsernameLabel(User.getContactName());

        chatViewController.setImageLabel("Default");



    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.setWidth(1040);
            stage.setHeight(620);
            stage.setScene(this.scene);
            conn.setUsernameLabel(User.getContactName());
            conn.setImageLabel("Default");
            stage.showAndWait();
        });
    }

    public void showErrorDialog(String message) {
        Platform.runLater(() -> {
            TrayNotification tray = new TrayNotification();
            tray.setNotificationType(NotificationType.ERROR);
            tray.setTitle("Помилка");
            String s = message;
            tray.setMessage(s);
            tray.setAnimationType(AnimationType.SLIDE);
            tray.showAndDismiss(Duration.millis(400));
        });

    }


    private void setOfferingRequest(InProcessing chosenAccount) {
        this.chosenAccount = chosenAccount;
    }

    @Override
    public void createTableColumns() {



        try {

            hashColumns.put(colNumber, "[tbl_RequestOffering].[Number]");
            hashColumns.put(colCreatedOn, "[tbl_RequestOffering].[CreatedOn]");
            hashColumns.put(colCreatedBy, "[tbl_Contact].[Name]");
            hashColumns.put(colAccountName, "[tbl_Account].[Name]");
            hashColumns.put(colAccountCode, "[tbl_Account].[Code]");
            hashColumns.put(colAccountSaldo, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colAccountIsSolid, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");
            hashColumns.put(colStatus, "[tbl_RequestOfferingStatus].[Name]");
            hashColumns.put(colStoreCity, "[tbl_StoreCity].[Name]");
            hashColumns.put(colOfferingGroupName, "[OfferingGroup].[Name]");
            hashColumns.put(colOriginalGroupName, "SELECT\n" +
                    "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n");
            hashColumns.put(colGroupChangedBy, "SELECT\n" +
                    "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n");
            hashColumns.put(colSpecialMarginTypeName, "[SMT].[Name]");
            List<?> listColumns = tableView.getColumns();

            colNumber.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("Number"));
            colCreatedOn.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("CreatedOn"));
            colCreatedBy.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("CreatedBy"));
            colAccountCode.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountCode"));
            colAccountName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountName"));
            colAccountSaldo.setCellValueFactory(new PropertyValueFactory<InProcessing, Double>("AccountSaldo"));
            colAccountIsSolid.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountIsSolid"));
            colStoreCity.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("StoreCity"));
            colStatus.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("Status"));

            colOfferingGroupName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("OfferingGroupName"));
            colOriginalGroupName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("OriginalGroupName"));
            colGroupChangedBy.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("GroupChangedBy"));
            colSpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("SpecialMarginTypeName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

        super.createTableColumns();
    }


    @Override
    public void loadDataFromDatabase() {
            data.clear();
        try {
            if(queryAll == true){
            List<InProcessing> listItems = account.findAllOne(true, (int) toIndex, User.getContactID(), User.getContactID());
            listItems.forEach(item -> data.add(item));

            tableView.setItems(data);
            }else if (queryAll == false){
                List<InProcessing> listItems = account.findAllInProcessing(true, (int) toIndex);
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }

    }




    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();
            loadDataFromDatabase();
            fromIndex = pageIndex * itemsPerPage;
            toIndex = Math.min(fromIndex + itemsPerPage, data.size());





            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }
        anchorPane.setCenter(tableView);
        return anchorPane;
    }

    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }

    private Stage primaryStage;
    private VBox serverLayout;


    @FXML
    private void hadleServerButton(ActionEvent event) {

    }

    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            loadDataFromDatabase();
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
            NotificationBUProduct();
        }

        UsefulUtils.fadeTransition(tableView);
    }




    private void searchCode() {
        FilteredList<InProcessing> filteredData = new FilteredList<>(data, e -> true);
        searchingField.setOnKeyReleased(e -> {
            searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super InProcessing>) user -> {
                    main.changeExists();
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
            SortedList<InProcessing> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);

        });
    }




    @FXML
    public void handleDelete(ActionEvent e) {

        try {

            selectedOffering = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
            new DerbyInProcessingDAO().deleteOffering(selectedOffering);
            main.changeExists();
            UsefulUtils.showSuccessful("Запит успішно видалено");


        } else return;


    }


    @FXML
    private void btnUpdateOfferingGroupName(ActionEvent e) {
            try {
                chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();


        loader.setLocation(UpdateOfferingGroupNameController.class.getResource("/views/UpdateOfferingGroupName.fxml"));
        UpdateOfferingGroupNameController createRequest = loader.getController();
        Pane serverrLayout = null;
        try {
            serverrLayout = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        // Show the scene containing the root layout.

        Scene scene = new Scene(serverrLayout);
        UpdateOfferingGroupNameController con = loader.getController();
        con.setOfferingRequest(this, chosenAccount);


        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.setMaxHeight(480);
        stage.setMaxWidth(620);
        stage.show();
        stage.requestFocus();


    }



    public void UpdateNotificationTable() {
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {


            chosenAccount = (InProcessing) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            if (chosenAccount != null) {
                if (event.getCode() == KeyCode.SPACE) {

                    if (chosenAccount.getIsReadMeassage() == null || chosenAccount.getIsReadMeassage().equals(0)) {
                        String queryUpdate = "UPDATE tbl_RequestOffering \n" +
                                "\t\t\t\t\tSET \n" +
                                "\t\t\t\t\tIsReadMeassage = '1'\n" +
                                "\t\t\t\t\tWHERE ID = ?";
                        try {
                            pst = con.prepareStatement(queryUpdate);

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
                            pst = con.prepareStatement(query);

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

    @FXML
    private void actionCalculator(ActionEvent e) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();


        loader.setLocation(InProcessingController.class.getResource("/views/CalculatorView.fxml"));
        CalculatorController createRequest = loader.getController();
        Pane serverrLayout = null;
        try {
            serverrLayout = loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Scene scene = new Scene(serverrLayout);
        CalculatorController con = loader.getController();
        con.setOfferingRequest(chosenAccount);
        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.show();
        stage.requestFocus();
    }
    @Override
    public void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/MiniFilter.fxml"));

        Pane content;
        try {
            content = fxmlLoader.load();
            MiniFilterController miniC = fxmlLoader.getController();
            miniC.setWindow(this, content);
            miniC.setFilter(column, type, value);
            hboxFilter.getChildren().add(content);
            hashMiniFilter.put(column, content);
            hboxFilter.setMargin(content, new Insets(0, 0, 0, 10));
            scrollPaneFilter.setFitToHeight(true);
            System.out.println("SETTTED");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }
    @Override
    public void disableFilter(TableColumn column, Pane content) {
        account.removeStringFilter(column);
        refreshData();
        if(content == null) {
            removeFilterFromHbox(column);
            return;
        }
        hboxFilter.getChildren().remove(content);
        System.out.println("DISABLED");
    }
    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }
    @Override
    public void update() {
        refreshData();

    }
}
