package com.mainPage.NotFulled.OfferingRequest;

import com.Utils.UsefulUtils;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.WorkArea;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;
import org.google.jhsheets.filtered.tablecolumn.FilterableIntegerTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExampleController extends WorkArea implements Initializable {


    private OfferingRequestQuery queries = new OfferingRequestQuery();
    private DerbyOfferingRequestDAO derbyOfferingRequest = new DerbyOfferingRequestDAO();


    private static Logger log = Logger.getLogger(ExampleController.class.getName());

    public ObservableList<OfferingRequest> data;
    private NotFulledController main;

    private NotFulfilled selectedRecord;
    public OfferingRequest offeringRequest;

    @FXML private FilterableStringTableColumn <OfferingRequest, String> colIndex;

    @FXML private FilterableStringTableColumn <OfferingRequest, String>colSkrut;

    @FXML private FilterableStringTableColumn <OfferingRequest, String>colNewDescription;

    @FXML private FilterableStringTableColumn <OfferingRequest, String>colOfferingName;

    @FXML private FilterableIntegerTableColumn<OfferingRequest, Integer> colQuantity;

    @FXML private FilterableStringTableColumn <OfferingRequest, String>colDefaultOfferingCode;

    @FXML private FilterableStringTableColumn <OfferingRequest, String>colNewOfferingCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getStylesheets().add
                (ExampleController.class.getResource("/styles/TableStyle.css").toExternalForm());
        try {
            createTableColumns();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
        tableView.setTableMenuButtonVisible(true);
        //loadDataFromDatabaseBottom();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


//        UsefulUtils.installCopyPasteHandler(tableView);

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



    public void createTableColumns() {

        try {
            hashColumns.put(colIndex, "[tbl_RequestOffering].[Number]");
            hashColumns.put(colSkrut, "[tbl_RequestOffering].[CreatedOn]");
            hashColumns.put(colNewDescription, "[tbl_Contact].[Name]");
            hashColumns.put(colOfferingName, "[tbl_Account].[Name]");
            hashColumns.put( colQuantity, "[tbl_Account].[Code]");
            hashColumns.put(colDefaultOfferingCode, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colNewOfferingCode, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");



            colIndex.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<OfferingRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<OfferingRequest,Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<OfferingRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<OfferingRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.createTableColumns();

    }


    public void refresh (){
        data.clear();

        loadDataFromDatabaseBottom();
        UsefulUtils.fadeTransition(tableView);
    }


}
