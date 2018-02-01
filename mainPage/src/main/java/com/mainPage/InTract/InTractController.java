package com.mainPage.InTract;

import com.Utils.CustomPaginationSkin;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.login.User;
import com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController;
import com.mainPage.InTract.InTractRequest.InTractRequestController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InTractController extends BorderPane implements Initializable, DictionaryPropertiesInTract, ObserverNF {

    private static Logger log = Logger.getLogger(InTractController.class.getName());
    private MainPageController main;
    private PreparedStatement pst = null;
    private Connection con = null;
    @FXML
    private InTractRequestController inTractRequestViewController;
    @FXML
    private TextField searchingField;
    @FXML
    private TableView tableView;
    @FXML
    private Pagination pagination;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnConfirmation;
    @FXML
    private JFXButton btn_Delete;
    @FXML
    private JFXButton btn_СancelRequest;
    @FXML
    private JFXButton btn_Create;
    @FXML
    private JFXButton btn_Refresh;
    public  ChatController conn;
    private Scene scene;
    /*@FXML
    private InTractRequestController inTractRequestController;*/
    private ObservableList<InTract> data;
    private InTract account = new InTract();
    public static InTract chosenAccount = null;
    private long fromIndex;
    private long toIndex;
    private InTractQuery accountQueries = new InTractQuery();


   /* public InTractController (){
        refreshData();
    }*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_Delete.setVisible(false);
        btn_СancelRequest.setVisible(false);
        btn_Create.setVisible(false);
        btn_Refresh.setVisible(false);


        inTractRequestViewController.init(this);

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    //closeWindow();
                }
            }
        });
        btnConfirmation.setOnAction(event -> {

            try {

                chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                System.out.println(chosenAccount.getID() + "8888888888888888888888888885555555555555555555555555555566666666666666666666666");
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви підтверджуєте, що запчастини прибули на стан і бажаєте завершити замовлення?") == ButtonType.OK) {
                String query = ("UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET [StatusID] = '{6F784E48-1474-4CB9-B28D-A4B580FB346C}',\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)");
                try {
                    pst = con.prepareStatement(query);
                   // pst.setString(1, User.getContactID());
                    pst.setString(1, User.getContactID());
                    pst.setString(2, chosenAccount.getID());
                    pst.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит "+chosenAccount.getNumber() +" завершено");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });


        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                       chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

                        NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);
                        loginButtonAction(chosenAccount);
                     //  ClientController.recordInTract = chosenAccount;
                   //     System.out.println(chosenAccount.getID() + "5649848949849845316546854684896");
                   //     ClientController clientController = new ClientController(chosenAccount, false);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });
//loadDataFromDatabase();
        createTableColumns();
        tableViewHandles();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination

        pagination.setSkin(pageSkin);
        pagination.setPageFactory(this::createPage);
        searchCode();

    }



    public void loginButtonAction(InTract chosenAccount) throws IOException {
        String hostname = "192.168.10.192";
        int port = 9001;
        String username = User.getContactName();
        String picture = "Default";


        FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));

        Parent window = (Pane) fmxlLoader.load();
        conn = fmxlLoader.<ChatController>getController();
        //  instance = fmxlLoader.<InProcessingController>getController();
        conn.setOfferingTract(chosenAccount);
        conn.setInTract(this);
        Listener listener = new Listener(hostname, port, username, picture, conn, this, chosenAccount);
        Thread x = new Thread(listener);
        x.start();

        this.scene = new Scene(window);


    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            // (Stage) hostnameTextfield.getScene().getWindow();
            //   stage.getScene();
//stage.showAndWait();
            stage.setResizable(true);
            stage.setWidth(1040);
            stage.setHeight(620);

            //        stage.setOnCloseRequest((WindowEvent e) -> {
            //           Platform.exit();
            //       System.exit(0);
            //    });
            stage.setScene(this.scene);

            //  stage.setMinWidth(800);
            //   stage.setMinHeight(300);

            ResizeHelper.addResizeListener(stage);
            //      stage.showAndWait();
            System.out.println("chatFive");

            conn.setUsernameLabel(User.getContactName());
            conn.setImageLabel("Default");
            conn.setIdTextFild(chosenAccount.getID());
            stage.showAndWait();
        });
    }
    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }


    private void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> fixSelectedRecord());
    }

    private void fixSelectedRecord() {
        InTract record = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println(record);
        inTractRequestViewController.handleTableView(record);

    }

    @Override
    public void createTableColumns() {

        try {
            TableColumn<InTract, String> number = new TableColumn<InTract, String>("Номер запиту");
            TableColumn<InTract, String> createdOn = new TableColumn<InTract, String>("Дата");
            TableColumn<InTract, String> createdBy = new TableColumn<InTract, String>("Створив");
            TableColumn<InTract, String> accountCode = new TableColumn<InTract, String>("Код контрагента");
            TableColumn<InTract, String> accountName = new TableColumn<InTract, String>("Контрагент");
            TableColumn<InTract, String> accountSaldo = new TableColumn<InTract, String>("Сальдо");
            TableColumn<InTract, String> accountIsSolid = new TableColumn<InTract, String>("Солідність");
            TableColumn<InTract, String> storeCity = new TableColumn<InTract, String>("Місто поставки");
            TableColumn<InTract, String> status = new TableColumn<InTract, String>("Статус");
            TableColumn<InTract, String> groupChangedBy = new TableColumn<InTract, String>("Змінив групу");
            TableColumn<InTract, String> specialMarginTypeName = new TableColumn<InTract, String>("Тип спец націнки");
            TableColumn<InTract, String> stateName = new TableColumn<InTract, String>("Статус запиту");
            TableColumn<InTract, String> offeringGroupName = new TableColumn<InTract, String>("Група товарів");
            TableColumn<InTract, String> originalGroupName = new TableColumn<InTract, String>("Початкова група");


           // number.setVisible(false);
            tableView.setTableMenuButtonVisible(true);

            number.setMinWidth(150);

            //    id.setVisible(false);

            tableView.getColumns().addAll(
                    number,
                    createdOn,
                    createdBy,
                    accountCode,
                    accountName,
                    accountSaldo,
                    accountIsSolid,
                    storeCity,
                    status,
                    groupChangedBy,
                    specialMarginTypeName,
                    stateName,
                    offeringGroupName,
                    originalGroupName);
            number.setCellValueFactory(new PropertyValueFactory<InTract, String>("Number"));
            createdOn.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedOn"));
            createdBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedBy"));
            accountCode.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountCode"));
            accountName.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountName"));
            accountSaldo.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountSaldo"));
            accountIsSolid.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountIsSolid"));
            storeCity.setCellValueFactory(new PropertyValueFactory<InTract, String>("StoreCity"));
            status.setCellValueFactory(new PropertyValueFactory<InTract, String>("Status"));
            groupChangedBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("GroupChangedBy"));
            specialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InTract, String>("SpecialMarginTypeName"));
            stateName.setCellValueFactory(new PropertyValueFactory<InTract, String>("StateName"));
            offeringGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OfferingGroupName"));
            originalGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OriginalGroupName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }

    @Override
    public List<InTract> loadDataFromDatabase() {
        // System.out.println(chosenAccount.getOfferingGroupID()+ "7899999999999999999999999999989854645613256131651156" + User.getContactID());
        try {
            List<InTract> listItems = account.findAllInTract(true, (int) toIndex, User.getContactID(), User.getContactID());

            listItems.forEach(item -> data.add(item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        return null;
    }




    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainInTractCount());


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }

    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            loadDataFromDatabase();
        }

        UsefulUtils.fadeTransition(tableView);



    }
    private void searchCode() {
        FilteredList<InTract> filteredData = new FilteredList<>(data, e -> true);
        searchingField.setOnKeyReleased(e -> {
            searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super InTract>) user -> {
                    if (newValue == null || newValue.isEmpty()) {

                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getAccountCode().contains(newValue)) {
                        return true;
                    } else if (user.getOfferingGroupName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;

                });
            });
            SortedList<InTract> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);

        });
    }


    @Override
    public void update() {
        refreshData();
    }
}
