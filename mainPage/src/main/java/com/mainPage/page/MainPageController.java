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
import com.mainPage.Sructure.StructureController;
import com.mainPage.Statistic.StatisticController;
import com.mainPage.createRequest.searchCounterpart.Counterpart;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public  ChatController chatController;

    public void setChatController(ChatController controller){

        chatController = controller;
    }

    public  ChatController getChatController() {
        return chatController;
    }

    public void setInProcessingViewController() {

    }

    /* public InProcessingController getInProcessingViewController() {
         return inProcessingViewController;
     }
     public void setInProcessingViewController (InProcessingController inProcessingController){
         this.inProcessingViewController = inProcessingController;
     }*/
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
    @FXML
    private Tab tabStructure;
    @FXML
    private StructureController structureViewController;

    private static List<ObserverNF> listElements = new LinkedList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        notFulledViewController.init(this);
        InProcessingViewController.init(this);
        inTractViewController.init(this);
        archiveFilesViewController.init(this);
        allViewController.init(this);
        structureViewController.init(this);
        statisticViewController.init(this);



        add(InProcessingViewController);
        add(notFulledViewController);
        add(inTractViewController);
        add(archiveFilesViewController);
        add(allViewController);


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

        Image imageTabStructure = new Image(getClass().getResourceAsStream("/images/Structur.png"));
        tabStructure.setGraphic(new ImageView(imageTabStructure));



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
        System.out.println("23232323232323");
        //notFulledViewController.refreshData();
        listElements.forEach(elem -> {
            System.out.println(elem);
            elem.update();
        });
    }
}
