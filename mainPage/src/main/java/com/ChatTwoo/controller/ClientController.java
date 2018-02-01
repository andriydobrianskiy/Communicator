package com.ChatTwoo.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.ChatTwoo.controller.MessageInRequest.MessageInRequestQuery;
import com.ChatTwoo.model.ChatMessage;
import com.connectDatabase.DBConnection;
import com.login.LoginControl;
import com.login.User;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InTract.InTract;
import tray.notification.TrayNotification;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController implements Initializable // MessageProperties
{

    private static Logger log = Logger.getLogger(ClientController.class.getName());

    @FXML
    private TextArea txtAreaServerMsgs;
    @FXML
    private ListView<String> listUser;
    @FXML
    private TextArea txtUserMsg;
    @FXML
    public JFXTextArea txtTitle;
    @FXML
    public JFXTextArea txtNumber;
    @FXML
    public TitledPane listOne;


    //  public static InProcessing record = null;
    public static InTract recordInTract = null;
    public ObservableList<ColumnMessage> list = FXCollections.observableArrayList();

    private Color DEFAULT_SENDER_COLOR = Color.SILVER;
    private Color DEFAULT_RECEIVER_COLOR = Color.WHITE;
    private Background DEFAULT_SENDER_BACKGROUND, DEFAULT_RECEIVER_BACKGROUND;
    private ObservableList<Node> speechBubbles = FXCollections.observableArrayList();


    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<ColumnMessage> dataMessage;
    //	private ObservableList <InProcessing> dataTitle;
    private MessageInRequestQuery query = new MessageInRequestQuery();
    private ColumnMessage columnMessage = new ColumnMessage();

    // private ObservableList<MessageInRequest> data;
    private ObservableList<String> users;


    private boolean connected;
    private String server;
    public String username;
    public  String usernameMessage;
    private static int port;
    private LoginControl loginControl = new LoginControl();
    private InProcessing selectedRecord = null;

    // for I/O
    private ObjectInputStream sInput;        // to read from the socket
    private ObjectOutputStream sOutput;        // to write on the socket
    private Socket socket;

    public static InProcessing offeringRequest;
    // public static InProcessing offeringStatic;

 /*   public InProcessing getOfferingRequest() {
        return offeringRequest ;
    }


    public void setOfferingRequest(InProcessing offeringRequest, boolean status) {
        this.offeringRequest = offeringRequest;
        try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
         txtTitle.setText(offeringRequest.getAccountName());
         txtNumber.setText(offeringRequest.getNumber());

    }*/

    //  private static User userID = null;
    public static InTract offeringRequestIntract;

    // private DataOutputStream dataoutputstream;


    //  Thread thread;
    //  private static int num = 0;


    public ClientController() {

    }


    public ClientController(InProcessing offeringRequest) {
        this.offeringRequest = offeringRequest;
    }

    public ClientController(ColumnMessage columnMessage) {
        this.columnMessage = columnMessage;
    }

    public ClientController(InProcessing offeringRequest, boolean status) {
        this.offeringRequest = offeringRequest;
        showWindow();


    }

    protected int PrivateWindowCount;
    int G_ILoop;
    ClientController [] chatMessage;
    public void RemovePrivateWindow(String ToUserName)
    {


        int m_UserIndex = 0;
        for(G_ILoop = 0; G_ILoop < PrivateWindowCount; G_ILoop++)
        {
            m_UserIndex++;
            if( chatMessage[G_ILoop].username.equals(ToUserName))
                break;
        }
        for(int m_iLoop = m_UserIndex;m_iLoop < PrivateWindowCount; m_iLoop++)
        {
            chatMessage[m_iLoop] = chatMessage[m_iLoop+1];
        }

        PrivateWindowCount--;
    }


    public ClientController(InTract offeringRequestIntract, boolean status) {

        this.offeringRequestIntract = offeringRequestIntract;
        //    System.out.println("||||||||||||||||||||||||||||||||||||||||||||||||||| " + offeringRequest.getID());
        showWindow();
       /* loginControl();

        dataMessage = FXCollections.observableArrayList();
        txtTitle.setText(offeringRequestIntract.getAccountName());
        txtNumber.setText(offeringRequestIntract.getNumber());
        listOne.setVisible(false);

        try {
            loadDataFromDataBaseInTract();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public ClientController(InProcessing offeringRequest, String msg) {
        this.offeringRequest = offeringRequest;
        try {
            sendMessage();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // showWindow();
    }


    private void loadDataFromDataBase() throws SQLException {
        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement("SELECT\n" +
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
                log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringRequest.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                txtAreaServerMsgs.setText(rs.getString("createdByID"));


                StringBuilder builder = new StringBuilder();
                dataMessage.forEach(item -> {
                    builder.append(item.getCreatedOn() + ": " + item.getSender() + "\n");
                    builder.append(item.getMessage() + "\n");
                });

                txtAreaServerMsgs.setText(builder.toString());


            }

		/*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //	tableview.setItems(dataMessage);


    }


    private void loadDataFromDataBaseInTract() throws SQLException {
        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement("SELECT\n" +
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
                log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringRequestIntract.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                dataMessage.add(new ColumnMessage(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(6)));
                txtAreaServerMsgs.setText(rs.getString("createdByID"));


                StringBuilder builder = new StringBuilder();
                dataMessage.forEach(item -> {
                    builder.append(item.getCreatedOn() + ": " + item.getSender() + "\n");
                    builder.append(item.getMessage() + "\n");
                });

                txtAreaServerMsgs.setText(builder.toString());


            }

		/*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //	tableview.setItems(dataMessage);


    }






    /*@FXML
    private TextArea txtAreaServerMsgs;
    @FXML
    private ListView<String> listUser;
    @FXML
    private TextArea txtUserMsg;
    @FXML
    public JFXTextArea txtTitle;
    @FXML
    public JFXTextArea txtNumber;

    private DerbyMessageInRequestDAO derbyMessageInRequest = new DerbyMessageInRequestDAO();


    private ObservableList<MessageInRequest> data;
	private ObservableList<String> users;

	private boolean connected;
	private String server, username;
	private int port;
	private LoginControl loginControl = new LoginControl();


	private static InProcessing selectedRecord = null;

	// for I/O
	private ObjectInputStream sInput;		// to read from the socket
	private ObjectOutputStream sOutput;		// to write on the socket
	private Socket socket;*/
  /*  public void showWindow() {
        try {
            System.out.println("22222222222222 " + offeringRequest);
            Stage primaryStage = new Stage();
            //primaryStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader();

            //  ClientController con = loader.getController();

            //loader.setRoot(this);
            //loader.setController(this);

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/sample/ChatTwoo/view/ClientGUI.fxml"));

            Parent root = fxml.load();

            //  con.setOfferingRequest(offeringRequest);


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void loginControl() {
        port = 1500;
        server = "192.168.10.131";

        //	String usetext = txtUsername.getText();
        //if(User.setAdminUnit() != null){
        //	System.out.println( loginControl.authenticate());
   //     if(offeringRequest == offeringRequest) {
            username = offeringRequest.getID();
        //}else if (offeringRequestIntract == offeringRequestIntract){
          //  username = offeringRequestIntract.getID();
        //}
        System.out.println("2222222222222222222222222222222222222222222222222222222222222222");
        //  username = offeringRequest.getID();
        System.out.println(username + "contactname");
        //usernameMessage = User.getContactName();

        System.out.println(User.getContactID() + "77777777777777777777777775255555555222222222222");
        //}else disconnect();
        //username = txtUsername.getText();
        // test if we can start the connection to the Server
        // if it failed nothing we can do
        if (!start())
            return;
        connected = true;
        //btnLogin.setDisable(true);
        //btnLogout.setDisable(false);
        //	txtUsername.setEditable(false);
        //txtHostIP.setEditable(false);
    }


    public void showWindow() {
        try {

            Stage primaryStage = new Stage();
          //  primaryStage.initModality(Modality.APPLICATION_MODAL);

            FXMLLoader loader = new FXMLLoader();

            //  ClientController con = loader.getController();

            //loader.setRoot(this);
            //loader.setController(this);

            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/ClientGUI.fxml"));

            Parent root = fxml.load();
            System.out.println("22222222222222 " + offeringRequest);
            //  con.setOfferingRequest(offeringRequest);


            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



	/*public boolean insertVehicle(ColumnMessage accountVehicle) {
        String vehicleName = (accountVehicle.getCreatedByID() + " " + accountVehicle.getRequestID() + " " + accountVehicle.getMessage()).toUpperCase();
		PreparedStatement st;

		try (Connection connection = DBConnection.getConnection()){
			dbAccess.update(connection, queries.insertQuery(),
					User.getContactID(),
					User.getContactID(),
					vehicleName,
					accountVehicle.getCreatedByID(),
					accountVehicle.getCreatedOn(),
					accountVehicle.getID(),
					accountVehicle.getMessage(),
					accountVehicle.getModifiedOn(),
					accountVehicle.getRecipientID(),
					accountVehicle.getRequestID()
			);
			return true;
		} catch (Exception e) {
			e.printStackTrace();


			log.log(Level.SEVERE, "Insert row exception: " + e);
			return false;
		}


	}*/


    public void logout() {
        if (connected) {
            ChatMessage msg = new ChatMessage(ChatMessage.LOGOUT, "" );
            try {
                sOutput.writeObject(msg);
                txtUserMsg.setText("");
            } catch (IOException e) {
                display("Exception writing to server: " + e);
            }
        }
    }

    /**
     * To send a message to the server
     */



    public void sendMessage() throws SQLException {

//if(User.getContactName() != offeringRequest.getCreatedBy()) {
    ChatMessage msg = new ChatMessage(ChatMessage.MESSAGE, txtUserMsg.getText());


    ChatMessage message = msg;
    String query = "\tINSERT INTO [dbo].[tbl_MessageInRequestOffering] ([CreatedOn], [CreatedByID], [ModifiedOn], [RecipientID], [RequestID],  [Message])\n" +
            "\tVALUES ( CURRENT_TIMESTAMP , ? , CURRENT_TIMESTAMP, ? , ? , ? )";
    pst = null;

    try {
        pst = DBConnection.getDataSource().getConnection().prepareStatement(query);
        pst.setString(1, User.getContactID());
        pst.setString(2, columnMessage.getRecipientID());
        pst.setString(3, offeringRequest.getID());
        pst.setString(4, msg.getMessage());
        pst.execute();


    } catch (SQLException e) {
        e.printStackTrace();
    }

    if (connected) {
        try {

            sOutput.writeObject(msg);
            txtUserMsg.setText("");

        } catch (IOException e) {
            e.printStackTrace();
        }
  //  }
}

    }

    /**
     * Sends message to server
     * Used by TextArea txtUserMsg to handle Enter key event
     */
    public void handleEnterPressed(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            sendMessage();

            event.consume();
        }
    }


    public void SoundMessage() {
        Media hit = new Media(getClass().getClassLoader().getResource("sample/resources/sounds/notification.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();

    }

    /**
     * To start the dialog
     */
    public boolean start() {
        // try to connect to the server
        try {
            socket = new Socket(server, port);
        //    System.out.println("98754833545646464668468468468468468" + server + "     " + port);

        }
        // if it failed not much I can so
        catch (Exception ec) {
            display("Помилка підключення до сервера: " + ec);
            return false;
        }

        String msg = "Підключено: " + username + socket.getInetAddress() + ":" + socket.getPort();
        display(msg);


		/* Creating both Data Stream */
        try {
            sInput = new ObjectInputStream(socket.getInputStream());
            sOutput = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server
        new ListenFromServer().start();
        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(username);
            sOutput.writeObject(User.getContactName());
          //  UsefulUtils.showSuccessful("reerherhrehre" + User.getContactName());


        } catch (IOException eIO) {
            display("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    private void display(String msg) {

        txtAreaServerMsgs.appendText(msg + "\n"); // append to the ServerChatArea


    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect not much to do in the catch clause
     */
    private void disconnect() {
        try {
            if (sInput != null) sInput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (sOutput != null) sOutput.close();
        } catch (Exception e) {
        } // not much else I can do
        try {
            if (socket != null) socket.close();
        } catch (Exception e) {
        } // not much else I can do

        // inform the GUI
        connectionFailed();

    }
  /*  public void keyPressed(KeyEvent var1) {
      //  if (var1.getCode(KeyCode) == 10 && !this.txtUserMsg.getText().trim().equals("")) {
            this.SendMessage();
        }

    }

    public void keyTyped(KeyEvent var1) {
    }

    public void keyReleased(KeyEvent var1) {
    }
    FontMetrics fontmetrics;
    protected void AddMessageToMessageObject(String Message, int MessageType)
    {

        String m_Message="";
        StringTokenizer tokenizer = new StringTokenizer(Message," ");
        while(tokenizer.hasMoreTokens())
        {



                AddMessage(m_Message,MessageType);
                m_Message="";
            }


        AddMessage(m_Message,MessageType);
    }

    private void SendMessage() {
        this.AddMessageToMessageObject(this.username + ": " + this.txtUserMsg.getText(), 0);
        this.SentPrivateMessageToServer(this.txtUserMsg.getText(), this.username);
        this.txtUserMsg.setText("");
        this.txtUserMsg.requestFocus();
    }


    private void SendMessageToServer(String var1) {
        try {
            this.dataoutputstream.writeBytes(var1 + "\r\n");
        } catch (IOException var3) {
            this.QuitConnection(0);
        }

    }
    protected void SentPrivateMessageToServer(String Message, String ToUserName)
    {
        SendMessageToServer("PRIV "+ToUserName+"~"+username+": "+Message);
    }

    private void QuitConnection(int var1) {
        if (this.socket != null) {
            try {
                if (var1 == 0) {
                    this.SendMessageToServer("QUIT " + this.username + "~");
                }

                if (var1 == 1) {
                    this.SendMessageToServer("KICK " + this.username + "~");
                }

                this.socket.close();
                this.socket = null;
                // this.tappanel.UserCanvas.ClearAll();
            } catch (IOException var3) {

            }
        }

        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }

        this.DisableAll();


    }*/

    private void DisableAll() {
        this.txtAreaServerMsgs.setEditable(false);
        this.txtUserMsg.setEditable(false);
        this.username = "";
    }

    public void connectionFailed() {
        connected = false;
    }


@Override
    public void initialize(URL location, ResourceBundle resources) {
            loginControl();
            dataMessage = FXCollections.observableArrayList();
            txtTitle.setText(offeringRequest.getAccountName());
    txtNumber.setText(offeringRequest.getNumber());
            listOne.setVisible(false);

            try {
                loadDataFromDataBase();
            } catch (SQLException e) {
                e.printStackTrace();
            }


      /*      dataMessage = FXCollections.observableArrayList();
            txtTitle.setText(offeringRequestIntract.getAccountName());
            txtNumber.setText(offeringRequestIntract.getNumber());
            listOne.setVisible(false);

            try {
                loadDataFromDataBaseInTract();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }*/



        // System.out.println(offeringRequest.getID() + "44444444444444444445555555555555555555555666666666666666");
        //privateLabel.setVisible(true);
        //privateLabel.setText("SEND  A PRIVATE MESSAGE TO :=>"+count);


    /*    try {
            loadDataFromDataBase();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }


    /*
     * a class that waits for the message from the server and append them to the JTextArea
     * if we have a GUI or simply System.out.println() it in console mode*/

    DataOutputStream outputToClient = null;

    class ListenFromServer extends Thread {
//InProcessing inProcessing = new InProcessing();
TrayNotification tray = new TrayNotification();
ChatMessage chatMessage;
        public void run() {
            //   inProcessingController.chosenAccount = (InProcessing) inProcessingController.tableProduct.getItems().get(inProcessingController.tableProduct.getSelectionModel().getSelectedIndex());

            users = FXCollections.observableArrayList();
            listUser.setItems(users);
            while (true) {
                try {

                    String msg = (String) sInput.readObject();


                    String[] split = msg.split(":");

                    if (split[1].equals("WHOISIN")) {
                        Platform.runLater(() -> {
                            users.add(split[0]);
                            //	users.clear();
                        });
                        //   txtAreaServerMsgs.appendText(msg);
                        //System.out.println(users + "Server User connect");

        /*            }else if (split[2].equals("REMOVE")) {
                        Platform.runLater(() -> {
                            users.remove(split[1]);
                        });
*/

                    } else {

                        txtAreaServerMsgs.appendText(msg);
                         //  chatMessage.showCustom();
                     //   UsefulUtils.showInformationDialog("Запит в обробці " + User.getContactName());
                      //  showWindow();
                    }


                } catch (IOException e) {
                    display("Server has close the connection");
                    connectionFailed();
                    Platform.runLater(() -> {
                        listUser.setItems(null);

                    });

                    break;

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                // can't happen with a String object but need the catch anyho


            }


        }


    }


}
