package com.mainLogin;

import com.jfoenix.controls.JFXTabPane;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author GOXR3PLUS
 *
 */
public class UpdateWindow extends StackPane {

    //--------------------------------------------------------------

    @FXML
    private Label topLabel;

    @FXML
    private Button download;

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






    // -------------------------------------------------------------

    /** The logger. */
    private final Logger logger = Logger.getLogger(getClass().getName());

    /** Window **/
    private Stage window = new Stage();

   // private final InlineCssTextArea whatsNewTextArea = new InlineCssTextArea();
   // private final InlineCssTextArea upcomingFeaturesTextArea = new InlineCssTextArea();
   // private final InlineCssTextArea knownBugsTextArea = new InlineCssTextArea();
   // private final VirtualizedScrollPane<InlineCssTextArea> whatsNewVirtualPane = new VirtualizedScrollPane<InlineCssTextArea>(whatsNewTextArea);
   // private final VirtualizedScrollPane<InlineCssTextArea> upcomingFeaturesVirtualPane = new VirtualizedScrollPane<InlineCssTextArea>(upcomingFeaturesTextArea);
  //  private final VirtualizedScrollPane<InlineCssTextArea> knownBugsVirtualPane = new VirtualizedScrollPane<InlineCssTextArea>(knownBugsTextArea);

    private int update;

    private final String style = "-fx-font-weight:bold; -fx-font-size:14; -fx-fill:white;  -rtfx-background-color:transparent;";

    /**
     * The Thread which is responsible for the update check
     */
    private static Thread updaterThread;

    /**
     * Constructor.
     */
    public UpdateWindow() {

        // ------------------------------------FXMLLOADER ----------------------------------------
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/UpdateWindow.fxml"));
        loader.setController(this);
        loader.setRoot(this);

    //    try {
//            loader.load();
     //   } catch (IOException ex) {
    ///       logger.log(Level.SEVERE, "", ex);
    //    }

        window.setTitle("Вікно Оновлення");
        window.initStyle(StageStyle.UTILITY);
        window.setScene(new Scene(this));
        window.getScene().setOnKeyReleased(k -> {
            if (k.getCode() == KeyCode.ESCAPE)
                window.close();
        });
    }

    /**
     * Called as soon as .fxml is initialized
     */
    @FXML
    private void initialize() {

        // -- whatsNewTextArea
     /*   whatsNewTextArea.setEditable(false);
        whatsNewTextArea.setFocusTraversable(false);
        whatsNewTextArea.getStyleClass().add("inline-css-text-area");

        // -- upcomingFeaturesTextArea
        upcomingFeaturesTextArea.setEditable(false);
        upcomingFeaturesTextArea.setFocusTraversable(false);
        upcomingFeaturesTextArea.setWrapText(true);
        upcomingFeaturesTextArea.getStyleClass().add("inline-css-text-area");

        // -- knownBugsTextArea
        knownBugsTextArea.setEditable(false);
        knownBugsTextArea.setFocusTraversable(false);
        knownBugsTextArea.setWrapText(true);
        knownBugsTextArea.getStyleClass().add("inline-css-text-area");*/

        //-- whatsNewContainer +  upcomingFeaturesContainer + knownBugsContainer
      //  whatsNewContainer.setCenter(whatsNewVirtualPane);
      //  upcomingFeaturesContainer.setCenter(upcomingFeaturesVirtualPane);
      //  knownBugsContainer.setCenter(knownBugsVirtualPane);

        // -- automaticUpdate
        automaticUpdate.setOnAction(a -> startXR3PlayerUpdater(update));

        // -- download
        download.setOnAction(a -> ActionTool.openWebSite("ftp://192.168.10.101/mainPage.rar"));

        // -- GitHub
      //  gitHubButton.setOnAction(a -> ActionTool.openWebSite("https://github.com/goxr3plus/XR3Player"));

        // -- closeWindow
        closeWindow.setOnAction(a -> window.close());

    }

