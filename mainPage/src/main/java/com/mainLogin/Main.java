package com.mainLogin;

import com.Utils.UsefulUtils;
import com.jfoenix.controls.JFXTabPane;
import com.login.CustomLauncherUI;
import com.login.User;
import com.mainLogin.objects.Modes;
import com.mainLogin.objects.Release;
import com.mainLogin.parsers.ReleaseXMLParser;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import org.apache.commons.net.ftp.FTPClient;
import tray.notification.NotificationType;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {
    private static Stage stageObj;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
  public Main () {
      int answer = 0;
       String people = //"Manager";
       "VZ";
      Release release = new Release();
      release.setpkgver("2.3");

      release.setPkgrel("4");
      FTPClient ftpClient = new FTPClient();


          ReleaseXMLParser parser = new ReleaseXMLParser();
          try {
              Release current = null;
              if(people == "Manager") {
                   current =
                          parser.parse("ftp://192.168.10.101/mainPage/latest.xml", Modes.URL);
              }else if(people == "VZ"){
                  current =
                          parser.parse("ftp://192.168.10.101/mainPageVZ/latest.xml", Modes.URL);
              }
              if (current.compareTo(release) > 0) {
                     // answer =
                      //    JOptionPane.showConfirmDialog(rootPane, "A new version of this"
                      //               + " program is available\nWould you like to install it?",
                      //        "Update", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
                  switch (answer) {
                      case 0:
                          /**
                           * Download needed files
                           */
                          if(people == "Manager") {
                              Downloader dl = new Downloader();
                              dl.download("ftp://192.168.10.101/mainPage/files.xml", "tmp", Modes.URL);
                          }else if(people == "VZ"){
                              Downloader dl = new Downloader();
                              dl.download("ftp://192.168.10.101/mainPageVZ/files.xml", "tmp", Modes.URL);
                          }
                          break;
                      case 1:
                          break;
                  }
              }
          } catch (FileNotFoundException ex) {

              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              answer = -1;
          } catch (IOException ex) {
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              answer = -1;
          } catch (InterruptedException ex) {
              UsefulUtils.showErrorDialogDown( "The connection has been lost!\n"
                        + "Please check your internet connectivity!");
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              answer = -1;
          } catch (org.xml.sax.SAXException e) {
              e.printStackTrace();
          }

/**
 * Start the updating procedure
 */
          if (answer == 0) {
              try {
                  com.mainLogin.Updater update = new com.mainLogin.Updater();
                  update.update("update.xml", "tmp", Modes.FILE);
              } catch (InterruptedException ex) {
                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              } catch (FileNotFoundException ex) {
                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              } catch (org.xml.sax.SAXException e) {
                  Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
              }
          }

          /**
           * Delete tmp directory
           */
          File tmp = new File("tmp");
          if (tmp.exists()) {
              for (File file : tmp.listFiles()) {
                  file.delete();
              }
              tmp.delete();
          }
      }

    /** Application logger. */
    public static final Logger logger = Logger.getGlobal();



    public static UpdateWindow updateWindow = new UpdateWindow();

    public static final UpdateScreen updateScreen = new UpdateScreen();

    public static final StackPane applicationStackPane = new StackPane();

    public static final BorderPane root = new BorderPane();
    /** The can save data. */
    public static boolean canSaveData = true;
    public static Stage window;

    public static JFXTabPane specialJFXTabPane = new JFXTabPane();
    private String Virsion = ("2.3");

    private static void handle(WindowEvent exit) {
        exit.consume();
    }

    // --------------END: The below have depencities on others------------------------

    public static Properties internalInformation = new Properties();
    static {
        //----------Properties-------------
        internalInformation.put("Version", 68);


    }

    @Override
    public void start(Stage stage) throws Exception {

        Platform.setImplicitExit(false);

        // --------Window---------

        stage.setTitle("Комунікатор "+ Virsion + " " + User.getContactName());

        CustomLauncherUI customLauncherUI = new CustomLauncherUI();

        stageObj = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        stage.setScene(new Scene(root));
        Image icon = new Image("/images/LoginPage.png");
        stage.getIcons().add(icon);
        stage.show();

    }

    private void setVisible(boolean b) {
        b = true;
    }

    public static void main(String[] args) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Main().setVisible(true);
            }
        });
        launch(args);
    }

    private void checkJavaCombatibility() {


        String[] javaVersionElements = System.getProperty("java.runtime.version").split("\\.|_|-b");


        String major = javaVersionElements[1];
        String update = javaVersionElements[3];

        if (Integer.parseInt(major) < 8 || Integer.parseInt(update) < 141)
            ActionTool.showNotification("Problematic Java Version Installed!", "XR3Player needs at least Java [ 1.8.0_141 ] -> Installed Java Version [ "
                            + System.getProperty("java.version") + " ]\nThe application may crash or not work at all!\n Update your Java Version :)", Duration.seconds(40),
                    NotificationType.ERROR);
    }
    private void startPart2() {


        updateWindow.getWindow().initOwner(window);


        // -------Root-----------
        root.setVisible(false);
        specialJFXTabPane.setTabMaxWidth(0);
        specialJFXTabPane.setTabMaxHeight(0);

        //Add listeners to each tab
        final AtomicInteger counter = new AtomicInteger(-1);
        root.setCenter(specialJFXTabPane);


    }

}
