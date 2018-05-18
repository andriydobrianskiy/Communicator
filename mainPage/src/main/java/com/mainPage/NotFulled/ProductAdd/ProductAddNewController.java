package com.mainPage.NotFulled.ProductAdd;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.stage.Stage;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.login.User;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.NotFulled.OfferingRequest.ExampleController;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProductAddNewController implements Initializable {

@FXML
private JFXTextField txtCodeProduct;
@FXML
private JFXTextField txtQuantity;
@FXML
private JFXTextArea txtName;
@FXML
private JFXButton btnOk;
@FXML
private Button btnCancel;

private PreparedStatement pst = null;
private Connection con = null;
private ResultSet rs;

public static NotFulfilled notFulfilled ;
@FXML
    ExampleController exampleController;

    private ProductAddController main;
    public void init(ProductAddController productAddController){main = productAddController;}
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();

        }


        //System.out.println(notFulfilled.getID()+"7887964356415604649898078468489+0079687469087608768746874089+08740987498");
    }
    @FXML
    private ArrayList<? extends Control> listFields;
    @FXML public void actionButtonOk (ActionEvent event) throws SQLException {
        exampleController = main.getExampleController();
        notFulfilled = main.getNotFulled();
     //   if (exampleController.data == null) {

        // System.out.println(a +  b+c + "ed2wewgweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        if (UsefulUtils.checkEmptyRequiredFields(listFields)) {
            UsefulUtils.showErrorDialogDown("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
            return;
        }
        if(UsefulUtils.checkEmptyRequiredFields(listFields)) {
            UsefulUtils.showErrorDialogDown("\n\tОбов'язкові поля не заповнені! Зараз вони позначені червоним");
            return;
        }

        String query = "INSERT INTO [dbo].[tbl_OfferingInRequestOffering] ( [ModifiedOn], [ModifiedByID], [RequestID], [Quantity], [NewOfferingCode], [NewDescription], [CreatedOn], [CreatedByID])\n" +
                "VALUES ( CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?)";

        Double a = Double.parseDouble(txtQuantity.getText());
        String b = txtCodeProduct.getText();
        String c = txtName.getText();

        try {
            System.out.println(notFulfilled.getID() + "jqoijoqiwjojofoiwjoqfjoejewoieojfejoirewjfowiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");
            pst = con.prepareStatement(query);
            pst.setString(1, User.getContactID());
            pst.setString(2, notFulfilled.getID());
            pst.setDouble(3, a);
            pst.setString(4, b);
            pst.setString(5, c);
            pst.setString(6, User.getContactID());
            if (exampleController.data.size() == 0) {
                pst.execute();


            UsefulUtils.showSuccessful("Продукт успішно добавлено");
            pst.close();
            txtQuantity.clear();
            txtCodeProduct.clear();
            txtName.clear();

            exampleController.refresh();

        }else {
                UsefulUtils.showErrorDialogDown("Не можливо записати більше одного продукту");
            }
            pst.close();
            rs.close();
            con.close();
        } catch(SQLException e){
            e.printStackTrace();
        }
   /* }else if (exampleController.data != null ) {
        UsefulUtils.showErrorDialog("Неможливо добавити більше одного продукту");
    }
*/
    }

    @FXML
    public void actionButtonCancel(ActionEvent evt){
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }




}
