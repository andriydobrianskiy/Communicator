package com.mainPage.ArchiveFiles.ArchiveFilesRequest;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.ArchiveFiles.ArchiveFiles;
import com.mainPage.ArchiveFiles.ArchiveFilesController;
import com.mainPage.InTract.InTractRequest.InTractRequest;
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
import java.util.logging.Logger;

public class ArchiveFilesRequestController extends WorkArea implements Initializable{

    private static Logger log = Logger.getLogger(ArchiveFilesRequestController.class.getName());
    @FXML private FilterableStringTableColumn<ArchiveFilesRequest, String> colIndex;

    @FXML private FilterableStringTableColumn <ArchiveFilesRequest, String>colSkrut;

    @FXML private FilterableStringTableColumn <ArchiveFilesRequest, String>colNewDescription;

    @FXML private FilterableStringTableColumn <ArchiveFilesRequest, String>colOfferingName;

    @FXML private FilterableIntegerTableColumn<ArchiveFilesRequest, Integer> colQuantity;

    @FXML private FilterableStringTableColumn <ArchiveFilesRequest, String>colDefaultOfferingCode;

    @FXML private FilterableStringTableColumn <ArchiveFilesRequest, String>colNewOfferingCode;
    private DerbyArchiveRequestDAO derbyArchiveRequestDAO = new DerbyArchiveRequestDAO();

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs= null;
    private ObservableList<ArchiveFilesRequest> data;
   /* @FXML
    private AnchorPane anchorPane;*/
    private ArchiveFiles selectedRecord;




    private ArchiveFilesController main;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();  DBConnection database = new DBConnection();
            database.reconnect();
        }
        tableView.setTableMenuButtonVisible(true);
        setCellTable();
        data = FXCollections.observableArrayList();
        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableView);
    }
    public void init(ArchiveFilesController archiveFilesController) {
        main = archiveFilesController;
    }

    public void handleTableView(ArchiveFiles value) {
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



            colIndex.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest,String>("index"));
            colSkrut.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest,String>("skrut"));
            colNewDescription.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest, String>("newDescription"));
            colOfferingName.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest,String>("offeringName"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest,Integer>("quantity"));
            colDefaultOfferingCode.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest, String>("defaultOfferingCode"));
            colNewOfferingCode.setCellValueFactory(new PropertyValueFactory<ArchiveFilesRequest,String>("newOfferingCode"));



        } catch (Exception e) {
            e.printStackTrace();
        }
        super.createTableColumns();
    }
    private StringBuilder query;
    public void loadDataFromDatabase(){
        try {

            data = FXCollections.observableArrayList();
            List<ArchiveFilesRequest> listItems = derbyArchiveRequestDAO.findAll(selectedRecord.getID());

            listItems.forEach(item -> data.add(item));
            super.loadDataFromDatabase();

            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }

    }



}
