package com.mainPage.NotFulled;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.InitComponents;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.TooltipTableRow;
import com.Utils.UsefulUtils;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.login.User;
import com.mainPage.NotFulled.CreateRequest.CreateMainController;
import com.mainPage.NotFulled.Edit.EditController;
import com.mainPage.NotFulled.OfferingRequest.DerbyOfferingRequestDAO;
import com.mainPage.NotFulled.OfferingRequest.ExampleController;
import com.mainPage.NotFulled.OfferingRequest.OfferingRequest;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.NotFulled.ProductAdd.ProductAddController;
import com.mainPage.NotFulled.ProductAdd.ProductAddNewController;
import com.mainPage.WorkArea;
import com.mainPage.page.MainPageController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.dbutils.QueryRunner;
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

public class NotFulledController extends WorkArea implements MiniFilterFunction, Initializable, DictionaryPropertiesNotfulled, ObserverNF, DictionaryProperties {

    public static User userID;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private QueryRunner dbAccess = new QueryRunner();
    User user = new User();
    // Top table
    private Connection con = null;
    @FXML
    private JFXButton tbn_CreateRequest;
    @FXML
    private JFXButton btn_changeRequest;
    @FXML
    private JFXButton btn_ConfirmRequest;
    @FXML
    private JFXButton btn_СancelRequest;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btn_IntoProcessing;
    @FXML
    private JFXButton btn_UpdateStatus;
    @FXML
    private MainPageController main;
    @FXML
    private BorderPane anchorPane;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXTextField searchingField;
    @FXML
    private HBox hboxFilter;
    @FXML
    private JFXButton btn_AddRequest;
    @FXML
    private JFXButton btn_RefreshRequest;
    @FXML
    private JFXButton btn_DeleteRequest;
    @FXML
    private ToggleButton btn_ButtonAll;
    @FXML
    private ToggleButton btn_ButtonPricingAll;
    private Integer pricingBoolean = 0;

    private InitComponents components;

    MenuBar menuBar = new MenuBar();
    Menu mainMenu = new Menu("Файл");
    MenuItem exitCmd = new MenuItem("Вихід");
    MenuItem textCmd = new MenuItem("Пошук");

    private static Logger log = Logger.getLogger(NotFulledController.class.getName());


    private ObservableList<NotFulfilled> data;
    private NotFulledQuery accountQueries = new NotFulledQuery();

    private long fromIndex;
    private long toIndex;
    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();


    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colNumber;
    @FXML
    private FilterableDateTableColumn<NotFulfilled, Date> colCreatedOn;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colCreatedBy;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colAccountCode;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colAccountName;
    @FXML
    private FilterableDoubleTableColumn<NotFulfilled, Double> colAccountSaldo;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colAccountIsSolid;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colStoreCity;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colStatus;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colOfferingGroupName;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colOriginalGroupName;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colGroupChangedBy;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colSpecialMarginTypeName;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colCashType;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colPricingDescription;
    @FXML
    private Pagination pagination;
    @FXML
    private ScrollPane scrollPaneFilter;
    public NotFulfilled chosenAccount = null;
    // Context menu
    private ContextMenu contextMenu;
    public static NotFulfilled account = new NotFulfilled();

    @FXML
    public ExampleController exampleProductTableController;

    private IntegerProperty index = new SimpleIntegerProperty();
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_UpdateStatus.setVisible(false);
        btn_IntoProcessing.setVisible(false);
        btnDelete.setVisible(false);
        btn_changeRequest.setVisible(false);
        btn_СancelRequest.setVisible(true); // Менеджер
        btn_RefreshRequest.setVisible(false);
        btn_DeleteRequest.setVisible(true); // Менеджер
        tbn_CreateRequest.setVisible(true); // Менеджер
        btn_ConfirmRequest.setVisible(true);// Менеджер
        btn_AddRequest.setVisible(true);// Менеджер
        tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.setRowFactory((tableView) -> {

            return new TooltipTableRow<NotFulfilled>((NotFulfilled person) -> {
                return person.getNumber();
            });
        });


        Image imageDecline = new Image(getClass().getResourceAsStream("/images/CreateRequest.png"));
        tbn_CreateRequest.setGraphic(new ImageView(imageDecline));

