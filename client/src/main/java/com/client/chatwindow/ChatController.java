package com.client.chatwindow;

import com.ColumnMessage;
import com.Utils.GridComp;
import com.Utils.UsefulUtils;
import com.client.util.VoicePlayback;
import com.client.util.VoiceRecorder;
import com.client.util.VoiceUtil;
import com.connectDatabase.DBConnection;
import com.mainPage.All.AllController;
import com.mainPage.ArchiveFiles.ArchiveFilesController;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTractController;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.page.MainPageController;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.User;
import com.messages.bubble.BubbleSpec;
import com.messages.bubble.BubbledLabel;
import com.traynotifications.animations.AnimationType;
import com.traynotifications.notification.TrayNotification;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;


public class ChatController implements Initializable {

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
    public Button buttonSend;
    @FXML
    ScrollPane scrollPane;
    @FXML
    private BorderPane rootPane;
    @FXML
    private AnchorPane chatPaneGeneral;
    @FXML
    ImageView microphoneImageView;
    @FXML
    private Button recordBtn;
    Image microphoneActiveImage = new Image(getClass().getClassLoader().getResource("images/microphone-active.png").toString());
    Image microphoneInactiveImage = new Image(getClass().getClassLoader().getResource("images/microphone.png").toString());

    ImageView userImageView = new ImageView();
    private PreparedStatement pst = null;
    private Connection connection = null;
    private ResultSet rs = null;
    private ObservableList<HBox> dataMessage;
    private ObservableList<ColumnMessage> data;
    private ColumnMessage columnMessage = new ColumnMessage();
    public GridComp offeringRequest;

