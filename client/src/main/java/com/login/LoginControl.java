package com.login;

import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXToggleButton;
import com.mainLogin.Main;
import com.mainLogin.updateProgram.Model.Modes;
import com.mainLogin.updateProgram.Model.Release;
import com.mainLogin.updateProgram.Model.ReleaseXMLParser;
import com.mainLogin.updateProgram.UpdateHelper;
import com.mainPage.InTract.InTractController;
import com.mainPage.page.MainPageController;
import com.mainPage.page.PageAdministration;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mainLogin.updateProgram.UpdateHelper.app_version;

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
    private MainPageController mainPageController;

    public Stage stage;
    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    private Credentials credentials = new Credentials();

    private ChatController chatController;
    PageAdministration pageAdministration = new PageAdministration();
    private ObservableList<PageAdministration> data;
    public static Boolean generalAccess;

    private DBConnection database = new DBConnection();
    private InTractController inTractController = new InTractController();
    private static LoginControl instance;

    public void setChatController(ChatController chatController){
        this.chatController = chatController;
    }
    public LoginControl() {
        instance = this;
    }

    public static LoginControl getInstance() {
        return instance;
    }
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
        String hostname = "192.168.10.144";
        int port = 10023;
        String username = User.getContactName();
        String picture = "Default";
        Stage stage = (Stage) anchorPane.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainPageController.class.getResource("/views/MainPage.fxml"));
        Pane root = loader.load();
        stage.setResizable(true);
        Release release = new Release();
        release.setpkgver(app_version);

        release.setPkgrel("4");

        ReleaseXMLParser parser = new ReleaseXMLParser();
        try {
            Release current = null;

            current = parser.parse("ftp://192.168.10.101/mainPage/latest.xml", Modes.URL);


            if (current.compareTo(release) > 0) {
                if (UsefulUtils.showConfirmDialog("Нове оновлення! "+current.getpkgver()+" \n Оновити зараз?") == ButtonType.OK) {
                    try {
                        stage.close();
                        if(new UpdateHelper().checkForUpdate() == true) return;

                    } catch (Exception e) {
                        Platform.runLater(() -> UsefulUtils.showErrorDialogDown("Оновлення неможливе. Зверніться до адміністраторів!"));
                        return;
                    }
                }
            } else {

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root, 1360, 760);
        MainPageController con = loader.getController();
        con.setOfferingRequest(userID, true);
        mainPageController = con;
        data = FXCollections.observableArrayList();
        loadDataFromDatabaseContact();

        administrationGeneralNotFulled(mainPageController);
        administrationGeneralInProcesing(mainPageController);
        administrationGeneralInTract(mainPageController);
        administrationGeneralArchive(mainPageController);
        administrationGeneralAll(mainPageController);
        administrationGeneralWindowTab(mainPageController);
        stage.setTitle("Комунікатор "+ UpdateHelper.app_version + " (" + User.getContactName() + ")");
        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));
        Parent window = (Pane) fmxlLoader.load();
        conn = fmxlLoader.getController();
        con.setChatController(conn);

        Listener listener = new Listener(hostname, port, username, picture, conn);
        Thread x = new Thread(listener);
        x.start();
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

    public void administrationGeneralNotFulled (MainPageController mainPageController){

        for(int j = 0; j< data.toArray().length; j++) {
            if (data.get(j).getType() != null) {

                if (data.get(j).getContactID().equals(User.getContactID()) || data.get(j).getJobID().equals(User.getJobID())) {
                    for (int i = 0; i < mainPageController.notFulledViewController.optionsStatusRequest.toArray().length; i++) {
                        if (mainPageController.notFulledViewController.tbn_CreateRequest.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.notFulledViewController.tbn_CreateRequest.setVisible(true);

                            break;
                        }else if(mainPageController.notFulledViewController.btn_ConfirmRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_ConfirmRequest.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_СancelRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_СancelRequest.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_changeRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_changeRequest.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btnDelete.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btnDelete.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_ButtonAll.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_ButtonAll.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_ButtonPricingAll.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_ButtonPricingAll.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_AddRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_AddRequest.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_RefreshRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_RefreshRequest.setVisible(true);
                            break;
                        }else if (mainPageController.notFulledViewController.btn_DeleteRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.notFulledViewController.btn_DeleteRequest.setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    public void administrationGeneralInProcesing (MainPageController mainPageController) {

        for (int j = 0; j < data.toArray().length; j++) {
            if (data.get(j).getType() != null) {

                if (data.get(j).getContactID().equals(User.getContactID()) || data.get(j).getJobID().equals(User.getJobID())) {
                    for (int i = 0; i < mainPageController.inProcessingViewController.optionsStatusRequest.toArray().length; i++) {
                        if (mainPageController.inProcessingViewController.btnConfirmation.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btnConfirmation.setVisible(true);

                            break;
                        } else if (mainPageController.inProcessingViewController.btn_СancelRequest.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_СancelRequest.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.btn_ConfirmRequest.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_ConfirmRequest.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.notatkaName.equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.notatka = true;
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_UpdateStatus.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_UpdateStatus.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_ButtonAll.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_ButtonAll.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_Button.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_Button.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_ButtonAllPricing.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_ButtonAllPricing.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_ButtonPricing.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_ButtonPricing.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.searchingField.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.searchingField.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_ReturnOwner.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_ReturnOwner.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_Calculator.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_Calculator.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.btn_NotPrice.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_NotPrice.setVisible(true);
                            break;
                        } else if (mainPageController.inProcessingViewController.notNotification.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.notNotification.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.dateLabel.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.dateLabel.setVisible(true);
                            mainPageController.inProcessingViewController.datePicker.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.inProcessingRequestViewController.cancelColorByOfferingSaleButton.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.inProcessingRequestViewController.cancelColorByOfferingSaleButton.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.inProcessingRequestViewController.editButton.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.inProcessingRequestViewController.editButton.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.inProcessingRequestViewController.colorByOfferingSaleButton.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.inProcessingRequestViewController.colorByOfferingSaleButton.setVisible(true);
                            break;
                        }else if (mainPageController.inProcessingViewController.btn_UpdateOfferingGrpoupAddName.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inProcessingViewController.btn_UpdateOfferingGrpoupAddName.setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }


    public void administrationGeneralInTract (MainPageController mainPageController){

        for(int j = 0; j< data.toArray().length; j++) {
            if (data.get(j).getType() != null) {

                if (data.get(j).getContactID().equals(User.getContactID()) || data.get(j).getJobID().equals(User.getJobID())) {
                    for (int i = 0; i < mainPageController.inTractViewController.optionsStatusRequest.toArray().length; i++) {
                        if (mainPageController.inTractViewController.btnConfirmation.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.inTractViewController.btnConfirmation.setVisible(true);

                            break;
                        }else if(mainPageController.inTractViewController.btn_Delete.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.btn_Delete.setVisible(true);
                            break;
                        }else if (mainPageController.inTractViewController.btn_СancelRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.btn_СancelRequest.setVisible(true);
                            break;
                        }else if (mainPageController.inTractViewController.btn_СancelRequest.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.btn_СancelRequest.setVisible(true);
                            break;
                        }else if (mainPageController.inTractViewController.btn_Create.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.btn_Create.setVisible(true);
                            break;
                        }else if (mainPageController.inTractViewController.btn_Refresh.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.btn_Refresh.setVisible(true);
                            break;
                        }else if (mainPageController.inTractViewController.btn_ButtonAll.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.btn_ButtonAll.setVisible(true);
                            break;
                        }else if (mainPageController.inTractViewController.notNotification.getText().equals(data.get(j).getNameElements())){
                            mainPageController.inTractViewController.notNotification.setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    public void administrationGeneralArchive (MainPageController mainPageController){

        for(int j = 0; j< data.toArray().length; j++) {
            if (data.get(j).getType() != null) {

                if (data.get(j).getContactID().equals(User.getContactID()) || data.get(j).getJobID().equals(User.getJobID())) {
                    for (int i = 0; i < mainPageController.archiveFilesViewController.observableList.toArray().length; i++) {
                        if (mainPageController.archiveFilesViewController.btn_ButtonAll.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.archiveFilesViewController.btn_ButtonAll.setVisible(true);

                            break;
                        }else if(mainPageController.archiveFilesViewController.btn_ButtonPricing.getText().equals(data.get(j).getNameElements())){
                            mainPageController.archiveFilesViewController.btn_ButtonPricing.setVisible(true);
                            break;
                        }else if (mainPageController.archiveFilesViewController.btn_Button.getText().equals(data.get(j).getNameElements())){
                            mainPageController.archiveFilesViewController.btn_Button.setVisible(true);
                            break;
                        }else if (mainPageController.archiveFilesViewController.btn_ButtonAllPricing.getText().equals(data.get(j).getNameElements())){
                            mainPageController.archiveFilesViewController.btn_ButtonAllPricing.setVisible(true);
                            break;
                        }else if (mainPageController.archiveFilesViewController.btn_ReturnInProcessing.getText().equals(data.get(j).getNameElements())){
                            mainPageController.archiveFilesViewController.btn_ReturnInProcessing.setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    public void administrationGeneralAll (MainPageController mainPageController){

        for(int j = 0; j< data.toArray().length; j++) {
            if (data.get(j).getType() != null) {

                if (data.get(j).getContactID().equals(User.getContactID()) || data.get(j).getJobID().equals(User.getJobID())) {
                    for (int i = 0; i < mainPageController.allViewController.observableList.toArray().length; i++) {
                        if (mainPageController.allViewController.btn_ButtonAll.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.allViewController.btn_ButtonAll.setVisible(true);

                            break;
                        }else if(mainPageController.allViewController.btn_ButtonPricing.getText().equals(data.get(j).getNameElements())){
                            mainPageController.allViewController.btn_ButtonPricing.setVisible(true);
                            break;
                        }else if (mainPageController.allViewController.btn_Button.getText().equals(data.get(j).getNameElements())){
                            mainPageController.allViewController.btn_Button.setVisible(true);
                            break;
                        }else if (mainPageController.allViewController.btn_ButtonAllPricing.getText().equals(data.get(j).getNameElements())){
                            mainPageController.allViewController.btn_ButtonAllPricing.setVisible(true);
                            break;
                        }
                    }
                }
            }
        }
    }
    public void administrationGeneralWindowTab(MainPageController mainPageController){

        for(int j = 0; j< data.toArray().length; j++) {
            if (data.get(j).getType() != null) {

                if (data.get(j).getContactID().equals(User.getContactID()) || data.get(j).getJobID().equals(User.getJobID())) {
                    for (int i = 0; i < mainPageController.optionsStatusRequest.toArray().length; i++) {
                        if (mainPageController.tabNotFulledView.getText().equals(data.get(j).getNameElements())) {
                            mainPageController.tabNotFulledView.setDisable(false);

                            break;
                        }else if(mainPageController.tabInProcessing.getText().equals(data.get(j).getNameElements() )){
                            mainPageController.tabInProcessing.setDisable(false);
                            break;
                        }else if (mainPageController.tabInTract.getText().equals(data.get(j).getNameElements())){
                            mainPageController.tabInTract.setDisable(false);
                            break;
                        }else if (mainPageController.tabArchive.getText().equals(data.get(j).getNameElements())){
                            mainPageController.tabArchive.setDisable(false);
                            break;
                        }else if (mainPageController.tabAll.getText().equals(data.get(j).getNameElements())){
                            mainPageController.tabAll.setDisable(false);
                            break;
                        }else if (mainPageController.tabStatistic.getText().equals(data.get(j).getNameElements())){
                            mainPageController.tabStatistic.setDisable(false);
                            break;
                        }else if (mainPageController.administrationID.getText().equals(data.get(j).getNameElements())){
                            mainPageController.administrationID.setVisible(true);
                            break;
                        }

                    }
                }
            }
        }
    }

    public void loadDataFromDatabaseContact() {

        try {
            ObservableList<PageAdministration> listItems = (ObservableList<PageAdministration>) pageAdministration.findAll(User.getContactID(), User.getJobID());

            listItems.forEach(item -> data.add((PageAdministration) item));

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
    }


}