    /**
     * This method is fetching data from github to check if the is a new update for XR3Player
     *
     * @param showTheWindow
     *            If not update is available then don't show the window
     */
  /*  public synchronized void searchForUpdates(boolean showTheWindow) {

        // Not already running
        if (updaterThread != null && updaterThread.isAlive())
            return;

        updaterThread = new Thread(() -> {
            if (showTheWindow)
                Platform.runLater(
                       () -> ActionTool.showNotification("Пошук оновлень", "Отримання інформації з сервера...", Duration.millis(10000), NotificationType.INFORMATION));


            if (InfoTool.isReachableByPing("www.google.com"))
                searchForUpdatesPart2(showTheWindow);
            else
                Platform.runLater(() -> ActionTool.showNotification("Немає з'єднання",
                        "Can't connect to the update site :\n1) Maybe there is not internet connection\n2)GitHub is down for maintenance", Duration.millis(25000),
                        NotificationType.ERROR));

        }, "Application Update Thread");

        updaterThread.setDaemon(true);
        updaterThread.start();

    }

    private void searchForUpdatesPart2(boolean showTheWindow) {
        try {

            Document doc = Jsoup.connect("1").get();

            //Document doc = Jsoup.parse(new File("XR3PlayerUpdatePage.html"), "UTF-8", "http://example.com/");

            Element lastArticle = doc.getElementsByTag("article").last();

            // Not disturb the user every time the application starts if there is not new update
            int currentVersion = (int) Main.internalInformation.get("Version");
            if (Integer.valueOf(lastArticle.id()) <= currentVersion && !showTheWindow)
                return;

            //			//GitHub Releases
            //			HttpURLConnection httpcon = (HttpURLConnection) new URL("https://api.github.com/repos/goxr3plus/XR3Player/releases").openConnection();
            //			httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
            //			BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
            //
            //			//Read line by line
            //			String responseSB = in.lines().collect(Collectors.joining());
            //			in.close();

            // Update is available or not?
            Platform.runLater(() -> {

                //--TopLabel
                if (Integer.valueOf(lastArticle.id()) <= currentVersion) {
                    window.setTitle("You have the latest update!");
                    topLabel.setText("You have the latest update ->( " + currentVersion + " )<-");
                } else {
                    window.setTitle("New update is available!");
//                    topLabel.setText("New Update ->( " + lastArticle.id() + " )<- is available !!!! | You currently have : ->( " + currentVersion + " )<-");
//                    tabPane.getSelectionModel().select(1);
                }

                				//Read the JSON response
                /*			JsonArray jsonRoot;
                try {
                					jsonRoot = (JsonArray) Jsoner.deserialize(responseSB);
                					//Avoid recreating again panes if no new releases have come
                					boolean create = jsonRoot.stream().count() != gitHubAccordion.getPanes().size();
                				if (create)
                						gitHubAccordion.getPanes().clear();

                					//For Each
                					int[] counter = { 0 };
                					jsonRoot.forEach(item -> {
                						//--
                						String prerelease = ( (JsonObject) item ).get("prerelease").toString();

                						//--
                					String tagName = ( (JsonObject) item ).get("tag_name").toString();

                						//--
                					String[] downloads = { "" };
                						( (JsonArray) ( (JsonObject) item ).get("assets") ).forEach(item2 -> downloads[0] = ( (JsonObject) item2 ).get("download_count").toString());
                						downloads[0] = ( downloads[0].isEmpty() ? "-" : downloads[0] );

                					//--
                						String[] size = { "" };
                						( (JsonArray) ( (JsonObject) item ).get("assets") ).forEach(item2 -> size[0] = ( (JsonObject) item2 ).get("size").toString());
                						size[0] = ( size[0].isEmpty() ? "-" : InfoTool.getFileSizeEdited(Long.parseLong(size[0])) );

                //						//--
                						String[] createdAt = { ( (JsonObject) item ).get("created_at").toString() };
                //
                //						//--
                					String[] publishedAt = { ( (JsonObject) item ).get("published_at").toString() };
                //
                //						//Create or Reuse the existing Panes of Accordion
                					GitHubRelease release;
                						if (!create)
                						release = (GitHubRelease) gitHubAccordion.getPanes().get(counter[0]++);
                						else {
                						release = new GitHubRelease();
                							gitHubAccordion.getPanes().add(release);
                						}

                						//Update the GitHubRelease Pane
                						release.setText("Update -> ( " + tagName.toLowerCase().replace("v3.", "") + " ) Downloads ( " + downloads[0] + " )");
                						release.updateLabels(Boolean.toString(!Boolean.parseBoolean(prerelease)), downloads[0], size[0], publishedAt[0].substring(0, 10),
                								createdAt[0].substring(0, 10));

                					});
                					gitHubAccordion.setExpandedPane(gitHubAccordion.getPanes().get(0));
                				} catch (Exception ex) {
                					ex.printStackTrace();
                					ActionTool.showNotification("Message", "Failed to connect update server :(\n Try again in 5 seconds", Duration.seconds(3), NotificationType.ERROR);
                				}

                //Clear the textAreas
               /* whatsNewTextArea.clear();
                upcomingFeaturesTextArea.clear();
                knownBugsTextArea.clear();*/

