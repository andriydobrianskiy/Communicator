package com.mainPage.ArchiveFiles;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import com.login.User;
import com.mainPage.ArchiveFiles.ArchiveFilesRequest.ArchiveFilesRequestController;
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

public class ArchiveFilesController extends WorkArea implements MiniFilterFunction, Initializable, DictionaryProperties, ObserverNF {
    @FXML
    private ArchiveFilesRequestController archiveFilesRequestViewController;
    private MainPageController main;
    private static Logger log = Logger.getLogger(ArchiveFilesController.class.getName());
    @FXML
    private BorderPane borderPane;
    private ArchiveFiles account = new ArchiveFiles();
    private ObservableList<ArchiveFiles> data;
    private long fromIndex;
    private long toIndex;
    private ArchiveFilesQuery accountQueries = new ArchiveFilesQuery();
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private Pagination pagination;
    @FXML
    private JFXButton btn_ReturnInProcessing;
    @FXML
    private ChatController chatViewController;
    @FXML
    private BorderPane splitPane1;
    @FXML
    private JFXTextField searchingField;
    @FXML
    private JFXToggleButton btn_ButtonAll;
    public ArchiveFiles chosenAccount;
    public static ArchiveFiles chosenNotis;

    public ChatController conn;

    private double xOffset;
    private double yOffset;
    private Scene scene;
    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();



    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colNumber;
    @FXML private FilterableDateTableColumn<ArchiveFiles, String> colCreatedOn;
    @FXML private FilterableStringTableColumn<ArchiveFiles, String> colCreatedBy;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colAccountCode;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colAccountName;
    @FXML private FilterableDoubleTableColumn<ArchiveFiles, Double> colAccountSaldo;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colAccountIsSolid;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colStoreCity;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colStatus;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colOfferingGroupName;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colOriginalGroupName;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colGroupChangedBy;
    @FXML private FilterableStringTableColumn <ArchiveFiles, String> colSpecialMarginTypeName;
    @FXML private FilterableStringTableColumn<ArchiveFiles, String> colCashType;
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
    private Boolean dataPagination = true;
    private String filterSorted = null;
    private String countfilter = null;
    private String sort = "DESC";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        archiveFilesRequestViewController.init(this);
        chatViewController.init(this);
        createTableColumns();
        tableViewHandles();
        tableViewHandles();
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(ArchiveFilesController.this, account, hashColumns,event).setFilter();
        });

        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        UsefulUtils.copySelectedCell(tableView);

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        borderPane.setCenter(tableView);
        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        // customizeScene();
        //    tableView.getSelectionModel().setCellSelectionEnabled(true);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
            pagination.setSkin(pageSkin);

            pagination.setPageFactory(this::createPage);
            //   sort();
            initializeTable();

            id.setOnMouseClicked(e->{
                dataPagination = false;
                pagination.setPageFactory(this::createPage);

            });
            Number.setOnMouseClicked(e->{
                dataPagination = false;
                pagination.setPageFactory(this::createPage);
            });
            Solid.setOnMouseClicked(e-> {
                dataPagination = false;
                pagination.setPageFactory(this::createPage);
            });
            Store.setOnMouseClicked(e->{
                dataPagination = false;
                pagination.setPageFactory(this::createPage);
            });



            SortButton(Number);
            SortButton(id);
            SortButton(Solid);
            SortButton(Store);
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вдається відкрити сторінки");

        }

        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        btn_ReturnInProcessing.setOnAction(event -> {

            try {

                chosenAccount = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                System.out.println(chosenAccount.getID() + "8888888888888888888888888885555555555555555555555555555566666666666666666666666");
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви бажаєте перевести в обробку?") == ButtonType.OK) {

                    if(chosenAccount.getStatusID() == "3E7F90E2-335C-41B2-828A-576A06375B8C" ){
                        UsefulUtils.showErrorDialogDown("Запит не є архівним");
                    }else {


                        String query = (
                                "\tUPDATE TOP(1) tbl_RequestOffering\n" +
                                "\tSET \n" +
                                "\t\tModifiedByID = ?,\n" +
                                "\t\tModifiedOn = CURRENT_TIMESTAMP,\n" +
                                "\t\tStatusID = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}'\n" +
                                "\tWHERE ID = ?\n" );
                        try {
                            pst = con.prepareStatement(query);
                            pst.setString(1, User.getContactID());
                            pst.setString(2, chosenAccount.getID());
                            pst.executeUpdate();
                            main.changeExists();
                            UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " поверне в обробку");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
            } else return;

        });


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
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {
                        chosenNotis = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

                com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController notesInProcessingController = new com.mainPage.InProcessing.NotesInProcessing.NotesInProcessingController(chosenAccount, false);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });
        tableView.getSelectionModel().select(0);
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
                //  options.add(rs.getString(1));
                List<ArchiveFiles> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /// Skrut = (String) options.get(0);


        tableView.setItems(data);


    }



    private void SortButton(Button button) {
        button.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent t) {
                sort(button);
                if (order) {
                    Collections.reverse(data);
                }
                order = !order;
                button.setGraphic((order) ? upImg : downImg);
                if (button.getGraphic() == upImg){
                    sort = "DESC";
                }else if(button.getGraphic() == downImg){
                    sort = "ASC";
                }
                updatePersonView();

            }
        });
    }

    private void sort(Button notFulfilled) {
        if (notFulfilled == id) {
            data.sort((ArchiveFiles p1, ArchiveFiles p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()));
        } else if (notFulfilled == Number) {
            data.sort((ArchiveFiles p1, ArchiveFiles p2) -> p1.getNumber().compareTo(p2.getNumber()));
        }else if (notFulfilled == Solid) {
            data.sort((ArchiveFiles p1, ArchiveFiles p2) -> p1.getAccountIsSolid().compareTo(p2.getAccountIsSolid()));
        } else if (notFulfilled == Store) {
            data.sort((ArchiveFiles p1, ArchiveFiles p2) -> p1.getNumber().compareTo(p2.getStoreCity()));
            pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                    currentPageIndex = newValue.intValue();
                    //updatePersonView();

                }
            });
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




        btn_ButtonAll.setVisible(true);
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
                            findByProperty(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                });
            }
        });


    }

    public void updatePersonView() {
        fromIndex = currentPageIndex * itemsPerPage;
        toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
    }

    public void loginButtonAction(ArchiveFiles chosenAccount) throws IOException {
        chatViewController.setOfferingArchive(chosenAccount);
        chatViewController.setArchiveController(this);
        chatViewController.setUsernameLabel(User.getContactName());
    }


    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }




    public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {
            UsefulUtils.searchCombination(eventKey, tableView);
            fixSelectedRecord();
        });
    }

    protected void fixSelectedRecord() {
        ArchiveFiles record = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        archiveFilesRequestViewController.handleTableView(record);

        chosenAccount = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
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
            hashColumns.put(colCashType, "[tbl_RequestOffering].[CashType]");
            List<?> listColumns = tableView.getColumns();

            colNumber.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("Number"));
            colCreatedOn.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("CreatedOn"));
            colCreatedBy.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("CreatedBy"));
            colAccountCode.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountCode"));
            colAccountName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountName"));
            colAccountSaldo.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, Double>("AccountSaldo"));
            colAccountIsSolid.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountIsSolid"));
            colStoreCity.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("StoreCity"));
            colStatus.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("Status"));

            colOfferingGroupName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("OfferingGroupName"));
            colOriginalGroupName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("OriginalGroupName"));
            colGroupChangedBy.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("GroupChangedBy"));
            colSpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("SpecialMarginTypeName"));
            colCashType.setCellValueFactory(new PropertyValueFactory<ArchiveFiles,String>("CashType"));


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
                List<ArchiveFiles> listItems = account.findInArchive(false, (int) toIndex, User.getContactID(), User.getContactID(), filterSorted);
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            }else if (queryAll == false){
                List<ArchiveFiles> listItems = account.findAllInArchive(false, (int) toIndex, filterSorted);
                listItems.forEach(item -> data.add(item));

                tableView.setItems(data);
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }

    }
    @FXML
    private void UpdateButton (ActionEvent event){
        refreshData();
    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {
        account.removeStringFilter(column);
        refreshData();

        //column.filteredProperty().setValue(true);

        if(content == null) {
            removeFilterFromHbox(column);
            return;
        }

        hboxFilter.getChildren().remove(content);


        System.out.println("DISABLED");
    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }

    public BorderPane createPage(int pageIndex) {

        try {

            if(queryAll == false) {
                data = FXCollections.observableArrayList();

                fromIndex = pageIndex * itemsPerPage;

                toIndex = Math.min(fromIndex + itemsPerPage, accountQueries.getMainArchiveFilesCount(false, countfilter));

                pageCount = getPageCount((int) accountQueries.getMainArchiveFilesCount(false, countfilter), itemsPerPage);
                pagination.setPageCount(pageCount);
                filterSorted = "ORDER BY [tbl_RequestOffering].[CreatedOn] "+sort+"\n" +
                        "             OFFSET  + ("+pageIndex+") * 40  \n" +
                        "             ROWS\n" +
                        "             FETCH NEXT 40 ROWS ONLY;";
                loadDataFromDatabase();


                tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
            }else if (queryAll == true){

                data = FXCollections.observableArrayList();

                fromIndex = pageIndex * itemsPerPage;
                countfilter = " AND (([CreatedByID] = '"+User.getContactID()+"' OR\n" +
                        "[OfferingGroupID] = '"+User.getContactID()+"'))\n";
                toIndex = Math.min(fromIndex + itemsPerPage, accountQueries.getMainArchiveFilesCount(true, countfilter));

                pageCount = getPageCount((int) accountQueries.getMainArchiveFilesCount(true, countfilter), itemsPerPage);
                pagination.setPageCount(pageCount);
                filterSorted = "ORDER BY [tbl_RequestOffering].[CreatedOn] "+sort+"\n" +
                        "             OFFSET  + ("+pageIndex+") * 40  \n" +
                        "             ROWS\n" +
                        "             FETCH NEXT 40 ROWS ONLY;";
                loadDataFromDatabase();
                tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
            }
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
            dataPagination = true;
            pagination.setPageFactory(this::createPage);

        }

        UsefulUtils.fadeTransition(tableView);
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
    public void update() {
        refreshData();
    }


}
