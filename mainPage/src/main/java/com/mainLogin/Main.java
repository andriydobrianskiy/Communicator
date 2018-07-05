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
//import main.java.com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
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
  //  public static PropertiesDb applicationProperties = new PropertiesDb(InfoTool.getAbsoluteDatabasePathWithSeparator() + "ApplicationProperties.properties", true);
  public Main () {
      int answer = 0;
      Release release = new Release();
      release.setpkgver("2.1");

      release.setPkgrel("4");
      FTPClient ftpClient = new FTPClient();


          //try {
            //  ftpClient.connect("ftp://82.207.107.237");
              //ftpClient.login("terrasoft_foto", "QRjdgCjotP8s");
              //ftpClient.enterLocalPassiveMode();
              //ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
          //} catch (IOException e) {
            //  e.printStackTrace();
          //}


          ReleaseXMLParser parser = new ReleaseXMLParser();
          try {
              Release current =
                      parser.parse("ftp://192.168.10.101/mainPage/latest.xml", Modes.URL);
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
                          Downloader dl = new Downloader();
                          dl.download("ftp://192.168.10.101/mainPage/files.xml", "tmp", Modes.URL);
                          break;
                      case 1:
                          break;
                  }
              }
          } catch (FileNotFoundException ex) {

              //  JOptionPane.showMessageDialog(rootPane,
          //    UsefulUtils.showErrorDialogDown("Files were unable to be read or created successfully!\n"
               //        + "Please be sure that you have the right permissions and"
               //      + " internet connectivity!");
              Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
              answer = -1;
          } catch (IOException ex) {
              // JOptionPane.showMessageDialog(rootPane, "IOEXception!",
           //   UsefulUtils.showErrorDialogDown(
           //          "Something went wrong!");
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
                  //  JOptionPane.showMessageDialog(rootPane,
                  //    "The update was completed successfuly.\n"
                  //             + "Please restart the application in order the changes take effect.");
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

    //----------------START: The below have not depencities on other ---------------------------------//

   // public static final WelcomeScreen welcomeScreen = new WelcomeScreen();

   // public static final MediaDeleteWindow mediaDeleteWindow = new MediaDeleteWindow();

    /** The star window. */
  //  public static final StarWindow starWindow = new StarWindow();

    /** The rename window. */
  //  public static final RenameWindow renameWindow = new RenameWindow();

    /** The rename window. */
   // public static final EmotionsWindow emotionsWindow = new EmotionsWindow();

    /**
     * Audio Tagging Window
     */
  //  public static final TagWindow tagWindow = new TagWindow();

    /**
     * This window is being used to export files from the application to the outside world
     */
  //  public static final ExportWindowController exportWindow = new ExportWindowController();

    /** The About Window of the Application */
  //  public static final ApplicationInformationWindow aboutWindow = new ApplicationInformationWindow();

    /** The console Window of the Application */
    //public static final ConsoleWindowController consoleWindow = new ConsoleWindowController();

    /**
     * This Window contains the settings for the whole application
     */
   // public static ApplicationSettingsController settingsWindow = new ApplicationSettingsController();

    /**
     * This class is used to capture the computer Screen or a part of it [ Check XR3Capture package]
     */
   // public static CaptureWindow captureWindow = new CaptureWindow();

    public static UpdateWindow updateWindow = new UpdateWindow();

    //

    /** The Top Bar of the Application */
  //  public static final TopBar topBar = new TopBar();

    /** The Bottom Bar of the Application */
 //   public static final BottomBar bottomBar = new BottomBar();

    /** The Side Bar of The Application */
  //  public static final SideBar sideBar = new SideBar();

    /** Application Update Screen */
    public static final UpdateScreen updateScreen = new UpdateScreen();

    /** The TreeView of DJMode */
   // public static final TreeViewManager treeManager = new TreeViewManager();

    /** The Constant advancedSearch. */
    //public static final AdvancedSearch advancedSearch = new AdvancedSearch()

   // public static final MediaInformation mediaInformation = new MediaInformation();
    //

  //  public static final TreeViewContextMenu treeViewContextMenu = new TreeViewContextMenu();е

    /** The Constant songsContextMenu. */
   // public static final MediaContextMenu songsContextMenu = new MediaContextMenu();

    /** The Constant specialChooser. */
  //  public static final FileAndFolderChooser specialChooser = new FileAndFolderChooser();

    //

    /** XPlayList holds the instances of XPlayerControllers */
  //  public static final XPlayersList xPlayersList = new XPlayersList();

    /** The Constant playedSongs. */
  //  public static final PlayedMediaList playedSongs = new PlayedMediaList();

    /** The Constant EmotionListsController. */
  ///  public static final EmotionListsController emotionListsController = new EmotionListsController();

    //

    /**
     * The WebBrowser of the Application
     */
  //  public static WebBrowserController webBrowser = new WebBrowserController();;

    //----------------END: The above have not depencities on other ---------------------------------//

    //----------------START: Vary basic for the application---------------------------------------//

    /** The window. */
   // public static Stage window;

    /** The scene. */
   // public static Scene scene;

    /** The stack pane root. */
    public static final StackPane applicationStackPane = new StackPane();

    /** The root. */
    public static final BorderPane root = new BorderPane();

    /** The can save data. */
    public static boolean canSaveData = true;
    public static Stage window;
  //  public static BorderlessScene scene;
    //---------------END:Vary basic for the application---------------------------------//

    // --------------START: The below have depencities on others------------------------

    /** The Constant dbManager. */
  //  public static DbManager dbManager = new DbManager();

    /** The Constant libraryMode. */
 //   public static LibraryMode libraryMode = new LibraryMode();

    /** The Constant djMode. */
   // public static DJMode djMode = new DJMode();

   // public static DropboxViewer dropBoxViewer = new DropboxViewer();

  //  public static EmotionsTabPane emotionsTabPane = new EmotionsTabPane(emotionListsController);

    /** The Search Window Smart Controller of the application */
  //  public static SmartController searchWindowSmartController = new SmartController(Genre.SEARCHWINDOW, "Searching any Media", null);

    //  public static PlayListModesTabPane playListModesTabPane = new PlayListModesTabPane();

    /** The Constant multipleTabs. */
   // public static PlayListModesSplitPane playListModesSplitPane = new PlayListModesSplitPane();

    /**
     * The Login Mode where the user of the applications has to choose an account to login
     */
 //   public static LoginMode loginMode = new LoginMode();

    /**
     * Entering in this mode you can change the user settings and other things that have to do with the user....
     */
  //  public static UserMode userMode = new UserMode();

    /**
     * This JavaFX TabPane represents a TabPane for Navigation between application Modes
     */
    public static JFXTabPane specialJFXTabPane = new JFXTabPane();
    private String Virsion = ("2.1");

    private static void handle(WindowEvent exit) {
        exit.consume();
    }

    // --------------END: The below have depencities on others------------------------

    public static Properties internalInformation = new Properties();
    static {
        //----------Properties-------------
        internalInformation.put("Version", 68);

       // internalInformation.put("ReleasedDate", "?/12/2018");

    }

    @Override
    public void start(Stage stage) throws Exception {

        Platform.setImplicitExit(false);

        // --------Window---------

        stage.setTitle("Комунікатор "+ Virsion + " " + User.getContactName());

      //  double width = UsefulUtils.getVisualScreenWidth();
     //   double height = UsefulUtils.getVisualScreenHeight();
        //width = 1280;
        //height = 600;
       // window.setWidth(width * 0.95);
       // window.setHeight(height * 0.95);
        //window.centerOnScreen();
      //  window.getIcons().add(UsefulUtils.getImageFromResourcesFolder("icon.png"));
      //  window.centerOnScreen();
     //   window.setOnCloseRequest(Main::handle);

        // Create BorderlessScene
     //   final int screenMinWidth = 800 , screenMinHeight = 600;
      //  scene = new BorderlessScene(window, StageStyle.UNDECORATED, applicationStackPane, screenMinWidth, screenMinHeight);
      //  scene.setMoveControl(loginMode.getXr3PlayerLabel());
       // scene.getStylesheets().add(getClass().getResource("/styles/application.css").toExternalForm());
      //  window.setScene(scene);
      //  window.show();
       // window.close();

        //Continue
    //    startPart2();

        //Show the Window
       // window.show();

        //---Login Mode---- It must be set after the window has been shown
       // loginMode.getSplitPane().setDividerPositions(0.65, 0.35);

        //Load the informations about every user
        //loginMode.usersInfoLoader.start();

        //Check Compatibility

       // checkJavaCombatibility();
      //  updateWindow.startXR3PlayerUpdater((Integer) internalInformation.get("Version"));
      //  ActionTool.deleteFile(new File( "/updater/XR3PlayerUpdater.jar"));
        //Check for updates
    //    updateWindow.searchForUpdates(false);


        //Delete AutoUpdate if it exists


        //============= ApplicationProperties GLOBAL
        //Properties properties = applicationProperties.loadProperties();

        //WelcomeScreen
       // if (properties.getProperty("Show-Welcome-Screen") == null)
       //     welcomeScreen.showWelcomeScreen();
      //  else
       //     Optional.ofNullable(properties.getProperty("Show-Welcome-Screen")).ifPresent(value -> {
        //        welcomeScreen.getShowOnStartUp().setSelected(Boolean.valueOf(value));
        //        if (welcomeScreen.getShowOnStartUp().isSelected())
            //        welcomeScreen.showWelcomeScreen();
         //       else
        //            welcomeScreen.hideWelcomeScreen();
       //     });

        //Users Color Picker
       // Optional.ofNullable(properties.getProperty("Users-Background-Color")).ifPresent(color -> loginMode.getColorPicker().setValue(Color.web(color)));

    ///    applicationProperties.setUpdatePropertiesLocked(false);


      //  System.out.println("XR3Player ready to rock!");
        CustomLauncherUI customLauncherUI = new CustomLauncherUI();

        stageObj = stage;
        //FXMLLoader
        //   loader = new FXMLLoader(getClass().getResource("resources/views/TestFile.fxml"));
        // loader.setController(new MainController(path));
        //  Pane mainPane = loader.load();
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        stage.setScene(new Scene(root));
        Image icon = new Image("/images/LoginPage.png");

        // System.out.println(+"6564898989");
        stage.getIcons().add(icon);
        //  stage.getIcons().add( new Image( String.valueOf( getClass().getClassLoader().getResource( "/images/plug.png" ) ) ) );
        stage.show();
      //  final Application.Parameters params = getParameters();
      //  List<String> parameters = params.getRaw();

      //  if ((parameters.size() & 0x01) == 0x01) {
         //   parameters = parameters.subList(0, parameters.size() - 1);
    //    }

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
     //   Updater getMyFiles = new Updater();
      //  System.out.println(args.length);
      //  if (args.length < 1)
     //   {
      //      System.err.println("Usage: java " + getMyFiles.getClass().getName()+
     //               " Properties_file");
     //       System.exit(1);
      //  }

     //   String propertiesFile = args[0].trim();
    //    getMyFiles.startFTP(propertiesFile);
    }

    private void checkJavaCombatibility() {

        //String minimumJavaVersion = "1.9.0_121"
        String[] javaVersionElements = System.getProperty("java.runtime.version").split("\\.|_|-b");

        //String discard = javaVersionElements[0]
        String major = javaVersionElements[1];
        //String minor = javaVersionElements[2]
        String update = javaVersionElements[3];
        //String build = javaVersionElements[4]
        //System.out.println(Arrays.asList(javaVersionElements))

        if (Integer.parseInt(major) < 8 || Integer.parseInt(update) < 141)
            ActionTool.showNotification("Problematic Java Version Installed!", "XR3Player needs at least Java [ 1.8.0_141 ] -> Installed Java Version [ "
                            + System.getProperty("java.version") + " ]\nThe application may crash or not work at all!\n Update your Java Version :)", Duration.seconds(40),
                    NotificationType.ERROR);
    }
    private void startPart2() {

        // ---- InitOwners -------
        //starWindow.getWindow().initOwner(window);
       // renameWindow.getWindow().initOwner(window);
      //  emotionsWindow.getWindow().initOwner(window);
      //  exportWindow.getWindow().initOwner(window);
      //  consoleWindow.getWindow().initOwner(window);
      //  settingsWindow.getWindow().initOwner(window);
     //   aboutWindow.getWindow().initOwner(window);
        updateWindow.getWindow().initOwner(window);
    //    tagWindow.getWindow().initOwner(window);
     //   dropBoxViewer.getAuthenticationBrowser().getWindow().initOwner(window);

        // --------- Fix the Background ------------
      //  determineBackgroundImage();

        // ---------LoginMode ------------
        //loginMode.getXr3PlayerLabel().setText("~" + window.getTitle() + "~");
     //   loginMode.userSearchBox.registerListeners(window);
     //   loginMode.setLeft(sideBar);

        // -------Root-----------
        root.setVisible(false);
     //   topBar.addXR3LabelBinding();
     //   root.setTop(topBar);
      //  root.setBottom(bottomBar);

        // ----Create the SpecialJFXTabPane for Navigation between Modes
      //  specialJFXTabPane.getTabs().add(new Tab("tab1", libraryMode));
       // specialJFXTabPane.getTabs().add(new Tab("tab2", djMode));
     //   specialJFXTabPane.getTabs().add(new Tab("tab3", userMode));
        //specialJFXTabPane.getTabs().add(new Tab("tab4", webBrowser));
        specialJFXTabPane.setTabMaxWidth(0);
        specialJFXTabPane.setTabMaxHeight(0);

        //Add listeners to each tab
        final AtomicInteger counter = new AtomicInteger(-1);
      //  specialJFXTabPane.getTabs().forEach(tab -> {
        //    final int index = counter.addAndGet(1);
          //  tab.selectedProperty().addListener((observable , oldValue , newValue) -> {
          //      if (specialJFXTabPane.getTabs().get(index).isSelected() && !topBar.isTabSelected(index))
         //           topBar.selectTab(index);
                //System.out.println("Entered Tab " + index) //this is inside curly braces with the above if

       //     });
      //  });
        root.setCenter(specialJFXTabPane);

        //---------LibraryMode ------------
        //libraryMode.librariesContextMenu.show(window, 0, 0);
       // libraryMode.librariesContextMenu.hide();

        //Remove this to be soore...
       // libraryMode.getTopSplitPane().getItems().remove(libraryMode.getNoLibrariesStackPane());

       // libraryMode.getTopSplitPane().getItems().add(playListModesSplitPane);
       // libraryMode.getBottomSplitPane().getItems().add(libraryMode.getNoLibrariesStackPane());
       // libraryMode.getBottomSplitPane().getItems().add(xPlayersList.getXPlayerController(0));

       // libraryMode.openedLibrariesViewer.getEmptyLabel().textProperty().bind(Bindings.when(libraryMode.teamViewer.getViewer().itemsWrapperProperty().emptyProperty())
         //       .then("Press here to create your first library").otherwise("Press here to open the first available library"));
       // libraryMode.librariesSearcher.registerListeners(window);

        //----------ApplicationStackPane---------
      //  applicationStackPane.getChildren().addAll(root, loginMode, updateScreen, welcomeScreen);

        //----------Load Application Users-------
     //   loadTheUsers();

        //----------Bottom Bar----------------
     //   bottomBar.getKeyBindings().selectedProperty().bindBidirectional(settingsWindow.getNativeKeyBindings().getKeyBindingsActive().selectedProperty());
     //   bottomBar.getSpeechRecognitionToggle().selectedProperty().bindBidirectional(consoleWindow.getSpeechRecognition().getActivateSpeechRecognition().selectedProperty());
//
        //-------------TOP BAR--------------------
     //   bottomBar.getSearchField().textProperty().bindBidirectional(searchWindowSmartController.getSearchService().getSearchField().textProperty());
      //  bottomBar.getSearchField().disableProperty().bind(searchWindowSmartController.getIndicatorVBox().visibleProperty());

    }

}
