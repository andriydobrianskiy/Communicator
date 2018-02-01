package com.mainPage.InProcessing;


import com.ChatTwoo.controller.ClientController;
import com.ChatTwoo.controller.ColumnMessage;
import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.login.User;
import com.mainPage.InProcessing.InProcessingRequest.InProcessingRequestController;
import com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController;
import com.mainPage.InProcessing.UpdateOfferingGroupName.UpdateOfferingGroupNameController;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.createRequest.searchCounterpart.SearchCounterpart;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InProcessingController extends AnchorPane implements DictionaryProperties, Initializable, ObserverNF {

    @FXML
    public TableView<InProcessing> tableProduct;
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
    private  TableView tableviewAll;
    @FXML
    private TextField searchingField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label dateLabel;

    private PreparedStatement pst = null;
    private Connection con = null;
    private static Logger log = Logger.getLogger(SearchCounterpart.class.getName());

    private ColumnMessage columnMessage = new ColumnMessage();
    private ObservableList<InProcessing> data;
    private ObservableList<InProcessing> dataAll;

    private static MainPageController main;
    InProcessing selectedOffering = null;

    private InProcessingQuery accountQueries = new InProcessingQuery();

    private long fromIndex;
    private long toIndex;

    public  ChatController conn;

    private double xOffset;
    private double yOffset;
    private Scene scene;

    private static InProcessingController instance;
    private InProcessing account = new InProcessing();


    //@FXML private JFXButton btnSearch;
    //  @FXML private JFXTextField searchField;

    /* @FXML
     JFXComboBox<AccountSearchType> choiceFilter;*/
    @FXML
    private Pagination pagination;
    @FXML
    private AnchorPane anchorPane;
    //@FXML private JFXButton btnOK;
    //  @FXML private JFXButton btnCancel;

    public  InProcessing chosenAccount = null;
    public InProcessing  chosenElement = null;
    @FXML
    private InProcessingRequestController inProcessingRequestViewController;




   /* public AccountDictionary() {
        this.accountDAO = accountDAO;

        Stage primaryStage = new Stage();
        primaryStage.initModality( Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();

        loader.setRoot(this);
        loader.setController(this);

        Pane root = null;
        try {
            root = loader.load(getClass().getResource("/sample/ua/ucountry/Utils/DictionaryWindow/AccountDictionaryWindow.fxml").openStream());
        } catch (IOException e) {
            UsefulUtils.showErrorDialog("Неможливо відкрити вікно!");
            return;
        }

        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();

        Scene scene = new Scene(root, visualBounds.getHeight(), visualBounds.getWidth());

        scene.getStylesheets().add("sample/ua/ucountry/Utils/DictionaryWindow/CustomerDictionary/AccountDictionary.css");

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.showAndWait();
    }*/
   @FXML
public NotFulledController notFulledController;

    public InProcessingController() {

    }

    public void handleTableView(NotFulledController value) {
        notFulledController = value;
       refreshData();
    }
    public void setNotFulledController (MainPageController mainPageController){
        this.main = mainPageController;
    }
    public MainPageController getNotFulledController () {
        return main;
    }





    public void tableViewHandles() {
        tableProduct.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableProduct.setOnKeyReleased(eventKey -> fixSelectedRecord());
    }

    private void fixSelectedRecord() {
        InProcessing record = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        inProcessingRequestViewController.handleTableView(record);
    }

    public void tableViewHandlesAll() {
        tableviewAll.setOnMouseClicked(mouseEvent -> fixSelectedRecordAll());
        tableviewAll.setOnKeyReleased(eventKey -> fixSelectedRecordAll());
    }

    private void fixSelectedRecordAll() {
        InProcessing record = (InProcessing) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        inProcessingRequestViewController.handleTableView(record);
    }

    // public void showChat()throws Exception{

           /* try {
                // Load root layout from fxml file.
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(ChatClient.class.getResource("/sample/ChatTwoo/view/ClientGUI.fxml"));
                loader.setController(new ClientController());
                chatLayout = (VBox) loader.load();

                // Show the scene containing the root layout.
                Scene scene = new Scene(chatLayout);
                primaryStage.setScene(scene);
                primaryStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

     /*   Stage stage = new Stage();
        Scene scene =null;
        try {
            scene=new Scene( FXMLLoader.load(getClass().getResource( "/sample/ChatOrigin/client/src/main/resources/views/LoginView.fxml" )));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.show();*/
       /* try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChatClient.class.getResource("/sample/ChatTwoo/view/ClientGUI.fxml"));
            loader.setController(new ClientController());
            chatLayout = (VBox) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(chatLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/


   /* private void initChatLayout() {
        try {


            Stage stage = new Stage();

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));
            Parent root = fxml.load();


            Scene scene = new Scene(root);

            //scene.getStylesheets().add("sample/ua/ucountry/MainWindow/TabRepairNote/Edit/RepairNote/RepairNoteEdit.css");

            //stage.setMaxWidth(1020);
            //stage.setMinHeight(950);
            //stage.setMaxHeight(950);

            stage.setScene(scene);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
       /* primaryStageObj = primaryStage;
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("/sample/ChatOrigin/client/src.main/resources/views/LoginView.fxml"));
        primaryStage.initStyle(StageStyle.UNDECORATED);
       // primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResource("images/plug.png").toString()));
        Scene mainScene = new Scene(root);
        mainScene.setRoot(root);
        primaryStage.setResizable(false);
        primaryStage.setScene(mainScene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
    }
    public static Stage getPrimaryStage() {
        return primaryStageObj;
    }*/

  //  private ChatApp chat = new ChatApp();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        tableviewAll.getStylesheets().add
                (InProcessingController.class.getResource("/styles/TableStyle.css").toExternalForm());
     //   System.out.println(accountQueries.getMainInProcessing(false, 1,));
       // Visible window and buttons
        inProcessingRequestViewController.init(this);
        tbn_CreateRequest.setVisible(false);
        btn_changeRequest.setVisible(false);
        btn_ConfirmRequest.setVisible(false);
        btn_IntoProcessing.setVisible(false);
      //  btn_Server.setVisible(false);
       // btnDelete.setVisible(false);
     //   btn_СancelRequest.setVisible(false);

        // Window include



        // table product
        ;


        // Connection Database
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Button Update
        btn_UpdateStatus.setOnAction(event -> {
            try {
                chosenAccount = (InProcessing) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());
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
                  //  UsefulUtils.showInformationDialog("Запит в обробці " + User.getContactName());
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });


        // Buttons Confirmation
        btnConfirmation.setOnAction(event -> {
            try {
                chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
                System.out.println(chosenAccount + " 7777777777777777777777777777788888888888888888888888888888899999999999999999999999" + User.getContactID());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            // System.out.println(a +  b+c + "ed2wewgweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            if (UsefulUtils.showConfirmDialog("Ви підтверджуєте своє замовлення?") == ButtonType.OK) {

                String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET [StatusID] = '{3B552198-B239-4801-819C-7033AA118B65}',\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, String.valueOf(chosenAccount));
                    System.out.println(User.getContactID() + chosenAccount.getID() + "99999999999999999999999999888888888888888888888777777777777777");

                    pst.executeUpdate();
                    AddDate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит "+chosenAccount.getNumber()+" переведено в тракт");

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else return;
        });

        // Button Cancel
        btn_СancelRequest.setOnAction(event -> {

            try {

                chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());

            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте анулювати запит?") == ButtonType.OK) {
                try {
                    System.out.println("9+684949894898");
                    con = DBConnection.getDataSource().getConnection();
                    CallableStatement callProc = con.prepareCall("{call dbo.tsp_ROActionNullifyRequest (?,?)}");
                    System.out.println(chosenAccount.getID());
                    callProc.setString(1, chosenAccount.getID());
                    callProc.setString(2, User.getContactID());
                    System.out.println(chosenAccount.getID() + " 444444444444444444444444444444444555555555555555555555555555");
                    callProc.executeUpdate();


                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит "+chosenAccount.getNumber() +" анульовано");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });

         // Active Chat TableView
        tableProduct.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            //    fixSelectedRecord();
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
                   //     ClientController.record = chosenAccount;
                      //  showWindowChatTwoo();
                        NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);
                    //   ClientController clientController = new ClientController(chosenAccount, false);
                        loginButtonAction(chosenAccount);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });


        // Active Chat TableView all
        tableviewAll.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        chosenAccount = (InProcessing) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());
                    //    ClientController.record = chosenAccount;
                        NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);
                    /*   ClientController clientController = new ClientController(chosenAccount, false);*/
                        loginButtonAction(chosenAccount);
                        conn.messageBox.setEditable(false);
                     //   showScene();
                       // showWindowChatTwoo();


                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });
datePicker.setValue(LocalDate.now());
        // ToggleButtons TableView
        tableProduct.setVisible(false);
        datePicker.setVisible(false);
        dateLabel.setVisible(false);

        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle() != null) {
                tableProduct.setVisible(true);
                tableviewAll.setVisible(false);
                datePicker.setVisible(true);
                dateLabel.setVisible(true);
                btn_UpdateStatus.setVisible(false);

            } else if (tableviewAll != null) {
                tableviewAll.setVisible(true);
                tableProduct.setVisible(false);
                datePicker.setVisible(false);
                dateLabel.setVisible(false);
                btn_UpdateStatus.setVisible(true);

            }
        });
       // datePicker.getStyleClass().add(DEFAULT_STYLE_CLASS);
        //Create Columns
        createTableColumns();
        createTableColumnsAll();

        tableProduct.getSelectionModel().setCellSelectionEnabled(false);
        tableProduct.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableProduct);
        tableviewAll.getSelectionModel().setCellSelectionEnabled(false);
        tableviewAll.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableviewAll);

        tableViewHandles();
        tableViewHandlesAll();
       // fixSelectedRecord();

        //Create Paginations
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
        pagination.setSkin(pageSkin);
        pagination.setPageFactory(this::createPage);
        pagination.setPageFactory(this::createPageAll);

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
searchCode();
searchCodeAll();
btnDelete.setOnAction(event -> {
    try {
        selectedOffering = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());

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
        while (numberOfSquares > 0){
          //  generateAnimation();
            numberOfSquares--;
        }

        //TableColumn select = new TableColumn("CheckBox");

        tableProduct.setRowFactory(new Callback<TableView<InProcessing>, TableRow<InProcessing>>() {
            @Override
            public TableRow<InProcessing> call(TableView<InProcessing> param) {
                return new TableRow<InProcessing>() {
                    @Override
                    protected void updateItem(InProcessing item, boolean empty) {
                        super.updateItem(item, empty);
                        for(int i= 0; i< data.size(); i++){
                            InProcessing isRead = data.get(i);
                            Integer isReadName = isRead.getIsReadMessage();

                        if (isReadName == null || isReadName.equals(0)) {
                            setStyle("");
                        } else {
                            //  Boolean res=item.getIsReadMessage();
                            // if (res.startsWith(" ")) {
                            //    setStyle("");
                            //  } else
                            setStyle("-fx-font-weight: bold");
                        }

                        }
                    }


                };
            }
        });

    }


    public void AddDate () {

        Date date = Date.valueOf(datePicker.getValue());
        System.out.println(date + "datePostavki");
        String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
                "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
        pst = null;


            try {
                pst = DBConnection.getDataSource().getConnection().prepareStatement(query);

                pst.setString(1, com.login.User.getContactID());
                pst.setString(2, columnMessage.getRecipientID());
                pst.setString(3, chosenAccount.getID());
                pst.setString(4, "Дата поставки: "+ date);
                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }

    }
      public void showWindowChatTwoo() {
        Stage stage = new Stage();

      //    String hostname = hostnameTextfield.getText();
          int port = 9001;
          String username = User.getContactName();
          String picture = User.getContactName();

        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(ClientController.class.getResource("/views/ChatView.fxml"));
            //AnchorPane root = (AnchorPane)loader.load(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml").openStream());
           // Parent root = loader.load(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));

          //  System.out.println("00000000000");
            Pane pane = loader.load();
        //    ClientController con = loader.getController();
         ///   con.setOfferingRequest(chosenAccount, true);
            conn = loader.<ChatController>getController();
         //   Listener listener = new Listener(hostname, port, username, picture, conn);
          // Thread x = new Thread(listener);
         //   x.start();
            Scene scene = new Scene(pane);

            stage.setScene(scene);

            stage.show();

//        stage.initModality(Modality.WINDOW_MODAL);
            stage.requestFocus();
        } catch (Exception e) {
          UsefulUtils.showErrorDialog(e.getMessage());

        }
    }




  /*  public void InProcessingController() {
        instance = this;
    }*/

   /* public static InProcessingController getInstance() {
        return instance;
    }*/
    public void loginButtonAction(InProcessing chosenAccount) throws IOException {
        String hostname = "192.168.0.102";
        int port = 9001;
        String username = User.getContactName();
        String picture = "Default";


        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));

        Parent window = (Pane) fmxlLoader.load();
    conn = fmxlLoader.<ChatController>getController();
    //  instance = fmxlLoader.<InProcessingController>getController();
        conn.setOfferingRequest(chosenAccount);
        conn.setInProcessingController(this);
   Listener listener = new Listener(hostname, port, username, picture, conn, this, chosenAccount);
    Thread x = new Thread(listener);
            x.start();

           this.scene = new Scene(window);


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
            conn.setIdTextFild(chosenAccount.getID());
            stage.showAndWait();
        });
    }

    public void showErrorDialog(String message) {
        Platform.runLater(()-> {
            TrayNotification tray = new TrayNotification();
            tray.setNotificationType(NotificationType.ERROR);
            tray.setTitle("Помилка");
            String s = message;
            tray.setMessage(s);
            tray.setAnimationType(AnimationType.SLIDE);
            tray.showAndDismiss(Duration.millis(400));
        });

    }

  /*  public void showWindowChatTwoo() {
        Stage stage = new Stage();


        try {
            chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
            System.out.println(chosenAccount + "77777777777788888888889999999999999");
        } catch (Exception e) {
            UsefulUtils.showErrorDialog("Exception");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(ClientController.class.getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));
            //AnchorPane root = (AnchorPane)loader.load(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml").openStream());
           // Parent root = loader.load(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));

          //  System.out.println("00000000000");
            Pane pane = loader.load();
            ClientController con = loader.getController();
            con.setOfferingRequest(chosenAccount, true);
            Scene scene = new Scene(pane);

            stage.setScene(scene);

            stage.show();

//        stage.initModality(Modality.WINDOW_MODAL);
            stage.requestFocus();
        } catch (Exception e) {
          UsefulUtils.showErrorDialog(e.getMessage());

        }
    }*/

   /* public void showWindowChatOne() {
       try {
           try {
               chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
               System.out.println(chosenAccount + "11111111111111111222222222222222333333333333");
           } catch (Exception e) {
               UsefulUtils.showErrorDialog("Exception");
               return;
           }
            Stage primaryStage = new Stage();
            //  primaryStage.initModality(Modality.APPLICATION_MODAL);
            //FXMLLoader fxml = new FXMLLoader(getClass().getResource("/ClientGUI.fxml"));

            //Pane root = fxml.load();

            FXMLLoader loader = new FXMLLoader();


            Pane root = loader.load(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));


            Scene scene = new Scene(root);

            // scene.getStylesheets().add("sample/ua/ucountry/MainWindow/TabRepairNote/Edit/OfferingInOrderActSTO/OfferingInOrderActSTOEdit.css");
            ClientController con = loader.getController();
            con.setOfferingRequest(chosenAccount, true);

            // con.initializeToo();
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
            UsefulUtils.showErrorDialog("Неможливо відкрити вікно чату!");
            throw new RuntimeException(e);
        }}*/
      /*  Stage stage = new Stage();
        InProcessing record = null;
        try {
            record = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            UsefulUtils.showErrorDialog("Exception");
            return;
        }
        try {

            FXMLLoader loader = new FXMLLoader();


              //  ((Node)event.getSource()).getScene().getWindow().hide();
                //Pane root = loader.load(getClass().getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));
            loader.setLocation(ClientController.class.getResource("/sample/ChatTwoo/controller/ClientGUI.fxml"));
          //  AnchorPane pane = loader.load();

                Scene scene = new Scene(loader.load());
            ClientController con = loader.getController();
            con.setOfferingRequest(record);
        stage.setScene(scene);

      //  stage.setMaxHeight(420);
       // stage.setMaxWidth(620);
        stage.show();
        stage.requestFocus();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

   /*  public void showWindowChat() {
         try {
             try {
                 chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
                 System.out.println(chosenAccount + "11111111111111111222222222222222333333333333");
             } catch (Exception e) {
                 UsefulUtils.showErrorDialog("Exception");
                 return;
             }
             // System.out.println("22222222222222 " + offeringRequest);
             Stage primaryStage = new Stage();
             //primaryStage.initModality(Modality.APPLICATION_MODAL);

            // FXMLLoader loader = new FXMLLoader();


             //loader.setRoot(this);
             //loader.setController(this);

             FXMLLoader fxml = new FXMLLoader(getClass().getResource("/sample/ChatTwoo/view/ClientGUI.fxml"));

             Parent root = fxml.load();


             Scene scene = new Scene(root);
             ClientController con = fxml.getController();
             con.setOfferingRequest(chosenAccount, true);

             primaryStage.setScene(scene);
             primaryStage.show();
         } catch (Exception e) {
             log.log(Level.SEVERE, "Exception: " + e);
             UsefulUtils.showErrorDialog("Неможливо відкрити вікно чату!");
             throw new RuntimeException(e);
         }
     }*/
   /* private void handleTableView() {
        main.setHandler((InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex()));
    }*/
    private void setOfferingRequest(InProcessing chosenAccount) {
        this.chosenAccount = chosenAccount;
    }

    @Override
    public void createTableColumns() {
        //customAccountFieldLabels();
        try {
            TableColumn<InProcessing, String> number = new TableColumn<InProcessing, String>("Номер запиту");
            TableColumn<InProcessing, String> createdOn = new TableColumn<InProcessing, String>("Дата");
            TableColumn<InProcessing, String> createdBy = new TableColumn<InProcessing, String>("Створив");
            TableColumn<InProcessing, String> accountCode = new TableColumn<InProcessing, String>("Код");
            TableColumn<InProcessing, String> accountName = new TableColumn<InProcessing, String>("Контрагент");
            TableColumn<InProcessing, String> accountSaldo = new TableColumn<InProcessing, String>("Сальдо");
            TableColumn<InProcessing, String> accountIsSolid = new TableColumn<InProcessing, String>("Солідність");
            TableColumn<InProcessing, String> storeCity = new TableColumn<InProcessing, String>("Місто поставки");
            TableColumn<InProcessing, String> status = new TableColumn<InProcessing, String>("Статус");
            TableColumn<InProcessing, String> offeringGroupName = new TableColumn<InProcessing, String>("Група товарів");
            TableColumn<InProcessing, String> originalGroupName = new TableColumn<InProcessing, String>("Початкова група");
            TableColumn<InProcessing, String> groupChangedBy = new TableColumn<InProcessing, String>("Змінив групу");
            TableColumn<InProcessing, String> stateName = new TableColumn<InProcessing, String>("Статус товару");
            TableColumn<InProcessing, String> specialMarginTypeName = new TableColumn<InProcessing, String>("Тип спец націнки");
            TableColumn<InProcessing, Integer> isReadMeassage = new TableColumn<InProcessing, Integer>("повіщення");


            tableProduct.setTableMenuButtonVisible(true);
            number.setMinWidth(150);

            //    id.setVisible(false);

            tableProduct.getColumns().addAll(
                    number,
                    createdOn,
                    createdBy,
                    accountCode,
                    accountName,
                    accountSaldo,
                    accountIsSolid,
                    storeCity,
                    status,
                    offeringGroupName,
                    originalGroupName,
                    groupChangedBy,
                    stateName,
                    specialMarginTypeName,
                    isReadMeassage
            );
            number.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("Number"));
            createdOn.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("CreatedOn"));
            createdBy.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("CreatedBy"));
            accountCode.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountCode"));
            accountName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountName"));
            accountSaldo.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountSaldo"));
            accountIsSolid.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountIsSolid"));
            storeCity.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("StoreCity"));
            status.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("Status"));
            offeringGroupName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("OfferingGroupName"));
            originalGroupName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("OriginalGroupName"));
            groupChangedBy.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("GroupChangedBy"));
            stateName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("StateName"));
            specialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("SpecialMarginTypeName"));
            isReadMeassage.setCellValueFactory(new PropertyValueFactory<InProcessing, Integer>("isReadMeassage"));
 // isReadMessage.setMinWidth(200);
         /*   isReadMessage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<InProcessing, CheckBox>, ObservableValue<CheckBox>>() {

                @Override
                public ObservableValue<CheckBox> call(
                        TableColumn.CellDataFeatures<InProcessing, CheckBox> arg0) {
                    InProcessing user = arg0.getValue();

                    CheckBox checkBox = new CheckBox();

                    checkBox.selectedProperty().setValue(user.getIsReadMessage());



                    checkBox.selectedProperty().addListener(new ChangeListener<String>() {
                        public void changed(ObservableValue<? extends Boolean> ov,
                                            String old_val, Boolean new_val) {

                            user.setIsReadMessage(new_val);

                        }
                    });

                    return new SimpleObjectProperty<CheckBox>(checkBox);

                }

            });
            tableProduct.getColumns().addAll( isReadMessage);
*/
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }

    @Override
    public void createTableColumnsAll() {
        //customAccountFieldLabels();
        try {
            TableColumn<InProcessing, String> number = new TableColumn<InProcessing, String>("Номер запиту");
            TableColumn<InProcessing, String> createdOn = new TableColumn<InProcessing, String>("Дата");
            TableColumn<InProcessing, String> createdBy = new TableColumn<InProcessing, String>("Створив");
            TableColumn<InProcessing, String> accountCode = new TableColumn<InProcessing, String>("Код");
            TableColumn<InProcessing, String> accountName = new TableColumn<InProcessing, String>("Контрагент");
            TableColumn<InProcessing, String> accountSaldo = new TableColumn<InProcessing, String>("Сальдо");
            TableColumn<InProcessing, String> accountIsSolid = new TableColumn<InProcessing, String>("Солідність");
            TableColumn<InProcessing, String> storeCity = new TableColumn<InProcessing, String>("Місто поставки");
            TableColumn<InProcessing, String> status = new TableColumn<InProcessing, String>("Статус");
            TableColumn<InProcessing, String> offeringGroupName = new TableColumn<InProcessing, String>("Група товарів");
            TableColumn<InProcessing, String> originalGroupName = new TableColumn<InProcessing, String>("Початкова група");
            TableColumn<InProcessing, String> groupChangedBy = new TableColumn<InProcessing, String>("Змінив групу");
            TableColumn<InProcessing, String> stateName = new TableColumn<InProcessing, String>("Статус товару");
            TableColumn<InProcessing, String> specialMarginTypeName = new TableColumn<InProcessing, String>("Тип спец націнки");


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
                    offeringGroupName,
                    originalGroupName,
                    groupChangedBy,
                    stateName,
                    specialMarginTypeName);
            number.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("Number"));
            createdOn.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("CreatedOn"));
            createdBy.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("CreatedBy"));
            accountCode.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountCode"));
            accountName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountName"));
            accountSaldo.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountSaldo"));
            accountIsSolid.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("AccountIsSolid"));
            storeCity.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("StoreCity"));
            status.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("Status"));
            offeringGroupName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("OfferingGroupName"));
            originalGroupName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("OriginalGroupName"));
            groupChangedBy.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("GroupChangedBy"));
            stateName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("StateName"));
            specialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InProcessing, String>("SpecialMarginTypeName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }


    @Override
    public List<InProcessing> loadDataFromDatabase() {

        try {
          //  chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
           // System.out.println(chosenAccount.getCreatedByID() + "778897979"+chosenAccount.getOfferingGroupID());
            List<InProcessing> listItems = account.findAllOne(true, (int) toIndex, User.getContactID(), User.getContactID());

            listItems.forEach(item -> data.add(item));

            tableProduct.setItems(data);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        return null;
    }

    @Override
    public List<InProcessing> loadDataFromDatabaseAll() {

        try {
            List<InProcessing> listItems = account.findAllInProcessing(true, (int) toIndex);

            listItems.forEach(item -> dataAll.add(item));

            tableviewAll.setItems(dataAll);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        return null;
    }


    public AnchorPane createPageAll(int pageIndex) {
        try {


            dataAll = FXCollections.observableArrayList();
            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainInProcessingCount());


            loadDataFromDatabaseAll();


            tableviewAll.setItems(FXCollections.observableArrayList(dataAll.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return anchorPane;
    }

    public AnchorPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();
            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainInProcessingCount());


            loadDataFromDatabase();


            tableProduct.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return anchorPane;
    }

    public static void init(MainPageController mainPageController) {
        main = mainPageController;
    }

    private Stage primaryStage;
    private VBox serverLayout;


   /* public void showWindow() {
        try {
         /*   try {
                chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
                System.out.println(chosenAccount + "11111111111111111222222222222222333333333333");
            } catch (Exception e) {
                UsefulUtils.showErrorDialog("Exception");
                return;
            }*/
           /* Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ChatClient.class.getResource("/views/ServerGUI.fxml"));
            ServerController serverController = new ServerController();
            loader.setController(serverController);
            Pane serverrLayout = loader.load();

            // Show the scene containing the root layout.
            System.out.println("window1");
            Scene scene = new Scene(serverrLayout);
         //   ServerController con = loader.getController();
          //  con.setOfferingRequest(chosenAccount, true);
            System.out.println("window2");
            stage.setScene(scene);
            System.out.println("window3");

            stage.show();
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    // We need to eliminate the Server Threads
                    // If the User decides to close it.
                    if (serverController.server != null) {
                        serverController.server.stop();
                        serverController.server = null;
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

     /*   try{
        System.out.println("22222222222222");
        Stage primaryStage = new Stage();
        //primaryStage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();

        //loader.setRoot(this);
        //loader.setController(this);

        FXMLLoader fxml = new FXMLLoader(getClass().getResource("/sample/ChatTwoo/view/ServerGUI.fxml"));
            System.out.println("window");
        Parent root = fxml.load();


        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }*/
    //}


    @FXML
    private void hadleServerButton(ActionEvent event) {

    }




    public void refreshData() {
        data.clear();
        dataAll.clear();
        loadDataFromDatabase();
        loadDataFromDatabaseAll();

        UsefulUtils.fadeTransition(tableviewAll);
        UsefulUtils.fadeTransition(tableProduct);
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
            sortedData.comparatorProperty().bind(tableProduct.comparatorProperty());
            tableProduct.setItems(sortedData);

        });
    }

    private void searchCodeAll() {
        FilteredList<InProcessing> filteredData = new FilteredList<>(dataAll, e -> true);
        searchingField.setOnKeyReleased(e -> {
            searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super InProcessing>) user -> {
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
            sortedData.comparatorProperty().bind(tableviewAll.comparatorProperty());
            tableviewAll.setItems(sortedData);

        });
    }


    @FXML
    public void handleDelete(ActionEvent e) {

        try {
            selectedOffering = (InProcessing) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());
            selectedOffering = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());

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
    private void btnUpdateOfferingGroupName (ActionEvent e){

        chosenAccount = (InProcessing) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());

if(chosenAccount != null){
        try {
            chosenAccount = (InProcessing) tableviewAll.getItems().get(tableviewAll.getSelectionModel().getSelectedIndex());
        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
} else {
        try {
            chosenAccount = (InProcessing) tableProduct.getItems().get(tableProduct.getSelectionModel().getSelectedIndex());
        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }}
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
       // main.changeExists();
        stage.show();
        stage.requestFocus();
       // refreshData();

    }



    @Override
    public void update() {
        refreshData();
    }
}
