package com.client.chatwindow;

import com.ChatTwoo.controller.ColumnMessage;
import com.Utils.UsefulUtils;
import com.client.login.MainLauncher;
import com.client.util.VoicePlayback;
import com.client.util.VoiceRecorder;
import com.client.util.VoiceUtil;
import com.connectDatabase.DBConnection;
import com.mainPage.ArchiveFiles.ArchiveFiles;
import com.mainPage.ArchiveFiles.ArchiveFilesController;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTract;
import com.mainPage.InTract.InTractController;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import com.messages.User;
import com.messages.bubble.BubbleSpec;
import com.messages.bubble.BubbledLabel;
import com.traynotifications.animations.AnimationType;
import com.traynotifications.notification.TrayNotification;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import static java.lang.String.valueOf;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


public class ChatController   implements Initializable {

    @FXML
    public TextArea messageBox;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label onlineCountLabel;
    @FXML
    private ListView userList;
    @FXML
    private ImageView userImageView;
    @FXML
    private Button recordBtn;
    @FXML
    ListView <HBox> chatPane;
    @FXML
    ListView statusList;
    @FXML
    BorderPane borderPane;
    @FXML
    ComboBox<String> statusComboBox;
    @FXML
    ImageView microphoneImageView;
    @FXML
    private TextArea txtTitle;
    @FXML
    private TextField txtNumber;
    @FXML
    private TextField idTextFild;


    Listener listener = null;

    private PreparedStatement pst = null;
    private Connection connection = null;
    private ResultSet rs = null;
    private ObservableList<HBox> dataMessage;
    private ColumnMessage columnMessage = new ColumnMessage();
    public InProcessing offeringRequest;
    public InProcessingController inProcessingController;
    public ArchiveFiles offeringArchive;
    public ArchiveFilesController archiveFilesController;
    public InTractController inTractController;
    public InTract offeringTract;
    public User user ;
public ArrayList<Message> usersID = new ArrayList<>();
public ArrayList<User> users = new ArrayList<>();

