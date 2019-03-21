package com.mainPage.All;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXTextField;
import com.login.User;
import com.mainPage.All.AllRequest.AllRequestController;
import com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.WorkArea;
import com.mainPage.page.MainPageController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableDateTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableDoubleTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllController extends WorkArea implements Initializable, ObserverNF, DictionaryProperties, MiniFilterFunction {

    private static Logger log = Logger.getLogger(AllController.class.getName());

    private PreparedStatement pst = null;
    private Connection con = null;
    private ResultSet rs = null;

    @FXML
    private Pagination pagination;
    @FXML
    private JFXTextField searchingField;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hboxFilter;
    @FXML
    private ScrollPane scrollPaneFilter;
    @FXML
    private ChatController chatViewController;
    @FXML
    private BorderPane splitPane;
    @FXML
    public ToggleButton btn_ButtonAll;
    @FXML
    public ToggleButton btn_ButtonAllPricing;
    @FXML
    public ToggleButton btn_ButtonPricing;
    @FXML
    public ToggleButton btn_Button;
    public ObservableList observableList = FXCollections.observableArrayList();

    private MainPageController main;

    private Scene scene;
    private Boolean queryAll = true;

    @FXML
    private AllRequestController allRequestViewController;

    public static All chosenAccount = null;
    private DerbyAllDAO account = new DerbyAllDAO();
    private ObservableList<All> data;
    private AllQuery accountQueries = new AllQuery();
    private long fromIndex;
    private long toIndex;
    // Main
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();
    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    @FXML
    private FilterableStringTableColumn<All, String> colNumber;
    @FXML
    private FilterableDateTableColumn<All, String> colCreatedOn;
    @FXML
    private FilterableStringTableColumn<All, String> colCreatedBy;
    @FXML
    private FilterableStringTableColumn<All, String> colAccountCode;
    @FXML
    private FilterableStringTableColumn<All, String> colAccountName;
    @FXML
    private FilterableDoubleTableColumn<All, Double> colAccountSaldo;
    @FXML
    private FilterableStringTableColumn<All, String> colAccountIsSolid;
    @FXML
    private FilterableStringTableColumn<All, String> colStoreCity;
    @FXML
    private FilterableStringTableColumn<All, String> colStatus;
    @FXML
    private FilterableStringTableColumn<All, String> colOfferingGroupName;
    @FXML
    private FilterableStringTableColumn<All, String> colOriginalGroupName;
    @FXML
    private FilterableStringTableColumn<All, String> colGroupChangedBy;
    @FXML
    private FilterableStringTableColumn<All, String> colSpecialMarginTypeName;
    @FXML
    private FilterableStringTableColumn<All, String> colCashType;
    @FXML
    private FilterableStringTableColumn<All, String> colPricingDescription;

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
    private Boolean dataPagination = true;
    private String filterSorted = null;
    private String countfilter = null;
    private String sort = "DESC";
    private Integer pricingBoolean = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btn_ButtonAll.setVisible(false); // Закуп Всі
        btn_ButtonPricing.setVisible(false);  // Ціноутворення Мої
        btn_Button.setVisible(false);  // Закуп Мої
        btn_ButtonAllPricing.setVisible(false); // Ціноутворення Всі
        observableList.addAll(btn_ButtonAll.getText(), btn_ButtonPricing.getText(), btn_Button.getText(), btn_ButtonAllPricing.getText());
        chatViewController.init(this);
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    chosenAccount = (All) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

                    NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);
                }
            }
        });
        createTableColumns();
        tableViewHandles();

        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(AllController.this, account, hashColumns, event).setFilter();
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        UsefulUtils.copySelectedCell(tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borderPane.setCenter(tableView);
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        //btn_ButtonAll.setSelected(true);
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
        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        btn_Button.setToggleGroup(group);
        btn_ButtonAllPricing.setToggleGroup(group);
        btn_ButtonPricing.setToggleGroup(group);
        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle().equals(btn_ButtonAllPricing)) {
                queryAll = false;
                pricingBoolean = 1;
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

            } else if (group.getSelectedToggle().equals(btn_ButtonPricing)) {
                queryAll = true;
                pricingBoolean = 1;
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
            } else if (group.getSelectedToggle().equals(btn_ButtonAll)) {
                queryAll = false;
                pricingBoolean = 0;
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
            } else if (group.getSelectedToggle().equals(btn_Button)) {
                queryAll = true;
                pricingBoolean = 0;
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

            }

        });


        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
            pagination.setSkin(pageSkin);

            pagination.setPageFactory(this::createPage);
            pagination.setPageFactory(this::createPage);
            //   sort();
            initializeTable();

            id.setOnMouseClicked(e -> {
                dataPagination = false;
                pagination.setPageFactory(this::createPage);

            });
            Number.setOnMouseClicked(e -> {
                dataPagination = false;
                pagination.setPageFactory(this::createPage);
            });
            Solid.setOnMouseClicked(e -> {
                dataPagination = false;
                pagination.setPageFactory(this::createPage);
            });
            Store.setOnMouseClicked(e -> {
                dataPagination = false;
                pagination.setPageFactory(this::createPage);
            });

            SortButton(Number);
            SortButton(id);
            SortButton(Solid);
            SortButton(Store);
            initializeTable();
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вдається відкрити сторінки");

        }


        allRequestViewController.init(this);
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace(); DBConnection database = new DBConnection();
            database.reconnect();
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
        tableView.getSelectionModel().select(0);
    }

    public void SortButton(Button button) {
        button.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent t) {
                //   pagination.setCurrentPageIndex(0);
                sort(button);
                if (order) {
                    Collections.reverse(data);
                }
                order = !order;
                button.setGraphic((order) ? upImg : downImg);
                if (button.getGraphic() == upImg) {
                    sort = "DESC";
                } else if (button.getGraphic() == downImg) {
                    sort = "ASC";
                }
                updatePersonView();

            }
        });
    }

    public void sort(Button notFulfilled) {
        try {
            if (notFulfilled == id) {
                data.sort((All p1, All p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()));
            } else if (notFulfilled == Number) {
                data.sort((All p1, All p2) -> p1.getNumber().compareTo(p2.getNumber()));
            } else if (notFulfilled == Solid) {
                data.sort((All p1, All p2) -> p1.getAccountIsSolid().compareTo(p2.getAccountIsSolid()));
            } else if (notFulfilled == Store) {
                data.sort((All p1, All p2) -> p1.getNumber().compareTo(p2.getStoreCity()));
                pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                        currentPageIndex = newValue.intValue();
                        //updatePersonView();

                    }
                });
            }

        }catch (Exception e) {
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

                List<All> listItems = account.findSearchSkrut( Skrut);
                listItems.forEach(item -> data.add(item));


            }

        } catch (SQLException e) {
            e.printStackTrace(); DBConnection database = new DBConnection();
            database.reconnect();
        }


        tableView.setItems(data);


    }

    public void loginButtonAction(All chosenAccount) throws IOException {
        chatViewController.setOfferingRequest(chosenAccount);
        chatViewController.setAllController(this);
        chatViewController.setUsernameLabel(User.getContactName());
    }


    public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {
            UsefulUtils.searchCombination(eventKey, tableView);
            fixSelectedRecord();
        });
    }

    protected void fixSelectedRecord() {
        All record = (All) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        allRequestViewController.handleTableView(record);
        chosenAccount = (All) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
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
            hashColumns.put(colAccountCode, "[tbl_Account].[Code]");
            hashColumns.put(colAccountName, "[tbl_Account].[Name]");
            hashColumns.put(colAccountSaldo, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colAccountIsSolid, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");
            hashColumns.put(colStoreCity, "[tbl_StoreCity].[Name]");
            hashColumns.put(colStatus, "[tbl_RequestOfferingStatus].[Name]");
            hashColumns.put(colGroupChangedBy, "[dbo].[tbl_Contact].[Name]");
            hashColumns.put(colSpecialMarginTypeName, "[SMT].[Name]");
            hashColumns.put(colOriginalGroupName, "[dbo].[tbl_Contact].[Name]");
            hashColumns.put(colOfferingGroupName, "[dbo].[tbl_Contact].[Name]");
            hashColumns.put(colCashType, "[tbl_RequestOffering].[CashType]");
            hashColumns.put(colPricingDescription, "[tbl_RequestOffering].[PricingDescription]");

            colNumber.setMinWidth(150);

            tableView.setTableMenuButtonVisible(true);

            colNumber.setCellValueFactory(new PropertyValueFactory<All, String>("Number"));
            colCreatedOn.setCellValueFactory(new PropertyValueFactory<All, String>("CreatedOn"));
            colCreatedBy.setCellValueFactory(new PropertyValueFactory<All, String>("CreatedBy"));
            colAccountCode.setCellValueFactory(new PropertyValueFactory<All, String>("AccountCode"));
            colAccountName.setCellValueFactory(new PropertyValueFactory<All, String>("AccountName"));
            colAccountSaldo.setCellValueFactory(new PropertyValueFactory<All, Double>("AccountSaldo"));
            colAccountIsSolid.setCellValueFactory(new PropertyValueFactory<All, String>("AccountIsSolid"));
            colStoreCity.setCellValueFactory(new PropertyValueFactory<All, String>("StoreCity"));
            colStatus.setCellValueFactory(new PropertyValueFactory<All, String>("Status"));
            colGroupChangedBy.setCellValueFactory(new PropertyValueFactory<All, String>("GroupChangedBy"));
            colSpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<All, String>("SpecialMarginTypeName"));
            colOriginalGroupName.setCellValueFactory(new PropertyValueFactory<All, String>("OriginalGroupName"));
            colOfferingGroupName.setCellValueFactory(new PropertyValueFactory<All, String>("OfferingGroupName"));
            colCashType.setCellValueFactory(new PropertyValueFactory<All, String>("CashType"));
            colPricingDescription.setCellValueFactory(new PropertyValueFactory<All, String>("PricingDescription"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);

        }

        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ///  borderPane.setCenter(tableView);
        super.createTableColumns();
    }


    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
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

    public void loadDataFromDatabase() {
        data.clear();
        try {

            if (queryAll == true) {
                List<All> listItems = account.findAll(false, (int) toIndex, User.getContactID(), User.getContactID(), filterSorted, pricingBoolean);
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            } else if (queryAll == false) {
                List<All> listItems = account.findAllAll(false, (int) toIndex, filterSorted, pricingBoolean);
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            }


        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }

    }


    public BorderPane createPage(int pageIndex) {
        try {

            if (queryAll == false) {
                data = FXCollections.observableArrayList();

                fromIndex = pageIndex * itemsPerPage;

                toIndex = Math.min(fromIndex + itemsPerPage, accountQueries.getMainAllCount());

                pageCount = getPageCount((int) accountQueries.getMainAllCount(), itemsPerPage);
                pagination.setPageCount(pageCount);
                filterSorted = "ORDER BY [tbl_RequestOffering].[CreatedOn] " + sort + "\n" +
                        "             OFFSET  + (" + pageIndex + ") * 40  \n" +
                        "             ROWS\n" +
                        "             FETCH NEXT 40 ROWS ONLY;";
                loadDataFromDatabase();


                tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
            } else if (queryAll == true) {

                data = FXCollections.observableArrayList();

                fromIndex = pageIndex * itemsPerPage;
                countfilter = "WHERE(([CreatedByID] = '" + User.getContactID() + "' OR\n" +
                        "[OfferingGroupID] = '" + User.getContactID() + "'))\n";
                toIndex = Math.min(fromIndex + itemsPerPage, accountQueries.getMainAllCount());

                pageCount = getPageCount((int) accountQueries.getMainAllCount(), itemsPerPage);
                pagination.setPageCount(pageCount);
                filterSorted = "ORDER BY [tbl_RequestOffering].[CreatedOn] " + sort + "\n" +
                        "             OFFSET  + (" + pageIndex + ") * 40  \n" +
                        "             ROWS\n" +
                        "             FETCH NEXT 40 ROWS ONLY;";
                loadDataFromDatabase();
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }


    public void refreshData() {
        tableView.setSelectedRow();
        data.clear();
        dataPagination = true;
        pagination.setPageFactory(this::createPage);
        UsefulUtils.fadeTransition(tableView);

    }

    @FXML
    private void UpdateButton(ActionEvent event) {
        refreshData();
    }

    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }

    @Override
    public void update() {
        refreshData();

    }
}
