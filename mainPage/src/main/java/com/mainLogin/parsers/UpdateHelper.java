package com.mainLogin.parsers;

import com.Utils.UsefulUtils;
import com.mainLogin.Updater;
import com.mainLogin.objects.Modes;
import com.Utils.AsyncTask;
import com.mainLogin.objects.Release;
import com.mainLogin.Downloader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UpdateHelper {
    private static Logger log = Logger.getLogger(UpdateHelper.class.getName());

    public static String app_version = ("2.3");
    private static int answer = 0;
    public static String  people = //"Manager";
            "VZ";
    private Release release;
    private Release current;


    private Stage dialogStage;

    public boolean checkForUpdate() {

        release = new Release();
        release.setpkgver(app_version);

        release.setPkgrel("4");

        ReleaseXMLParser parser = new ReleaseXMLParser();
        try {
            Release current = null;
            if (people == "Manager") {
                current =
                        parser.parse("ftp://192.168.10.101/mainPage/latest.xml", Modes.URL);
            } else if (people == "VZ") {
                current =
                        parser.parse("ftp://192.168.10.101/mainPageVZ/latest.xml", Modes.URL);
            }


            if (current.compareTo(release) > 0) {
                new UpdateTask().execute();
                return true;
            } else {
                answer = -1;
                return false;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

        return false;
    }


    private static void handle(WindowEvent exit) {
        exit.consume();
    }

    public static Properties internalInformation = new Properties();

    static {
        internalInformation.put("Version", 68);
    }

    private void showProgressIndicator() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.UTILITY);
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Label label = new Label("Триває оновлення...");
        ProgressBar progressBar = new ProgressBar();
        label.setStyle("-fx-font-size: 14pt; -fx-font-weight: bold; -fx-font-family: \'Century Gothic\';");

        HBox hBox = new HBox(label);
        hBox.setAlignment(Pos.CENTER);

        final BorderPane borderPane = new BorderPane();
        borderPane.setMinHeight(250);
        borderPane.setMinWidth(250);
        borderPane.setTop(hBox);
        borderPane.setCenter(progressBar);

        Scene scene = new Scene(borderPane);
        dialogStage.setScene(scene);
        dialogStage.show();
    }

    private void closeProgressIndicator() {
        dialogStage.close();
    }

    public class UpdateTask extends AsyncTask<Void, Void, Void> {

        public UpdateTask() {

        }

        @Override
        public void onPreExecute() {
            showProgressIndicator();
        }

        @Override
        public Void doInBackground(Void... params) {
            try {
                switch (answer) {
                    case 0:
                        Downloader dl = new Downloader();
                        if (people == "Manager") {

                            dl.download("ftp://192.168.10.101/mainPage/files.xml", "tmp", Modes.URL);
                        } else if (people == "VZ") {
                            dl.download("ftp://192.168.10.101/mainPageVZ/files.xml", "tmp", Modes.URL);
                        }
                        break;
                    case 1:
                        break;
                }


            } catch (FileNotFoundException ex) {

                log.log(Level.SEVERE, null, ex);
                answer = -1;
            } catch (IOException ex) {

                log.log(Level.SEVERE, null, ex);
                answer = -1;

            } catch (InterruptedException ex) {

                //UsefulUtils.showTrayErrorDialog( "The connection has been lost!\n"
                //        + "Please check your internet connectivity!");

                log.log(Level.SEVERE, null, ex);
                answer = -1;
            } catch (org.xml.sax.SAXException e) {
                answer = -1;
                e.printStackTrace();
            }

            if(answer == -1) return null;


            if (answer == 0) {
                try {
                    Updater update = new Updater();
                    update.update("update.xml", "tmp", Modes.FILE);
                } catch (InterruptedException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (FileNotFoundException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    log.log(Level.SEVERE, null, ex);
                } catch (org.xml.sax.SAXException e) {
                    log.log(Level.SEVERE, null, e);
                }
            }

            File tmp = new File("tmp");
            if (tmp.exists()) {
                for (File file : tmp.listFiles()) {
                    file.delete();
                }
                tmp.delete();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            if(answer != -1) {
                UsefulUtils.showConfirmDialogDown("Оновлення завершене! Перезапустіть програму. ");

                closeProgressIndicator();


            }

        }

        @Override
        public void progressCallback(Void... params) {

        }
    }

}
