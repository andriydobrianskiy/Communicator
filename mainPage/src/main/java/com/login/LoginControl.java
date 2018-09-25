package com.login;

import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import com.mainLogin.updateProgram.UpdateHelper;
import com.mainPage.InTract.InTractController;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginControl implements Initializable {


    private static Logger log = Logger.getLogger(DBConnection.class.getName());
    public User userID = new User();
    @FXML
    private TextField txtUser;
    @FXML
    private JFXToggleButton btnIP;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Label loginPage;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    TableView<String> tableView = new TableView<>();
    @FXML
    private JFXCheckBox chSaveMe;
    public Stage stage;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Credentials credentials = new Credentials();
 

    private DBConnection database = new DBConnection();
    private InTractController inTractController = new InTractController();
    @FXML
    private void loginAction(ActionEvent evt) {
        authenticate();
    }

    public void authenticate() {
        User user = new User(txtUser.getText(), txtPass.getText());

        if (chSaveMe.isSelected()) {
            credentials.setCredentials(txtUser.getText(), txtPass.getText());
        }
        try {


            final String URL = "jdbc:sqlserver://" + DBConnection.serverName +
                    ";databaseName=" + DBConnection.databaseName +
                    ";username=" + user.getName() +
                    ";password=" + user.getPassword();

            if (database.connect(URL)) {
                user.setAdminUnit();
                hideLoginScene();
            } else loginPage.setText("Неправильний пароль або логін");

        } catch (NullPointerException y){
      y.printStackTrace();
  } catch (Exception e ) {
            log.log(Level.SEVERE, "Exception " + e);


          }


    }


    public void handleEnterPressed(KeyEvent event) throws SQLException {
        if (event.getCode() == KeyCode.ENTER) {
            authenticate();

        }
    }

    public static ChatController conn;

    private void hideLoginScene() throws IOException {
        String hostname = "localhost";
        int port = 9001;
        String username = User.getContactName();
        String picture = "Default";
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainPageController.class.getResource("/views/MainPage.fxml"));
        Pane root = loader.load();
        stage.setResizable(true);
        Scene scene = new Scene(root, 1360, 760);
        MainPageController con = loader.getController();
        con.setOfferingRequest(userID, true);
        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
        Parent window = (Pane) fmxlLoader.load();
        conn = fmxlLoader.<ChatController>getController();
        Listener listener = new Listener(hostname, port, username, picture, conn);
        Thread x = new Thread(listener);
        x.start();
        stage.setTitle("Комунікатор "+ UpdateHelper.app_version + " (" + User.getContactName() + ")");
        stage.setScene(scene);
        stage.showAndWait();


    }


    @FXML
    public void closeSystem() {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtUser.setText(credentials.getUsername());
        txtPass.setText(credentials.getPassword());
        database.setServerName(ServerName2);

        database.setServerChatName(serverChatName2);
        ToggleGroup group = new ToggleGroup();
        btnIP.setToggleGroup(group);
        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle() != null) {

                database.setServerName(serverName1);
                inTractController.setServerName(serverNew1);

                database.setServerChatName(serverChatName1);


            } else {
                database.setServerName(ServerName2);
                inTractController.setServerName(serverNew2);
                database.setServerChatName(serverChatName2);
            }
        });
    }
}
