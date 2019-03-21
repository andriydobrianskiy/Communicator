package com.mainPage.NotFulled.CreateRequest;

import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.NotFulled.ProductAdd.ProductAddNewController;
import com.mainPage.createRequest.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateMainController implements Initializable{
    @FXML
    NotFulledController notFulledController;
    public void setOfferingRequest(NotFulledController oper) {
        this.notFulledController = oper;
    }

    public NotFulledController getOfferingRequest() {
        return this.notFulledController;
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
    @FXML
    private void actionCreateRequest (ActionEvent event) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProductAddNewController.class.getResource("/views/CreateView.fxml"));
        CreateRequest createRequest = loader.getController();
        Pane serverrLayout = loader.load();
        stage.setResizable(false);
        Scene scene = new Scene(serverrLayout);
        CreateRequest con = loader.getController();
        con.setOfferingRequest(notFulledController);
        con.createPricingDescription.setVisible(false);
        con.labelDescription.setVisible(false);
        con.DeliveryCity.setVisible(true);
        con.labelDelivery.setVisible(true);
        con.labelGroupPeople.setVisible(false);
        con.GroupPeople.setVisible(false);
        con.pricingBoolean = 0;
        con.idSetTitle.setText("Створення запиту (Закуп продукту)");
        stage.setScene(scene);
        stage.setMaxHeight(480);
        stage.setMaxWidth(620);
        stage.show();

    }
    @FXML
    private void actionCreatePricing (ActionEvent event) throws IOException {

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProductAddNewController.class.getResource("/views/CreateView.fxml"));
        CreateRequest createRequest = loader.getController();

        Pane serverrLayout = loader.load();
        stage.setResizable(false);
        Scene scene = new Scene(serverrLayout);
        CreateRequest con = loader.getController();
        con.setOfferingRequest(notFulledController);
        con.DeliveryCity.setVisible(false);
        con.labelDelivery.setVisible(false);
        con.labelDescription.setVisible(true);
        con.createPricingDescription.setVisible(true);
        con.labelGroupPeople.setVisible(true);
        con.GroupPeople.setVisible(true);
        con.pricingBoolean = 1;
        con.idSetTitle.setText("Створення запиту (Звернення до ціноутворення)");
        stage.setScene(scene);
        stage.setMaxHeight(480);
        stage.setMaxWidth(620);
        stage.show();


    }
}
