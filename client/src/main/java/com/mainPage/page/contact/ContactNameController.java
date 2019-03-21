package com.mainPage.page.contact;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.google.jhsheets.filtered.operators.IFilterOperator;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ContactNameController extends AnchorPane implements DictionaryProperties, Initializable {
    private static Logger log = Logger.getLogger(ContactNameController.class.getName());
    @FXML
    JFXComboBox<ContactSearchType> choiceFilter;
    @FXML
    private TableView tableView;
    @FXML
    private JFXButton btnSearch;
    @FXML
    private JFXTextField searchField;
    @FXML
    private Pagination pagination;
    @FXML
    private BorderPane borderPane;
    @FXML
    private JFXButton btnOK;
    @FXML
    private JFXButton btnCancel;
    Contact contact = new Contact();
    private ObservableList<Contact> data;
    public static Contact chosenElement = null;
    private long fromIndex;
    private long toIndex;
    private QuerySearchContact querySearchContact = new QuerySearchContact();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    chosenElement = (Contact) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                    closeWindow();
                }
            }
        });
        Image imageSearchCreateRequest = new Image(getClass().getResourceAsStream("/images/SearchCreateRequest.png"));
        btnSearch.setGraphic(new ImageView(imageSearchCreateRequest));

        Image imageButtonOk = new Image(getClass().getResourceAsStream("/images/ButtonOk.png"));
        btnOK.setGraphic(new ImageView(imageButtonOk));

        Image imageButtonCancel = new Image(getClass().getResourceAsStream("/images/ButtonCancel.png"));
        btnCancel.setGraphic(new ImageView(imageButtonCancel));

        searchField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                try {
                    if (keyEvent.getCode() == KeyCode.ENTER) {
                        if (!searchField.getText().isEmpty())
                            filterData();
                    }
                } catch (Exception e) {
                    log.log(Level.SEVERE, "handleButtonSearch exception: " + e);
                }
            }
        });
        createTableColumns();
        fillChoiceBox(); // choice box filter
        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
        btnOK.setOnAction(event -> {

            chosenElement = (Contact) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            closeWindow();
        });

        btnCancel.setOnAction(event -> {
            closeWindow();
        });

        pagination.setSkin(pageSkin);

        pagination.setPageFactory(this::createPage);

        choiceFilter.getSelectionModel().select(ContactSearchType.NAMECONTACT);



        choiceFilter.setOnAction(e -> setEmptyFilterData());
    }
    public void createTableColumns() {
        try {
            TableColumn<Contact, String> nameContact = new TableColumn<Contact, String>("П.І.Б");
            TableColumn<Contact, String> nameJob = new TableColumn<Contact, String>("Посада");

            nameContact.setMinWidth(150);
            tableView.getColumns().addAll(
                    nameContact,
                    nameJob);
            nameContact.setCellValueFactory(new PropertyValueFactory<Contact, String>("ContactName"));
            nameJob.setCellValueFactory(new PropertyValueFactory<Contact, String>("JobName"));



        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }


    @Override
    public void loadDataFromDatabase() {

        try {
            ObservableList<Contact> listItems = (ObservableList<Contact>) contact.findAll(true, (int) toIndex);

            listItems.forEach(item -> data.add((Contact) item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {

    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {

    }

    @Override
    public void refreshData() {
        data.clear();
        loadDataFromDatabase();
    }

    @Override
    public void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {

    }

    private void filterData() {
        try {

            data.clear();

            String value = searchField.getText().toLowerCase();

            List<Contact> items = new ArrayList<Contact>();


            switch (choiceFilter.getValue()) {
                case NAMECONTACT:
                    items = contact.findByProperty(value, ContactSearchType.NAMECONTACT);
                    break;
                case NAMEJOB:
                    items = contact.findByProperty(value, ContactSearchType.NAMEJOB);
                    break;
            }


            items.forEach(item -> data.add(item));


            tableView.setItems(data);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Filtering tableview data exception: " + e);
        }

    }
    private void setEmptyFilterData() {
        ObservableList<Contact> items = (ObservableList<Contact>) contact.findAll(true, (int) toIndex);
        items.forEach(item -> data.add((Contact) item));

        tableView.setItems(data);
    }

    public void fillChoiceBox() {
        try {
            choiceFilter.getItems().addAll(ContactSearchType.values());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Set choiceBox values exception: " + e);
        }
    }

    @FXML
    private void searchFieldOnKeyReleased(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            filterData();
        }

        if (searchField.getText().isEmpty()) {
            setEmptyFilterData();
        }
    }

    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, (querySearchContact.getMainContactCount()));


            loadDataFromDatabase();


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }
    @FXML
    private void handleButtonSearch() {

        try {
            if (!searchField.getText().isEmpty())
                filterData();

        } catch (Exception e) {
            log.log(Level.SEVERE, "handleButtonSearch exception: " + e);
        }


    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

}
