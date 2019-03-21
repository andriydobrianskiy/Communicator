package com.mainPage.OrderRegistryUkraine;

import com.mainPage.page.MainPageController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderRegistryUkraineController implements Initializable{
    @FXML
    private TableView tableView;
    @FXML
    private MainPageController main;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }
}