        exampleProductTableController.init(this);
        Image imageChange = new Image(getClass().getResourceAsStream("/images/ChangeRequest.png"));
        btn_changeRequest.setGraphic(new ImageView(imageChange));


        Image imageConfirm = new Image(getClass().getResourceAsStream("/images/ConfirmRequest.png"));
        btn_ConfirmRequest.setGraphic(new ImageView(imageConfirm));

        Image imageCancel = new Image(getClass().getResourceAsStream("/images/СancelRequest.png"));
        btn_СancelRequest.setGraphic(new ImageView(imageCancel));

        Image imageDelete = new Image(getClass().getResourceAsStream("/images/DeleteRequest.png"));
        btnDelete.setGraphic(new ImageView(imageDelete));

        Image imageIntoProcessing = new Image(getClass().getResourceAsStream("/images/IntoProcessing.png"));
        btn_IntoProcessing.setGraphic(new ImageView(imageIntoProcessing));

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }


        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        btn_ButtonAll.setSelected(true);
        btn_ButtonPricingAll.setToggleGroup(group);

        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle().equals(btn_ButtonAll)) {
                pricingBoolean = 0;
                refreshData();
            } else if (group.getSelectedToggle().equals(btn_ButtonPricingAll)) {
                pricingBoolean = 1;
                refreshData();
            }
        });

        btn_ConfirmRequest.setOnAction(event -> {
            try {
                chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }

            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте надіслати запит?") == ButtonType.OK) {

                String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                        "\t\t\t\t\tIsReadMeassage = '1',\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, chosenAccount.getID());

                    pst.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " надіслано в обробку");
                } catch (SQLException e) {
                    e.printStackTrace();
                    DBConnection database = new DBConnection();
                    database.reconnect();
                }

            } else return;


        });


        btn_СancelRequest.setOnAction(event -> {
            try {
                chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте анулювати запит?") == ButtonType.OK) {
                try {
                    con = DBConnection.getDataSource().getConnection();
                    CallableStatement callProc = con.prepareCall("{call dbo.tsp_ROActionNullifyRequest (?,?)}");
                    System.out.println(chosenAccount.getID());
                    callProc.setString(1, chosenAccount.getID());
                    callProc.setString(2, User.getContactID());
                    System.out.println(chosenAccount.getID() + " 444444444444444444444444444444444555555555555555555555555555");
                    callProc.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " анульовано");


                } catch (SQLException e) {
                    e.printStackTrace();
                    DBConnection database = new DBConnection();
                    database.reconnect();
                }

            } else return;
        });


        btn_UpdateStatus.setOnAction(event -> {
            try {
                chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте взяти в роботу?") == ButtonType.OK) {

                String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                        "\tSET  [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                        "[OfferingGroupID] = ?,\n" +
                        "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                        "\t[ModifiedByID] = ?\n" +
                        "WHERE([tbl_RequestOffering].[ID] = ?)";
                try {
                    pst = con.prepareStatement(query);
                    pst.setString(1, User.getContactID());
                    pst.setString(2, User.getContactID());
                    pst.setString(3, chosenAccount.getID());

                    pst.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + User.getContactName() + " взято в роботу");
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " анульовано");

                } catch (SQLException e) {
                    e.printStackTrace();
                    DBConnection database = new DBConnection();
                    database.reconnect();

                }
            } else return;
        });

        createTableColumns();
        tableViewHandles();


        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(NotFulledController.this, account, hashColumns, event).setFilter();
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        UsefulUtils.copySelectedCell(tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        anchorPane.setCenter(tableView);
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        colCreatedOn.setSortType(TableColumn.SortType.ASCENDING);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


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

        UsefulUtils.installCopyPasteHandler(tableView);
        //  UsefulUtils.copySelectedCell(tableRequestOffering);


        MenuItem item = new MenuItem("Copy");
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        tableView.setContextMenu(menu);


        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
            pagination.setSkin(pageSkin);

            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);

            System.out.println("pageCount=" + pageCount);
            pagination.setPageCount(pageCount);
            //   sort();
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


        BorderPane root = new BorderPane();
        Text text = new Text("BEFORE");
        root.setCenter(text);
        mainMenu.getItems().addAll(textCmd, exitCmd);
        root.setTop(menuBar);

        tableView.getSelectionModel().select(0);
        exitCmd.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {

            }
        });

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
                pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                        currentPageIndex = newValue.intValue();
                        updatePersonView();
                    }
                });
            }
        });
    }

    private void sort(Button notFulfilled) {
        if (notFulfilled == id) {
            data.sort((NotFulfilled p1, NotFulfilled p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()));
        } else if (notFulfilled == Number) {
            data.sort((NotFulfilled p1, NotFulfilled p2) -> p1.getNumber().compareTo(p2.getNumber()));
        } else if (notFulfilled == Solid) {
            data.sort((NotFulfilled p1, NotFulfilled p2) -> p1.getAccountIsSolid().compareTo(p2.getAccountIsSolid()));
        } else if (notFulfilled == Store) {
            data.sort((NotFulfilled p1, NotFulfilled p2) -> p1.getNumber().compareTo(p2.getStoreCity()));
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

    @FXML
    private void actionSearchingField(ActionEvent event) {

        String value = searchingField.getText();

        if (value.equals("")) {
            refreshData();
        } else
            findByProperty(value);

    }

    private ObservableList options = FXCollections.observableArrayList();

    String Skrut = null;

    public void findByProperty(String value) {
        data.clear();

        try {
            pst = con.prepareStatement("SELECT RequestID " +
                            "\t\t\t\t\tFROM dbo.tbl_OfferingInRequestOffering \n" +
                            " WHERE [tbl_OfferingInRequestOffering].[OfferingID] IN (SELECT ID\n" +
                            "        FROM [tbl_Offering] WHERE [tbl_Offering].Skrut LIKE '" + value.toString() + "%' OR [tbl_Offering].[Index] LIKE '" + value.toString() + "%')"//+
                    //  "FOR XML PATH('')"
            );
            rs = pst.executeQuery();
            while (rs.next()) {
                Skrut = rs.getString(1);
                //  options.add(rs.getString(1));
                List<NotFulfilled> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));


            }
            tableView.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setID(String ID) {

    }

    @Override
    public void setName(String name) {

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
            hashColumns.put(colOfferingGroupName, "[tbl_Contact].[Name]");
            hashColumns.put(colOriginalGroupName, "SELECT\n" +
                    "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n");
            hashColumns.put(colGroupChangedBy, "SELECT\n" +
                    "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n");
            hashColumns.put(colSpecialMarginTypeName, "[SMT].[Name]");
            hashColumns.put(colCashType, "[tbl_RequestOffering].[CashType]");
            hashColumns.put(colPricingDescription, "[tbl_RequestOffering].PricingDescription");
            List<?> listColumns = tableView.getColumns();

            colNumber.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("Number"));
            colCreatedOn.setCellValueFactory(new PropertyValueFactory<NotFulfilled, Date>("CreatedOn"));
            colCreatedBy.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("CreatedBy"));
            colAccountCode.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountCode"));
            colAccountName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountName"));
            colAccountSaldo.setCellValueFactory(new PropertyValueFactory<NotFulfilled, Double>("AccountSaldo"));
            colAccountIsSolid.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountIsSolid"));
            colStoreCity.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("StoreCity"));
            colStatus.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("Status"));

            colOfferingGroupName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("OfferingGroupName"));
            colOriginalGroupName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("OriginalGroupName"));
            colGroupChangedBy.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("GroupChangedBy"));
            colSpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("SpecialMarginTypeName"));
            colCashType.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("CashType"));
            colPricingDescription.setCellValueFactory(new PropertyValueFactory<NotFulfilled,String>("PricingDescription"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
        super.createTableColumns();

    }


    @Override
    public void disableFilter(TableColumn column, Pane content) {
        account.removeStringFilter(column);
        refreshData();

        //column.filteredProperty().setValue(true);

        if (content == null) {
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
            //content.getStylesheets().add("sample/ua/ucountry/MainTables/Account/MainDictionaryTable.css");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }

    public void loadDataFromDatabase() {
        try {


            List<NotFulfilled> listItems = account.findAllNotFulled(true, (int) toIndex, User.getContactID(), User.getContactID(), pricingBoolean);

            listItems.forEach(item -> data.add(item));

            tableView.setItems(data);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        //  return null;
    }


    public BorderPane createPage(int pageIndex) {
        try {
            data = FXCollections.observableArrayList();
            loadDataFromDatabase();
            fromIndex = pageIndex * itemsPerPage;
            toIndex = Math.min(fromIndex + itemsPerPage, data.size());


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
            // Collections.sort(data);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }

    private void searchCode() {
        FilteredList<OfferingRequest> filteredData = new FilteredList<OfferingRequest>(exampleProductTableController.data, e -> true);
        searchingField.setOnKeyReleased(e -> {
            searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super OfferingRequest>) user -> {


                    if (newValue == null || newValue.isEmpty()) {

                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getSkrut().contains(newValue)) {
                        return true;
                    } else if (user.getSkrut().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;

                });
            });
            SortedList<OfferingRequest> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableView.comparatorProperty());
            tableView.setItems(sortedData);

        });
    }

    @FXML
    private void UpdateButton (ActionEvent event){
        refreshData();
    }
    public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {
            UsefulUtils.searchCombination(eventKey, tableView);
            fixSelectedRecord();
        });
    }

    public void fixSelectedRecord() {
        NotFulfilled record = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println(record);
        exampleProductTableController.handleTableView(record);

    }


   /* @FXML
    CreateRequest createRequest;*/

    public void handleTableView() {
        refreshData();
    }

    public void showEdit(NotFulfilled value) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ProductAddNewController.class.getResource("/views/EditView.fxml"));
        EditController createRequest = loader.getController();
        Pane serverrLayout = null;
        try {
            serverrLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Show the scene containing the root layout.
        Scene scene = new Scene(serverrLayout);
        EditController con = loader.getController();
        // con.setOfferingRequest(value);
        stage.setScene(scene);

        stage.setMaxHeight(520);
        stage.setMaxWidth(620);
        main.changeExists();
        stage.show();
        stage.requestFocus();
        refreshData();
    }

    @FXML
    private void handleCreateRequest(ActionEvent evt) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();


        loader.setLocation(ProductAddNewController.class.getResource("/views/CreateMainView.fxml"));
        CreateMainController createRequest = loader.getController();
        Pane serverrLayout = loader.load();
        // Show the scene containing the root layout.
        stage.setResizable(false);
        Scene scene = new Scene(serverrLayout);
        CreateMainController con = loader.getController();
        con.setOfferingRequest(this);

        stage.setScene(scene);
       // ResizeHelper.addResizeListener(stage);
        stage.setMaxHeight(480);
        stage.setMaxWidth(620);
        //     main.changeExists();
        stage.show();
        stage.requestFocus();
        //refreshData();


    }

    OfferingRequest offeringRequest;

    @FXML
    private void actionAddButton(ActionEvent ev) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            NotFulfilled record = null;
            try {
                record = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception e) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного запиту");
                return;
            }

            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(ProductAddNewController.class.getResource("/views/ProductAddView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            ProductAddController productAddController = loader.getController();
            productAddController.setNotFulled(record);

            productAddController.setExampleController(exampleProductTableController, this);
            stage.setScene(scene);
            ResizeHelper.addResizeListener(stage);
            stage.setMaxHeight(298);
            stage.setMaxWidth(620);
            stage.show();
            stage.requestFocus();
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown(e.getMessage());

        }
    }


    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }

    NotFulfilled selectedOffering = null;

    @FXML
    private void actionChangeRequest(ActionEvent event) {
        try {
            chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного запиту");
            return;
        }

        EditController editController = new EditController(chosenAccount, false);
    }

    @FXML
    public void handleDelete(ActionEvent e) {

        try {
            selectedOffering = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
            new DerbyNotFulledDAO().deleteOffering(selectedOffering);
            main.changeExists();
            UsefulUtils.showSuccessful("Запит успішно видалено");

        } else return;


    }

    @FXML
    public void handleDeleteProduct(ActionEvent e) {

        try {
            offeringRequest = (OfferingRequest) exampleProductTableController.tableView.getItems().get(exampleProductTableController.tableView.getSelectionModel().getSelectedIndex());

        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
            new DerbyOfferingRequestDAO().deleteAccount(offeringRequest);
            exampleProductTableController.refresh();
            UsefulUtils.showSuccessful("Продукт успішно видалено");
            ;
        } else return;

    }

    public void refreshData() {
        try {

            data.clear();
            loadDataFromDatabase();
            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
            UsefulUtils.fadeTransition(tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        refreshData();
    }
}
