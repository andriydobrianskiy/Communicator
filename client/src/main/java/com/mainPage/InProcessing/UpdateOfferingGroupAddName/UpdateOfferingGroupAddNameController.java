package com.mainPage.InProcessing.UpdateOfferingGroupAddName;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.mainPage.page.contact.ContactNameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateOfferingGroupAddNameController implements Initializable {
    protected static Logger log = Logger.getLogger(UpdateOfferingGroupAddNameController.class.getName());
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;

    @FXML
    private Tab tabGroupId;
    @FXML
    private Tab tabNotPriceId;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn colName;
    protected ObservableList<UpdateOfferingGroupAddName> data;
    private UpdateOfferingGroupAddName updateOfferingGroupAddName = new UpdateOfferingGroupAddName();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {

            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            //  database.disconnect();
            database.reconnect();
        }
        data = FXCollections.observableArrayList();

        createTableColumns();
        loadDataFromDatabase();
    }

    @FXML
    private void actionContactName(ActionEvent event) {
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/ContactNameView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setMaxHeight(570);
        stage.setMaxWidth(900);


        stage.showAndWait();
        ContactNameController contactNameController = new ContactNameController();
        //   colName.setUserData(contactNameController.chosenElement.getContactName());
       if (tabGroupId.getTabPane().getSelectionModel().getSelectedIndex() == 0) {
            administrationTable(contactNameController.chosenElement.getID(), 1 );
        } else {
            administrationTable(contactNameController.chosenElement.getID(), 2 );
       }
        refreshData();
        //  tableView.getItems().setAll(contactNameController.chosenElement.getContactName());
         //  tableView.getSelectionModel().select(contactNameController.chosenElement.getContactName());
    }

    public void administrationTable(String nameController, Integer type) {

        String sql = "UPDATE tbl_Contact \n" +
                "SET \n" +
                "CommunicatorTypeGroup = ? \n" +
                "WHERE \n" +
                "ID = ?";

        try {
            pst = con.prepareStatement(sql);
            pst.setInt(1, type);
            pst.setString(2, nameController);


            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        //refreshData();

    }

    public void createTableColumns() {
        try {
            colName.setCellValueFactory(new PropertyValueFactory<UpdateOfferingGroupAddName, String>("name"));

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }
    public void loadDataFromDatabase() {
        int element;
        if (tabGroupId.getTabPane().getSelectionModel().getSelectedIndex() == 0) {
            element = 1;
        } else {
            element = 2;
        }
        try {
            ObservableList<UpdateOfferingGroupAddName> listItems = (ObservableList<UpdateOfferingGroupAddName>) updateOfferingGroupAddName.findAll(true, (int) 50, element);

            listItems.forEach(item -> data.add((UpdateOfferingGroupAddName) item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
    }



    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            loadDataFromDatabase();
            UsefulUtils.fadeTransition(tableView);
        }

    }

    public void actionDeleteContact(ActionEvent event) {

        try {
            updateOfferingGroupAddName = (UpdateOfferingGroupAddName) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }

            String sql = "UPDATE tbl_Contact \n" +
                    "SET \n" +
                    "CommunicatorTypeGroup = ? \n" +
                    "WHERE \n" +
                    "ID = ?";

            try {
                pst = con.prepareStatement(sql);
                pst.setInt(1, 0);
                pst.setString(2, updateOfferingGroupAddName.getID());


                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace();
                DBConnection database = new DBConnection();
                database.reconnect();
            }
            refreshData();


    }
}