                // --------------------------------------- whatsNewTextArea -----------------------------------
          /*      doc.getElementsByTag("article").stream().collect(Collectors.toCollection(ArrayDeque::new)).descendingIterator()
                        .forEachRemaining(element -> analyzeUpdate(whatsNewTextArea, element));

                whatsNewTextArea.moveTo(0);
               // whatsNewTextArea.requestFollowCaret();

                // --------------------------------------- upcomingTextArea -----------------------------------
                doc.getElementsByTag("section").stream().filter(section -> "Upcoming Features".equals(section.id())).forEach(section -> {

                    // Append the text to the textArea
                    upcomingFeaturesTextArea.appendText("\n");

                    //Most Important
                    upcomingFeaturesTextArea.appendText("  Most Important:\n\n");
                    upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - 17, upcomingFeaturesTextArea.getLength() - 1, style.replace("white", "#3DFF53"));
                    final AtomicInteger counter2 = new AtomicInteger(-1);
                    Arrays.asList(section.getElementById("most important").text().split("\\*")).forEach(el -> {
                        if (counter2.addAndGet(1) >= 1) {
                            String s = "\t" + counter2 + " ";
                            upcomingFeaturesTextArea.appendText(s);
                            upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - s.length(), upcomingFeaturesTextArea.getLength() - 1, style2);
                            upcomingFeaturesTextArea.appendText(el + "\n");
                            upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - el.length(), upcomingFeaturesTextArea.getLength() - 1, style3);
                        }
                    });

                    //Less Important
                    upcomingFeaturesTextArea.appendText("\n\n  Less Important:\n\n");
                    upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - 17, upcomingFeaturesTextArea.getLength() - 1, style.replace("white", "#3DFF53"));
                    final AtomicInteger counter = new AtomicInteger(-1);
                    Arrays.asList(section.getElementById("info").text().split("\\*")).forEach(el -> {
                        if (counter.addAndGet(1) >= 1) {
                            String s = "\t" + counter + " ";
                            upcomingFeaturesTextArea.appendText(s);
                            upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - s.length(), upcomingFeaturesTextArea.getLength() - 1, style2);
                            upcomingFeaturesTextArea.appendText(el + "\n");
                            upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - el.length(), upcomingFeaturesTextArea.getLength() - 1, style3);
                        }
                    });

                    //Last Updated
                    upcomingFeaturesTextArea.appendText("\n  Last Updated: ");
                    upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - 14, upcomingFeaturesTextArea.getLength() - 1, style.replace("white", "#FFEC00"));
                    upcomingFeaturesTextArea.appendText(section.getElementById("lastUpdated").text());
                    upcomingFeaturesTextArea.setStyle(upcomingFeaturesTextArea.getLength() - section.getElementById("lastUpdated").text().length(),
                            upcomingFeaturesTextArea.getLength(), style3);

                });*/

