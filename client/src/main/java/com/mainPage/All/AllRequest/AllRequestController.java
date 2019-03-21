package com.mainPage.All.AllRequest;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.All.All;
import com.mainPage.All.AllController;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class AllRequestController extends WorkArea implements Initializable {



    @FXML
    private AllController allController;

    @FXML private FilterableStringTableColumn<AllRequest, String> colIndex;

    @FXML private FilterableStringTableColumn <AllRequest, String>colSkrut;

    @FXML private FilterableStringTableColumn <AllRequest, String>colNewDescription;

    @FXML private FilterableStringTableColumn <AllRequest, String>colOfferingName;

    @FXML private FilterableIntegerTableColumn<AllRequest, Integer> colQuantity;

    @FXML private FilterableStringTableColumn <AllRequest, String>colDefaultOfferingCode;

    @FXML private FilterableStringTableColumn <AllRequest, String>colNewOfferingCode;

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs= null;
    private ObservableList<AllRequest> data;
    private All selectedRecord;
    public DerbyAllRequestDAO derbyAllRequestDAO = new DerbyAllRequestDAO();


public void init (AllController allController){
    this.allController = allController;
}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setTableMenuButtonVisible(true);
        setCellTable();
        data = FXCollections.observableArrayList();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);
    }

    public void handleTableView(All value) {
        this.selectedRecord = value;
        data.clear();
        loadDataFromDatabase();
    }
    private void setCellTable(){
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



            colIndex.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<AllRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<AllRequest,Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<AllRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<AllRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.createTableColumns();
    }
    private StringBuilder query;
    public void loadDataFromDatabase(){
        try {

            data = FXCollections.observableArrayList();
            List<AllRequest> listItems = derbyAllRequestDAO.findAll(selectedRecord.getID());

            listItems.forEach(item -> data.add(item));
            super.loadDataFromDatabase();

            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }

    }



}
