package com.mainPage.InProcessing.NotesInProcessing;

import com.jfoenix.controls.JFXButton;
import com.mainPage.ArchiveFiles.ArchiveFiles;
import com.mainPage.InTract.InTract;
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
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.login.User;
import com.mainPage.InProcessing.InProcessing;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;



public class NotesInProcessingController implements Initializable {

    private static Logger log = Logger.getLogger(NotesInProcessingController.class.getName());

    private Connection con= null;
    private PreparedStatement pst= null;
    private ResultSet rs = null;
    public static InProcessing offeringRequest;
    public static InTract offeringTract;
    public static ArchiveFiles offeringArchive;
    @FXML
    private JFXButton btn_OK;
    @FXML
    private JFXButton btn_Cancel;
    @FXML
    private TextArea textArea;
    private NotesInProcessing columnMessage = new NotesInProcessing();

    public NotesInProcessingController() {

    }


    /*public NotesInProcessingController(InProcessing offeringRequest) {
        this.offeringRequest = offeringRequest;
    }*/

    public NotesInProcessingController(NotesInProcessing columnMessage) {
        this.columnMessage = columnMessage;
    }

    public NotesInProcessingController(InProcessing offeringRequest, boolean status) {
        this.offeringRequest = offeringRequest;
    //    loadDataFromDataBase();
        showWindow();


    }
    public NotesInProcessingController(InTract offeringTract, boolean status) {
        this.offeringTract = offeringTract;

        showWindow();



    }

    public NotesInProcessingController(ArchiveFiles offeringArchive, boolean status) {
        this.offeringArchive = offeringArchive;
        loadDataFromDataBaseArchive();
        showWindow();


    }

