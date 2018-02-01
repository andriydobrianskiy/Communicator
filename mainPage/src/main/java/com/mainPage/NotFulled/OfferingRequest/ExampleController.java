package com.mainPage.NotFulled.OfferingRequest;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import com.Utils.UsefulUtils;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleController implements Initializable {

    @FXML public TableView tableView;

    private OfferingRequestQuery queries = new OfferingRequestQuery();
    private DerbyOfferingRequestDAO derbyOfferingRequest = new DerbyOfferingRequestDAO();


    private static Logger log = Logger.getLogger(ExampleController.class.getName());

    public ObservableList<OfferingRequest> data;
    @FXML
    private BorderPane anchorPane;

    private NotFulledController main;

    private NotFulfilled selectedRecord;
    public OfferingRequest offeringRequest;

//private NotFulledController notFulledController = new NotFulledController();
//private ProductList productList = new ProductList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getStylesheets().add
                (ExampleController.class.getResource("/styles/TableStyle.css").toExternalForm());
        try {
            createTableColumnsProduct();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
        //loadDataFromDatabaseBottom();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);

    }


    public void init(NotFulledController notFulledController) {
        main = notFulledController;
    }

    public void handleTableView(NotFulfilled value) {
        this.selectedRecord = value;
        loadDataFromDatabaseBottom();
    }



    public void loadDataFromDatabaseBottom() {

        try {

            data = FXCollections.observableArrayList();
            System.out.println(selectedRecord.getID()+ "Offering Request");
            List<OfferingRequest> listItems = derbyOfferingRequest.findAll(10, selectedRecord.getID() );

            listItems.forEach(item -> data.add(item));

            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }



    public void createTableColumnsProduct() {

        try {
            TableColumn<OfferingRequest, String> index = new TableColumn<OfferingRequest, String>("Індекс");
            TableColumn<OfferingRequest, String> skrut = new TableColumn<OfferingRequest,String>("Скорочення");
            TableColumn<OfferingRequest, String> newDescription = new TableColumn<>("Назва/Опис");
            TableColumn<OfferingRequest, String> offeringName = new TableColumn<OfferingRequest, String>("Продукт");
            TableColumn<OfferingRequest, String> quantity = new TableColumn<OfferingRequest, String>("Кількість");
            TableColumn<OfferingRequest, String> defaultOfferingCode = new TableColumn<OfferingRequest, String>("Код товару");
            TableColumn<OfferingRequest, String> newOfferingCode = new TableColumn<>("Код товару (новий)");



            index.setMinWidth(150);

             //   tableProduct.setVisible(false);
            tableView.setTableMenuButtonVisible(true);
            tableView.getColumns().addAll(
                    index,
                    skrut,
                    newDescription,
                    offeringName,
                    quantity,
                    defaultOfferingCode,
                    newOfferingCode
            );
            index.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("index"));
            skrut.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("skrut"));
            newDescription.setCellValueFactory(new PropertyValueFactory<OfferingRequest, String>("newDescription"));
            offeringName.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("offeringName"));
            quantity.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("quantity"));
            defaultOfferingCode.setCellValueFactory(new PropertyValueFactory<OfferingRequest, String>("defaultOfferingCode"));
            newOfferingCode.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void refresh (){
        data.clear();

        loadDataFromDatabaseBottom();
        UsefulUtils.fadeTransition(tableView);
    }




}
