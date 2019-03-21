package com.mainPage;

import com.Utils.*;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.login.User;
import com.mainPage.tableutils.CustomTableView;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.util.Callback;
import javafx.util.Duration;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import tray.animations.AnimationType;
import tray.notification.TrayNotification;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

public abstract class WorkArea extends BorderPane implements MiniFilterFunction, DictionaryProperties {
    protected static Logger log = Logger.getLogger(WorkArea.class.getName());

    private final static String VISIBILITY_PREFERENCE = ":visibility";
    private final static String RESIZE_PREFERENCE = ":resize";
    private final static Integer ROWS_PER_PAGE = 10;

    protected DerbyComp derbyOrderDAO;
    protected ObservableList<GridComp> data;
    public GridComp offeringRequest;

    protected Preferences windowPreference = Preferences.userNodeForPackage(this.getClass());

    @FXML
    protected Pagination pagination;
    @FXML
    public CustomTableView tableView;

    @FXML
    private TextArea textArea;
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    @FXML protected BorderPane borderPaneTV;
    @FXML protected BorderPane borderPane;
    @FXML protected HBox hboxFilter;
    @FXML protected ScrollPane scrollPaneFilter;
    @FXML protected BorderPane anchorPane;
    @FXML
    public CheckBox notNotification;
    public Button id = new Button();
    public Button Number = new Button();
    public Button Solid = new Button();
    public Button Store = new Button();

    public ImageView upImg = new ImageView(new Image("/images/Sort_Top.png"));
    public ImageView downImg = new ImageView(new Image("/images/Sort_Bottom.png"));
    public boolean order = true;
    public int pageCount = 5;
    public int itemsPerPage = 40;
    public int currentPageIndex = 0;

    protected SummaryTable summaryTable;

    protected long fromIndex;
    protected long toIndex;


    protected HashMap<TableColumn, String> hashColumns = new HashMap<>();
    protected HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();

    protected void initComponents() {}

    public void createTableColumns() {
        try {
            invokePreferenceHandler();

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }


    protected void fillWindowComponents() {

    }

    private void invokePreferenceHandler() {
        try {
            startTableViewPropertyListeners();
            setTableViewPropertyListeners();
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }

    public void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/views/MiniFilter.fxml"));

        Pane content;
        try {
            content = fxmlLoader.load();

            MiniFilterController miniC = fxmlLoader.getController();

            miniC.setWindow(this, content);
            miniC.setFilter(column, type, value);

            hboxFilter.getChildren().add(content);

            hashMiniFilter.put(column, content);

            hboxFilter.setMargin(content, new Insets(0,0,0,10));

            scrollPaneFilter.setFitToHeight(true);

            log.log(Level.INFO, "SETTED");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }
    public synchronized void loadDataFromDatabase() {
        try {
            tableView.setTableItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }


    protected synchronized void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {

            UsefulUtils.searchCombination(eventKey, tableView);


            if(eventKey.getCode().equals(KeyCode.DOWN) || eventKey.getCode().equals(KeyCode.UP)) { // arrow up down
                fixSelectedRecord();

            }
        });

        UsefulUtils.copySelectedCell(tableView);

    }
    public void updateColor (){
        tableView.setRowFactory(new Callback<TableView<GridComp>, TableRow<GridComp>>() {
            @Override
            public TableRow<GridComp> call(TableView<GridComp> param) {
                return new TableRow<GridComp>() {
                    @Override
                    protected void updateItem(GridComp item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            setStyle(" ");
                            if(item.getColorByOfferingSale().equals(0) || item.getColorByOfferingSale().equals(null)) {
                                setStyle(" ");
                            }else if (item.getColorByOfferingSale().equals(1)) {
                                setStyle("-fx-background-color: #2eff3b;");
                            }

                        } catch (NullPointerException ex) {
                            ex.printStackTrace();
                        }


                    }
                };
            }
        });
    }


    protected void fixSelectedRecord() { }
    protected void fixSelectedRecordAll() {}

    protected void showEditWindow() {}

