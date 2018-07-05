package com.mainPage.Sructure;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import com.connectDatabase.DBConnection;
import com.login.User;
import com.mainPage.page.MainPageController;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StructureController implements Initializable {


    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;
    private MainPageController main;
    @FXML
    private BorderPane borderPane;
    @FXML
    private ImageView imageView;
    @FXML
    private JFXButton buttonImage;
    private Image image;
    private File file;
    private FileChooser fileChooser;
    private Desktop desktop = Desktop.getDesktop();
    private FileInputStream fs;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();  DBConnection database = new DBConnection();
            database.reconnect();
        }

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.jpeg", "*.pdf", "*.tiff"),
                new FileChooser.ExtensionFilter("JPEG (*.JPEG, *.jpeg)", "*.jpeg", "*.JPEG"),
                new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"),
                new FileChooser.ExtensionFilter("PDF (*.PDF, *.pdf)", "*.pdf", "*.PDF"),
                new FileChooser.ExtensionFilter("TIF (*.tif, *.tiff)", "*.tif", "*.tiff"));

    }

    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }

    private Stage stage;

    public void addImage() {

        /*    String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                    "\tSET [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                    "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                    "\t[ModifiedByID] = ?\n" +
                    "WHERE([tbl_RequestOffering].[ID] = ?)";
            try {
                pst = con.prepareStatement(query);
                pst.setString(1, User.getContactID());
                pst.setString(2, chosenAccount.getID());

                pst.executeUpdate();
                main.changeExists();


                //    ExampleController exampleController = loader.getController();
                //
                // exampleProductTableController.refresh();
             //   productAddController.setExampleController(exampleProductTableController);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            } else return;
*/

        String sql = "Insert Into tbl_fotoStructure (CreatedOn, CreatedByID, ModifiedOn, ModifiedByID, [Image])\n" +
                "VALUES (CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?, ?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, User.getContactID());
            pst.setString(2, User.getContactID());
            try {
                fs = new FileInputStream(file);
                pst.setBinaryStream(3, fs, file.length());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void handleButton(ActionEvent event) {

        stage = (Stage) borderPane.getScene().getWindow();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            System.out.println("" + file.getAbsolutePath());
            image = new Image(file.getAbsoluteFile().toURI().toString(), imageView.getFitWidth(), imageView.getFitHeight(), true, true);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
        }

//addImage();

    }




}