    public NotFulledController notFulledController;
    public InProcessingController inProcessingController;
    public ArchiveFilesController archiveFilesController;
    public AllController allController;
    public InTractController inTractController;
    public User user;

    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.ENTER,
            KeyCombination.CONTROL_DOWN);

    private double xOffset;
    private double yOffset;
    public MainPageController mainPageController;

    public void setOfferingRequest(GridComp offeringRequest) {
        this.offeringRequest = offeringRequest;
    }

    public GridComp getOfferingRequest() {
        return offeringRequest;
    }

    public void init(NotFulledController notFulledController) {
        this.notFulledController = notFulledController;
    }

    public void init(InProcessingController inProcessingController) {
        this.inProcessingController = inProcessingController;
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


    public void setInTract(InTractController inTractController) {
        this.inTractController = inTractController;
        try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace(); DBConnection database = new DBConnection();
            database.reconnect();
        }
    }

    public void setArchiveController(ArchiveFilesController archiveFilesController) {
        this.archiveFilesController = archiveFilesController;
        buttonSend.setVisible(false);
        messageBox.setVisible(false);
        chatPaneGeneral.setVisible(false);
        btnEmoji.setVisible(false);
        try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace(); DBConnection database = new DBConnection();
            database.reconnect();
        }
    }

    public void setAllController(AllController all) {
        this.allController = all;
        messageBox.setVisible(false);
        buttonSend.setVisible(false);
        chatPaneGeneral.setVisible(false);
        btnEmoji.setVisible(false);

        try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
        }
    }

    @FXML
    public void sendButtonAction() throws IOException {
        if (messageBox.getText().trim().equals("")) return;
        String msg = messageBox.getText();


        try {

            try {
                Listener.send(msg);
                Listener.sendNotification(msg);
            } catch (IOException e) {
                UsefulUtils.showErrorDialogDown("Сервер не підключено!!!");
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
        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
            UsefulUtils.showErrorDialogDown("Сервер не підключено!!!");

        }
        messageBox.clear();
        messageBox.requestFocus();


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

    @FXML
    private void btnSendAttachAction(ActionEvent event) {
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();

        //Show save file dialog
        File file = fileChooser.showOpenDialog(st);
        chatPane.getChildren().addAll((Collection<? extends Node>) file);
       /* if (file != null) {
            try {
                Image image = new Image("file:///" + file.getAbsolutePath());
             //   Listener.sendPhoto(image);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }*/


       /* chatPane.setOnCloseRequest(new EventHandler<WindowEvent>() {
public void handle(WindowEvent we) {
        System.exit(0);
        }
        });*/
       /* Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

        tr.start();*/
    }


    public void loadDataFromDataBase() throws SQLException {
        chatPane.getChildren().size();
        chatPane.getChildren().clear();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
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
                    "ORDER BY \n" +
                    "\t2 ASC");

            pst.setString(1, offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                String date = rs.getString(2);
                String user = rs.getString(4);
                String message = rs.getString(6);
                date = date.substring(0, 19);

                if (user.equals(com.login.User.getContactName())) {
                    TextFlow textFlow = new TextFlow();
                    TextArea b19 = new TextArea();
                    TextField b18 = new TextField();
                    b18.setEditable(false);
                    b19.setEditable(false);
                    b18.setText(date + " " + user + ":");
                    b19.setText(message);
                    b18.setFont(Font.font(null, 12));
                    b19.setFont(Font.font(null, 14));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    String[] to = b19.getText().split("\n");
                    double count = ((double) to.length);
                    int rowHeight = (int) ((count + 1) * 55);
                    b19.setPrefHeight(rowHeight);
                    b19.setPrefWidth(320);
                    b19.getStylesheets().add("styles/TextArea.css");
                    b19.setWrapText(true);
                    b19.getOnDragEntered();

                    VBox hBox = new VBox();

                    hBox.setStyle("-fx-background-color: rgba(188,143,143,0.46);" +
                            "    -fx-background-radius: 15, 8;\n" +
                            "    -fx-background-insets: 1, 0;\n" +
                            "    -fx-text-fill: #000000;");
                    hBox.setMinHeight(b19.getPrefHeight());
                    hBox.setMinWidth(b19.getPrefWidth());
                    hBox.setMaxHeight(b19.getPrefHeight());
                    hBox.setMaxWidth(b19.getPrefWidth());
                    hBox.getChildren().addAll(b19);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    chatPane.getChildren().addAll(b18, hBox);
                    scrollPane.setContent(chatPane);

                } else {
                    Image image = new Image(getClass().getClassLoader().getResource("images/default.png").toString());
                    ImageView profileImage = new ImageView(image);
                    profileImage.setFitHeight(32);
                    profileImage.setFitWidth(32);
                    TextArea b19 = new TextArea();

                    TextField b18 = new TextField();
                    b18.setEditable(false);
                    b18.setText(date + " " + user + ":");
                    b19.setText(message);

                    String[] to = b19.getText().split("\n");
                    double count = ((double) to.length);

                    int rowHeight = (int) ((count + 1) * 55);

                    b19.setPrefHeight(rowHeight);
                    b19.setPrefWidth(320);

                    b18.setFont(Font.font(null, 12));
                    b18.setAlignment(Pos.CENTER_LEFT);
                    b19.getPrefWidth();
                    b19.getPrefHeight();
                    b19.setFont(Font.font(null, 14));
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");
                    b19.setStyle(
                            "-fx-background-insets: 0;" +
                                    "-fx-background-color: transparent, white, transparent, white;" +
                                    "-fx-background-radius: 0, 0, 0, 0;" +
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
                    b19.setStyle("-fx-background-color: transparent;");
                    b18.setStyle("-fx-background-color: transparent;");

                    b19.getStylesheets().add("styles/TextArea.css");
                    b19.setWrapText(true);
                    b19.setEditable(false);
                    hBox.setMinHeight(b19.getPrefHeight());
                    hBox.setMinWidth(b19.getPrefWidth());
                    hBox.setMaxHeight(b19.getPrefHeight());
                    hBox.setMaxWidth(b19.getPrefWidth());

                    hBox.getChildren().addAll(b19);
                    hBox.setAlignment(Pos.CENTER_LEFT);

                    chatPane.getChildren().addAll(b18, hBox);
                    scrollPane.setContent(chatPane);

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
        if (emojiList.isVisible()) {

            emojiList.setVisible(false);
        } else {
            emojiList.setVisible(true);
        }
    }

   /* public void sendButtonAction() throws IOException {
        String msg = messageBox.getText();
        if (!messageBox.getText().isEmpty()) {
            Listener.send(msg);
            messageBox.clear();
        }
    }*/

    /*public void recordVoiceMessage() throws IOException {
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
    }*/


    public //synchronized
    void addToChat(Message msg) {
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
                    bl6.setText(msg.getName() + ": " + msg.getMsg());
                }
                bl6.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                HBox x = new HBox();
                bl6.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                x.getChildren().addAll(profileImage, bl6);
                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            //   newUserNotification(msg);
            chatPane.getChildren().add(othersMessages.getValue());

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
                    bl6.setText(msg.getMsg());
                }
                bl6.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,
                        null, null)));
                HBox x = new HBox();
                x.setMaxWidth(chatPane.getWidth() - 20);
                x.setAlignment(Pos.TOP_RIGHT);
                bl6.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                x.getChildren().addAll(bl6, profileImage);

                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> {
            chatPane.getChildren().add(yourMessages.getValue());

        });


        if (msg.getContactID().equals(com.login.User.getContactID())) {
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            if ((msg.getOfferingGroupID().equals(com.login.User.getContactID()) && com.login.User.getContactID() != msg.getContactID()) ||
                    (msg.getCreatedByID().equals(com.login.User.getContactID()) && com.login.User.getContactID() != msg.getContactID())) {
                Thread t = new Thread(othersMessages);
                t.setDaemon(true);
                t.start();
            }
        }
    }

    public void setUsernameLabel(String username) {
        this.usernameLabel.setText(username);
    }

    public void setImageLabel() throws IOException {
        this.userImageView.setImage(new Image(getClass().getClassLoader().getResource("images/default.png").toString()));
    }


    public void setUserList(Message msg) {
    }

    /* Displays Notification when a user joins */
   /* public void newUserNotification(Message msg) {
        Platform.runLater(() -> {
            Image profileImg = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture().toLowerCase() +".png").toString(),50,50,false,false);
            TrayNotification tray = new TrayNotification();
            tray.setTitle("A new user has joined!");
            tray.setMessage(msg.getName() + " Онлайн!");
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

        });
    }*/
    public void newUserNotification(Message msg) {
        if (msg.getContactID().equals(com.login.User.getContactID())) {

        } else {
            if ((msg.getOfferingGroupID().equals(com.login.User.getContactID()) && com.login.User.getContactID() != msg.getContactID()) ||
                    (msg.getCreatedByID().equals(com.login.User.getContactID()) && com.login.User.getContactID() != msg.getContactID())) {
                Platform.runLater(() -> {
                    TrayNotification tray = new TrayNotification();
                    tray.setTitle("Повідомлення");
                    tray.setName(msg.getName() + ", номер " + msg.getNumber());
                    tray.setMessage(msg.getMsg());
                    tray.setAnimationType(AnimationType.SLIDE);
                    tray.showAndDismiss(Duration.seconds(3600));
                    try {
                        Media hit = new Media(getClass().getClassLoader().getResource("sounds/not-bad.mp3").toString());
                        MediaPlayer mediaPlayer = new MediaPlayer(hit);
                        mediaPlayer.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
            }
        }
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
            chatPane.getChildren().add(task.getValue());
        });

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        recordBtn.setVisible(false);
        dataMessage = FXCollections.observableArrayList();
        chatPane.setFocusTraversable(false);
        buttonSend.setTooltip(new Tooltip("Відправити"));
        try {
            connection = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace(); DBConnection database = new DBConnection();
            database.reconnect();
        }

        try {
            setImageLabel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scrollPane.vvalueProperty().bind(chatPane.heightProperty());

        for (Node text : emojiList.getChildren()) {
            text.setOnMouseClicked(event -> {
                messageBox.setText(messageBox.getText() + " " + ((Text) text).getText());
                emojiList.setVisible(true);
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
        messageBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyComb1.match(keyEvent)) {
                    try {
                        sendButtonAction();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

}