    public synchronized void refreshData() {
        try {
            tableView.setSelectedRow();
            data.clear();
        } catch (NullPointerException ex) {
        } finally {
            loadDataFromDatabase();
          /*  pagination.setPageFactory(this::createPage);
            UpdateColorNotificationNotPrice();
            UsefulUtils.fadeTransition(tableView);*/
           // loadDataFromDatabase();
           // pagination.setPageFactory(this::createPage);
         //   pageCount = getPageCount(data.size(), itemsPerPage);
          //  pagination.setPageCount(pageCount);
            UsefulUtils.fadeTransition(tableView);

        }

    }
    public void UpdateColorNotificationNotPrice() {
        tableView.setRowFactory(new Callback<TableView<GridComp>, TableRow<GridComp>>() {
            @Override
            public TableRow<GridComp> call(TableView<GridComp> param) {
                return new TableRow<GridComp>() {
                    @Override
                    protected void updateItem(GridComp item, boolean empty) {
                        super.updateItem(item, empty);
                        try {

                            if (item == null || item.getIsReadMeassage() == null || item.getIsReadMeassage().equals(0)) {
                                setStyle("");
                                if (item.getOfferingGroupID().equals("C763A6EF-0115-41E4-A00C-219E246F7E0D") ||
                                        item.getOfferingGroupID().equals("9FAB93B7-A92A-498B-AF29-732A88DB86CF") ||
                                        item.getOfferingGroupID().equals("EDC2623A-7F70-4AA0-B4A7-72A900F69C5D")) {
                                    setStyle("-fx-background-color: #15ff73;");
                                }
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2)) {
                                    setStyle("-fx-background-color: #FF0000;");

                                }
                            } else {
                                setStyle("-fx-font-weight: bold");
                                if (item.getOfferingGroupID() == User.getContactID() || item.getCreatedByID().equals(User.getContactID())) {
                                    newNotification(item.getNumber(), item.getStatus());
                                }
                                if (item.getOfferingGroupID().equals("C763A6EF-0115-41E4-A00C-219E246F7E0D") ||
                                        item.getOfferingGroupID().equals("9FAB93B7-A92A-498B-AF29-732A88DB86CF") ||
                                        item.getOfferingGroupID().equals("EDC2623A-7F70-4AA0-B4A7-72A900F69C5D")) {
                                    setStyle("-fx-background-color: #15ff73;" +
                                            "-fx-font-weight: bold");
                                    if (item.getOfferingGroupID().equals(User.getContactID()) || item.getCreatedByID().equals(User.getContactID())) {
                                        newNotification(item.getNumber(), item.getStatus());
                                    }
                                }
                                if (item.getJointAnnulment().equals(1) || item.getJointAnnulment().equals(2))
                                    setStyle("-fx-background-color: #FF0000;" +
                                            "-fx-font-weight: bold");

                            }


                        } catch (NullPointerException ex) {

                        }


                    }
                };
            }
        });

    }
    private void newNotification(String number, String status) {
        if (notNotification.isSelected()) {
        } else {
            Platform.runLater(() -> {
                // Image profileImg = new Image(getClass().getClassLoader().getResource("images/" + msg.getPicture().toLowerCase() +".png").toString(),50,50,false,false);
                TrayNotification tray = new TrayNotification();
                tray.setTitle("Нове повідомлення");
                tray.setMessage(number + ". В дановму запиті є непрочитані\n повідомлення в статусі " + status);
                tray.setRectangleFill(Paint.valueOf("#2C3E50"));
                tray.setAnimationType(AnimationType.POPUP);
                //  tray.setImage(profileImg);
                tray.showAndDismiss(Duration.seconds(11));
                try {
                    Media hit = new Media(getClass().getClassLoader().getResource("sounds/notification.wav").toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(hit);
                    mediaPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });


        }

    }


    public void setTableViewPropertyListeners() {

        tableView.getColumns().forEach(item -> {
            ((TableColumn) item).widthProperty().addListener((ObservableValue<? extends Number> ov, Number oldWidth, Number newWidth) -> {
                if(windowPreference.get(((TableColumn) item).getId()+ RESIZE_PREFERENCE, null) != null)
                    windowPreference.remove(((TableColumn) item).getId()+ RESIZE_PREFERENCE);
                windowPreference.putDouble(((TableColumn) item).getId()+ RESIZE_PREFERENCE, newWidth.doubleValue());
            });

            ((TableColumn) item).visibleProperty().addListener((ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) -> {
                if(windowPreference.get(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE, null) != null)
                    windowPreference.remove(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE);
                windowPreference.putBoolean(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE, newValue);
            });
        });
    }

    public void startTableViewPropertyListeners() {

        tableView.getColumns()
                .forEach(item -> {
                    ((TableColumn) item).setPrefWidth(windowPreference.getDouble(((TableColumn) item).getId()+ RESIZE_PREFERENCE, ((TableColumn) item).getWidth()));
                    ((TableColumn) item).setVisible(windowPreference.getBoolean(((TableColumn) item).getId()+ VISIBILITY_PREFERENCE, ((TableColumn) item).isVisible()));
                });
    }



    protected void setWindowPreference(Preferences preference) {
        this.windowPreference = preference;
    }

    protected void executeTask(TableType type) {
        new PreLoader(this, type).execute();
    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {

    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }


    public class PreLoader extends AsyncTask<Void, Void, Void> {

        private WorkArea area;
        private TableType type;


        public PreLoader(WorkArea area, TableType type) {
            this.area = area;
            this.type = type;

            borderPaneTV.setCenter(UsefulUtils.getProgressIndicator());
            if(pagination != null) getPagination();
        }


        @Override
        public void onPreExecute() {
            area.fillWindowComponents();
        }

        @Override
        public Void doInBackground(Void... params) {
            area.createTableColumns();
            area.initComponents();

            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            switch (type) {
                case REGULAR_TABLE:
                    borderPaneTV.setCenter(tableView);
                    break;
                case SUMMARY_TABLE:
                    borderPaneTV.setCenter(summaryTable.setSummaryTable());
                    break;
            }

        }

        @Override
        public void progressCallback(Void... params) {

        }

        private synchronized void getPagination() {
            try {
                CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination);
                pagination.setSkin(pageSkin);
                pagination.setPageFactory(num -> area.createPage(num));

            } catch (Exception e) {
                log.log(Level.SEVERE, "Exception: " + e);
            }
        }


    }

    public class RefreshTask extends AsyncTask<Void, Void, Void> {
        private WorkArea area;
        private BorderPane borderPaneTV;
        private TableView tableView;
        private ObservableList<GridComp> data;

        public RefreshTask(WorkArea area, BorderPane borderPaneTV, TableView tableView, ObservableList data) {
            this.area = area;
            this.borderPaneTV = borderPaneTV;
            this.tableView = tableView;
            this.data = data;

            borderPaneTV.setCenter(UsefulUtils.getProgressIndicator());

        }


        @Override
        public void onPreExecute() {
            try {
                data.clear();
            } catch (NullPointerException ex) {
                // ignored
            }
        }

        @Override
        public Void doInBackground(Void... params) {
            if(pagination == null) area.loadDataFromDatabase();

            return null;
        }

        @Override
        public void onPostExecute(Void params) {
            UsefulUtils.fadeTransition(tableView);
            borderPaneTV.setCenter(tableView);
        }

        @Override
        public void progressCallback(Void... params) {

        }


    }

    public void setTablePaginationSorted (){

    }
    protected int getPageCount(int totalCount, int itemsPerPage) {
        float floatCount = Float.valueOf(totalCount) / Float.valueOf(itemsPerPage);
        int intCount = totalCount / itemsPerPage;
        System.out.println("floatCount=" + floatCount + ", intCount=" + intCount);
        return ((floatCount > intCount) ? ++intCount : intCount);
    }

    public void SortButton(Button button) {
        button.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent t) {
                pagination.setCurrentPageIndex(0);
                sort(button);
                if (order) {
                    Collections.reverse(data);
                }
                order = !order;
                button.setGraphic((order) ? upImg : downImg);
                updatePersonView();
                pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                        currentPageIndex = newValue.intValue();
                        updatePersonView();
                    }
                });
            }
        });
    }

    public void sort(Button button) {
        if (button == id) {
            data.sort((GridComp p1, GridComp p2) -> p1.getCreatedOn().compareTo(p2.getCreatedOn()));
        } else if (button == Number) {
            data.sort((GridComp p1, GridComp p2) -> p1.getNumber().compareTo(p2.getNumber()));
        } else if (button == Solid) {
            data.sort((GridComp p1, GridComp p2) -> p1.getAccountIsSolid().compareTo(p2.getAccountIsSolid()));
        } else if (button == Store) {
            data.sort((GridComp p1, GridComp p2) -> p1.getStoreCity().compareTo(p2.getStoreCity()));
        } else {
            UsefulUtils.showErrorDialogDown("Помилка сортування");
        }
    }
    public void updatePersonView() {
        fromIndex = currentPageIndex * itemsPerPage;
        toIndex = Math.min(fromIndex + itemsPerPage, data.size());
        tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
    }

    public synchronized BorderPane createPage(int pageIndex) {
        try {
            data = FXCollections.observableArrayList();
            loadDataFromDatabase();
            fromIndex = pageIndex * itemsPerPage;
            toIndex = Math.min(fromIndex + itemsPerPage, data.size());
            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }

        return borderPane;
    }





}

