package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import com.Utils.CustomPaginationSkin;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

//import sample.mainPage.InProcessing.InProcessing;

public class ProductSearchController extends BorderPane implements Initializable  {

    private static Logger log = Logger.getLogger(ProductSearchController.class.getName());

    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private Pagination pagination;
    @FXML
    private JFXButton btnSearch;
    @FXML
    private TableView  tableView;
  /*  @FXML TableColumn<?,?> colunmIndex;
    @FXML TableColumn<?,?> columnIsHiddenSkrut;
    @FXML TableColumn<?,?> columnSkrut;
    @FXML TableColumn<?,?> columnOfferingName;
    @FXML TableColumn<?,?> OfferingTypeName;
    @FXML TableColumn<?,?> columnCustomsCode;
    @FXML TableColumn<?,?> columnOfferingPhoto;
    @FXML TableColumn<?,?> columnSaledByYear;
    @FXML TableColumn<?,?> columnSeasonIndex;
    @FXML TableColumn<?,?> columnOfferingBrandCode;
    @FXML TableColumn<?,?> columnAvailable;
*/
    @FXML
    private JFXTextField searchField;
    @FXML
    private JFXComboBox<ProductSearchType> choiceFilter;
    @FXML
    private BorderPane borderPane;
    private ProductQuery accountQueries = new ProductQuery();
    private ObservableList<ProductSearch> data;
    public static ProductSearch chosenElement = null;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs= null;

    private ProductSearch account = new ProductSearch();
    private ProductSearchDAO derbyOfferingRequest = new ProductSearchDAO();
    private long fromIndex;
    private long toIndex;



