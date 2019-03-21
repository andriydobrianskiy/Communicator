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



    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
