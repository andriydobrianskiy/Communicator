package com.mainPage.page;

import com.client.chatwindow.ChatController;
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
import com.mainPage.OrderRegistryPoland.OrderRegistryPolandController;
import com.mainPage.OrderRegistryUkraine.OrderRegistryUkraineController;
import com.mainPage.Statistic.StatisticController;
import com.mainPage.createRequest.searchCounterpart.Counterpart;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainPageController  implements Initializable, ObservableNF {


    @FXML
    private BorderPane anchorPane;
    public User offeringRequest;


    public User getOfferingRequest() {
        return offeringRequest;
    }


    public void setOfferingRequest(User offeringRequest, boolean status) {
        this.offeringRequest = offeringRequest;


    }



    public void setChatController(ChatController controller) {

        chatController = controller;
    }

    public ChatController getChatController() {
        return chatController;
    }

    public void setInProcessingViewController() {

    }

    private InProcessing inProcessing;

    @FXML
    private NotFulledController notFulledViewController;

    @FXML
    private InProcessingController InProcessingViewController;
    // @FXML private ExampleController exampleProductTableController;
    @FXML
    private InTractController inTractViewController;
    @FXML
    private ArchiveFilesController archiveFilesViewController;
    @FXML
    private AllController allViewController;
    @FXML
    private StatisticController statisticViewController;
    @FXML
    private OrderRegistryUkraineController orderRegistryUkraineViewController;
    @FXML
    private OrderRegistryPolandController orderRegistryPolandViewController;


    @FXML
    private TabPane TabPane;
    @FXML
    private Tab tabInProcessing;
    @FXML
    private Tab tabDictionary;
    @FXML
    private Tab tabInTract;
    @FXML
    private Tab tabArchive;
    @FXML
    private Tab tabAll;
    @FXML
    private Tab tabStatistic;
  //  @FXML
   // private Tab tabStructure;
   // @FXML
   // private StructureController structureViewController;
    @FXML
    private Tab tabOrderRegistryPoland;
    @FXML
    private Tab tabOrderRegistryUkraine;
    public ChatController chatController;
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.ENTER,
            KeyCombination.CONTROL_DOWN);
    private static List<ObserverNF> listElements = new LinkedList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {


        notFulledViewController.init(this);
        InProcessingViewController.init(this);
        inTractViewController.init(this);
        archiveFilesViewController.init(this);
        allViewController.init(this);
        //structureViewController.init(this);
        statisticViewController.init(this);
        orderRegistryUkraineViewController.init(this);
        orderRegistryPolandViewController.init(this);


        add(InProcessingViewController);
        add(notFulledViewController);
        add(inTractViewController);
        add(archiveFilesViewController);
        add(allViewController);
     /*   TabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if(newTab == TabPane.getTabs().get(1)){
                System.out.println(TabPane.getTabs().get(1).toString() + "Tab pane");
                //                    chatController.sendButtonAction();
            }else if(newTab == TabPane.getTabs().get(2)){
                //      chatController.sendButtonActionTract();
            };



        });*/




        notFulledViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.F5) {
                changeExists();
            }
        });
        InProcessingViewController.tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
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

        System.out.println("WWWWWWWWWWWWWWWWWWWW");
        listElements.forEach(System.out::println);
        Image imageTabNotefulled = new Image(getClass().getResourceAsStream("/images/NotFulle.png"));
        tabDictionary.setGraphic(new ImageView(imageTabNotefulled));

        Image imageTabInProcessing = new Image(getClass().getResourceAsStream("/images/InProces.png"));
        tabInProcessing.setGraphic(new ImageView(imageTabInProcessing));

        Image imageTabInTract = new Image(getClass().getResourceAsStream("/images/InTract.png"));
        tabInTract.setGraphic(new ImageView(imageTabInTract));

        Image imageTabArchive = new Image(getClass().getResourceAsStream("/images/Archive.png"));
        tabArchive.setGraphic(new ImageView(imageTabArchive));

        Image imageTabAll = new Image(getClass().getResourceAsStream("/images/All.png"));
        tabAll.setGraphic(new ImageView(imageTabAll));

        Image imageTabStatistic = new Image(getClass().getResourceAsStream("/images/Statisti.png"));
        tabStatistic.setGraphic(new ImageView(imageTabStatistic));

       // Image imageTabStructure = new Image(getClass().getResourceAsStream("/images/Structur.png"));
       //tabStructure.setGraphic(new ImageView(imageTabStructure));


    }
    public void messageSend(){

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

    @Override
    public void changeExists() {
        listElements.forEach(elem -> {
            System.out.println(elem);
            elem.update();
        });
    }


}
