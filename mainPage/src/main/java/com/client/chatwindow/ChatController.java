package com.client.chatwindow;

import com.ChatTwoo.controller.ColumnMessage;
import com.Utils.UsefulUtils;
import com.client.util.VoiceRecorder;
import com.client.util.VoiceUtil;
import com.connectDatabase.DBConnection;
import com.mainPage.All.All;
import com.mainPage.All.AllController;
import com.mainPage.ArchiveFiles.ArchiveFiles;
import com.mainPage.ArchiveFiles.ArchiveFilesController;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTract;
import com.mainPage.InTract.InTractController;
import com.mainPage.page.MainPageController;
import com.messages.Message;
import com.messages.User;
import com.traynotifications.animations.AnimationType;
import com.traynotifications.notification.TrayNotification;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ChatController extends ClientModelInt implements Initializable {

    @FXML
    public TextArea messageBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private VBox chatPane;
    @FXML
    private Button btnEmoji;
    @FXML
    private TextFlow emojiList;
    @FXML
    private AnchorPane titleBar;
    @FXML
    public Button buttonSendTract;
    @FXML
    public Button buttonSend;
    @FXML
    ScrollPane scrollPane;
    @FXML
    private BorderPane rootPane;
    @FXML
    private AnchorPane chatPaneGeneral;
    @FXML
    private Button saveButton;


    ImageView userImageView = new ImageView();
    Listener listener = null;

    private PreparedStatement pst = null;
    private Connection connection = null;
    private ResultSet rs = null;
    private ObservableList<HBox> dataMessage;
    private ColumnMessage columnMessage = new ColumnMessage();
    public InProcessing offeringRequest;
    public InProcessingController inProcessingController;

    public ArchiveFiles offeringArchive;
    public All offeringAll;
    public ArchiveFilesController archiveFilesController;
    public AllController allController;
    public InTractController inTractController;
    public InTract offeringTract;
    public User user;

    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.ENTER,
            KeyCombination.CONTROL_DOWN);
    final KeyCombination keyComb2 = new KeyCodeCombination(KeyCode.ENTER,
            KeyCombination.ALT_DOWN);
    public ArrayList<Message> usersID = new ArrayList<>();
    public ArrayList<User> users = new ArrayList<>();

    private Image microphoneActiveImage = new Image(getClass().getClassLoader().getResource("images/microphone-active.png").toString());
    private Image microphoneInactiveImage = new Image(getClass().getClassLoader().getResource("images/microphone.png").toString());
    private Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());

    private double xOffset;
    private double yOffset;
    //MainPageController mainPageController = new MainPageController();
    public MainPageController mainPageController;

    // Logger logger = LoggerFactory.getLogger(ChatController.class);

    public void setOfferingRequest(InProcessing inProcessing) {
        offeringRequest = inProcessing;
    }

    public void setOfferingTract(InTract inTract) {
        offeringTract = inTract;
    }

    public InTract getOfferingTract() {
        return offeringTract;
    }

    public InProcessing getOfferingRequest() {
        return offeringRequest;
    }

    public void setOfferingArchive(ArchiveFiles archive) {
        offeringArchive = archive;
    }

    public ArchiveFiles getOfferingArchive() {
        return offeringArchive;
    }

    public void setOfferingAll(All all) {
        offeringAll = all;
    }

    public All getOfferingAll() {
        return offeringAll;
    }

    public void init(InProcessingController inProcessingController) {
        this.inProcessingController = inProcessingController;
        // try {
        //     loadDataFromDataBase();
        // } catch (SQLException e) {
        //      e.printStackTrace();
        //  }
    }

    public void init(InTractController inTractController) {
        this.inTractController = inTractController;

    }

    public void init(ArchiveFilesController archiveFilesController) {
        this.archiveFilesController = archiveFilesController;

    }

    public void init(AllController allController) {
        this.allController = allController;
    }


    /*   public void setInProcessingController(InProcessingController inProcessingController) {
           this.inProcessingController = inProcessingController;
           // txtTitle.setText(offeringRequest.getAccountName());
           //  txtNumber.setText(offeringRequest.getNumber());


           //   user.setOfferingID(String.valueOf(offeringRequest));
       }
   */
    public void setMainPageController(MainPageController controller) {

        mainPageController = controller;
    }

    public MainPageController getMainPageController() {
        return mainPageController;
    }


    public void messageSendEnter() {

    }

    public void setInTract(InTractController inTractController) {
        this.inTractController = inTractController;
        try {
            loadDataFromDataBaseInTract();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setArchiveController(ArchiveFilesController archiveFilesController) {
        this.archiveFilesController = archiveFilesController;
        buttonSend.setVisible(false);
        buttonSendTract.setVisible(false);
        messageBox.setVisible(false);
        chatPaneGeneral.setVisible(false);
        btnEmoji.setVisible(false);
        try {
            loadDataFromDataBaseArchive();
        } catch (SQLException e) {
            e.printStackTrace();
        };
    }

    public void setAllController(AllController all) {
        this.allController = all;
        messageBox.setVisible(false);
        buttonSend.setVisible(false);
        buttonSendTract.setVisible(false);
        chatPaneGeneral.setVisible(false);
        btnEmoji.setVisible(false);

        try {
            loadDataFromDataBaseAll();
       } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public InProcessingController getInprocessingController() {
        return inProcessingController;
    }

    public InProcessingController getInProcessingController() {
        return inProcessingController;
    }

    @FXML
    public void sendButtonAction() throws IOException {
        if(messageBox.getText().trim().equals(""))return;
        String msg = messageBox.getText();


            try {

                try {
                    Listener.send(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
                        "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
                pst = null;


                pst = DBConnection.getDataSource().getConnection().prepareStatement(query);
                pst.setString(1, com.login.User.getContactID());
                pst.setString(2, columnMessage.getRecipientID());
                pst.setString(3, offeringRequest.getID());
                pst.setString(4, msg);
                pst.execute();
                String queryUpdate = "UPDATE tbl_RequestOffering \n" +
                        "\t\t\t\t\tSET \n" +
                        "\t\t\t\t\tIsReadMeassage = '1'\n" +
                        "\t\t\t\t\tWHERE ID = ?";
                pst = DBConnection.getDataSource().getConnection().prepareStatement(queryUpdate);
                pst.setString(1, offeringRequest.getID());
                pst.executeUpdate();
                loadDataFromDataBase();
                //   inProcessingController.refreshData();
            } catch (SQLException e) {
                UsefulUtils.showErrorDialogDown("Сервер не підключено!!!");
                DBConnection database = new DBConnection();
                database.reconnect();


            }


Listener.sendNotification(msg);
        messageBox.clear();
            messageBox.requestFocus();


    }


    @FXML
    public void sendButtonActionTract() throws IOException {
        if(messageBox.getText().trim().equals(""))return;
        String msg = messageBox.getText();

            ListinerTract.send(msg);
            String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
                    "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
            pst = null;

            try {
                connection = DBConnection.getDataSource().getConnection();
                pst = connection.prepareStatement(query);
                pst.setString(1, com.login.User.getContactID());
                pst.setString(2, columnMessage.getRecipientID());
                pst.setString(3, offeringTract.getID());
                pst.setString(4, msg);
                pst.execute();
                String queryUpdate = "UPDATE tbl_RequestOffering \n" +
                        "\t\t\t\t\tSET \n" +
                        "\t\t\t\t\tIsReadMeassage = '1'\n" +
                        "\t\t\t\t\tWHERE ID = ?";
                pst = DBConnection.getDataSource().getConnection().prepareStatement(queryUpdate);
                pst.setString(1, offeringTract.getID());
                pst.executeUpdate();
                loadDataFromDataBaseInTract();
               //    inTractController.refreshData();
            } catch (SQLException e) {
                UsefulUtils.showErrorDialogDown("Сервер не підключено!!!");
            }


        messageBox.clear();
            messageBox.requestFocus();

    }


    public void sendButtonActionOne() throws IOException {

        String msg = messageBox.getText();

        if (!messageBox.getText().isEmpty()) {
            try {

                try {
                    Listener.send(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
                        "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
                pst = null;


                pst = DBConnection.getDataSource().getConnection().prepareStatement(query);
                pst.setString(1, com.login.User.getContactID());
                pst.setString(2, columnMessage.getRecipientID());
                pst.setString(3, offeringRequest.getID());
                pst.setString(4, msg);
                pst.execute();
                String queryUpdate = "UPDATE tbl_RequestOffering \n" +
                        "\t\t\t\t\tSET \n" +
                        "\t\t\t\t\tIsReadMeassage = '1'\n" +
                        "\t\t\t\t\tWHERE ID = ?";
                pst = DBConnection.getDataSource().getConnection().prepareStatement(queryUpdate);
                pst.setString(1, offeringRequest.getID());
                pst.executeUpdate();
                loadDataFromDataBase();
                //   inProcessingController.refreshData();
            } catch (SQLException e) {
                UsefulUtils.showErrorDialogDown("Сервер не підключено!!!");
            }


        }
        messageBox.clear();



         /*   Platform.runLater(() -> {
             //   Image profileImg = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture().toLowerCase() +".png").toString(),50,50,false,false);
                TrayNotification tray = new TrayNotification();
                tray.setTitle("A new user has joined!");
                tray.setMessage(//msg.getNamke() +
                        " has joined the JavaFX Chatroom!");
                tray.setRectangleFill(Paint.valueOf("#2C3E50"));
                tray.setAnimationType(AnimationType.POPUP);
          //      tray.setImage(profileImg);
                tray.showAndDismiss(Duration.seconds(5));
                try {
                    Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
                    mediaPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });*/

    }


    public void recordVoiceMessage() throws IOException {
        if (VoiceUtil.isRecording()) {
            Platform.runLater(() -> {
                      //  microphoneImageView.setImage(microphoneInactiveImage);
                    }
            );
            VoiceUtil.setRecording(false);
        } else {
            Platform.runLater(() -> {
                        //microphoneImageView.setImage(microphoneActiveImage);

                    }
            );
            VoiceRecorder.captureAudio();
        }
    }

    private SimpleDateFormat sdf;

    public void loadDataFromDataBase() throws SQLException {
        chatPane.getChildren().size();
        chatPane.getChildren().clear();

        try {
            pst = connection.prepareStatement("SELECT\n" +
                    "\t[tbl_MessageInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[Sender].[Name] AS [Sender],\n" +
                    "\t[tbl_MessageInRequestOffering].[RecipientID] AS [RecipientID],\n" +
                    "\t[tbl_MessageInRequestOffering].[Message] AS [Message],\n" +
                    "\t[tbl_MessageInRequestOffering].[RequestID] AS [RequestID]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_MessageInRequestOffering] AS [tbl_MessageInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Contact] AS [Sender] ON [Sender].[ID] = [tbl_MessageInRequestOffering].[CreatedByID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_MessageInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_MessageInRequestOffering].[RequestID] = ?)\n" +
                    "ORDER BY\n" +
                    "\t2 ASC");
            pst.setString(1, offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                String date = rs.getString(2);
                String user = rs.getString(4);
                String message = rs.getString(6);
                date = date.substring(0,19);

                if (user.equals(com.login.User.getContactName())) {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                  //  b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b19.setFont(Font.font(null, 16));
                    b18.setAlignment(Pos.CENTER_LEFT);
                 //   b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");

                //    b19.getStylesheets().add("styles/TextArea.css");
                  //  b19.setWrapText(true);



                    b19.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            // your algorithm to change height
                         //   b19.setPrefHeight(b19.getPrefHeight()+28);
                        }
                    });

                    VBox hBox = new VBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setStyle("-fx-background-color: rgba(188,143,143,0.46);" +
                           "    -fx-background-radius: 15, 8;\n" +
                           "    -fx-background-insets: 1, 0;\n" +
                            "    -fx-text-fill: #000000;");
                   textFlow.getChildren().addAll( b19);
                  //  hBox.setAlignment(Pos.CENTER);
                    hBox.setPadding(new Insets(10));

                    // Set Hgrow for TextField
                    HBox.setHgrow(b19, Priority.ALWAYS);
                  //  hBox.autosize();
                //    hBox.setHgrow(textFlow,  Priority.ALWAYS);
                    hBox.getChildren().addAll(profileImage, b19);


                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                } else {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                   // b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setFont(Font.font(null, 16));
                    b18.getTypeSelector();
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setStyle(
                        "-fx-background-insets: 0;" +
                        "-fx-background-color: transparent, white, transparent, white;" +
                        "-fx-background-radius: 0, 0, 0, 0;"+
                                "-fx-box-border: none;" +
                        "-fx-focus-color: -fx-control-inner-background;" +
                        "-fx-faint-focus-color: -fx-control-inner-background;" +
                        "-fx-text-box-border: -fx-control-inner-background;" +
                        "-fx-border-width: -1;");
                    HBox hBox = new HBox();
                    hBox.setStyle("-fx-background-color: rgba(0,191,255,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 0, 1;\n" +
                            "    -fx-text-fill: #000000;"
                    );
                    textFlow.getChildren().addAll( b19);
                    hBox.setPrefWidth(Region.USE_PREF_SIZE);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }
    }
    @FXML
    void emojiAction(ActionEvent event) {
        if(emojiList.isVisible()){

            emojiList.setVisible(false);
        }else {
            emojiList.setVisible(true);
        }
    }

    private void loadDataFromDataBaseInTract() throws SQLException {
        chatPane.getChildren().size();
        chatPane.getChildren().clear();

        try {
            pst = connection.prepareStatement("SELECT\n" +
                    "\t[tbl_MessageInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[Sender].[Name] AS [Sender],\n" +
                    "\t[tbl_MessageInRequestOffering].[RecipientID] AS [RecipientID],\n" +
                    "\t[tbl_MessageInRequestOffering].[Message] AS [Message],\n" +
                    "\t[tbl_MessageInRequestOffering].[RequestID] AS [RequestID]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_MessageInRequestOffering] AS [tbl_MessageInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Contact] AS [Sender] ON [Sender].[ID] = [tbl_MessageInRequestOffering].[CreatedByID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_MessageInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_MessageInRequestOffering].[RequestID] = ?)\n" +
                    "ORDER BY\n" +
                    "\t2 ASC");
            pst.setString(1, offeringTract.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                String date = rs.getString(2);
                String user = rs.getString(4);
                String message = rs.getString(6);
                date = date.substring(0,19);

                if (user.equals(com.login.User.getContactName())) {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                   // b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setFont(Font.font(null, 14));
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setStyle("-fx-background-color: rgba(188,143,143,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 1, 0;\n" +
                            "    -fx-text-fill: #000000;");
                    textFlow.getChildren().addAll( b19);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                } else {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                 //   b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setFont(Font.font(null, 14));
                    b18.getTypeSelector();
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setStyle(
                            "-fx-background-insets: 0;" +
                                    "-fx-background-color: transparent, white, transparent, white;" +
                                    "-fx-background-radius: 0, 0, 0, 0;"+
                                    "-fx-box-border: none;" +
                                    "-fx-focus-color: -fx-control-inner-background;" +
                                    "-fx-faint-focus-color: -fx-control-inner-background;" +
                                    "-fx-text-box-border: -fx-control-inner-background;" +
                                    "-fx-border-width: -1;");
                    HBox hBox = new HBox();
                    hBox.setStyle("-fx-background-color: rgba(0,191,255,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 0, 1;\n" +
                            "    -fx-text-fill: #000000;"
                    );
                    textFlow.getChildren().addAll( b19);
                    hBox.setPrefWidth(Region.USE_PREF_SIZE);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    private void loadDataFromDataBaseArchive() throws SQLException {
        chatPane.getChildren().size();
        chatPane.getChildren().clear();

        try {
            pst = connection.prepareStatement("SELECT\n" +
                    "\t[tbl_MessageInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[Sender].[Name] AS [Sender],\n" +
                    "\t[tbl_MessageInRequestOffering].[RecipientID] AS [RecipientID],\n" +
                    "\t[tbl_MessageInRequestOffering].[Message] AS [Message],\n" +
                    "\t[tbl_MessageInRequestOffering].[RequestID] AS [RequestID]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_MessageInRequestOffering] AS [tbl_MessageInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Contact] AS [Sender] ON [Sender].[ID] = [tbl_MessageInRequestOffering].[CreatedByID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_MessageInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_MessageInRequestOffering].[RequestID] = ?)\n" +
                    "ORDER BY\n" +
                    "\t2 ASC");
            pst.setString(1, offeringArchive.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                String date = rs.getString(2);
                String user = rs.getString(4);
                String message = rs.getString(6);
                date = date.substring(0,19);

                if (user.equals(com.login.User.getContactName())) {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                  //  b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setFont(Font.font(null, 14));
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setStyle("-fx-background-color: rgba(188,143,143,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 1, 0;\n" +
                            "    -fx-text-fill: #000000;");
                    textFlow.getChildren().addAll( b19);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                } else {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                  //  b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setFont(Font.font(null, 14));
                    b18.getTypeSelector();
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setStyle(
                            "-fx-background-insets: 0;" +
                                    "-fx-background-color: transparent, white, transparent, white;" +
                                    "-fx-background-radius: 0, 0, 0, 0;"+
                                    "-fx-box-border: none;" +
                                    "-fx-focus-color: -fx-control-inner-background;" +
                                    "-fx-faint-focus-color: -fx-control-inner-background;" +
                                    "-fx-text-box-border: -fx-control-inner-background;" +
                                    "-fx-border-width: -1;");
                    HBox hBox = new HBox();
                    hBox.setStyle("-fx-background-color: rgba(0,191,255,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 0, 1;\n" +
                            "    -fx-text-fill: #000000;"
                    );
                    textFlow.getChildren().addAll( b19);
                    hBox.setPrefWidth(Region.USE_PREF_SIZE);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    private void loadDataFromDataBaseAll() throws SQLException {
        chatPane.getChildren().size();
        chatPane.getChildren().clear();

        try {
            pst = connection.prepareStatement("SELECT\n" +
                    "\t[tbl_MessageInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_MessageInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[Sender].[Name] AS [Sender],\n" +
                    "\t[tbl_MessageInRequestOffering].[RecipientID] AS [RecipientID],\n" +
                    "\t[tbl_MessageInRequestOffering].[Message] AS [Message],\n" +
                    "\t[tbl_MessageInRequestOffering].[RequestID] AS [RequestID]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_MessageInRequestOffering] AS [tbl_MessageInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Contact] AS [Sender] ON [Sender].[ID] = [tbl_MessageInRequestOffering].[CreatedByID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_MessageInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_MessageInRequestOffering].[RequestID] = ?)\n" +
                    "ORDER BY\n" +
                    "\t2 ASC");
            pst.setString(1, offeringAll.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                String date = rs.getString(2);
                String user = rs.getString(4);
                String message = rs.getString(6);
                date = date.substring(0,19);

                if (user.equals(com.login.User.getContactName())) {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                   // b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setFont(Font.font(null, 14));
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setStyle("-fx-background-color: rgba(188,143,143,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 1, 0;\n" +
                            "    -fx-text-fill: #000000;");
                    textFlow.getChildren().addAll( b19);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                } else {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextFlow textFlow = new TextFlow();
                    Text b19 = new Text();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                   // b19.setEditable(false);
                    b18.setText(date + " " + user + ":" );
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setFont(Font.font(null, 14));
                    b18.getTypeSelector();
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setStyle(
                            "-fx-background-insets: 0;" +
                                    "-fx-background-color: transparent, white, transparent, white;" +
                                    "-fx-background-radius: 0, 0, 0, 0;"+
                                    "-fx-box-border: none;" +
                                    "-fx-focus-color: -fx-control-inner-background;" +
                                    "-fx-faint-focus-color: -fx-control-inner-background;" +
                                    "-fx-text-box-border: -fx-control-inner-background;" +
                                    "-fx-border-width: -1;");
                    HBox hBox = new HBox();
                    hBox.setStyle("-fx-background-color: rgba(0,191,255,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 0, 1;\n" +
                            "    -fx-text-fill: #000000;"
                    );
                    textFlow.getChildren().addAll( b19);
                    hBox.setPrefWidth(Region.USE_PREF_SIZE);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.autosize();
                    hBox.getChildren().addAll(profileImage, textFlow);
                    chatPane.getChildren().addAll(b18, hBox);
                    chatPane.setAlignment(Pos.CENTER_LEFT);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addToChat(Message msg) {

       sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                TextFlow textFlow = new TextFlow();
                Text b19 = new Text();
                b19.setText(msg.getMsg());

                b19.setFont(Font.font(null, 14));
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setStyle("-fx-background-color: rgba(0,191,255,0.46);" +
                        "    -fx-background-radius: 15, 8;\n" +
                        "    -fx-background-insets: 0, 1;\n" +
                        "    -fx-text-fill: #000000;"
                );
                textFlow.getChildren().addAll(b19);
                hBox.autosize();
                hBox.getChildren().addAll(profileImage, textFlow);
                return hBox;
            }
        };
        Text b18 = new Text();
        b18.setText(sdf.format(new Date()) + " " + usernameLabel.getText() + ":");
        b18.setFont(Font.font(null, 12));
        b18.setTextAlignment(TextAlignment.CENTER);
        othersMessages.setOnSucceeded(event -> {
            chatPane.getChildren().addAll(b18,othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                TextFlow textFlow = new TextFlow();
                Text b19 = new Text();
                Text b18 = new Text();
                b18.setText(sdf.format(new Date()) + " " + usernameLabel.getText() + ": \n");
                b19.setText(msg.getMsg());
                b18.setFont(Font.font(null, 12));
                b18.setTextAlignment(TextAlignment.CENTER);
                b19.setFont(Font.font(null, 14));
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                hBox.setStyle("-fx-background-color: rgba(188,143,143,0.46);" +
                        "    -fx-background-radius: 15, 8;\n" +
                        "    -fx-background-insets: 1, 0;\n" +
                        "    -fx-text-fill: #000000;");
                textFlow.getChildren().addAll(b19);
                hBox.autosize();
                hBox.getChildren().addAll(profileImage, textFlow);
                chatPane.getChildren().addAll(b18, hBox);
                chatPane.setAlignment(Pos.CENTER_LEFT);
              return hBox;
            }



        };
        yourMessages.setOnSucceeded(event -> chatPane.getChildren().addAll(b18,othersMessages.getValue()));

        if (msg.getName().equals(usernameLabel.getText())) {
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }
    }
    String receiver;
    @Override
    public ClientModelInt getConnection(String Client) {
        return getConnection(Client);
    }
    @Override
    public User getUserInformation() {
        return getUserInformation();
    }
    @FXML
    private void btnSendAttachAction(ActionEvent event) {

        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        //Show save file dialog
        File file = fileChooser.showOpenDialog(st);

        //no file choosen
        if (file == null) {
            return;
        }

        //  make connection with peer client
        ClientModelInt peer = getConnection(receiver);

        //peer user is offline
        if (peer == null) {
            UsefulUtils.showErrorDialogDown("Offline Offline User User you try to connect is offline right now");
            return;
        }

        Thread tr = new Thread(() -> {
            try {
                FileInputStream in = null;

                //get path to save file on other user

                    String path = peer.getSaveLocation(getUserInformation().getName(), file.getName());

                // other client refuse file transfare
                if (path == null) {

                    Platform.runLater(() -> {
                        UsefulUtils.showErrorDialogDown("Відмова користувача на запит файлу");
                    });
                    return;
                }

                in = new FileInputStream(file);
                byte[] data = new byte[1024 * 1024];
                int dataLength = in.read(data);
                    boolean append = false;
                while (dataLength > 0) {
                    peer.reciveFile(path, file.getName(), append, data, dataLength);
                    dataLength = in.read(data);
                    append = true;
               }

            } catch (RemoteException ex) {
                Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
               Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ChatController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        tr.start();
    }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }
//public void setIdTextFild (String idName) {

    //this.idTextFild.setText(idName);
//}


    public void setImageLabel() throws IOException {
        this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
    }

    public void setUserList(Message msg) {
        //   logger.info("setUserList() method Enter");
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(msg.getUsers());
            System.out.println(users + "usersOnline");
            //   userList.setItems(users);
            //   userList.setCellFactory(new CellRenderer());
            //   setOnlineLabel(valueOf(msg.getUserlist().size()));
        });
        //  logger.info("setUserList() method Exit");
    }

    /* Displays Notification when a user joins */
    public void newUserNotification(Message msg) {


        //   ObservableList<User> users = FXCollections.observableList(msg.getUsers());
        //    this.user = new User();
        this.user.setName(usernameLabel.getText());
        //  users.add(this.user);
        // for (int j = 0; j < users.size(); j++) {
        //     User name = users.get(j);
        //     String one = name.getName();
        //     if (one.equalsIgnoreCase(user)) {
        //     if (name.equalsIgnoreCase(usernameLabel.getText())) {
        //     System.out.println(user + usernameLabel.getText());
        //      } else {
        //    offeringRequest = (InProcessing) inProcessingController.tableviewAll.getItems().get(inProcessingController.tableviewAll.getSelectionModel().getSelectedIndex());
        //        if ((offeringRequest.equals(msg.getOfferingID()) && user != usernameLabel.getText()) && (userone.equals(usernameLabel.getText()) || com.login.User.getContactID().equals(userzero))) {
        //        } else {
        //        if (oneID.equalsIgnoreCase(user.getOfferingID())) {
        //    if () {
        // inProcessingController.tableProduct.getStylesheets().add
        //    (InProcessingController.class.getResource("/styles/Notification.css").toExternalForm());
        Platform.runLater(() -> {

            Image profileImg = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture().toLowerCase() + ".png").toString(), 50, 50, false, false);
            TrayNotification tray = new TrayNotification();
            tray.setTitle("Нове повідомлення!");
            tray.setMessage("Від " + msg.getName() + ": " + msg.getMsg());
            tray.setRectangleFill(Paint.valueOf("#2C3E50"));
            tray.setAnimationType(AnimationType.POPUP);
            tray.setImage(profileImg);

            tray.showAndDismiss(Duration.seconds(5));
            try {
                Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
                MediaPlayer mediaPlayer = new MediaPlayer(hit);
                mediaPlayer.play();
            } catch (Exception e) {
                e.printStackTrace();
            }

           /* try {
                inProcessingController.showScene();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

        });
        //    }
        //  }
        //  break;
        // }
    }

    public void sendMethod(KeyEvent event) throws IOException {
        if (keyComb1.match(event)) {

            try {
                sendButtonAction();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    /* Method to display server messages */
    public synchronized void addAsServer(Message msg) {


        Task<HBox> task = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                TextFlow textFlow = new TextFlow();
                Text b19 = new Text();
                Text b18 = new Text();
                b18.setText(sdf.format(new Date()) + " " + usernameLabel.getText() + ": \n");
                b19.setText(msg.getMsg());
                b18.setFont(Font.font(null, 12));
                b18.setTextAlignment(TextAlignment.CENTER);
                b19.setFont(Font.font(null, 14));
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_LEFT);
                textFlow.getChildren().addAll(b19);
                hBox.autosize();
                hBox.getChildren().addAll( textFlow);
                return hBox;
            }
        };
        task.setOnSucceeded(event -> {
            chatPane.getChildren().add(task.getValue());
        });

        chatPane.getChildren().add(task.getValue());

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dataMessage = FXCollections.observableArrayList();
        chatPane.setFocusTraversable( false );
        saveButton.setVisible(false);
        buttonSend.setTooltip(new Tooltip("Відправити"));
        try {
            connection = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            setImageLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scrollPane.vvalueProperty().bind(chatPane.heightProperty());

        for(Node text : emojiList.getChildren()){
            text.setOnMouseClicked(event -> {
                messageBox.setText(messageBox.getText()+" "+((Text)text).getText());
                emojiList.setVisible(false);
            });
        }
        titleBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        titleBar.setOnMouseDragged(event -> {
            titleBar.getScene().getWindow().setX(event.getScreenX() - xOffset);
            titleBar.getScene().getWindow().setY(event.getScreenY() - yOffset);
        });
    /*    buttonSend.setOnAction(event -> {
            try {
                sendButtonActionOne();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });*/
      /*  messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (/*keyEvent.getCode() == KeyCode.ENTER *//*keyComb1.match(keyEvent))  {
                    String text = messageBox.getText();

                    // do your thing...

                    // clear text
                    try {

                        sendButtonActionOne();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //messageBox.setText("\n");
                }
            }
        });*/


        messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (/*keyEvent.getCode() == KeyCode.ENTER */keyComb1.match(keyEvent)) {


                    try {
                        sendButtonAction();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        //closeWindow();






      /*  messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (/*keyEvent.getCode() == KeyCode.ENTER *//*keyComb2.match(keyEvent))  {
                    String text = messageBox.getText();

                    // do your thing...

                    // clear text
                 /*   getChatController().TabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
                        if(newTab == getChatController().TabPane.getTabs().get(1)){

                            try {

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else if(newTab == getChatController().TabPane.getTabs().get(2)){
                            try {
                                sendButtonActionTract();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        };



                    });*/
                   /* try {
                        sendButtonActionTract();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (NullPointerException ne){
                        // Logger.log(ne);
                    }
                    //            sendButtonAction();
//                 mainPageController.messageSend();

                }
            }
        });*/

                /* Drag and Drop */
       // borderPane.setOnMousePressed(event -> {
         //   xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
         //   yOffset = MainLauncher.getPrimaryStage().getY() - event.getScreenY();
            //borderPane.setCursor(Cursor.CLOSED_HAND);
     //   });

      //  borderPane.setOnMouseDragged(event -> {
        //    MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
        //    MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

      //  });

      //  borderPane.setOnMouseReleased(event -> {
      //      borderPane.setCursor(Cursor.DEFAULT);
     //   });

        //   statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
        //       public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        //        try {
        //             listener.sendStatusUpdate(Status.valueOf(newValue.toUpperCase()));
        //               } catch (IOException e) {
        //                e.printStackTrace();
        //              }
        //         }
        //       });

        /* Added to prevent the enter from adding a new line to inputMessageBox */
     /*   messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendButtonAction();
                    // sendButtonActionTract();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });*/




        /*try {
            connection = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
        //dataMessage = FXCollections.observableArrayList();

      /*  try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

    }

    public void setImageLabel(String selectedPicture) {
        switch (selectedPicture) {
            case "Dominic":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/Dominic.png").toString()));
                break;
            case "Sarah":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/sarah.png").toString()));
                break;
            case "Default":
                this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
                break;
        }
    }

    /*public void logoutScene() {
        Platform.runLater(() -> {
            FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/LoginView.fxml"));
            Parent window = null;
            try {
                window = (Pane) fmxlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = MainLauncher.getPrimaryStage();
            Scene scene = new Scene(window);
            stage.setMaxWidth(350);
            stage.setMaxHeight(420);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.centerOnScreen();
        });
    }*/
}