    private Image microphoneActiveImage = new Image(getClass().getClassLoader().getResource("images/microphone-active.png").toString());
    private Image microphoneInactiveImage = new Image(getClass().getClassLoader().getResource("images/microphone.png").toString());
    private Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());

    private double xOffset;
    private double yOffset;


    // Logger logger = LoggerFactory.getLogger(ChatController.class);

    public void setOfferingRequest(InProcessing inProcessing) {
        offeringRequest = inProcessing;
    }
    public void setOfferingTract (InTract inTract){
        offeringTract = inTract;
    }
    public InTract getOfferingTract () {
        return offeringTract;
    }

    public InProcessing getOfferingRequest() {
        return offeringRequest;
    }
    public void setOfferingArchive (ArchiveFiles archive) {
        offeringArchive = archive;
    }
    public ArchiveFiles getOfferingArchive () {
        return offeringArchive;
    }

    public void setInProcessingController(InProcessingController inProcessingController) {
        this.inProcessingController = inProcessingController;
        txtTitle.setText(offeringRequest.getAccountName());
        txtNumber.setText(offeringRequest.getNumber());

        try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     //   user.setOfferingID(String.valueOf(offeringRequest));
    }
    public void setInTract(InTractController inTractController) {
        this.inTractController = inTractController;
        txtTitle.setText(offeringTract.getAccountName());
        txtNumber.setText(offeringTract.getNumber());

        try {
            loadDataFromDataBaseInTract();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //   user.setOfferingID(String.valueOf(offeringRequest));
    }

    public void setArchiveController(ArchiveFilesController archiveFilesController) {
        this.archiveFilesController = archiveFilesController;
        txtTitle.setText(offeringArchive.getAccountName());
        txtNumber.setText(offeringArchive.getNumber());
messageBox.setVisible(false);
        recordBtn.setVisible(false);


        try {
            loadDataFromDataBaseArchive();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //   user.setOfferingID(String.valueOf(offeringRequest));
    }
    public InProcessingController getInprocessingController(){
        return inProcessingController;
    }

    public InProcessingController getInProcessingController() {
        return inProcessingController;
    }

    public void sendButtonAction() throws IOException {
        String msg = messageBox.getText();
        if (!messageBox.getText().isEmpty()) {


            Listener.send(msg);
            String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
                    "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
            pst = null;

            try {
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
inProcessingController.refreshData();
            } catch (SQLException e) {
                UsefulUtils.showErrorDialogDown("Сервер не підключено!!!");
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
    }

    public void recordVoiceMessage() throws IOException {
        if (VoiceUtil.isRecording()) {
            Platform.runLater(() -> {
                microphoneImageView.setImage(microphoneInactiveImage);
                    }
            );
            VoiceUtil.setRecording(false);
        } else {
            Platform.runLater(() -> {
                microphoneImageView.setImage(microphoneActiveImage);

                    }
            );
            VoiceRecorder.captureAudio();
        }
    }

    private SimpleDateFormat sdf;

    private void loadDataFromDataBase() throws SQLException {
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
            try {
                System.out.println(offeringRequest.getID());
            } catch (NullPointerException e) {
                //       log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringRequest.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
           //   chatPane = new ListView<ColumnMessage>();




                String date = rs.getString(2);
                String user = rs.getString(4);
                String message =  rs.getString(6);



               if (user.equals(com.login.User.getContactName())) {

                Task<HBox> yourMessages = new Task<HBox>() {
                    @Override
                    public HBox call() throws Exception {
                  //      Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                        ImageView profileImage = new ImageView(image);
                        profileImage.setFitHeight(32);
                        profileImage.setFitWidth(32);

                        BubbledLabel bl6 = new BubbledLabel();

                            bl6.setText(date + "  " + user + ":  \n" + message);

                            bl6.setStyle("-fx-background-color: rgba(245,248,255,0.46);" +
                                    "    -fx-background-radius: 6, 5;\n" +
                                    "    -fx-background-insets: 0, 1;\n" +
                                    "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n" +
                                    "    -fx-text-fill: #395306;");


                            //  bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                        //        null, null)));
                        HBox x = new HBox();
                     //   x.setMaxWidth(chatPane.getWidth() - 20);
                        x.setAlignment(Pos.TOP_LEFT);
                        bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                        x.getChildren().addAll(bl6, profileImage);

                   //     setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                        return x;
                    }
                };
                yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));



                    Thread t2 = new Thread(yourMessages);
                   t2.setDaemon(true);
                    try {
                        t2.sleep(300);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                    }
                   t2.start();
//
                   // chatPane.setItems((ObservableList<HBox>) yourMessages.getValue());
                } else {
                Task<HBox> othersMessages = new Task<HBox>() {
                    @Override
                    public HBox call() throws Exception {
                      // Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                        ImageView profileImage = new ImageView(image);
                        profileImage.setFitHeight(32);
                        profileImage.setFitWidth(32);
                        BubbledLabel bl6 = new BubbledLabel();
                        bl6.setText(date + "  " + user + ":  \n" + message);
                     //   bl6.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                        bl6.setStyle("-fx-background-color: rgba(89,255,245,0.46);" +
                                "    -fx-background-radius: 6, 5;\n" +
                                "    -fx-background-insets: 0, 1;\n" +
                                "    -fx-effect: dropshadow( three-pass-box , rgb(0,0,0) , 5, 0.0 , 0 , 1 );\n" +
                                "    -fx-text-fill: #395306;");
                        HBox x = new HBox();
                        x.setAlignment(Pos.TOP_RIGHT);
                        bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                        x.getChildren().addAll(profileImage, bl6);
                        //    logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
                        //   setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                        return x;
                    }
                };

                othersMessages.setOnSucceeded(event -> {
                    chatPane.getItems().add(othersMessages.getValue());
                });



                   Thread t = new Thread(othersMessages);
                            t.setDaemon(true);
                    try {
                        t.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                          t.start();
              //      chatPane.setItems((ObservableList<HBox>) othersMessages.getValue());
                        }

                //    yourMessages.setOnSucceeded(event -> chatPane.setItems(dataMessage));

              //  chatPane.setItems(dataMessage);
                //   } else {
                //      othersMessages.setOnSucceeded(event -> chatPane.setItems(dataMessage));
                //    }

        /*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/



                //    chatPane.setItems(dataMessage);


            }
         //   chatPane.setItems((ObservableList<HBox>) dataMessage.get(6));
                //chatPane.setItems(dataMessage);

///chatPane.setItems(dataMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //	tableview.setItems(dataMessage);


    }

    private void loadDataFromDataBaseInTract() throws SQLException {
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
            try {
                System.out.println(offeringTract.getID());
            } catch (NullPointerException e) {
                //       log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringTract.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                //   chatPane = new ListView<ColumnMessage>();




                String date = rs.getString(2);
                String user = rs.getString(4);
                String message =  rs.getString(6);



                if (user.equals(com.login.User.getContactName())) {

                    Task<HBox> yourMessages = new Task<HBox>() {
                        @Override
                        public HBox call() throws Exception {
                            //      Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                            ImageView profileImage = new ImageView(image);
                            profileImage.setFitHeight(32);
                            profileImage.setFitWidth(32);

                            BubbledLabel bl6 = new BubbledLabel();

                            bl6.setText(date + "  " + user + ":  \n" + message);

                            bl6.setStyle("-fx-background-color: rgba(245,248,255,0.46);" +
                                    "    -fx-background-radius: 6, 5;\n" +
                                    "    -fx-background-insets: 0, 1;\n" +
                                    "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n" +
                                    "    -fx-text-fill: #395306;");


                            //  bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                            //        null, null)));
                            HBox x = new HBox();
                            //   x.setMaxWidth(chatPane.getWidth() - 20);
                            x.setAlignment(Pos.TOP_LEFT);
                            bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                            x.getChildren().addAll(bl6, profileImage);

                            //     setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                            return x;
                        }
                    };
                    yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));
                    Thread t2 = new Thread(yourMessages);
                    t2.setDaemon(true);
                    try {
                        t2.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t2.start();

                    // chatPane.setItems((ObservableList<HBox>) yourMessages.getValue());
                } else {
                    Task<HBox> othersMessages = new Task<HBox>() {
                        @Override
                        public HBox call() throws Exception {
                            // Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                            ImageView profileImage = new ImageView(image);
                            profileImage.setFitHeight(32);
                            profileImage.setFitWidth(32);
                            BubbledLabel bl6 = new BubbledLabel();
                            bl6.setText(date + "  " + user + ":  \n" + message);
                            //   bl6.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                            bl6.setStyle("-fx-background-color: rgba(89,255,245,0.46);" +
                                    "    -fx-background-radius: 6, 5;\n" +
                                    "    -fx-background-insets: 0, 1;\n" +
                                    "    -fx-effect: dropshadow( three-pass-box , rgb(0,0,0) , 5, 0.0 , 0 , 1 );\n" +
                                    "    -fx-text-fill: #395306;");
                            HBox x = new HBox();
                            x.setAlignment(Pos.TOP_RIGHT);
                            bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                            x.getChildren().addAll(profileImage, bl6);
                            //    logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
                            //   setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                            return x;
                        }
                    };

                    othersMessages.setOnSucceeded(event -> {
                        chatPane.getItems().add(othersMessages.getValue());
                    });



                    Thread t = new Thread(othersMessages);
                    t.setDaemon(true);
                    try {
                        t.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t.start();
                    //      chatPane.setItems((ObservableList<HBox>) othersMessages.getValue());
                }

                //    yourMessages.setOnSucceeded(event -> chatPane.setItems(dataMessage));

                //  chatPane.setItems(dataMessage);
                //   } else {
                //      othersMessages.setOnSucceeded(event -> chatPane.setItems(dataMessage));
                //    }

        /*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/



                //    chatPane.setItems(dataMessage);


            }

            //chatPane.setItems(dataMessage);

///chatPane.setItems(dataMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //	tableview.setItems(dataMessage);


    }



    private void loadDataFromDataBaseArchive() throws SQLException {
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
            try {
                System.out.println(offeringArchive.getID());
            } catch (NullPointerException e) {
                //       log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringArchive.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                //   chatPane = new ListView<ColumnMessage>();




                String date = rs.getString(2);
                String user = rs.getString(4);
                String message =  rs.getString(6);



                if (user.equals(com.login.User.getContactName())) {

                    Task<HBox> yourMessages = new Task<HBox>() {
                        @Override
                        public HBox call() throws Exception {
                            //      Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                            ImageView profileImage = new ImageView(image);
                            profileImage.setFitHeight(32);
                            profileImage.setFitWidth(32);

                            BubbledLabel bl6 = new BubbledLabel();

                            bl6.setText(date + "  " + user + ":  \n" + message);

                            bl6.setStyle("-fx-background-color: rgba(245,248,255,0.46);" +
                                    "    -fx-background-radius: 6, 5;\n" +
                                    "    -fx-background-insets: 0, 1;\n" +
                                    "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n" +
                                    "    -fx-text-fill: #395306;");


                            //  bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                            //        null, null)));
                            HBox x = new HBox();
                            //   x.setMaxWidth(chatPane.getWidth() - 20);
                            x.setAlignment(Pos.TOP_LEFT);
                            bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                            x.getChildren().addAll(bl6, profileImage);

                            //     setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                            return x;
                        }
                    };
                    yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));
                    Thread t2 = new Thread(yourMessages);
                    t2.setDaemon(true);
                    try {
                        t2.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t2.start();

                    // chatPane.setItems((ObservableList<HBox>) yourMessages.getValue());
                } else {
                    Task<HBox> othersMessages = new Task<HBox>() {
                        @Override
                        public HBox call() throws Exception {
                            // Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                            ImageView profileImage = new ImageView(image);
                            profileImage.setFitHeight(32);
                            profileImage.setFitWidth(32);
                            BubbledLabel bl6 = new BubbledLabel();
                            bl6.setText(date + "  " + user + ":  \n" + message);
                            //   bl6.setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
                            bl6.setStyle("-fx-background-color: rgba(89,255,245,0.46);" +
                                    "    -fx-background-radius: 6, 5;\n" +
                                    "    -fx-background-insets: 0, 1;\n" +
                                    "    -fx-effect: dropshadow( three-pass-box , rgb(0,0,0) , 5, 0.0 , 0 , 1 );\n" +
                                    "    -fx-text-fill: #395306;");
                            HBox x = new HBox();
                            x.setAlignment(Pos.TOP_RIGHT);
                            bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                            x.getChildren().addAll(profileImage, bl6);
                            //    logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
                            //   setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                            return x;
                        }
                    };

                    othersMessages.setOnSucceeded(event -> {
                        chatPane.getItems().add(othersMessages.getValue());
                    });



                    Thread t = new Thread(othersMessages);
                    t.setDaemon(true);
                    try {
                        t.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    t.start();
                    //      chatPane.setItems((ObservableList<HBox>) othersMessages.getValue());
                }

                //    yourMessages.setOnSucceeded(event -> chatPane.setItems(dataMessage));

                //  chatPane.setItems(dataMessage);
                //   } else {
                //      othersMessages.setOnSucceeded(event -> chatPane.setItems(dataMessage));
                //    }

        /*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/



                //    chatPane.setItems(dataMessage);


            }

            //chatPane.setItems(dataMessage);

///chatPane.setItems(dataMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //	tableview.setItems(dataMessage);


    }


    public synchronized void addToChat(Message user, Message msg) {
        sdf = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture() + ".png").toString());
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);
                BubbledLabel bl6 = new BubbledLabel();
                if (msg.getType() == MessageType.VOICE) {
                    ImageView imageview = new ImageView(new Image(getClass().getClassLoader().getResource("images/sound.png").toString()));
                    bl6.setGraphic(imageview);
                    bl6.setText("Sent a voice message!");
                    VoicePlayback.playAudio(msg.getVoiceMsg());
                } else {


                    bl6.setText(sdf.format(new Date()) + "  " + msg.getName() + ":  \n" + msg.getMsg());
                }
                bl6.setStyle("-fx-background-color: rgba(89,255,245,0.46);" +
                        "    -fx-background-radius: 6, 5;\n" +
                        "    -fx-background-insets: 0, 1;\n" +
                        "    -fx-effect: dropshadow( three-pass-box , rgb(0,0,0) , 5, 0.0 , 0 , 1 );\n" +
                        "    -fx-text-fill: #395306;");
                HBox x = new HBox();
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);
                //    logger.debug("ONLINE USERS: " + Integer.toString(msg.getUserlist().size()));
                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            chatPane.getItems().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = userImageView.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                BubbledLabel bl6 = new BubbledLabel();
                if (msg.getType() == MessageType.VOICE) {
                    bl6.setGraphic(new ImageView(new Image(getClass().getClassLoader().getResource("images/sound.png").toString())));
                    bl6.setText("Sent a voice message!");
                    VoicePlayback.playAudio(msg.getVoiceMsg());
                } else {
                    bl6.setText(sdf.format(new Date()) + "  " + usernameLabel.getText() + ":  \n" + msg.getMsg());
                }
                bl6.setStyle("-fx-background-color: rgba(245,248,255,0.46);" +
                        "    -fx-background-radius: 15, 155;\n" +
                        "    -fx-background-insets: 0, 1;\n" +
                        "    -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,0.4) , 5, 0.0 , 0 , 1 );\n" +
                        "    -fx-text-fill: #395306;");
                HBox x = new HBox();
                x.setMaxWidth(chatPane.getWidth() - 20);
                x.setAlignment(Pos.TOP_LEFT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);

                setOnlineLabel(Integer.toString(msg.getOnlineCount()));
                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));

                //   System.out.println(offeringRequest.getID() + idTextFild.getText());
  //     this.user = new User();
  //     this.user.setOfferingID(user.getName());
     //   users.add(this.user);
        usersID.add(msg);
        //for (int j = 0; j < usersID.size(); j++) {
      //      Message nameID = usersID.get(j);
     //      String oneID = nameID.getOfferingID();
              //  if (user.equals(offeringRequest.getID())) {
            //     if (name.equalsIgnoreCase(usernameLabel.getText())) {
      //  if (user.getOfferingID().equals(idTextFild.getText())) {

      //      if (oneID.equalsIgnoreCase(user.getOfferingID())) {
            //    if (msg.getName().equals(usernameLabel.getText())) {
             //       this.user = new User();
             //       this.user.setName(user.getName());
            //        users.add(this.user);
             //       for (int i = 0; i < users.size(); i++) {
             //           User name = users.get(i);
            //            String one = name.getOfferingID();



        //if(offeringRequest.getID().equalsIgnoreCase(user.getOfferingID())){
            if(offeringRequest.getID().equals(msg.getOfferingID())){
        if (usernameLabel.getText().equals(msg.getName())){
         //
                        Thread t2 = new Thread(yourMessages);
                        t2.setDaemon(true);
                        t2.start();
               //     }
               // }
                } else {

            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }
                //     System.out.println(user + usernameLabel.getText());
        //   }
       }
    }
    //  } else {
    //        System.out.println(user + usernameLabel.getText());
    //     }

 //   }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }
public void setIdTextFild (String idName) {

    this.idTextFild.setText(idName);
}


    public void setImageLabel() throws IOException {
        this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
    }

    public void setOnlineLabel(String usercount) {
        Platform.runLater(() -> onlineCountLabel.setText(usercount));
    }

    public void setUserList(Message msg) {
     //   logger.info("setUserList() method Enter");
        Platform.runLater(() -> {
            ObservableList<User> users = FXCollections.observableList(msg.getUsers());
            System.out.println(users + "usersOnline");
            userList.setItems(users);
            userList.setCellFactory(new CellRenderer());
            setOnlineLabel(valueOf(msg.getUserlist().size()));
        });
      //  logger.info("setUserList() method Exit");
    }

    /* Displays Notification when a user joins */
    public void newUserNotification(String userzero, String userone, String user, Message msg) {



    ObservableList<User> users = FXCollections.observableList(msg.getUsers());
      /*  this.user = new User();
            this.user.setName(usernameLabel.getText());
            users.add(this.user);
             for (int j = 0; j < users.size(); j++) {
                 User name = users.get(j);
                 String one = name.getName();
                 if (one.equalsIgnoreCase(user)) {*/
                     //     if (name.equalsIgnoreCase(usernameLabel.getText())) {
                //     System.out.println(user + usernameLabel.getText());
                // } else {

        if(/*idTextFild.getText().equals(msg.getOfferingID())*/ (user != usernameLabel.getText()) && (userone.equals(usernameLabel.getText())|| com.login.User.getContactID().equals(userzero))){
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
                 }//}
            // }
    }

    public void sendMethod(KeyEvent event) throws IOException {
        if (event.getCode() == KeyCode.ENTER) {
            sendButtonAction();
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
                BubbledLabel bl6 = new BubbledLabel();
                bl6.setText(msg.getMsg());
                System.out.println(msg.getMsg());
                bl6.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,
                        null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_BOTTOM);
                x.setAlignment(Pos.CENTER);
                x.getChildren().addAll(bl6);
                return x;
            }
        };
        task.setOnSucceeded(event -> {
            chatPane.getItems().add(task.getValue());
        });



            Thread t = new Thread(task);
            t.setDaemon(true);
            t.start();
            }

   // }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idTextFild.setVisible(false);
        dataMessage = FXCollections.observableArrayList();


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
                /* Drag and Drop */
        borderPane.setOnMousePressed(event -> {
            xOffset = MainLauncher.getPrimaryStage().getX() - event.getScreenX();
            yOffset = MainLauncher.getPrimaryStage().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            MainLauncher.getPrimaryStage().setX(event.getScreenX() + xOffset);
            MainLauncher.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });

        statusComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    listener.sendStatusUpdate(Status.valueOf(newValue.toUpperCase()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        /* Added to prevent the enter from adding a new line to inputMessageBox */
        messageBox.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                try {
                    sendButtonAction();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ke.consume();
            }
        });
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