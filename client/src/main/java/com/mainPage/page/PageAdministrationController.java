package com.mainPage.page;

import com.Utils.CustomPaginationSkin;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.login.User;
import com.mainPage.All.AllController;
import com.mainPage.ArchiveFiles.ArchiveFilesController;
import com.Utils.ComboBoxAutoComplete;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTractController;
import com.mainPage.NotFulled.NotFulledController;
import com.mainPage.Statistic.StatisticController;
import com.mainPage.page.contact.ContactNameController;
import com.mainPage.page.group.GroupNameController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageAdministrationController extends AnchorPane implements Initializable {
    private static Logger log = Logger.getLogger(PageAdministrationController.class.getName());
    @FXML
    private JFXComboBox idWindow;

    private ObservableList optionsIDButton = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox idButton;
    @FXML
    private TableView tableView;
    @FXML
    private TableColumn colName;
    @FXML
    private TableColumn colAccess;
    @FXML
    private TableColumn colType;
    @FXML
    private TableColumn colJob;
    @FXML
    private Pagination pagination;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private JFXButton idContactName;
    @FXML
    private JFXButton idGroupName;
    @FXML
    private JFXButton idDelete;
    private Connection con = null;
    private PreparedStatement pst;
    private ResultSet rs;
    private ObservableList<PageAdministration> data;
    public PageAdministration chosenElement = null;
    private MainPageController mainPageController;
    private QueryPageAdministration queryPageAdministration = new QueryPageAdministration();
    PageAdministration pageAdministration = new PageAdministration();
    private long fromIndex;
    private long toIndex;


    private NotFulledController notFulledController;
    private InProcessingController inProcessingController;
    private InTractController inTractController;
    private ArchiveFilesController archiveFilesController;
    private AllController allController;
    private StatisticController statisticController;

    public void setWindow(NotFulledController notFulledController, InProcessingController inProcessingController, InTractController inTractController, ArchiveFilesController archiveFilesController, AllController allController, StatisticController statisticController) {
        this.notFulledController = notFulledController;
        this.inProcessingController = inProcessingController;
        this.inTractController = inTractController;
        this.archiveFilesController = archiveFilesController;
        this.allController = allController;
        this.statisticController = statisticController;
    }

    public void setMainPageController(MainPageController mainPageController) {
        this.mainPageController = mainPageController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idButton.setVisible(false);
        idWindow.setOnMouseClicked(event -> {
            idWindow.getItems().addAll(mainPageController.tabPane.getTabs().get(0).getText(), mainPageController.tabPane.getTabs().get(1).getText(), mainPageController.tabPane.getTabs().get(2).getText(),
                    mainPageController.tabPane.getTabs().get(3).getText(), mainPageController.tabPane.getTabs().get(4).getText(), mainPageController.tabPane.getTabs().get(5).getText(), mainPageController.administrationID.getText());//, inProcessingController.toString(), inTractController.toString(), archiveFilesController.toString(), allController, statisticController);
            new ComboBoxAutoComplete<String>(idWindow);
        });

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();

            database.reconnect();
        }
        if (idWindow.getValue() == null) {
            idContactName.setDisable(true);
            idGroupName.setDisable(true);
            idDelete.setDisable(true);
        }
        createTableColumns();
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination);
        pagination.setSkin(pageSkin);

        pagination.setPageFactory(this::createPage);



    }

    @FXML
    private void actionWindow(ActionEvent event) {
        idContactName.setDisable(false);
        idGroupName.setDisable(false);
        idDelete.setDisable(false);
        idButton.setVisible(true);
        idButton.getItems().clear();

        Object[] arrayList = new Object[0];
        if (idWindow.getValue().equals("Не виконані")) {
            arrayList = notFulledController.optionsStatusRequest.toArray();
        } else if (idWindow.getValue().equals("В обробці")) {
            arrayList = inProcessingController.optionsStatusRequest.toArray();
        } else if (idWindow.getValue().equals("В тракті")) {
            arrayList = inTractController.optionsStatusRequest.toArray();
        } else if (idWindow.getValue().equals("Архівні")) {
            arrayList = archiveFilesController.observableList.toArray();
        } else if (idWindow.getValue().equals("Всі")) {
            arrayList = allController.observableList.toArray();
        } else if (idWindow.getValue().equals("Статистика")) {
            arrayList = statisticController.observableList.toArray();
        }

        idButton.getItems().addAll(arrayList);

        new ComboBoxAutoComplete<String>(idButton);
        refreshData();
    }

    @FXML
    private void actionButton(ActionEvent event) {
        refreshData();
    }

    public void loadDataFromDatabase() {
        String element;
        if (idButton.getValue() == null) {
            element = String.valueOf(idWindow.getValue());
        } else {
            element = String.valueOf(idButton.getValue());
        }
        try {
            ObservableList<PageAdministration> listItems = (ObservableList<PageAdministration>) pageAdministration.findAll(true, (int) toIndex, element);

            listItems.forEach(item -> data.add((PageAdministration) item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
    }

    public void activeWindowButton() {

    }

    public void createTableColumns() {
        try {

            colName.setCellValueFactory(new PropertyValueFactory<PageAdministration, String>("contactName"));
            colAccess.setCellValueFactory(new PropertyValueFactory<PageAdministration, String>("access"));
            colType.setCellValueFactory(new PropertyValueFactory<PageAdministration, String>("type"));
            colJob.setCellValueFactory(new PropertyValueFactory<PageAdministration, String>("jobName"));

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }


    @FXML
    private void actionContactName(ActionEvent event) {
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/ContactNameView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setMaxHeight(570);
        stage.setMaxWidth(900);


        stage.showAndWait();
        ContactNameController contactNameController = new ContactNameController();
        //   colName.setUserData(contactNameController.chosenElement.getContactName());
        if (idButton.getValue() == null) {
            administrationTable(contactNameController.chosenElement.getID(), idWindow, "Користувач");
        } else {
            administrationTable(contactNameController.chosenElement.getID(), idButton, "Користувач");
        }

    }

    @FXML
    private void actionGroupName(ActionEvent event) {
        Stage stage = new Stage();
        Scene scene = null;
        try {
            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/GroupNameView.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(scene);
        stage.setMaxHeight(570);
        stage.setMaxWidth(900);


        stage.showAndWait();
        GroupNameController groupNameController = new GroupNameController();


        if (idButton.getValue() == null) {
            administrationTable(groupNameController.chosenElement.getID(), idWindow, "Група");
        } else {
            administrationTable(groupNameController.chosenElement.getID(), idButton, "Група");
        }
    }

    public void administrationTable(String nameController, JFXComboBox comboBox, String typeName) {

        String sql = "INSERT tbl_AdministrationAccess \n" +
                "\t\t\t\t(ID,\n" +
                "\t\t\t\tCreatedOn,\n" +
                "\t\t\t\tCreatedByID,\n" +
                "\t\t\t\tModifiedOn,\n" +
                "\t\t\t\tModifiedByID,\n" +
                "\t\t\t\tContactID,\n" +
                "JobID,\n" +
                "\t\t\t\tNameElements,\n" +
                "\t\t\t\tAccess,\n" +
                "[Type])\n" +
                "\t\t\t\tVALUES\n" +
                "(newID(),\n" +
                "CURRENT_TIMESTAMP,\n" +
                "?,\n" +
                "CURRENT_TIMESTAMP,\n" +
                "?,\n" +
                "?,\n" +
                "?,\n" +
                "?,\n" +
                "?,\n" +
                "?)";

        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, User.getContactID());
            pst.setString(2, User.getContactID());
            pst.setString(3, nameController);
            pst.setString(4, nameController);
            pst.setString(5, String.valueOf(comboBox.getValue()));
            pst.setInt(6, 1);
            pst.setString(7, typeName);

            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace(); DBConnection database = new DBConnection();
            database.reconnect();
        }
        refreshData();

    }

    public AnchorPane createPage(int pageIndex) {
        try {
            String element;
            if (idButton.getValue() == null) {
                element = String.valueOf(idWindow.getValue());
            } else {
                element = String.valueOf(idButton.getValue());
            }

            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, (queryPageAdministration.getMainPageAdminostrationCount(element)));


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return anchorPane;
    }

    @FXML
    private void actionDelete(ActionEvent event) {
        try {
            chosenElement = (PageAdministration) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте видалити доступ?") == ButtonType.OK) {
            try {

                String sql = "\tDELETE FROM tbl_AdministrationAccess\n" +
                        "\tWHERE\n" +
                        "\tID = ?";
                pst = con.prepareStatement(sql);
                pst.setString(1, chosenElement.getID());
                pst.execute();
            } catch (SQLException e) {
                e.printStackTrace(); DBConnection database = new DBConnection();
                database.reconnect();
            }
            refreshData();
        } else return;
    }

    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            pagination.setPageFactory(this::createPage);
            UsefulUtils.fadeTransition(tableView);
        }

    }

}
