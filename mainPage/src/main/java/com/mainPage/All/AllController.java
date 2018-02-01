package com.mainPage.All;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.google.jhsheets.filtered.FilteredTableView;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;
import com.Utils.CustomPaginationSkin;
import com.Utils.FilterQuery;
import com.Utils.UsefulUtils;
import com.login.User;
import com.mainPage.All.AllRequest.AllRequestController;
import com.mainPage.page.MainPageController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AllController implements Initializable, DictionaryPropertiesAll, ObserverNF {

    private static Logger log = Logger.getLogger(AllController.class.getName());

    @FXML
    private TableView tableView;
    @FXML
    private Pagination pagination;
    @FXML
    private JFXTextField searchingField;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hboxFilter;

    private MainPageController main;

    @FXML
    private AllRequestController allRequestViewController;

    public static All chosenAccount = null;
    private DerbyAllDAO account = new DerbyAllDAO();
    private ObservableList<All> data;
    private AllQuery accountQueries = new AllQuery();
    private long fromIndex;
    private long toIndex;

    private HashMap<TableColumn, String> hashColumns = new HashMap<>();
    private TableColumn<All, String> CreatedOn;
    private TableColumn<All, String> CreatedBy;
    private TableColumn<All, String> AccountName;
    private TableColumn<All, String> AccountCode;
    private TableColumn<All, String> AccountSaldo;
    private TableColumn<All, String> AccountIsSolid;
    private TableColumn<All, String> Number;
    private TableColumn<All, String> Status;
    private TableColumn<All, String> StoreCity;
    private TableColumn<All, String> OriginalGroupName;
    private TableColumn<All, String> GroupChangedBy;
    private TableColumn<All, String> SpecialMarginTypeName;
    private TableColumn<All, String> StateName;





    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
//fixSelectedRecord();
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    chosenAccount = (All) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    //closeWindow();
                }
            }
        });
        createTableColumns();
        tableViewHandles();
        System.out.println("777777777777777777777777777777777777777777777777777777777777777777777");
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination

            pagination.setSkin(pageSkin);
            pagination.setPageFactory(this::createPage);

        allRequestViewController.init(this);
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);

    }

    public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> fixSelectedRecord());
    }

    private void fixSelectedRecord() {
        All record = (All) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        allRequestViewController.handleTableView(record);
    }



    @Override
    public void createTableColumns() {
        try {
            tableView = new FilteredTableView();
            Number = new FilterableStringTableColumn<>("Номер запиту");
            hashColumns.put(Number, "[tbl_RequestOffering].[Number]");

            CreatedOn = new FilterableStringTableColumn<>("Дата");
            hashColumns.put(CreatedOn, "[tbl_RequestOffering].[CreatedOn]");

            CreatedBy = new FilterableStringTableColumn<>("Створив");
            hashColumns.put(CreatedBy, "[tbl_Contact].[Name]");

            AccountCode = new FilterableStringTableColumn<>("Код контрагента");
            hashColumns.put(AccountCode, "[tbl_Account].[Code]");

            AccountName = new FilterableStringTableColumn<>("Контрагент");
            hashColumns.put(AccountName, "[tbl_Account].[Name]");

            AccountSaldo = new FilterableStringTableColumn<>("Сальдо");
            hashColumns.put(AccountSaldo, "[tbl_Account].[SaldoSel]");

            AccountIsSolid = new FilterableStringTableColumn<>("Солідність");
            hashColumns.put(AccountIsSolid, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");

            StoreCity = new FilterableStringTableColumn<>("Місто поставки");
            hashColumns.put(StoreCity, "[tbl_StoreCity].[Name]");

            Status = new FilterableStringTableColumn<>("Статус");
            hashColumns.put(Status, "[tbl_RequestOfferingStatus].[Name]");

            GroupChangedBy = new FilterableStringTableColumn<>("Змінив групу");
            hashColumns.put(GroupChangedBy, "[dbo].[tbl_Contact].[Name]");

            SpecialMarginTypeName = new FilterableStringTableColumn<>("Тип спец націнки");
            hashColumns.put(SpecialMarginTypeName, "[SMT].[Name]");

            StateName = new FilterableStringTableColumn<>("Статус запиту");
            hashColumns.put(StateName, "[tbl_RequestOfferingState].[Name]");

          /*  OfferingGroupName = new FilterableStringTableColumn<>("Група товарів");
            hashColumns.put(OfferingGroupName, "[OfferingGroup].[Name]");*/

            OriginalGroupName = new FilterableStringTableColumn<>("Початкова група");
            hashColumns.put(OriginalGroupName, "[dbo].[tbl_Contact].[Name]");

            Number.setMinWidth(150);

            tableView.setTableMenuButtonVisible(true);

            tableView.getColumns().addAll(
                    Number,
                    CreatedOn,
                    CreatedBy,
                    AccountCode,
                    AccountName,
                    AccountSaldo,
                    AccountIsSolid,
                    StoreCity,
                    Status,
                    GroupChangedBy,
                    SpecialMarginTypeName,
                    StateName,
                    OriginalGroupName);

            Number.setCellValueFactory(new PropertyValueFactory<All, String>("Number"));
            CreatedOn.setCellValueFactory(new PropertyValueFactory<All, String>("CreatedOn"));
            CreatedBy.setCellValueFactory(new PropertyValueFactory<All, String>("CreatedBy"));
            AccountCode.setCellValueFactory(new PropertyValueFactory<All, String>("AccountCode"));
            AccountName.setCellValueFactory(new PropertyValueFactory<All, String>("AccountName"));
            AccountSaldo.setCellValueFactory(new PropertyValueFactory<All, String>("AccountSaldo"));
            AccountIsSolid.setCellValueFactory(new PropertyValueFactory<All, String>("AccountIsSolid"));
            StoreCity.setCellValueFactory(new PropertyValueFactory<All, String>("StoreCity"));
            Status.setCellValueFactory(new PropertyValueFactory<All, String>("Status"));
            GroupChangedBy.setCellValueFactory(new PropertyValueFactory<All, String>("GroupChangedBy"));
            SpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<All, String>("SpecialMarginTypeName"));
            StateName.setCellValueFactory(new PropertyValueFactory<All, String>("StateName"));
            OriginalGroupName.setCellValueFactory(new PropertyValueFactory<All, String>("OriginalGroupName"));


            tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, new EventHandler<ColumnFilterEvent>() {
                @Override
                public void handle(ColumnFilterEvent t) {
                    //System.out.println("Filtered column count: " + tableView.getFilteredColumns().size());
                    System.out.println("Filtering changed on column: " + t.sourceColumn().getText());
                    System.out.println("Current filters on column " + t.sourceColumn().getText() + " are:");
                    final List<IFilterOperator> filters = t.sourceColumn().getFilters();

                    System.out.println("COLUMN - " + t.sourceColumn());
                    for (IFilterOperator filter : filters) {
                        System.out.println("  Type=" + filter.getType() + ", Value=" + filter.getValue());
                    }

                    if (filters.get(0).getType() == IFilterOperator.Type.NONE) {
                        account.removeStringFilter(t.sourceColumn());
                        disableFilter(t.sourceColumn(), null);
                        refreshData();
                        return;
                    }

                    // fillHboxFilter(t.sourceColumn(), filters.get(0).getType(), filters.get(0).getValue());
                    account.setStringFilter(t.sourceColumn(), new FilterQuery(hashColumns.get(t.sourceColumn()), filters.get(0).getType(), filters.get(0).getValue()).getWhereClause());


                    refreshData();
                }
            });

            // UsefulUtils.copySelectedCell(tableView);

          /* tableRequestOffering.setOnMousePressed(new EventHandler<MouseEvent>() {
               @Override
               public void handle(MouseEvent event) {
                   if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                       handleButtonEdit(new ActionEvent());
                   }
               }
           });*/

            tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            borderPane.setCenter(tableView);

            //    tableRequestOffering.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            //  borderPane.setCenter(tableRequestOffering);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {


        account.removeStringFilter(column);
        refreshData();

        //column.filteredProperty().setValue(true);

        hboxFilter.getChildren().remove(content);
        System.out.println("DISABLED");
    }


    public List<All> loadDataFromDatabase() {
        try {


            List<All> listItems = account.findAll(true, (int) toIndex, User.getContactID(), User.getContactID());

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
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainAllCount());


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }


    public void refreshData() {
        data.clear();
        loadDataFromDatabase();
    }
    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }


    @Override
    public void update() {
        refreshData();
    }
}
