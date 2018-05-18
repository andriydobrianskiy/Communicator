package com.login;

import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
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
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Credentials credentials = new Credentials();


    /*@FXML
    private Button loginButton;
    @FXML
    private AnchorPane anchorPane;*/

    private DBConnection database = new DBConnection();


    @FXML
    private void loginAction(ActionEvent evt) {

        //  PauseTransition pt = new PauseTransition();
        //   pt.setDuration(Duration.seconds(6));
        //     pt.setOnFinished(ev -> {
        //        System.out.println("dswgfegfe");
        //     });

        //     pt.play();
      /*  Stage stage1 = new Stage();
        StackPane root1 = new StackPane();

        //   StackPane root1 = FXMLLoader.load(getClass().getResource( "/views/Login.fxml" ));
        stage1.setScene(new Scene(root1));

        CustomLauncherUI ui = new CustomLauncherUI();
        ui.init(stage1);

        Parent updater = ui.createLoader();
        root1.getChildren().addAll(updater);

        stage1.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(6));
        delay.setOnFinished( event -> stage1.close() );
        delay.play();*/
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
       /* Stage stage1 = new Stage();
        StackPane root1 = new StackPane();

        //   StackPane root1 = FXMLLoader.load(getClass().getResource( "/views/Login.fxml" ));
        stage1.setScene(new Scene(root1));

        CustomLauncherUI ui = new CustomLauncherUI();
        ui.init(stage1);

        Parent updater = ui.createLoader();
        root1.getChildren().addAll(updater);

        stage1.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(6));
        delay.setOnFinished( event -> stage1.close() );
        delay.play();*/
        String hostname = "localhost";
        int port = 9001;
        String username = User.getContactName();
        String picture = "Default";
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        //  stage.close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainPageController.class.getResource("/views/MainPage.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/view/MainPage.fxml"));
        Pane root = loader.load();
        Scene scene = new Scene(root, 1360, 760);
        MainPageController con = loader.getController();
        con.setOfferingRequest(userID, true);


        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
        Parent window = (Pane) fmxlLoader.load();
        conn = fmxlLoader.<ChatController>getController();
        //  instance = fmxlLoader.<InProcessingController>getController();


        Listener listener = new Listener(hostname, port, username, picture, conn);
        Thread x = new Thread(listener);
        x.start();
        stage.setTitle("Комунікатор 1.9 " + "(" + User.getContactName() + ")");

        tableView.setFixedCellSize(25);
        tableView.prefHeightProperty().bind(tableView.fixedCellSizeProperty().multiply(Bindings.size(tableView.getItems()).add(1.01)));
        tableView.minHeightProperty().bind(tableView.prefHeightProperty());
        tableView.maxHeightProperty().bind(tableView.prefHeightProperty());
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    public void closeSystem() {
        Platform.exit();
        System.exit(0);
    }

   /* @FXML
    public void minimizeWindow() {
        Main.getPrimaryStage().setIconified(true);
    }*/

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
                database.setServerChatName(serverChatName1);


            } else {
                database.setServerName(ServerName2);
                database.setServerChatName(serverChatName2);
            }
        });
        //   btnIP.setOnAction(event -> {

        //   });
    }
}
