package com.login;

import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.connectDatabase.DBConnection;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
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

public class LoginControl  implements Initializable {


    private static Logger log = Logger.getLogger(DBConnection.class.getName());
    public User userID  = new User();
    @FXML
    private TextField txtUser;
    @FXML
    private PasswordField txtPass;
    @FXML
    private Label loginPage;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView progress;
    @FXML
    TableView<String> tableView = new TableView<>();
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    /*@FXML
    private Button loginButton;
    @FXML
    private AnchorPane anchorPane;*/

    private DBConnection database = new DBConnection();



    @FXML
    private void loginAction(ActionEvent evt) {
        progress.setVisible(true);
      //  PauseTransition pt = new PauseTransition();
     //   pt.setDuration(Duration.seconds(6));
   //     pt.setOnFinished(ev -> {
    //        System.out.println("dswgfegfe");
   //     });

   //     pt.play();
        authenticate();


    }

    public void authenticate() {
        try {


            User user = new User(txtUser.getText(), txtPass.getText());

            final String URL = "jdbc:sqlserver://" + DBConnection.serverName +
                    ";databaseName=" + DBConnection.databaseName +
                    ";username=" + user.getName() +
                    ";password=" + user.getPassword();

            if (database.connect(URL)) {
                progress.setVisible(true);




                user.setAdminUnit();
                hideLoginScene();

                System.out.println(User.getContactID()+ "2222222222222222222222222222222");


            } else loginPage.setText("Неправильний пароль або логін");

        } catch (Exception e) {
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
        stage.close();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainPageController.class.getResource("/views/MainPage.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("/sample/resources/view/MainPage.fxml"));
        Pane root  = loader.load();
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
        txtUser.setText("Добрянський Андрій");
        txtPass.setText("357753");

        progress.setVisible(false);

    }
}
