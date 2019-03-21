package com.mainPage.page;

import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXTabPane;
import com.login.User;
import com.mainPage.All.AllController;
import com.mainPage.ArchiveFiles.ArchiveFilesController;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTractController;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.NotFulled.ProductAdd.ObservableNF;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.Statistic.StatisticController;
import com.mainPage.createRequest.searchCounterpart.Counterpart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class MainPageController implements Initializable, ObservableNF {


    @FXML
    private BorderPane anchorPane;
    @FXML
    public Menu administrationID;
    public User offeringRequest;
    public ObservableList optionsStatusRequest = FXCollections.observableArrayList();

    public User getOfferingRequest() {
        return offeringRequest;
    }


    public void setOfferingRequest(User offeringRequest, boolean status) {
        this.offeringRequest = offeringRequest;


    }


    public void setChatController(ChatController controller) {
        chatController = controller;
        notFulledViewController.setChatController(chatController);
    }


    public ChatController getChatController() {
        return chatController;
    }

    public void setInProcessingViewController() { }

    private InProcessing inProcessing;

    @FXML
    public NotFulledController notFulledViewController;

    @FXML
    public InProcessingController inProcessingViewController;

    @FXML
    public InTractController inTractViewController;
    @FXML
    public ArchiveFilesController archiveFilesViewController;
    @FXML
    public AllController allViewController;
    @FXML
    public StatisticController statisticViewController;


    @FXML
    public JFXTabPane tabPane;
    @FXML
    public Tab tabInProcessing;
    @FXML
    public Tab tabNotFulledView;
    @FXML
    public Tab tabInTract;
    @FXML
    public Tab tabArchive;
    @FXML
    public Tab tabAll;
    @FXML
    public Tab tabStatistic;
    //  @FXML
    // private Tab tabStructure;
    // @FXML
    // private StructureController structureViewController;


    public ChatController chatController;
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.ENTER,
            KeyCombination.CONTROL_DOWN);
    private static List<ObserverNF> listElements = new LinkedList<>();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        administrationID.setVisible(false);
        try {
            // tabPane.setDisableAnimation(true);
            notFulledViewController.init(this);
            inProcessingViewController.init(this);
            inTractViewController.init(this);
            archiveFilesViewController.init(this);
            allViewController.init(this);
            //structureViewController.init(this);
            statisticViewController.init(this);
            new DBConnection().initMainWindow(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        add(inProcessingViewController);
        add(notFulledViewController);
        add(inTractViewController);
        add(archiveFilesViewController);
        add(allViewController);




        tabPane.setDisableAnimation(true);

        optionsStatusRequest.addAll(tabNotFulledView, tabInProcessing, tabInTract, tabArchive, tabAll, tabStatistic);
        tabNotFulledView.setDisable(true);
        tabInProcessing.setDisable(true);
        tabInTract.setDisable(true);
        tabArchive.setDisable(true);
        tabAll.setDisable(true);
        tabStatistic.setDisable(true);



        notFulledViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                changeExists();
            }
        });
        inProcessingViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                changeExists();
            }
        });
        inTractViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                changeExists();
            }
        });
        archiveFilesViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                changeExists();
            }
        });
        allViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                changeExists();
            }
        });


        setTabImages();
        // Image imageTabStructure = new Image(getClass().getResourceAsStream("/images/Structur.png"));
        //tabStructure.setGraphic(new ImageView(imageTabStructure));


    }

    public void setTabImages() {
        ImageView imageTabNotefulled = new ImageView(new Image("/images/NotFulle.png"));


        ImageView imageTabInProcessing = new ImageView(new Image("/images/InProces.png"));


        ImageView imageTabInTract = new ImageView(new Image("/images/InTract.png"));


        ImageView imageTabArchive = new ImageView(new Image("/images/Archive.png"));


        ImageView imageTabAll = new ImageView(new Image("/images/All.png"));


        ImageView imageTabStatistic = new ImageView(new Image("/images/Statisti.png"));

        setImageSize((new ArrayList<>(Arrays.asList(imageTabNotefulled, imageTabInProcessing, imageTabInTract, imageTabArchive, imageTabAll, imageTabStatistic))), 45);


        tabNotFulledView.setGraphic(imageTabNotefulled);
        tabInProcessing.setGraphic(imageTabInProcessing);
        tabInTract.setGraphic(imageTabInTract);
        tabArchive.setGraphic(imageTabArchive);
        tabAll.setGraphic(imageTabAll);
        tabStatistic.setGraphic(imageTabStatistic);
    }

    private void setImageSize(ArrayList<ImageView> image, int size) {

        image.forEach(value -> {
            value.setFitWidth(size);
            value.setFitHeight(size);
        });


    }

    public void mainGridHandle(NotFulfilled value) {
        //  NotFulledController.setSelectedRecord(value);
    }


    private static Logger log = Logger.getLogger(Counterpart.class.getName());
    @FXML
    private ListView<Object> mainListView;


    public void setDisableWindow(boolean type) {
        //    mainWindow.setDisable(type);
    }


    @Override
    public void add(ObserverNF value) {
        listElements.add(value);
    }

    @Override
    public void remove(ObserverNF value) {
        listElements.remove(value);
    }

    @FXML
    private void actionUpdateProgram(ActionEvent event) {
        Stage stage = new Stage();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/UpdateWindow.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    public void actionPageAdministation(ActionEvent event) {
        try {
        Stage stage = new Stage();


            FXMLLoader root = new FXMLLoader(getClass().getResource("/views/PageAdministration.fxml"));
            Parent window = (Pane) root.load();

       PageAdministrationController conn = root.getController();
       conn.setWindow(notFulledViewController, inProcessingViewController, inTractViewController, archiveFilesViewController, allViewController, statisticViewController);
       conn.setMainPageController(this);
       stage.setScene(new Scene(window));
        stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeExists() {
        listElements.forEach(elem -> {
            System.out.println(elem);
            elem.update();
        });
    }


}
