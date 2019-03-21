package com.mainLogin.updateProgram;

import com.Utils.UsefulUtils;
import com.jfoenix.controls.JFXTabPane;
import com.mainLogin.updateProgram.Model.MissionsService;
import com.mainLogin.updateProgram.Model.Modes;
import com.mainLogin.updateProgram.Model.Release;
import com.mainLogin.updateProgram.Model.ReleaseXMLParser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
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
    @FXML
    private TextArea upcomingFeaturesTextArea;
   @FXML
    private TextArea whatsNewTextArea;
    private final MissionsService service = new MissionsService();




    private final Logger logger = Logger.getLogger(getClass().getName());
    private Stage window = new Stage();
    @FXML
    private void initialize() {
        closeWindow.setOnAction(a -> window.close());
        textConnectWhatsNew();
        textConnectUpcoming();



    }

    private void textConnectUpcoming (){
        upcomingFeaturesTextArea.clear();

        upcomingFeaturesTextArea.positionCaret(0);
        byte ptext[] = getInfo("textReadUpdater").getBytes();
        try {
            String value = new String(ptext, "UTF-8");
            upcomingFeaturesTextArea.setText(value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }



    }
    private void textConnectWhatsNew (){
        whatsNewTextArea.clear();

        whatsNewTextArea.positionCaret(0);
        whatsNewTextArea.appendText(getInfo("textReadWhatsNew"));

        byte ptext[] = getInfo("textReadWhatsNew").getBytes();
        try {
            String value = new String(ptext, "UTF-8");
            whatsNewTextArea.setText(value);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public String getInfo(String selectedItem) {
        PrintWriter stackTraceWriter = new PrintWriter(new StringWriter());
        String missionInfo = null ;

        try {
            missionInfo = service.getMissionInfo(selectedItem);
        } catch (IOException exception) {
            exception.printStackTrace (stackTraceWriter);

        }

        return missionInfo;
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
    @FXML
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
                        current =
                                    parser.parse("ftp://192.168.10.101/mainPage/latest.xml", Modes.URL);


                    } catch (SAXException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                        if (current.compareTo(release) > 0) {
                            try {
                        if(new UpdateHelper().checkForUpdate() == true) return;
                    } catch (Exception e) {
                        Platform.runLater(() -> UsefulUtils.showErrorDialogDown("Оновлення неможливе. Зверніться до адміністраторів!"));
                        return;
                    }
                    }else{
                        UsefulUtils.showConfirmDialogDown("Ви використовуєте найновішу версію програми");
                    }
            } catch (Exception e) {
                Platform.runLater(() -> UsefulUtils.showErrorDialogDown("Оновлення не відбулося!"));
                return;
            }
    }
}
