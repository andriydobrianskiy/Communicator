package com.mainPage.createRequest.searchCounterpart;

import com.Utils.AccountSearchType;
import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mainPage.createRequest.Querys.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.google.jhsheets.filtered.operators.IFilterOperator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchCounterpart extends AnchorPane implements DictionaryProperties, Initializable {

    private static Logger log = Logger.getLogger(SearchCounterpart.class.getName());

    private ObservableList<Counterpart> data;

    private Query accountQueries = new Query();

    private long fromIndex;
    private long toIndex;


    private Counterpart account = new Counterpart();


    @FXML
    private TableView tableView;
    @FXML
    private JFXButton btnSearch;
    @FXML
    private JFXTextField searchField;

    @FXML
    JFXComboBox<AccountSearchType> choiceFilter;
    @FXML
    private Pagination pagination;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnCancel;

    public static Counterpart chosenAccount = null;

    public static Counterpart chosenElement = null;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountQueries.getMainAccount(false, 1);
        System.out.println(accountQueries.getMainAccount(false, 0));

        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    // chosenAccount = (Counterpart) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                   /* btnOK.setOnAction(evt -> {*/
                    chosenElement = (Counterpart) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    closeWindow();
                    //  });
                    //closeWindow();
                }
            }
        });
        Image imageSearchCreateRequest = new Image(getClass().getResourceAsStream("/images/SearchCreateRequest.png"));
        btnSearch.setGraphic(new ImageView(imageSearchCreateRequest));

        Image imageButtonOk = new Image(getClass().getResourceAsStream("/images/ButtonOk.png"));
        btnOK.setGraphic(new ImageView(imageButtonOk));

        Image imageButtonCancel = new Image(getClass().getResourceAsStream("/images/ButtonCancel.png"));
        btnCancel.setGraphic(new ImageView(imageButtonCancel));

        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (!searchField.getText().isEmpty())
                            filterData();
                    }
                } catch (Exception e) {
                    log.log(Level.SEVERE, "handleButtonSearch exception: " + e);
                }
            }
        });
        createTableColumns();

        // creating columns
        fillChoiceBox(); // choice box filter
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
        // tableViewHandles(); // handles for labels info from tableview

        btnOK.setOnAction(event -> {
            chosenElement = (Counterpart) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            closeWindow();
        });

        btnCancel.setOnAction(event -> {
            closeWindow();
        });

        pagination.setSkin(pageSkin);

        pagination.setPageFactory(this::createPage);

        choiceFilter.getSelectionModel().select(AccountSearchType.CODE);
       /* choiceFilter.getSelectionModel().select(AccountSearchType.NAME);
        choiceFilter.getSelectionModel().select(AccountSearchType.ACCOUNTCASHBOXNAME);
        choiceFilter.getSelectionModel().select(AccountSearchType.ISSOLID);
        choiceFilter.getSelectionModel().select(AccountSearchType.COMPANYIDENTIFIER);
        choiceFilter.getSelectionModel().select(AccountSearchType.ACCOUNTSTATENAME);
        choiceFilter.getSelectionModel().select(AccountSearchType.ACCOUNTMEDALNAME);
        choiceFilter.getSelectionModel().select(AccountSearchType.ACTIVITIESNAME);*/


        choiceFilter.setOnAction(e -> setEmptyFilterData());//setEmptyTableViewFilter()); //searchField.setText("");


    }

    @Override
    public void createTableColumns() {
        //customAccountFieldLabels();
        try {
            TableColumn<Counterpart, Integer> code = new TableColumn<Counterpart, Integer>("Код контрагента");
            TableColumn<Counterpart, String> name = new TableColumn<Counterpart, String>("Назва контрагента");
            TableColumn<Counterpart, String> accountCashboxName = new TableColumn<Counterpart, String>("Каса");
            TableColumn<Counterpart, String> isSolid = new TableColumn<Counterpart, String>("Солідність");
            TableColumn<Counterpart, String> companyIdentifier = new TableColumn<Counterpart, String>("Фірма");
            TableColumn<Counterpart, String> accountStateName = new TableColumn<Counterpart, String>("Статус");
            TableColumn<Counterpart, String> accountMedalName = new TableColumn<Counterpart, String>("Медаль");
            TableColumn<Counterpart, String> activitiesName = new TableColumn<Counterpart, String>("Діяльність");

            name.setMinWidth(150);
            tableView.getColumns().addAll(
                    code,
                    name,
                    accountCashboxName,
                    isSolid,
                    companyIdentifier,
                    accountStateName,
                    accountMedalName,
                    activitiesName);
            code.setCellValueFactory(new PropertyValueFactory<Counterpart, Integer>("Code"));
            name.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("Name"));
            accountCashboxName.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("AccountCashboxName"));
            isSolid.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("IsSolid"));
            companyIdentifier.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("CompanyIdentifier"));
            accountStateName.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("AccountStateName"));
            accountMedalName.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("AccountMedalName"));
            activitiesName.setCellValueFactory(new PropertyValueFactory<Counterpart, String>("ActivitiesName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }



    @Override
    public void loadDataFromDatabase() {

        try {
            ObservableList<Counterpart> listItems = (ObservableList<Counterpart>) account.findAll(true, (int) toIndex);

            listItems.forEach(item -> data.add((Counterpart) item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        //    return null;
    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {

    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {

    }

    @Override
    public void refreshData() {

    }

    @Override
    public void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {

    }




    private void filterData() {
        try {

            data.clear();

            String value = searchField.getText().toLowerCase();

            List<Counterpart> items = new ArrayList<Counterpart>();


            switch (choiceFilter.getValue()) {
                case CODE:
                    items = account.findByProperty(value, AccountSearchType.CODE);
                    break;
                case NAME:
                    items = account.findByProperty(value, AccountSearchType.NAME);
                    break;
                case ACCOUNTCASHBOXNAME:
                    items = account.findByProperty(value, AccountSearchType.ACCOUNTCASHBOXNAME);
                    break;
                case ISSOLID:
                    items = account.findByProperty(value, AccountSearchType.ISSOLID);
                    break;
                case COMPANYIDENTIFIER:
                    items = account.findByProperty(value, AccountSearchType.COMPANYIDENTIFIER);
                    break;
                case ACCOUNTSTATENAME:
                    items = account.findByProperty(value, AccountSearchType.ACCOUNTSTATENAME);
                    break;
                case ACCOUNTMEDALNAME:
                    items = account.findByProperty(value, AccountSearchType.ACCOUNTMEDALNAME);
                    break;
                case ACTIVITIESNAME:
                    items = account.findByProperty(value, AccountSearchType.ACTIVITIESNAME);
                    break;
                default:
            }


            items.forEach(item -> data.add(item));


            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Filtering tableview data exception: " + e);
        }

    }

    private void setEmptyFilterData() {
        ObservableList<Counterpart> items = (ObservableList<Counterpart>) account.findAll(true, (int) toIndex);
        items.forEach(item -> data.add((Counterpart) item));

        tableView.setItems(data);
    }

    public void fillChoiceBox() {
        try {
            choiceFilter.getItems().addAll(AccountSearchType.values());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Set choiceBox values exception: " + e);
        }
    }

    @FXML
    private void searchFieldOnKeyReleased(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            filterData();
        }

        if (searchField.getText().isEmpty()) {
            setEmptyFilterData();
        }
    }
  /*  private QueryRunner dbAccess = new QueryRunner();
 //   private Query accountQueries = new Query();
    private static final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";
        public List<Counterpart> findByProperty(Object value, Enum<? extends SearchType > searchType) {
            String whereClause="";
            String valueClause="";

            switch ((AccountSearchType) searchType) {
                case CODE:
                    whereClause=" WHERE [tbl_Account].[Code] = ?";
                    valueClause=value.toString();
                    break;

                case NAME:
                    whereClause=" WHERE [tbl_Account].[Name] LIKE '%" + searchField.getText() + "%'";
                    //  valueClause = "%" + value.toString() + "%";
                    break;
                case ACCOUNTCASHBOXNAME:
                    whereClause=" WHERE [tbl_AccountCashbox].[Identifier] = ?";
                    valueClause=value.toString();
                    break;
                case ISSOLID:
                    whereClause=" WHERE [tbl_Account].[IsSolid] LIKE ?";
                    valueClause="%" + value.toString() + "%";
                    break;
                case COMPANYIDENTIFIER:
                    whereClause=" WHERE [tbl_AccountCashbox].[CompanyIdentifier] = ?";
                    valueClause=value.toString();
                    break;
                case ACCOUNTSTATENAME:
                    whereClause=" WHERE [tbl_AccountState].[Name] LIKE ?";
                    valueClause="%" + value.toString() + "%";
                    break;
                case ACCOUNTMEDALNAME:
                    whereClause=" WHERE [tbl_AccountMedal].[Name] LIKE ?";
                    valueClause="%" + value.toString() + "%";
                    break;
                case ACTIVITIESNAME:
                    whereClause=" WHERE [tbl_Activities].[Name] LIKE ?";
                    valueClause="%" + value.toString() + "%";
                    break;

            }

            try {
                return dbAccess.query( DBConnection.getConnection(), accountQueries.getMainAccount(false, 0) + whereClause, new BeanListHandler<Counterpart>(Counterpart.class), valueClause);
            } catch (Exception e) {
                log.log( Level.SEVERE, "Column filter exception (findAccountByProperty): " + e);
            }

            return EMPTYLIST;
        }*/


    @FXML
    private void handleButtonSearch() {

        try {
            if (!searchField.getText().isEmpty())
                filterData();

        } catch (Exception e) {
            log.log(Level.SEVERE, "handleButtonSearch exception: " + e);
        }


    }


    /* @FXML
     private void handleButtonChoose() {
         Counterpart selectedAccount;
         try {
             selectedAccount = (Counterpart) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
         } catch (NullPointerException e) {
             UsefulUtils.showErrorDialog("Елемент не вибрано!");
             return;
         }
         chosenAccount = selectedAccount;
         closeWindow();
     }
 */
    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainAccountCount());


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }




 /*   public void tableViewHandles() {
        tableView.setOnMouseClicked(eventMouse -> setCellValues());
        tableView.setOnKeyReleased(eventKey -> setCellValues());
    }

   /* private void setCellValues() {

        labelList.forEach(action -> {
            Counterpart acc = (Counterpart)tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            switch (action.getId()) {
                case "label1": action.setText(acc.getName()); break;
                case "label2": action.setText(acc.getCode()); break;
                case "label3": action.setText(acc.getAccountCashboxName()); break;
                case "label4": action.setText(acc.getAccountMedalName()); break;
                case "label5": action.setText(acc.getAccountStateName()); break;
                case "label6": action.setText(acc.getActivitiesName()); break;
                case "label7": action.setText(acc.getCompanyIdentifier()); break;
                case "label8": action.setText(acc.getIsSolid()); break;

            }
        });
    }*/

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }


 /*   @FXML
    private Pagination pagination;
    private static StringBuilder query;
@FXML private TextField txt_search;
    private Connection con= null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<Counterpart> data;
    @FXML
    private TableView<Counterpart> tableSearchCounterpart;
    @FXML
    private TableColumn<?,?> columnCode;
    @FXML
    private TableColumn <?,?> columnName;
    @FXML
    private TableColumn <?,?> columnAccountCashboxName;
    @FXML
    private TableColumn <?,?> columnIsSolid;
    @FXML
    private TableColumn <?,?> columnCompanyIdentifier;
    @FXML
    private TableColumn <?,?> columnAccountStateName;
    @FXML
    private TableColumn<?,?> columnAccountMedalName;
    @FXML
    private TableColumn <?,?> columnActivitiesName;


    public void setCellTable(){
        columnCode.setCellValueFactory(new PropertyValueFactory<>("Code"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        columnAccountCashboxName.setCellValueFactory(new PropertyValueFactory<>("AccountCashboxName"));
        columnIsSolid.setCellValueFactory(new PropertyValueFactory<>("IsSolid"));
        columnCompanyIdentifier.setCellValueFactory(new PropertyValueFactory<>("CompanyIdentifier"));
        columnAccountStateName.setCellValueFactory(new PropertyValueFactory<>("AccountStateName"));
        columnAccountMedalName.setCellValueFactory(new PropertyValueFactory<>("AccountMedalName"));
        columnActivitiesName.setCellValueFactory(new PropertyValueFactory<>("ActivitiesName"));
    }*/
/* private Connection con= null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    public void loadDataForTable (){
        try {
            pst = con.prepareStatement( "SELECT TOP 40 " +
                    "\t[tbl_Account].[ID] AS [ID],\n" +
                    "\t[tbl_Account].[Name] AS [Name],\n" +
                    "\t[tbl_Account].[Code] AS [Code],\n" +
                    "\t[tbl_Account].[AccountCashboxID] AS [AccountCashboxID],\n" +
                    "\t[tbl_AccountCashbox].[Identifier] AS [AccountCashboxName],\n" +
                    "\tCASE WHEN [tbl_Account].[UnblockDate] > GETDATE() THEN\n" +
                    "\t(SELECT TOP 1\n" +
                    "\t\tN'Не солидный' AS [Exist]\n" +
                    "\tFROM\n" +
                    "\t\ttbl_Cashflow AS [Cashflow]\n" +
                    "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                    "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                    "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                    "                [Cashflow].[DestinationID] <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}'/*Advance*/// AND \n" +
                 /*   "\t\t([Cashflow].[PayDate] < (CAST(FLOOR(CAST(GETDATE() AS FLOAT)) AS DATETIME)))) \n" +
                   "                   AND [tbl_Account].[UnblockDate] < GETDATE()))\n" +
                    "\tELSE \n" +
                    "\t(SELECT TOP 1\n" +
                    "\t\tN'Не солидный' AS [Exist]\n" +
                    "\tFROM\n" +
                    "\t\ttbl_Cashflow AS [Cashflow]\n" +
                    "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                    "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                    "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                    "                [Cashflow].[DestinationID] <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}'/*Advance*/// AND\n" +
                  /* "\t\t(CAST([Cashflow].[PayDate] AS DATE) < CAST(CURRENT_TIMESTAMP AS DATE)))))\n" +
                   "END AS [IsSolid],\n" +
                    "\t[tbl_AccountCashbox].[CompanyIdentifier] AS [CompanyIdentifier],\n" +
                    "\t[tbl_Account].[AccountStateID] AS [AccountStateID],\n" +
                    "\t[tbl_AccountState].[Name] AS [AccountStateName],\n" +
                    "\t[tbl_Account].[AccountMedalID] AS [AccountMedalID],\n" +
                    "\t[tbl_AccountMedal].[Name] AS [AccountMedalName],\n" +
                    "\t[tbl_Account].[ActivitiesID] AS [ActivitiesID],\n" +
                    "\t[tbl_Activities].[Name] AS [ActivitiesName]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_Account] AS [tbl_Account]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_AccountCashbox] AS [tbl_AccountCashbox] ON [tbl_AccountCashbox].[ID] = [tbl_Account].[AccountCashboxID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_AccountState] AS [tbl_AccountState] ON [tbl_AccountState].[ID] = [tbl_Account].[AccountStateID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_AccountMedal] AS [tbl_AccountMedal] ON [tbl_AccountMedal].[ID] = [tbl_Account].[AccountMedalID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Activities] AS [tbl_Activities] ON [tbl_Activities].[ID] = [tbl_Account].[ActivitiesID]\n" +
                    "ORDER BY\n" +
                    "\t3 DESC" );
            rs = pst.executeQuery();
            while (rs.next()){
                data.add( new Counterpart( rs.getString( 3 ),rs.getString( 2 ),rs.getString( 5 ),rs.getString( 6 ),rs.getString( 7 ), rs.getString( 9 ), rs.getString( 11 ),rs.getString( 13 )) );
            }
        } catch (SQLException e) {
            Logger log = Logger.getLogger(DBConnection.class.getName());;
        }

        tableView.setItems(data);
    }
*/
/* private void searchCounterpart() {
        FilteredList<Counterpart> filteredData = new FilteredList< >(data, e -> true  );
        txt_search.setOnKeyReleased( e -> {
            txt_search.textProperty().addListener( (observable, oldValue, newValue) -> {
                filteredData.setPredicate( (Predicate<? super Counterpart>) user -> {
                    if (newValue == null || newValue.isEmpty()) {

                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getCode().contains( newValue )){
                        return true;
                    } else if (user.getName().toLowerCase().contains( lowerCaseFilter )){
                        return true;
                    }

                    return false;

                } );
            } );
            SortedList<Counterpart> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind( tableSearchCounterpart.comparatorProperty() );
            tableSearchCounterpart.setItems(sortedData);

        } );
    }
/*
    public long getMainAccountCount() {

        query = new StringBuilder();
        long count = 0;
        try {
            query.append("SELECT COUNT([tbl_Account].ID) AS [rowsCount] FROM tbl_Account \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_AccountType] AS [tbl_AccountType] ON [tbl_AccountType].[ID] = [tbl_Account].[AccountTypeID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_VATPayer] AS [tbl_VATPayer] ON [tbl_VATPayer].[ID] = [tbl_Account].[VATPayerID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_Taxation] AS [tbl_Taxation] ON [tbl_Taxation].[ID] = [tbl_Account].[TaxationID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_ContractReceived] AS [tbl_ContractReceivedFOP] ON [tbl_ContractReceivedFOP].[ID] = [tbl_Account].[ContractFOPReceivedID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_ContractReceived] AS [tbl_ContractReceivedFOPRepair] ON [tbl_ContractReceivedFOPRepair].[ID] = [tbl_Account].[ContractFOPRepairReceivedID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_ContractReceived] AS [tbl_ContractReceivedLLC] ON [tbl_ContractReceivedLLC].[ID] = [tbl_Account].[ContractLLCReceivedID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_PaymentType] AS [tbl_PaymentType] ON [tbl_PaymentType].[ID] = [tbl_Account].[PaymentTypeID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_AccountProblem] AS [tbl_AccountProblem] ON [tbl_AccountProblem].[ID] = [tbl_Account].[AccountProblemID] \n" +
                    "        LEFT OUTER JOIN \n" +
                    "        [dbo].[tbl_AccountMedal] AS [tbl_AccountMedal] ON [tbl_AccountMedal].[ID] = [tbl_Account].[AccountMedalID]\n");

            ResultSet rs = DBConnection.getConnection().createStatement().executeQuery(query.toString());

            while(rs.next())
            {
                count = rs.getLong("rowsCount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
    @FXML
    private void handleButtonSearch() {

        try {
            if(!txt_search.getText().isEmpty())
                searchCounterpart();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private long fromIndex;
    private long toIndex;

    public void loadDataFromDatabase() {

        try
        {
            List<Counterpart> listItems = new ArrayList<>( data.size() );

            listItems.forEach(item -> data.add(item));

            tableSearchCounterpart.setItems(data);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private void setEmptyFilterData() {
        List<Counterpart> items = new ArrayList<>(data.size());
        items.forEach(item -> data.add(item));

       tableSearchCounterpart.setItems(data);
    }
    @FXML
    private void searchFieldOnKeyReleased(KeyEvent e) {
        if(e.getCode().equals( KeyCode.ENTER)) {
            searchCounterpart();
        }

        if (txt_search.getText().isEmpty()) {
            setEmptyFilterData();
        }
    }



    public AnchorPane createPage(int pageIndex) {
        try {
            data = FXCollections.observableArrayList();
            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, getMainAccountCount());
            loadDataFromDatabase();
            tableSearchCounterpart.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        } catch (Exception e) {
            e.printStackTrace();
        }



        return new AnchorPane(tableSearchCounterpart);
    }
    public void tableViewHandles() {
        tableSearchCounterpart.setOnMouseClicked(eventMouse -> setCellTable());
        tableSearchCounterpart.setOnKeyReleased(eventKey -> setCellTable());
    }



   @Override
    public void initialize(URL location, ResourceBundle resources) {
       con = DBConnection.getConnection();
       data = FXCollections.observableArrayList();
       loadDataForTable();
       setCellTable();
       searchCounterpart();
       handleButtonSearch();*/
    //  CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
    //   tableViewHandles();
    // pagination.setSkin(pageSkin);

    //  pagination.setPageFactory(this::createPage);

    // }



 /*   private class CustomPaginationSkin extends PaginationSkin {
        private HBox controlBox;
        private Button prev;
        private Button next;
        private Button first;
        private Button last;

        private JFXTextField page;
        private void patchNavigation() {
            Pagination pagination = getSkinnable();
            Node control = pagination.lookup(".control-box");
            if (!(control instanceof HBox))
                return;
         //   controlBox = (HBox) control;
           // prev = (Button) controlBox.getChildren().get(0);
           // next = (Button) controlBox.getChildren().get(controlBox.getChildren().size() - 1);

         //   page = new JFXTextField();
           // page.setPromptText("Page");


            page.setOnAction(e -> {
                pagination.setCurrentPageIndex(Integer.parseInt(page.getText())-1);
            });
         //   page.setStyle("");
           /// page.setStyle("-fx-background-color: lightblue;" +
              //      "-fx-font-size: 15px;" +
               //     "-fx-text-fill: black");
            //page.setStyle("-fx-font-size: 15px");
            //page.setStyle("-fx-left");

            //controlBox.setSpacing(12);

            first = new Button("A");
            first.setOnAction(e -> {
                pagination.setCurrentPageIndex(0);
            });
            first.disableProperty().bind(
                    pagination.currentPageIndexProperty().isEqualTo(0));

            last = new Button("Z");
            last.setOnAction(e -> {
                pagination.setCurrentPageIndex(pagination.getPageCount());
            });
            last.disableProperty().bind(
                    pagination.currentPageIndexProperty().isEqualTo(
                            pagination.getPageCount() - 1));

            ListChangeListener childrenListener =c -> {
                while (c.next()) {
                    // implementation detail: when nextButton is added, the setup is complete
                    if (c.wasAdded() && !c.wasRemoved() // real addition
                            && c.getAddedSize() == 1 // single addition
                            && c.getAddedSubList().get(0) == next) {
                        addCustomNodes();
                    }
                }
            };
            controlBox.getChildren().addListener(childrenListener);
            addCustomNodes();
        }
        protected void addCustomNodes() {
            // guarding against duplicate child exception
            // (some weird internals that I don't fully understand...)
            if (first.getParent() == controlBox) return;
            controlBox.getChildren().add(0, first);
            controlBox.getChildren().add(last);
            controlBox.getChildren().add(page);
        }



        public CustomPaginationSkin(Pagination pagination) {

            super(pagination);
            patchNavigation();

        }
    }*/
}
