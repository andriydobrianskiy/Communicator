package com.mainPage.InProcessing.Calculator;

import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.createRequest.CreateInterface;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CalculatorController implements Initializable {
    @FXML
    private JFXComboBox cmbVendor;
    @FXML
    private JFXTextField txtPurchasePrice;
    @FXML
    private JFXTextField txtMaximumDiscount;
    @FXML
    private JFXTextField txtCPEvroMaxSellingPrice1;
    @FXML
    private JFXTextField txtCPEvroMaxSellingPrice2;
    @FXML
    private JFXTextField txtCPUAHMaxSellingPrice1;
    @FXML
    private JFXTextField txtCPUAHMaxSellingPrice2;
    @FXML
    private JFXTextField txtCPEvroSpecialMargin01;
    @FXML
    private JFXTextField txtCPEvroSpecialMargin02;
    @FXML
    private JFXTextField txtCPUAHSpecialMargin01;
    @FXML
    private JFXTextField txtCPUAHSpecialMargin02;
    @FXML
    private JFXTextField txtCPEvroSpecialMargin11;
    @FXML
    private JFXTextField txtCPEvroSpecialMargin12;
    @FXML
    private JFXTextField txtCPUAHSpecialMargin11;
    @FXML
    private JFXTextField txtCPUAHSpecialMargin12;
    @FXML
    private JFXTextField txtCPEvroSpecialMargin21;
    @FXML
    private JFXTextField txtCPEvroSpecialMargin22;
    @FXML
    private JFXTextField txtCPUAHSpecialMargin21;
    @FXML
    private JFXTextField txtCPUAHSpecialMargin22;
    @FXML
    private JFXTextField txtCPEvroSpecialMarginVIP1;
    @FXML
    private JFXTextField txtCPEvroSpecialMarginVIP2;
    @FXML
    private JFXTextField txtCPUAHSpecialMarginVIP1;
    @FXML
    private JFXTextField txtCPUAHSpecialMarginVIP2;
    @FXML
    private JFXButton btnOk;
    @FXML
    private JFXButton btnCancel;
    @FXML
    private ArrayList<? extends Control> listComboBox;
    @FXML
    private ArrayList<? extends Control> listFields;
    public InProcessing chosenElement;
    Calculator calculator = new Calculator();
    public Double MinimumMargin = 0.0;
    public Double MaximumMargin = 0.0;
    public Double SpMarginZero = 0.0;
    public Double SpMarginOne = 0.0;
    public Double SpMarginTwo = 0.0;
    public Double SpMarginVIP = 0.0;

    String cmdID;


    public Double PriceMin;
    public Double PriceMax;

    public Double Price = 0.0;


    public Double MinRate = 0.0;
    public Double MaxRate = 0.0;
    public Double Coeficient = 0.0;

    private PreparedStatement pst = null;
    private Connection con = null;
    private ResultSet rs = null;
    private PreparedStatement pst1 = null;
    private ResultSet rs1 = null;
    private PreparedStatement pst2 = null;
    private ResultSet rs2 = null;
    private PreparedStatement pst3 = null;
    private ResultSet rs3 = null;
    private PreparedStatement pst4 = null;
    private ResultSet rs4 = null;
    private PreparedStatement pst5 = null;
    private ResultSet rs5 = null;
    private PreparedStatement pst6 = null;
    private ResultSet rs6 = null;
    private PreparedStatement pst7 = null;
    private ResultSet rs7 = null;
    private PreparedStatement pst8 = null;
    private ResultSet rs8 = null;
    private ObservableList data = FXCollections.observableArrayList();
    private ObservableList data1 = FXCollections.observableArrayList();
    private ObservableList data2 = FXCollections.observableArrayList();
    private ObservableList data3 = FXCollections.observableArrayList();
    private ObservableList data4 = FXCollections.observableArrayList();
    private ObservableList data5 = FXCollections.observableArrayList();
    private ObservableList data6 = FXCollections.observableArrayList();
    private ObservableList data7 = FXCollections.observableArrayList();
    private ObservableList data8 = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        data = FXCollections.observableArrayList();
        data1 = FXCollections.observableArrayList();
        data2 = FXCollections.observableArrayList();
        data3 = FXCollections.observableArrayList();
        data4 = FXCollections.observableArrayList();
        data5 = FXCollections.observableArrayList();
        data6 = FXCollections.observableArrayList();
        data7 = FXCollections.observableArrayList();
        data8 = FXCollections.observableArrayList();
        Vendor();
        btnOk.setDisable(true);
        txtPurchasePrice.setOnKeyReleased(e -> {
            if (UsefulUtils.checkEmptyRequiredFields(listComboBox)) {
                UsefulUtils.showErrorDialogDown("Обов'язкові поля не заповнені!\nЗараз вони позначені червоним");
                return;

            }
            Double price = Double.valueOf(txtPurchasePrice.getText());
            if (price >= 0.01)
                btnOk.setDisable(false);
            if (price < 0.01)
                btnOk.setDisable(true);
        });

        try {

            btnOk.setOnAction(action -> {

                initAccountObject();
               // FxUtilTest.autoCompleteComboBoxPlus(cmbVendor, (typedText, itemToCompare) -> itemToCompare.toString().toLowerCase().contains(typedText.toLowerCase()) || itemToCompare.toString().equals(typedText));
               // FxUtilTest.getComboBoxValue(cmbVendor);
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        txtPurchasePrice.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,7}([\\.]\\d{0,4})?")) {
                    txtPurchasePrice.setText(oldValue);
                }
            }
        });
        new ComboBoxAutoComplete<Integer>(cmbVendor);

    //    cmbVendor.setEditable(true);
     //   cmbVendor.getEditor().focusedProperty().addListener(observable -> {
      //      if (cmbVendor.getSelectionModel().getSelectedIndex() < 0) {
      //          cmbVendor.getEditor().setText(null);
      //      }
    //    });
    }

    public void setOfferingRequest(InProcessing inProcessing) {
        //   this.inProcessingController = oper;
        this.chosenElement = inProcessing;
    }

    public void Vendor() {

        try {
            String query = "SElect * FROM \n" +
                    "(\n" +
                    "\t\t\t\t\t\tSELECT DISTINCT V.Name , OT.ID, ROW_NUMBER() OVER (PARTITION BY V.Name order by V.Name) AS RN   from tbl_Vendor as V\n" +
                    "\t\t\t\t\t\tLEFT JOIN \n" +
                    "\t\t\t\t\t\ttbl_OType as OT On OT.OfferingVendorID = V.ID\n" +
                    "\t\t\t\t\t\t\t) AS K\n" +
                    "WHERE RN = 1\n" +
                    "ORDER BY\n" +
                    " 1 ASC" ;

            pst = con.prepareStatement(query);

            rs = pst.executeQuery();
            while (rs.next()) {
                data.add(new Calculator(rs.getString(2), rs.getString(1)));
            }
            cmbVendor.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //  cmdID = (String) data.get(1);


    }


    private void initAccountObject() throws NullPointerException {
        try {


            if (UsefulUtils.checkEmptyRequiredFields(listComboBox)) {
                UsefulUtils.showErrorDialogDown("Обов'язкові поля не заповнені!\nЗараз вони позначені червоним");
                return;

            }
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не можливо записати запит");
        }

        try {
            listFields.forEach(field -> {
                String value = null;
                String objectValue = null;

                try {

                    if (field instanceof TextField) {
                        value = ((TextField) field).getText().isEmpty() ? null : ((TextField) field).getText();
                    } else if (field instanceof TextArea) {
                        value = ((TextArea) field).getText().isEmpty() ? null : ((TextArea) field).getText();
                    } else if (field instanceof DatePicker) {
                        value = ((DatePicker) field).getValue() == null ? null : UsefulUtils.dateTimeConverter((DatePicker) field);
                    } else if (field instanceof ComboBox) {
                        value = ((ComboBox<? extends CreateInterface>) field).getValue() == null ? null : ((ComboBox<? extends CreateInterface>) field).getValue().getID();
                    }

                } catch (NullPointerException npe) { // ignore NullPointerException

                }

                switch (field.getId()) {

                    case "cmbVendor":
                        cmdID = value;
                        break;

                }
                System.out.println(cmdID);
            });
        } catch (Exception e) {

            UsefulUtils.showErrorDialogDown(
                    "Дані введені не правильно!");

            return;
        }


        Generate();
    }

    public void Generate() {
        try {
            String MinimumMargin = "SELECT OT.MarginMin FROM tbl_OType AS OT WHERE OT.ID = '" + cmdID + "'" ;
            pst1 = con.prepareStatement(MinimumMargin);
            rs1 = pst1.executeQuery();
            while (rs1.next()) {
                data1.add(rs1.getDouble(1));
            }
            System.out.println(data1.get(0));
            this.MinimumMargin = (Double) data1.get(0);
        } catch (SQLException e) {
            UsefulUtils.showErrorDialogDown("MarginMin дорівнює 0");
        }


        try {
            String MinimumMargin = "SELECT OT.MarginMax FROM tbl_OType AS OT WHERE OT.ID = '" + cmdID + "'" ;
            pst2 = con.prepareStatement(MinimumMargin);
            rs2 = pst2.executeQuery();
            while (rs2.next()) {
                data2.add(rs2.getDouble(1));
            }
            MaximumMargin = (Double) data2.get(0);
        } catch (SQLException e) {
            UsefulUtils.showErrorDialogDown("MarginMin дорівнює 0");
        }



        try {
            String MinimumMargin = "SELECT OT.SpecialMarginZero FROM tbl_OType AS OT WHERE OT.ID = '" + cmdID + "'" ;
            pst3 = con.prepareStatement(MinimumMargin);
            rs3 = pst3.executeQuery();
            while (rs3.next()) {
                data3.add(rs3.getDouble(1));
            }
            SpMarginZero = (Double) data3.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            String MinimumMargin = "SELECT OT.SpecialMarginOne FROM tbl_OType AS OT WHERE OT.ID = '" + cmdID + "'" ;
            pst4 = con.prepareStatement(MinimumMargin);
            rs4 = pst4.executeQuery();
            while (rs4.next()) {
                data4.add(rs4.getDouble(1));
            }
            SpMarginOne = (Double) data4.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            String MinimumMargin = "SELECT OT.SpecialMarginTwo FROM tbl_OType AS OT WHERE OT.ID = '" + cmdID + "'" ;
            pst5 = con.prepareStatement(MinimumMargin);
            rs5 = pst5.executeQuery();
            while (rs5.next()) {
                data5.add(rs5.getDouble(1));
            }
            SpMarginTwo = (Double) data5.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        try {
            String MinimumMargin = "SELECT OT.SpecialMarginVIP FROM tbl_OType AS OT WHERE OT.ID = '" + cmdID + "'" ;
            pst6 = con.prepareStatement(MinimumMargin);
            rs6 = pst6.executeQuery();
            while (rs6.next()) {
                data6.add(rs6.getDouble(1));
            }
            SpMarginVIP = (Double) data6.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        try {
            String MinimumMargin = "SELECT TOP 1 TodayRateMin FROM tbl_TodayRate ORDER BY CreatedOn DESC" ;
            pst7 = con.prepareStatement(MinimumMargin);
            rs7 = pst7.executeQuery();
            while (rs7.next()) {
                data7.add(rs7.getDouble(1));
            }
            MinRate = (Double) data7.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        try {
            String MinimumMargin = "SELECT TOP 1 TodayRate FROM tbl_TodayRate ORDER BY CreatedOn DESC" ;
            pst8 = con.prepareStatement(MinimumMargin);
            rs8 = pst8.executeQuery();
            while (rs8.next()) {
                data8.add(rs8.getDouble(1));
            }
            MaxRate = (Double) data8.get(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }




        try {
            Price = Double.valueOf(txtPurchasePrice.getText());


            Double MinPriceEURO = (Price * MinimumMargin * 1.2);
            //var MinPriceUAH  = RoundFloatValue(  (((MinPriceEURO * MinimumMargin * 1.2)/1.1) * MaxRate) , 2 );
            Double MaxPriceEURO = (Price * MaximumMargin * 1.2);
            //var MaxPriceUAH  = RoundFloatValue(  (((Price * MaximumMargin * 1.2)/1.1) * MaxRate) , 2 );
            Double MaxDiscount = ((MaxPriceEURO - MinPriceEURO) / MaxPriceEURO * 100);

            txtCPEvroMaxSellingPrice2.setText(String.valueOf(String.format("%6.2f", (MaxPriceEURO))));
            txtCPUAHMaxSellingPrice2.setText(String.valueOf(String.format("%6.2f", (Price * MaximumMargin * 1.2) * MinRate)));
            //Self('edtPricePDVGRN').Value 	= RoundFloatValue(MaxPriceUAH, 2);
            txtCPEvroMaxSellingPrice1.setText(String.valueOf(String.format("%6.2f", ((MaxPriceEURO) / 1.1))));
            txtCPUAHMaxSellingPrice1.setText(String.valueOf(String.format("%6.2f", ((Price * MaximumMargin * 1.2) / 1.1) * MaxRate)));
            //Self('edtPriceGRN').Value 	= RoundFloatValue(RoundFloatValue(MaxPriceUAH, 2), 2);
            txtMaximumDiscount.setText(String.valueOf(String.format("%6.0f", (MaxDiscount))));
            //  Double.parseDouble(txtMaximumDiscount.setText(String.valueOf(MaxDiscount)));
            //Spec 0
            txtCPEvroSpecialMargin02.setText(String.valueOf(String.format("%6.2f", (Price * SpMarginZero * 1.2))));
            txtCPEvroSpecialMargin01.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginZero * 1.2) / 1.1))));

            txtCPUAHSpecialMargin02.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginZero * 1.2) * MinRate))));
            txtCPUAHSpecialMargin01.setText(String.valueOf(String.format("%6.2f", (((Price * SpMarginZero * 1.2) / 1.1) * MaxRate))));

            //Spec 1
            txtCPEvroSpecialMargin12.setText(String.valueOf(String.format("%6.2f", (Price * SpMarginOne * 1.2))));
            txtCPEvroSpecialMargin11.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginOne * 1.2) / 1.1))));

            txtCPUAHSpecialMargin12.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginOne * 1.2) * MinRate))));
            txtCPUAHSpecialMargin11.setText(String.valueOf(String.format("%6.2f", (((Price * SpMarginOne * 1.2) / 1.1) * MaxRate))));

            //Spec 2
            txtCPEvroSpecialMargin22.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginTwo * 1.2)))));
            txtCPEvroSpecialMargin21.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginTwo * 1.2) / 1.1))));

            txtCPUAHSpecialMargin22.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginTwo * 1.2) * MinRate))));
            txtCPUAHSpecialMargin21.setText(String.valueOf(String.format("%6.2f", (((Price * SpMarginTwo * 1.2) / 1.1) * MaxRate))));

            //Spec VIP
            txtCPEvroSpecialMarginVIP2.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginVIP * 1.2)))));
            txtCPEvroSpecialMarginVIP1.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginVIP * 1.2) / 1.1))));

            txtCPUAHSpecialMarginVIP2.setText(String.valueOf(String.format("%6.2f", ((Price * SpMarginVIP * 1.2) * MinRate))));
            txtCPUAHSpecialMarginVIP1.setText(String.valueOf(String.format("%6.2f", (((Price * SpMarginVIP * 1.2) / 1.1) * MaxRate))));
            data1.clear();
            data2.clear();
            data3.clear();
            data4.clear();
            data5.clear();
            data6.clear();
            data7.clear();
            data8.clear();

        } catch (Exception e)

        {
            e.printStackTrace();

        }

    }


}
