package com.mainPage.OrderRegistryPoland;

import com.mainPage.page.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderRegistryPolandController implements Initializable {
    @FXML
    private TableView tableView;
    private MainPageController mainPageController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void init(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }
}
