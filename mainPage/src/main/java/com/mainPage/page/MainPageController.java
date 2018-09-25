package com.mainPage.page;

import com.client.chatwindow.ChatController;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;
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
    private JFXTabPane tabPane;
    @FXML
    private Tab tabInProcessing;
    @FXML
    private Tab tabNotFulledView;
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


    public ChatController chatController;
    final KeyCombination keyComb1 = new KeyCodeCombination(KeyCode.ENTER,
            KeyCombination.CONTROL_DOWN);
    private static List<ObserverNF> listElements = new LinkedList<>();



    @Override
    public void initialize(URL location, ResourceBundle resources) {

       // tabPane.setDisableAnimation(true);
        notFulledViewController.init(this);
        InProcessingViewController.init(this);
        inTractViewController.init(this);
        archiveFilesViewController.init(this);
        allViewController.init(this);
        //structureViewController.init(this);
        statisticViewController.init(this);



        add(InProcessingViewController);
        add(notFulledViewController);
        add(inTractViewController);
        add(archiveFilesViewController);
        add(allViewController);



        tabPane.setDisableAnimation(true);

     /*   TabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            if(newTab == TabPane.getTabs().get(1)){
                System.out.println(TabPane.getTabs().get(1).toString() + "Tab pane");
                //                    chatController.sendButtonAction();
            }else if(newTab == TabPane.getTabs().get(2)){
                //      chatController.sendButtonActionTract();
            };



        });*/

       // tabInTract.setDisable(true);
      //  tabStatistic.setDisable(true);


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


setTabImages();
       // Image imageTabStructure = new Image(getClass().getResourceAsStream("/images/Structur.png"));
       //tabStructure.setGraphic(new ImageView(imageTabStructure));


    }
    public void setTabImages(){
        ImageView imageTabNotefulled = new ImageView(new Image("/images/NotFulle.png"));


        ImageView imageTabInProcessing = new  ImageView(new Image("/images/InProces.png"));


        ImageView imageTabInTract = new  ImageView(new Image("/images/InTract.png"));


        ImageView imageTabArchive = new  ImageView(new Image("/images/Archive.png"));


        ImageView imageTabAll = new  ImageView(new Image("/images/All.png"));


        ImageView imageTabStatistic = new ImageView(new Image("/images/Statisti.png"));

        setImageSize((new ArrayList<>(Arrays.asList(imageTabNotefulled, imageTabInProcessing,imageTabInTract, imageTabArchive, imageTabAll,imageTabStatistic))), 45);



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
    private void actionUpdateProgram(ActionEvent event){
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
   /* @FXML
    public void actionPageAdministation(ActionEvent event) {
        Stage stage = new Stage();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/views/PageAdministration.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(new Scene(root));
        stage.show();
    }*/

    @Override
    public void changeExists() {
        listElements.forEach(elem -> {
            System.out.println(elem);
            elem.update();
        });
    }



}