    @Override
    public void initialize(URL location, ResourceBundle resources) {




        createTableColumns();
        fillChoiceBox();


        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {

                    chosenElement = (ProductSearch) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    closeWindow();
                }
            }
        });




        btnOK.setOnAction( event -> {

            chosenElement = (ProductSearch) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            closeWindow();
        });

        btnCancel.setOnAction(event -> {
            closeWindow();
        });
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination

        pagination.setSkin(pageSkin);
        pagination.setPageFactory(this::createPage);
        choiceFilter.getSelectionModel().select(ProductSearchType.INDEX);
     //   choiceFilter.getSelectionModel().select(ProductSearchType.SKRUT);

        choiceFilter.setOnAction(e ->  setEmptyFilterData());
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
    }


    @FXML
    public void handleButtonSearch (ActionEvent event){

    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    public void createTableColumns () {
        TableColumn<ProductSearch, Integer> index = new TableColumn<ProductSearch, Integer>("Індекс");
        TableColumn<ProductSearch, String> isHiddenSkrut = new TableColumn<ProductSearch, String>("Скорочення");
        TableColumn<ProductSearch, String> skrut = new TableColumn<ProductSearch, String>("Скрут");
        TableColumn<ProductSearch, String> offeringName = new TableColumn<ProductSearch, String>("Назва продукту (UA)");
        TableColumn<ProductSearch, String> offeringTypeName = new TableColumn<ProductSearch, String>("Тип");
        TableColumn<ProductSearch, String> customsCode = new TableColumn<ProductSearch, String>("Митний код");
        TableColumn<ProductSearch, String> offeringPhoto = new TableColumn<ProductSearch, String>("Фото товару");
        TableColumn<ProductSearch, Double> saledByYear = new TableColumn<ProductSearch, Double>("Продано за рік");
        TableColumn<ProductSearch, Integer> seasonIndex = new TableColumn<ProductSearch, Integer>("Індекс сезону");
        TableColumn<ProductSearch, String> offeringBrandCode = new TableColumn<ProductSearch, String>("Код бренда");
        TableColumn<ProductSearch, Double> available = new TableColumn<ProductSearch, Double>("Доступно");


        // number.setVisible(false);
        // tableRequestOffering.setTableMenuButtonVisible(true);

        index.setMinWidth(150);
        //  Number = new FilterableStringTableColumn<>("Номер запиту");
        //FilterableStringTableColumnNumber);
        //    id.setVisible(false);
        tableView.setTableMenuButtonVisible(true);
        tableView.getColumns().addAll(
                index,
                isHiddenSkrut,
                skrut,
                offeringName,
                offeringTypeName,
                customsCode,
                offeringPhoto,
                saledByYear,
                seasonIndex,
                offeringBrandCode,
                available);




        index.setCellValueFactory(new PropertyValueFactory<ProductSearch, Integer>("index"));
        isHiddenSkrut.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("isHiddenSkrut"));
        skrut.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("skrut"));
        offeringName.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("offeringName"));
        offeringTypeName.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("offeringTypeName"));
        customsCode.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("customsCode"));
        offeringPhoto.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("OfferingPhoto"));
        saledByYear.setCellValueFactory(new PropertyValueFactory<ProductSearch, Double>("saledByYear"));
        seasonIndex.setCellValueFactory(new PropertyValueFactory<ProductSearch, Integer>("seasonIndex"));
        offeringBrandCode.setCellValueFactory(new PropertyValueFactory<ProductSearch, String>("offeringBrandCode"));
        available.setCellValueFactory(new PropertyValueFactory<ProductSearch, Double>("available"));

    }

    //@Override
    public void createTableColumnsAll() {

    }


    public void loadDataFromDatabase() {
        try {
            List<ProductSearch> listItems =  derbyOfferingRequest.findAll(true, (int) toIndex);
            listItems.forEach(item -> data.add(item));
            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }

    }



    public BorderPane createPage(int pageIndex) {
        try {


           data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainProductSearchCount());


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }
        //borderPane.setCenter(tableView);

        return borderPane;
    }


    private void filterData() {
        try {

            data.clear();

            String value = searchField.getText();

            List<ProductSearch> items = new ArrayList<ProductSearch>();


            switch (choiceFilter.getValue()) {
                case INDEX:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.INDEX);
                    break;
                case ISHIDDENSKRUT:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.ISHIDDENSKRUT);
                    break;
                case SKRUT:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.SKRUT);
                    break;
                case OFFERINGNAME:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.OFFERINGNAME);
                    break;
               /* case HASANALOGUE:
                    items = account.findByProperty(value, ProductSearchType.HASANALOGUE);
                    break;*/
              /*  case DEFAULTOFFERINGCODE:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.DEFAULTOFFERINGCODE);
                    break;
                /*case OFFERINGTYPEID:
                    items = account.findByProperty(value, ProductSearchType.OFFERINGTYPEID);
                    break;
                case SUPLIERID:
                    items = account.findByProperty(value, ProductSearchType.SUPLIERID);
                    break;
                case OWNERID:
                    items = account.findByProperty(value, ProductSearchType.OWNERID);
                    break;*/
             /*   case NAMEPOLISH:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.NAMEPOLISH);
                    break;
                case REMARK:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.REMARK);
                    break;
               /* case OFFERINGVENDORID:
                    items = account.findByProperty(value, ProductSearchType.OFFERINGVENDORID);
                    break;*/
              /*  case OFFERINGVENDORNAME:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.OFFERINGVENDORNAME);
                    break;
               /* case PRICENOTFROMEUROID:
                    items = account.findByProperty(value, ProductSearchType.PRICENOTFROMEUROID);
                    break;*/
           /*     case CUSTOMSCODE:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.CUSTOMSCODE);
                    break;
               /* case OFFERINGANALOGUE:
                    items = account.findByProperty(value, ProductSearchType.OFFERINGANALOGUE);
                    break;*/
               /* case NAMEWMD:
                    items = account.findByProperty(value, ProductSearchType.NAMEWMD);
                    break;*/
              /*  case CLEANOFFERINGCODE:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.CLEANOFFERINGCODE);
                    break;

                case SEASONINDEX:
                    items = derbyOfferingRequest.findByProperty(value, ProductSearchType.SEASONINDEX);
                    break;*/
                default:
            }


            items.forEach(item -> data.add((ProductSearch) item));


            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Filtering tableview data exception: " + e);
        }

    }

    private void setEmptyFilterData() {
        List<ProductSearch> items = derbyOfferingRequest.findAll(true, (int) toIndex);
        items.forEach(item -> data.add((ProductSearch) item));

        tableView.setItems(data);
    }

    public void fillChoiceBox() {
        try {
            choiceFilter.getItems().addAll(ProductSearchType.values());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Set choiceBox values exception: " + e);
        }
    }

    @FXML
    private void searchFieldOnKeyReleased(KeyEvent e) {
        if (e.getCode().equals( KeyCode.ENTER )) {
            filterData();
        }

        if (searchField.getText().isEmpty()) {
            setEmptyFilterData();
        }
    }



    @FXML
    private void handleButtonSearch() {

        try {
            if(!searchField.getText().isEmpty()) {
                filterData();
            }else {
                setEmptyFilterData();
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "handleButtonSearch exception: " + e);
        }

    }


}
