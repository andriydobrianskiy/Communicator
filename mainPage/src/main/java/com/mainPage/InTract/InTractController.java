package com.mainPage.InTract;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.ListinerTract;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import com.login.User;
import com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController;
import com.mainPage.InTract.InTractRequest.InTractRequestController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.WorkArea;
import com.mainPage.page.MainPageController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableDateTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableDoubleTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InTractController extends WorkArea  implements MiniFilterFunction, Initializable, DictionaryProperties, ObserverNF {

    private static Logger log = Logger.getLogger(InTractController.class.getName());
    private MainPageController main;
    private PreparedStatement pst = null;
    private Connection con = null;
    private ResultSet rs = null;
    @FXML
    private InTractRequestController inTractRequestViewController;
    @FXML
    private TextField searchingField;

    @FXML
    private Pagination pagination;

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
    @FXML
    private ChatController chatViewController;
    @FXML
    private BorderPane anchorPane;

    @FXML
    private JFXToggleButton btn_ButtonAll;

    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();



    @FXML private FilterableStringTableColumn<InTract, String> colNumber;
    @FXML private FilterableDateTableColumn<InTract, String> colCreatedOn;
    @FXML private FilterableStringTableColumn <InTract, String> colCreatedBy;
    @FXML private FilterableStringTableColumn <InTract, String> colAccountCode;
    @FXML private FilterableStringTableColumn <InTract, String> colAccountName;
    @FXML private FilterableDoubleTableColumn<InTract, Double> colAccountSaldo;
    @FXML private FilterableStringTableColumn <InTract, String> colAccountIsSolid;
    @FXML private FilterableStringTableColumn <InTract, String> colStoreCity;
    @FXML private FilterableStringTableColumn <InTract, String> colStatus;
    @FXML private FilterableStringTableColumn <InTract, String> colOfferingGroupName;
    @FXML private FilterableStringTableColumn <InTract, String> colOriginalGroupName;
    @FXML private FilterableStringTableColumn <InTract, String> colGroupChangedBy;
    @FXML private FilterableStringTableColumn <InTract, String> colSpecialMarginTypeName;
    public  ChatController conn;
    private Scene scene;
    /*@FXML
    private InTractRequestController inTractRequestController;*/
    private ObservableList<InTract> data;

    private InTract account = new InTract();
    public  InTract chosenAccount = null;
    private long fromIndex;
    private long toIndex;
    private InTractQuery accountQueries = new InTractQuery();
    private Button id = new Button();
    private Button Number = new Button();
    private Button Solid = new Button();
    private Button Store = new Button();
    boolean order = true;
    private ImageView upImg = new ImageView(new Image("/images/Sort_Top.png"));
    private ImageView downImg = new ImageView(new Image("/images/Sort_Bottom.png"));
    int pageCount = 5;
    int itemsPerPage = 40;
    int currentPageIndex = 0;
    private Boolean queryAll = false;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getStylesheets().add
                (InTractController.class.getResource("/styles/TableStyle.css").toExternalForm());
        btnConfirmation.setVisible(true);
        btn_Delete.setVisible(false);
        btn_СancelRequest.setVisible(true);
        btn_Create.setVisible(false);
        btn_Refresh.setVisible(false);

        inTractRequestViewController.init(this);
        chatViewController.init(this);

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        btn_ButtonAll.setVisible(true);
        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle() != null) {
                queryAll = true;
                refreshData();
                searchingField.setOnAction(event1 -> {
                    String value = searchingField.getText();

                    if (value.equals("")) {
                        refreshData();
                    } else
                        try {
                            findByProperty(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });

            } else
            {

                queryAll = false;
                refreshData();
                searchingField.setOnAction(event1 -> {
                    String value = searchingField.getText();

                    if (value.equals("")) {
                        refreshData();
                    } else
                        try {
                          //  findByPropertyAll(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });

            }
        });
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);
                }
            }
        });
        btnConfirmation.setOnAction(event -> {

            try {

                chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
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
                    pst.setString(1, User.getContactID());
                    pst.setString(2, chosenAccount.getID());
                    pst.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " завершено");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });

        createTableColumns();
        tableViewHandles();
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(InTractController.this, account, hashColumns,event).setFilter();
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        UsefulUtils.copySelectedCell(tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        anchorPane.setCenter(tableView);
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);

            tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableView.getSelectionModel().setCellSelectionEnabled(false);





        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
            pagination.setSkin(pageSkin);

            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
            initializeTable();

            pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                    currentPageIndex = newValue.intValue();
                    updatePersonView();
                }
            });


            SortButton(Number);
            SortButton(id);
            SortButton(Solid);
            SortButton(Store);
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вдається відкрити сторінки");

        }

        searchingField.setOnAction(event1 -> {
            String value = searchingField.getText();

            if (value.equals("")) {
                refreshData();
            } else
                try {
                    findByProperty(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });
        // Button Cancel
        btn_СancelRequest.setOnAction(event -> {

            try {

                chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте анулювати запит?") == ButtonType.OK) {
                try {
                    CallableStatement callProc = con.prepareCall("{call dbo.tsp_ROActionNullifyRequest (?,?)}");
                    callProc.setString(1, chosenAccount.getID());
                    callProc.setString(2, User.getContactID());
                    callProc.executeUpdate();

                    tableView.setRowFactory(new Callback<TableView<InTract>, TableRow<InTract>>() {
                        @Override
                        public TableRow<InTract> call(TableView<InTract> param) {
                            return new TableRow<InTract>() {
                                @Override
                                protected void updateItem(InTract item, boolean empty) {
                                    super.updateItem(item, empty);

                                    setStyle("-fx-background-color: #ff0700");

                                }
                            };
                        }
                    });

                 refreshData();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " анульовано");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });

        tableView.setRowFactory(new Callback<TableView<InTract>, TableRow<InTract>>() {
            @Override
            public TableRow<InTract> call(TableView<InTract> param) {
                return new TableRow<InTract>() {
                    @Override
                    protected void updateItem(InTract item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            if (item == null || item.getIsReadMeassage() == null || item.getIsReadMeassage().equals(0)) {
                                setStyle("");
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2)){
                                    setStyle("-fx-background-color: #FF0000;");

                                }
                            } else {
                                setStyle("-fx-font-weight: bold");
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2))
                                    setStyle("-fx-background-color: #FF0000;" +
                                            "-fx-font-weight: bold");

                            }
                        } catch (NullPointerException ex) {

                        }
                    }
                };
            }
        });

        try {
            UpdateNotificationAllTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        tableView.getSelectionModel().select(0);

    }


    String Skrut = null;
    public void findByProperty(String value) {
        data.clear();

        try {
            pst = con.prepareStatement ("SELECT RequestID " +
                            "\t\t\t\t\tFROM dbo.tbl_OfferingInRequestOffering \n" +
                            " WHERE [tbl_OfferingInRequestOffering].[OfferingID] IN (SELECT ID\n" +
                            "        FROM [tbl_Offering] WHERE [tbl_Offering].Skrut LIKE '"+value.toString()+"%' OR [tbl_Offering].[Index] LIKE '"+value.toString()+"%')"//+
                    //  "FOR XML PATH('')"
            );
            rs = pst.executeQuery();
            while (rs.next()) {
                Skrut = rs.getString(1);
                List<InTract> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));



            }

        } catch (SQLException e) {
            e.printStackTrace();
        }



        tableView.setItems(data);



    }

    private void SortButton(Button button) {
        button.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent t) {
                pagination.setCurrentPageIndex(0);
                sort(button);
                if (order) {
                    Collections.reverse(data);
                }
                order = !order;
                button.setGraphic((order) ? upImg : downImg);
                updatePersonView();
            }
        });
    }

    private void sort(Button notFulfilled) {
        if (notFulfilled == id) {
            data.sort((InTract p1, InTract p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()));
        } else if (notFulfilled == Number) {
            data.sort((InTract p1, InTract p2) -> p1.getNumber().compareTo(p2.getNumber()));
        }else if (notFulfilled == Solid) {
            data.sort((InTract p1, InTract p2) -> p1.getAccountIsSolid().compareTo(p2.getAccountIsSolid()));
        } else if (notFulfilled == Store) {
            data.sort((InTract p1, InTract p2) -> p1.getNumber().compareTo(p2.getStoreCity()));
        } else {
            UsefulUtils.showErrorDialogDown("Помилка сортування");
        }
    }

    private void initializeTable() {


        colCreatedOn.setGraphic(id);
        colCreatedOn.setSortable(false);
        colNumber.setGraphic(Number);
        colNumber.setSortable(false);
        colAccountIsSolid.setGraphic(Solid);
        colAccountIsSolid.setSortable(false);
        colStoreCity.setGraphic(Store);
        colStoreCity.setSortable(false);

    }

    public void updatePersonView() {
        fromIndex = currentPageIndex * itemsPerPage;
        toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
    }



    public void loginButtonAction(InTract chosenAccount) throws IOException {
        String hostname = "192.168.10.146";
        int port = 9002;
        String username = User.getContactName();
        String picture = "Default";
        chatViewController.setOfferingTract(chosenAccount);
        chatViewController.setInTract(this);
        chatViewController.buttonSend.setVisible(false);
        chatViewController.setUsernameLabel(User.getContactName());
        ListinerTract listener = new ListinerTract(hostname, port, username, picture, chatViewController, this, chosenAccount);
        Thread x = new Thread(listener);
        x.start();




    }

    public void showScene() throws IOException {
        Platform.runLater(() -> {

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(true);
            stage.setWidth(1040);
            stage.setHeight(620);

            stage.setScene(this.scene);


            ResizeHelper.addResizeListener(stage);
            System.out.println("chatFive");

            conn.setUsernameLabel(User.getContactName());
            conn.setImageLabel("Default");
            stage.showAndWait();
        });
    }
    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }


    protected void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey ->{
            UsefulUtils.searchCombination(eventKey, tableView);
            fixSelectedRecord();});
    }

    protected void fixSelectedRecord() {
        InTract record = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println(record);
        inTractRequestViewController.handleTableView(record);
        chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        try {
            loginButtonAction(chosenAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void createTableColumns() {

        try {

            hashColumns.put(colNumber, "[tbl_RequestOffering].[Number]");
            hashColumns.put(colCreatedOn, "[tbl_RequestOffering].[CreatedOn]");
            hashColumns.put(colCreatedBy, "[tbl_Contact].[Name]");
            hashColumns.put(colAccountName, "[tbl_Account].[Name]");
            hashColumns.put(colAccountCode, "[tbl_Account].[Code]");
            hashColumns.put(colAccountSaldo, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colAccountIsSolid, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");
            hashColumns.put(colStatus, "[tbl_RequestOfferingStatus].[Name]");
            hashColumns.put(colStoreCity, "[tbl_StoreCity].[Name]");
            hashColumns.put(colOfferingGroupName, "[OfferingGroup].[Name]");
            hashColumns.put(colOriginalGroupName, "SELECT\n" +
                    "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n");
            hashColumns.put(colGroupChangedBy, "SELECT\n" +
                    "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n");
            hashColumns.put(colSpecialMarginTypeName, "[SMT].[Name]");
            List<?> listColumns = tableView.getColumns();

            colNumber.setCellValueFactory(new PropertyValueFactory<InTract, String>("Number"));
            colCreatedOn.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedOn"));
            colCreatedBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("CreatedBy"));
            colAccountCode.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountCode"));
            colAccountName.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountName"));
            colAccountSaldo.setCellValueFactory(new PropertyValueFactory<InTract, Double>("AccountSaldo"));
            colAccountIsSolid.setCellValueFactory(new PropertyValueFactory<InTract, String>("AccountIsSolid"));
            colStoreCity.setCellValueFactory(new PropertyValueFactory<InTract, String>("StoreCity"));
            colStatus.setCellValueFactory(new PropertyValueFactory<InTract, String>("Status"));

            colOfferingGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OfferingGroupName"));
            colOriginalGroupName.setCellValueFactory(new PropertyValueFactory<InTract, String>("OriginalGroupName"));
            colGroupChangedBy.setCellValueFactory(new PropertyValueFactory<InTract, String>("GroupChangedBy"));
            colSpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<InTract, String>("SpecialMarginTypeName"));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
        super.createTableColumns();
    }


    @Override
    public void loadDataFromDatabase() {
        data.clear();
        try {
            if(queryAll == true){
                List<InTract> listItems = account.findInTract(true, (int) toIndex, User.getContactID(), User.getContactID());
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            }else if (queryAll == false){
                List<InTract> listItems = account.findInTractAll(true, (int) toIndex);
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }

    }






    public BorderPane createPage(int pageIndex) {
        try {
            data = FXCollections.observableArrayList();
            loadDataFromDatabase();
            fromIndex = pageIndex * itemsPerPage;
            toIndex = Math.min(fromIndex + itemsPerPage, data.size());





            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }

        anchorPane.setCenter(tableView);
        return anchorPane;
    }

    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
         //   loadDataFromDatabase();
            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
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


    public void UpdateNotificationAllTable() throws IOException {
        tableView.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            chosenAccount = (InTract) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            if (chosenAccount != null) {
                if (event.getCode() == KeyCode.SPACE) {

                    if (chosenAccount.getIsReadMeassage() == null || chosenAccount.getIsReadMeassage().equals(0)) {
                        String queryUpdate = "UPDATE tbl_RequestOffering \n" +
                                "\t\t\t\t\tSET \n" +
                                "\t\t\t\t\tIsReadMeassage = '1'\n" +
                                "\t\t\t\t\tWHERE ID = ?";
                        try {
                            pst = DBConnection.getDataSource().getConnection().prepareStatement(queryUpdate);

                            pst.setString(1, chosenAccount.getID());
                            pst.executeUpdate();
                            main.changeExists();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String query = "UPDATE tbl_RequestOffering \n" +
                                "\t\t\t\t\tSET \n" +
                                "\t\t\t\t\tIsReadMeassage = '0'\n" +
                                "\t\t\t\t\tWHERE ID = ?";
                        try {
                            pst = DBConnection.getDataSource().getConnection().prepareStatement(query);

                            pst.setString(1, chosenAccount.getID());
                            pst.executeUpdate();
                            main.changeExists();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    @Override
    public void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/MiniFilter.fxml"));

        Pane content;
        try {
            content = fxmlLoader.load();


            MiniFilterController miniC = fxmlLoader.getController();

            miniC.setWindow(this, content);
            miniC.setFilter(column, type, value);

            hboxFilter.getChildren().add(content);


            hashMiniFilter.put(column, content);

            hboxFilter.setMargin(content, new Insets(0, 0, 0, 10));

            scrollPaneFilter.setFitToHeight(true);

            System.out.println("SETTTED");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }
    @Override
    public void disableFilter(TableColumn column, Pane content) {
        account.removeStringFilter(column);
        refreshData();
        if(content == null) {
            removeFilterFromHbox(column);
            return;
        }

        hboxFilter.getChildren().remove(content);


        System.out.println("DISABLED");
    }
    public void clearTable() {
        try {
            data.clear();

        } catch (Exception e) {
            data = FXCollections.observableArrayList();
        } finally {
            tableView.setItems(data);
        }
    }
    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }

    @Override
    public void update() {
        refreshData();
    }
}
