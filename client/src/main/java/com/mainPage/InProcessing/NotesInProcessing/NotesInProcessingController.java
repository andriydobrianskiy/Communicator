package com.mainPage.InProcessing.NotesInProcessing;

import com.Utils.GridComp;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.login.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class NotesInProcessingController implements Initializable {

    private static Logger log = Logger.getLogger(NotesInProcessingController.class.getName());

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    public static GridComp offeringRequest;
    @FXML
    private JFXButton btn_OK;
    @FXML
    private JFXButton btn_Cancel;
    @FXML
    private TextArea textArea;
    private NotesInProcessing columnMessage = new NotesInProcessing();

    public NotesInProcessingController() {

    }


    public NotesInProcessingController(NotesInProcessing columnMessage) {
        this.columnMessage = columnMessage;
    }

    public NotesInProcessingController(GridComp offeringRequest, boolean status) {
        this.offeringRequest = offeringRequest;
        showWindow();
        loadDataFromDataBase();

    }


    private ObservableList<NotesInProcessing> data;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();  DBConnection database = new DBConnection();
            database.reconnect();
        }
        data = FXCollections.observableArrayList();
            loadDataFromDataBase();
    }

    public void loadDataFromDataBase() {
        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement("SELECT Note from tbl_RequestOffering where ID = ?");

            pst.setString(1, offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new NotesInProcessing(rs.getString(1)));
                textArea.setText(rs.getString(1));

            }

        } catch (SQLException e) {
            e.printStackTrace();  DBConnection database = new DBConnection();
            database.reconnect();
        }
    }




    @FXML
    private void actionOK(ActionEvent event) {


        String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                "\tSET [Note] = ?,\n" +
                "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                "\t[ModifiedByID] = ?\n" +
                "WHERE([tbl_RequestOffering].[ID] = ?)";

        try {
            try {
                pst = con.prepareStatement(query);

            } catch (SQLException e) {
                e.printStackTrace();
            }
            pst.setString(1, textArea.getText());
            pst.setString(2, User.getContactID());
            pst.setString(3, offeringRequest.getID());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) btn_Cancel.getScene().getWindow();
        stage.close();

    }

    public void showWindow() {
        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/NotesInProcessingView.fxml"));
            Parent root = fxml.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void actionCancel(ActionEvent event) {
        Stage stage = (Stage) btn_Cancel.getScene().getWindow();
        stage.close();
    }


}