                //upcomingFeaturesTextArea.moveTo(upcomingFeaturesTextArea.getLength())
                //upcomingFeaturesTextArea.requestFollowCaret()

                // --------------------------------------- knownBugsTextArea -----------------------------------
   /*             doc.getElementsByTag("section").stream().filter(section -> "Bugs".equals(section.id())).forEach(section -> {

                    // Append the text to the textArea
                    /*knownBugsTextArea.appendText("\n");

                    // Information
                    knownBugsTextArea.appendText("  Bugs:\n\n");
                    knownBugsTextArea.setStyle(knownBugsTextArea.getLength() - 7, knownBugsTextArea.getLength() - 1, style.replace("white", "#FF130F"));
                    final AtomicInteger counter = new AtomicInteger(-1);
                    Arrays.asList(section.getElementById("info").text().split("\\*")).forEach(el -> {
                        if (counter.addAndGet(1) >= 1) {
                            String s = "\t" + counter + " ";
                            knownBugsTextArea.appendText(s);
                            knownBugsTextArea.setStyle(knownBugsTextArea.getLength() - s.length(), knownBugsTextArea.getLength() - 1, style2);
                            knownBugsTextArea.appendText(el + "\n");
                            knownBugsTextArea.setStyle(knownBugsTextArea.getLength() - el.length(), knownBugsTextArea.getLength() - 1, style3);
                        }
                    });
*/
                    //Last Updated
                   /* knownBugsTextArea.appendText("\n  Last Updated: ");
                    knownBugsTextArea.setStyle(knownBugsTextArea.getLength() - 14, knownBugsTextArea.getLength() - 1, style.replace("white", "#FFEC00"));
                    knownBugsTextArea.appendText(section.getElementById("lastUpdated").text());
                    knownBugsTextArea.setStyle(knownBugsTextArea.getLength() - section.getElementById("lastUpdated").text().length(), knownBugsTextArea.getLength(), style3);
*/
 /*               });

                //knownBugsTextArea.moveTo(knownBugsTextArea.getLength())
                //knownBugsTextArea.requestFollowCaret()
            });

            //show?
            if (showTheWindow || Integer.valueOf(lastArticle.id()) > currentVersion) {
//                download.setDisable(Integer.valueOf(lastArticle.id()) <= currentVersion);
          //      automaticUpdate.setDisable(download.isDisable());
                update = Integer.valueOf(lastArticle.id());
                show();
            }

        } catch (

                IOException ex) {
            Platform.runLater(() -> ActionTool.showNotification("Error", "Trying to fetch update information a problem occured", Duration.millis(2500), NotificationType.ERROR));
            logger.log(Level.WARNING, "", ex);
        }
    }

    private final String style2 = style.replace("white", "#329CFF");
    private final String style3 = style.replace("bold", "400");

    //For [ New , Improved , Bug Fixes ] counters
    private final String sectionNewCounters = style.replace("white", "#00D993");
    private final String sectionImrpovedCounters = style.replace("white", "#00BBEF");
    private final String sectionBugsFixedCounters = style.replace("white", "#F0004C");

    //Other styles
    private final String updateStyle = style.replace("transparent", "#000000");
    private final String releaseDateStyle = style.replace("white", "#3DFF53");
    private final String minimumJREStyle = style.replace("white", "#FF8800");
    private final String changeLogStyle = style.replace("white", "#FFEC00");
    private final String newStyle = style.replace("transparent", "#00D993");
    private final String improvedStyle = style.replace("transparent", "#00BBEF");
    private final String bugFixesStyle = style.replace("transparent", "#F0004C");

    /**
     * Streams the given update and appends it to the InlineCssTextArea in a specific format
     *
     * @param extArea
     * @param Element
     */
   /* private void analyzeUpdate(InlineCssTextArea textArea , Element element) {
        textArea.appendText("\n\n");

        //Update Version
        int id = Integer.parseInt(element.id());

        // Update
        String text = "\t\t\t\t\t\t\t\t\t\t Update  ~  " + id + " \n";
        textArea.appendText(text);
        textArea.setStyle(textArea.getLength() - text.length() + 11, textArea.getLength() - 1, updateStyle);

        // Release Date
        text = "\t\t\t\t\t\t  Released: ";
        textArea.appendText(text);
        textArea.setStyle(textArea.getLength() - text.length() + 7, textArea.getLength() - 1, releaseDateStyle);
        textArea.appendText(element.getElementsByClass("releasedate").text() + " ");
        textArea.setStyle(textArea.getLength() - element.getElementsByClass("releasedate").text().length() - 1, textArea.getLength() - 1, style3);

        // Minimum JRE
        text = "  Requires Java: ";
        textArea.appendText(text);
        textArea.setStyle(textArea.getLength() - text.length() - 1, textArea.getLength() - 1, minimumJREStyle);
        textArea.appendText(element.getElementsByClass("minJavaVersion").text() + "\n");
        textArea.setStyle(textArea.getLength() - element.getElementsByClass("minJavaVersion").text().length() - 1, textArea.getLength() - 1, style3);

        // ChangeLog
        if (id < 91) { //After Update 91 change log contains more sections
            text = "  ChangeLog:\n";
            textArea.appendText(text);
            textArea.setStyle(textArea.getLength() - text.length() - 1, textArea.getLength() - 1, changeLogStyle);

            final AtomicInteger counter = new AtomicInteger(-1);
            Arrays.asList(element.getElementsByClass("changelog").text().split("\\*")).forEach(improvement -> {
                if (counter.addAndGet(1) >= 1) {
                    String s = "\t" + counter + " ";
                    textArea.appendText(s);
                    textArea.setStyle(textArea.getLength() - s.length(), textArea.getLength() - 1, style2);
                    textArea.appendText(improvement + "\n");
                    textArea.setStyle(textArea.getLength() - improvement.length() - 1, textArea.getLength() - 1, style3);
                }
            });
        } else {

            //new
            text = "      New/Added \n";
            textArea.appendText(text);
            textArea.setStyle(textArea.getLength() - text.length() + 5, textArea.getLength() - 1, newStyle);
            final AtomicInteger counter = new AtomicInteger(-1);
            Arrays.asList(element.getElementsByClass("new").text().split("\\*")).forEach(improvement -> {
                if (counter.addAndGet(1) >= 1) {
                    String s = "\t\t" + counter + " ";
                    textArea.appendText(s);
                    textArea.setStyle(textArea.getLength() - s.length(), textArea.getLength() - 1, sectionNewCounters);
                    textArea.appendText(improvement + "\n");
                    textArea.setStyle(textArea.getLength() - improvement.length() - 1, textArea.getLength() - 1, style3);
                }
            });

            //improved
            text = "      Improved \n";
            textArea.appendText(text);
            textArea.setStyle(textArea.getLength() - text.length() + 5, textArea.getLength() - 1, improvedStyle);
            final AtomicInteger counter2 = new AtomicInteger(-1);
            Arrays.asList(element.getElementsByClass("improved").text().split("\\*")).forEach(improvement -> {
                if (counter2.addAndGet(1) >= 1) {
                    String s = "\t\t" + counter2 + " ";
                    textArea.appendText(s);
                    textArea.setStyle(textArea.getLength() - s.length(), textArea.getLength() - 1, sectionImrpovedCounters);
                    textArea.appendText(improvement + "\n");
                    textArea.setStyle(textArea.getLength() - improvement.length() - 1, textArea.getLength() - 1, style3);
                }
            });

            //fixed
            text = "      Bug Fixes \n";
            textArea.appendText(text);
            textArea.setStyle(textArea.getLength() - text.length() + 5, textArea.getLength() - 1, bugFixesStyle);
            final AtomicInteger counter3 = new AtomicInteger(-1);
            Arrays.asList(element.getElementsByClass("fixed").text().split("\\*")).forEach(improvement -> {
                if (counter3.addAndGet(1) >= 1) {
                    String s = "\t\t" + counter3 + " ";
                    textArea.appendText(s);
                    textArea.setStyle(textArea.getLength() - s.length(), textArea.getLength() - 1, sectionBugsFixedCounters);
                    textArea.appendText(improvement + "\n");
                    textArea.setStyle(textArea.getLength() - improvement.length() - 1, textArea.getLength() - 1, style3);
                }
            });
        }
    }
*/
    /**
     * Calling this method to start the main Application which is XR3Player
     *
     */
    public void startXR3PlayerUpdater(int update) {

        String applicationName = "mainPage";

        // Start XR3Player Updater
        new Thread(() -> {
            String path = InfoTool.getBasePathForClass(Main.class);
            String[] applicationPath = { new File(path + applicationName + ".jar").getAbsolutePath() };

         //   Show message that application is restarting
            Platform.runLater(() -> ActionTool.showNotification("Розпочинаємо " + applicationName,
                    "Application Path:[ " + applicationPath[0] + " ]\n\tЯкщо це займає більше 10 секунд, або комп'ютер працює повільно або він не працює....", Duration.seconds(25),
                   NotificationType.INFORMATION));

            try {

                //----Auto Update Button
          //      Platform.runLater(() -> automaticUpdate.setDisable(true));

                //------------Export XR3PlayerUpdater
                ActionTool.copy(UpdateWindow.class.getResourceAsStream("/updater/" + applicationName + ".jar"), applicationPath[0]);

                //------------Wait until XR3Player is created
                File XR3PlayerUpdater = new File(applicationPath[0]);
                while (!XR3PlayerUpdater.exists()) {
                    Thread.sleep(50);
                    System.out.println("Waiting " + applicationName + " Jar to be created...");
                }

                System.out.println(applicationName + " Path is : " + applicationPath[0]);

                //Create a process builder
                ProcessBuilder builder = new ProcessBuilder("java", "-jar", applicationPath[0], String.valueOf(update));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Wait n seconds
                PauseTransition pause = new PauseTransition(Duration.seconds(10));
                pause.setOnFinished(f -> Platform.runLater(() -> ActionTool.showNotification("Starting " + applicationName + " failed",
                        "\nApplication Path: [ " + applicationPath[0] + " ]\n\tTry to do it manually...", Duration.seconds(10), NotificationType.ERROR)));
                pause.play();

                // Continuously Read Output to check if the main application started
                String line;
                while (process.isAlive())
                    while ( ( line = bufferedReader.readLine() ) != null) {
                        System.out.println(line);
                        if (line.isEmpty())
                            break;
                        if (line.contains(applicationName + " Application Started"))
                            System.exit(0);
                    }

            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.INFO, null, ex);

                // Show failed message
                Platform.runLater(() -> Platform.runLater(() -> ActionTool.showNotification("Starting " + applicationName + " failed",
                        "\nApplication Path: [ " + applicationPath[0] + " ]\n\tTry to do it manually...", Duration.seconds(10), NotificationType.ERROR)));

            }

            //----Auto Update Button
          //  Platform.runLater(() -> automaticUpdate.setDisable(false));

        }, "Start XR3Application Thread").start();
//        download.setOnAction(a -> ActionTool.openWebSite("ftp://192.168.10.101/mainPage.rar"));
    }

    /**
     * Show the Window
     */
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

}