    private ObservableList<NotesInProcessing> data;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data = FXCollections.observableArrayList();
//        loadDataFromDataBase();
        loadDataFromDataBase();
    }

    public void loadDataFromDataBase () {
        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement("SELECT \n" +
                    "\t[tbl_OfferingInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_OfferingInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[ModifiedOn] AS [ModifiedOn],\n" +
                    "\t[tbl_OfferingInRequestOffering].[ModifiedByID] AS [ModifiedByID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[OfferingID] AS [OfferingID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[RequestID] AS [RequestID],\n" +
                    "\t[tbl_RequestOffering].Note AS [Note],\n" +
                    "\t[tbl_Offering].[Index] AS [Index],\n" +
                    "\t[tbl_Offering].[Skrut] AS [Skrut],\n" +
                    "\t[tbl_Offering].[Name] AS [OfferingName],\n" +
                    "\t[tbl_Offering].[DefaultOfferingCode] AS [DefaultOfferingCode],\n" +
                    "\t[tbl_OfferingInRequestOffering].[Quantity] AS [Quantity],\n" +
                    "\t[tbl_OfferingInRequestOffering].[NewOfferingCode] AS [NewOfferingCode],\n" +
                    "\t[tbl_OfferingInRequestOffering].[NewDescription] AS [NewDescription],\n" +
                    "\t(SELECT TOP 1\n" +
                    "\t\t'-' AS [Exist]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_OfferingAnalogue] AS [tbl_OfferingAnalogue]\n" +
                    "\tWHERE([tbl_OfferingAnalogue].[OfferingID] = [tbl_OfferingInRequestOffering].[OfferingID])) AS [IsRoot]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_OfferingInRequestOffering] AS [tbl_OfferingInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Offering] AS [tbl_Offering] ON [tbl_Offering].[ID] = [tbl_OfferingInRequestOffering].[OfferingID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_OfferingInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_OfferingInRequestOffering].[RequestID] = ?)");
            try {
                System.out.println(offeringRequest.getID());
            } catch (NullPointerException e) {
                log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringRequest.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new NotesInProcessing(rs.getString(8)));
                textArea.setText(rs.getString("note"));


               /* StringBuilder builder = new StringBuilder();
                data.forEach(item -> {
                    builder.append(item.getNote());

                   // builder.append(item.getNote() + "\n");

                });

                textArea.setText(builder.toString());*/


            }

		/*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
       data.clear();
    }


    public void loadDataFromDataBaseTract () {
        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement("SELECT \n" +
                    "\t[tbl_OfferingInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_OfferingInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[ModifiedOn] AS [ModifiedOn],\n" +
                    "\t[tbl_OfferingInRequestOffering].[ModifiedByID] AS [ModifiedByID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[OfferingID] AS [OfferingID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[RequestID] AS [RequestID],\n" +
                    "\t[tbl_RequestOffering].Note AS [Note],\n" +
                    "\t[tbl_Offering].[Index] AS [Index],\n" +
                    "\t[tbl_Offering].[Skrut] AS [Skrut],\n" +
                    "\t[tbl_Offering].[Name] AS [OfferingName],\n" +
                    "\t[tbl_Offering].[DefaultOfferingCode] AS [DefaultOfferingCode],\n" +
                    "\t[tbl_OfferingInRequestOffering].[Quantity] AS [Quantity],\n" +
                    "\t[tbl_OfferingInRequestOffering].[NewOfferingCode] AS [NewOfferingCode],\n" +
                    "\t[tbl_OfferingInRequestOffering].[NewDescription] AS [NewDescription],\n" +
                    "\t(SELECT TOP 1\n" +
                    "\t\t'-' AS [Exist]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_OfferingAnalogue] AS [tbl_OfferingAnalogue]\n" +
                    "\tWHERE([tbl_OfferingAnalogue].[OfferingID] = [tbl_OfferingInRequestOffering].[OfferingID])) AS [IsRoot]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_OfferingInRequestOffering] AS [tbl_OfferingInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Offering] AS [tbl_Offering] ON [tbl_Offering].[ID] = [tbl_OfferingInRequestOffering].[OfferingID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_OfferingInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_OfferingInRequestOffering].[RequestID] = ?)");
            try {
                System.out.println(offeringTract.getID());
            } catch (NullPointerException e) {
                log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringTract.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new NotesInProcessing(rs.getString(8)));
                textArea.setText(rs.getString("note"));


               /* StringBuilder builder = new StringBuilder();
                data.forEach(item -> {
                    builder.append(item.getNote());

                   // builder.append(item.getNote() + "\n");

                });

                textArea.setText(builder.toString());*/


            }

		/*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.clear();
    }
    public void loadDataFromDataBaseArchive () {
        try {
            pst = DBConnection.getDataSource().getConnection().prepareStatement("SELECT \n" +
                    "\t[tbl_OfferingInRequestOffering].[ID] AS [ID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                    "\t[tbl_OfferingInRequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[ModifiedOn] AS [ModifiedOn],\n" +
                    "\t[tbl_OfferingInRequestOffering].[ModifiedByID] AS [ModifiedByID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[OfferingID] AS [OfferingID],\n" +
                    "\t[tbl_OfferingInRequestOffering].[RequestID] AS [RequestID],\n" +
                    "\t[tbl_RequestOffering].Note AS [Note],\n" +
                    "\t[tbl_Offering].[Index] AS [Index],\n" +
                    "\t[tbl_Offering].[Skrut] AS [Skrut],\n" +
                    "\t[tbl_Offering].[Name] AS [OfferingName],\n" +
                    "\t[tbl_Offering].[DefaultOfferingCode] AS [DefaultOfferingCode],\n" +
                    "\t[tbl_OfferingInRequestOffering].[Quantity] AS [Quantity],\n" +
                    "\t[tbl_OfferingInRequestOffering].[NewOfferingCode] AS [NewOfferingCode],\n" +
                    "\t[tbl_OfferingInRequestOffering].[NewDescription] AS [NewDescription],\n" +
                    "\t(SELECT TOP 1\n" +
                    "\t\t'-' AS [Exist]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_OfferingAnalogue] AS [tbl_OfferingAnalogue]\n" +
                    "\tWHERE([tbl_OfferingAnalogue].[OfferingID] = [tbl_OfferingInRequestOffering].[OfferingID])) AS [IsRoot]\n" +
                    "FROM\n" +
                    "\t[dbo].[tbl_OfferingInRequestOffering] AS [tbl_OfferingInRequestOffering]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_Offering] AS [tbl_Offering] ON [tbl_Offering].[ID] = [tbl_OfferingInRequestOffering].[OfferingID]\n" +
                    "LEFT OUTER JOIN\n" +
                    "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering] ON [tbl_RequestOffering].[ID] = [tbl_OfferingInRequestOffering].[RequestID]\n" +
                    "WHERE([tbl_OfferingInRequestOffering].[RequestID] = ?)");
            try {
                System.out.println(offeringArchive.getID());
            } catch (NullPointerException e) {
                log.log(Level.SEVERE, "NULLLLL + " + e);
            }
            pst.setString(1, offeringArchive.getID());
            //  System.out.println("odinff" + offeringRequest.getID());

            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new NotesInProcessing(rs.getString(8)));
                textArea.setText(rs.getString("note"));


               /* StringBuilder builder = new StringBuilder();
                data.forEach(item -> {
                    builder.append(item.getNote());

                   // builder.append(item.getNote() + "\n");

                });

                textArea.setText(builder.toString());*/


            }

		/*	StringBuilder builder = new StringBuilder();
            builder.append("Дата\n"+ dataMessage.add(new ColumnMessage (rs.getString(5))) );
			txtAreaServerMsgs.setText(builder.toString());*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data.clear();
    }

    @FXML
    private void actionOK (ActionEvent event) {


        String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                "\tSET [Note] = ?,\n" +
                "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                "\t[ModifiedByID] = ?\n" +
                "WHERE([tbl_RequestOffering].[ID] = ?)";
      /*String query = "INSERT INTO [dbo].[tbl_RequestOffering] (Note, CreatedOn, CreatedByID) \n" +
              "VALUES (?, CURRENT_TIMESTAMP, ?, ?)" ;*/


        try {
            try {
                pst = con.prepareStatement(query);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            pst.setString(1, textArea.getText());
            System.out.println(textArea.getText() + "888888888888888888888888888888888888888888");
            pst.setString(2, User.getContactID());
            pst.setString(3, offeringRequest.getID());
            System.out.println(offeringRequest.getID() + "999999999999999999999999999999999");

            pst.execute();

            UsefulUtils.showInformationDialog("Нотатка успішно записана " + User.getContactName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) btn_Cancel.getScene().getWindow();
        stage.close();

    }

  public void  showWindow(){
      try{
        Stage primaryStage = new Stage();

      FXMLLoader loader = new FXMLLoader();


      FXMLLoader fxml = new FXMLLoader(getClass().getResource("/views/NotesInProcessingView.fxml"));

      Parent root = fxml.load();
      System.out.println("22222222222222 " + offeringRequest);



      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.show();
  } catch (Exception e) {
        e.printStackTrace();
    }

  }


@FXML
    private void actionCancel (ActionEvent event) {
    Stage stage = (Stage) btn_Cancel.getScene().getWindow();
    stage.close();
}



}
