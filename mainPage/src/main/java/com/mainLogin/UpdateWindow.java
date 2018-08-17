package com.mainLogin;

import com.Utils.UsefulUtils;
import com.jfoenix.controls.JFXTabPane;
import com.mainLogin.objects.Modes;
import com.mainLogin.objects.Release;
import com.mainLogin.parsers.ReleaseXMLParser;
import com.mainLogin.parsers.UpdateHelper;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.logging.Logger;

public class UpdateWindow extends StackPane {


    @FXML
    private Label topLabel;


    @FXML
    private JFXTabPane tabPane;

    @FXML
    private Button automaticUpdate;

    @FXML
    private Button closeWindow;

    @FXML
    private Tab releasesHistoryTab;

    @FXML
    private Accordion gitHubAccordion;

    @FXML
    private Tab whatsNewTab;

    @FXML
    private BorderPane whatsNewContainer;

    @FXML
    private Tab upcomingFeaturesTab;




    private final Logger logger = Logger.getLogger(getClass().getName());
    private Stage window = new Stage();
    @FXML
    private void initialize() {


        closeWindow.setOnAction(a -> window.close());


    }




    public void show() {
        Platform.runLater(() -> {
            if (!window.isShowing())
                window.show();
            else
                window.requestFocus();
        });
    }

    /**
     * Close the Window
     */
    public void close() {
        window.close();
    }

    /**
     * @return the window
     */
    public Stage getWindow() {
        return window;
    }


    public void actionAutomaticUpdate(ActionEvent event) {


                ReleaseXMLParser parser = new ReleaseXMLParser();
                Release release = new Release();
                release.setpkgver(UpdateHelper.app_version);

                release.setPkgrel("4");
                try {
                    Release current = null;
                    try {
                    if (UpdateHelper.people == "Manager") {

                            current =
                                    parser.parse("ftp://192.168.10.101/mainPage/latest.xml", Modes.URL);

                    } else if (UpdateHelper.people == "VZ") {
                        current =
                                parser.parse("ftp://192.168.10.101/mainPageVZ/latest.xml", Modes.URL);
                    }
                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (current.compareTo(release) > 0) {
                        if (new UpdateHelper().checkForUpdate() == true) return;
                    }else{
                    UsefulUtils.showConfirmDialogDown("Ви використовуєте найновішу версію програми");
                    }

            } catch (Exception e) {
                Platform.runLater(() -> UsefulUtils.showErrorDialogDown("Оновлення не відбулося!"));
                return;
            }
    }
}
