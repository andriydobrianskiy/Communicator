package com.mainPage.NotFulled;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.InitComponents;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.client.chatwindow.Listener;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.login.User;
import com.mainPage.InProcessing.Edit.WindowOperation;
import com.mainPage.NotFulled.CreateRequest.CreateMainController;
import com.mainPage.NotFulled.Edit.EditController;
import com.mainPage.NotFulled.OfferingRequest.DerbyOfferingRequestDAO;
import com.mainPage.NotFulled.OfferingRequest.ExampleController;
import com.mainPage.NotFulled.OfferingRequest.OfferingRequest;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.NotFulled.ProductAdd.ProductAddController;
import com.mainPage.NotFulled.ProductAdd.ProductAddNewController;
import com.mainPage.WorkArea;
import com.mainPage.page.MainPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.dbutils.QueryRunner;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableDateTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableDoubleTableColumn;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.io.Externalizable;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotFulledController extends WorkArea implements MiniFilterFunction, Initializable, ObserverNF, DictionaryProperties {

    public static User userID;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private QueryRunner dbAccess = new QueryRunner();
    User user = new User();
    // Top table
    private Connection con = null;
    @FXML
    public JFXButton tbn_CreateRequest;
    @FXML
    public JFXButton btn_changeRequest;
    @FXML
    public JFXButton btn_ConfirmRequest;
    @FXML
    public JFXButton btn_СancelRequest;
    @FXML
    public JFXButton btnDelete;

    @FXML
    private MainPageController main;
    @FXML
    private JFXTextField searchingField;
    @FXML
    private HBox hboxFilter;
    @FXML
    public JFXButton btn_AddRequest;
    @FXML
    public JFXButton btn_RefreshRequest;
    @FXML
    public JFXButton btn_DeleteRequest;
    @FXML
    public ToggleButton btn_ButtonAll;
    @FXML
    public ToggleButton btn_ButtonPricingAll;
    @FXML
    public HBox buttonView;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colNumber;
    @FXML
    private FilterableDateTableColumn<NotFulfilled, Date> colCreatedOn;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colCreatedBy;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colAccountCode;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colAccountName;
    @FXML
    private FilterableDoubleTableColumn<NotFulfilled, Double> colAccountSaldo;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colAccountIsSolid;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colStoreCity;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colStatus;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colOfferingGroupName;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colOriginalGroupName;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colGroupChangedBy;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colSpecialMarginTypeName;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colCashType;
    @FXML
    private FilterableStringTableColumn<NotFulfilled, String> colPricingDescription;
    @FXML
    private Pagination pagination;
    @FXML
    private ScrollPane scrollPaneFilter;
    @FXML
    public ExampleController exampleProductTableController;

    public ChatController chatViewController;
    private Integer pricingBoolean = 0;

    private InitComponents components;
    private static Logger log = Logger.getLogger(NotFulledController.class.getName());

    private long fromIndex;
    private long toIndex;
    private OfferingRequest offeringRequest;
    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();
    public NotFulfilled chosenAccount = null;
    public static NotFulfilled account = new NotFulfilled();

    public ObservableList optionsStatusRequest = FXCollections.observableArrayList();
    public void setChatController(ChatController chatViewController){
    this.chatViewController = chatViewController;
                                                    }
    public ChatController getChatViewController () {return this.chatViewController;}
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnDelete.setVisible(false);
        btn_changeRequest.setVisible(false);
        btn_СancelRequest.setVisible(false); // Менеджер
        btn_RefreshRequest.setVisible(false);
        btn_DeleteRequest.setVisible(false); // Менеджер
        tbn_CreateRequest.setVisible(false); // Менеджер
        btn_ConfirmRequest.setVisible(false);// Менеджер
        btn_AddRequest.setVisible(false);// Менеджер
        btn_ButtonAll.setVisible(false);
        btn_ButtonPricingAll.setVisible(false);
        tableView.getSelectionModel().setCellSelectionEnabled(true);

        optionsStatusRequest.addAll( btnDelete.getText(), btn_changeRequest.getText(), btn_СancelRequest.getText(),
                btn_RefreshRequest.getText(), btn_DeleteRequest.getText(), tbn_CreateRequest.getText(), btn_ConfirmRequest.getText(), btn_AddRequest.getText(), btn_ButtonAll.getText(), btn_ButtonPricingAll.getText());

        Image imageDecline = new Image(getClass().getResourceAsStream("/images/CreateRequest.png"));
        tbn_CreateRequest.setGraphic(new ImageView(imageDecline));

        exampleProductTableController.init(this);
        Image imageChange = new Image(getClass().getResourceAsStream("/images/ChangeRequest.png"));
        btn_changeRequest.setGraphic(new ImageView(imageChange));


        Image imageConfirm = new Image(getClass().getResourceAsStream("/images/ConfirmRequest.png"));
        btn_ConfirmRequest.setGraphic(new ImageView(imageConfirm));

        Image imageCancel = new Image(getClass().getResourceAsStream("/images/СancelRequest.png"));
        btn_СancelRequest.setGraphic(new ImageView(imageCancel));

        Image imageDelete = new Image(getClass().getResourceAsStream("/images/DeleteRequest.png"));
        btnDelete.setGraphic(new ImageView(imageDelete));

      //  Image imageIntoProcessing = new Image(getClass().getResourceAsStream("/images/IntoProcessing.png"));
       /// btn_IntoProcessing.setGraphic(new ImageView(imageIntoProcessing));
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnection database = new DBConnection();
            database.reconnect();
        }
        ToggleGroup group = new ToggleGroup();
        btn_ButtonAll.setToggleGroup(group);
        btn_ButtonAll.setSelected(true);
        btn_ButtonPricingAll.setToggleGroup(group);

        group.selectedToggleProperty().addListener(event -> {
            if (group.getSelectedToggle().equals(btn_ButtonAll)) {
                pricingBoolean = 0;
                refreshData();
            } else if (group.getSelectedToggle().equals(btn_ButtonPricingAll)) {
                pricingBoolean = 1;
                refreshData();
            }
        });

        ConfirmRequest(); // button
        СancelRequest(); // button
        createTableColumns();
        tableViewHandles();


        tableView.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableView);
        tableView.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, event -> {
            new MiniFilter(NotFulledController.this, account, hashColumns, event).setFilter();
        });
        UsefulUtils.copySelectedCell(tableView);


        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        anchorPane.setCenter(tableView);
        searchingField.setOnAction(event1 -> {
            String value = searchingField.getText();

            if (value.equals("")) {
                refreshData();
            } else
                try {
                    findByProperty(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        });

        UsefulUtils.installCopyPasteHandler(tableView);

        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
            pagination.setSkin(pageSkin);

            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
            SortButton(Number);
            SortButton(id);
            SortButton(Solid);
            SortButton(Store);
            colCreatedOn.setGraphic(id);
            colCreatedOn.setSortable(false);
            colNumber.setGraphic(Number);
            colNumber.setSortable(false);
            colAccountIsSolid.setGraphic(Solid);
            colAccountIsSolid.setSortable(false);
            colStoreCity.setGraphic(Store);
            colStoreCity.setSortable(false);
         /*   pagination.currentPageIndexProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    System.out.println("Pagination Changed from " + oldValue + " , to " + newValue);
                    currentPageIndex = newValue.intValue();
                    updatePersonView();
                }
            });*/



        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вдається відкрити сторінки");

        }
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }



   /*private void UpdateStatus(){
       btn_UpdateStatus.setOnAction(event -> {
           try {
               chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
           } catch (Exception ex) {
               UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
               return;
           }
           if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте взяти в роботу?") == ButtonType.OK) {

               String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                       "\tSET  [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                       "[OfferingGroupID] = ?,\n" +
                       "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                       "\t[ModifiedByID] = ?\n" +
                       "WHERE([tbl_RequestOffering].[ID] = ?)";
               try {
                   pst = con.prepareStatement(query);
                   pst.setString(1, User.getContactID());
                   pst.setString(2, User.getContactID());
                   pst.setString(3, chosenAccount.getID());

                   pst.executeUpdate();
                   main.changeExists();
                   UsefulUtils.showSuccessful("Запит " + User.getContactName() + " взято в роботу");
                   main.changeExists();
                   UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " анульовано");

               } catch (SQLException e) {
                   e.printStackTrace();
                   DBConnection database = new DBConnection();
                   database.reconnect();

               }
           } else return;
       });

   }*/

   private void СancelRequest(){
       btn_СancelRequest.setOnAction(event -> {
           try {
               chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
           } catch (Exception ex) {
               UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
               return;
           }
           if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте анулювати запит?") == ButtonType.OK) {
               try {
                   con = DBConnection.getDataSource().getConnection();
                   CallableStatement callProc = con.prepareCall("{call dbo.tsp_ROActionNullifyRequest (?,?)}");
                   System.out.println(chosenAccount.getID());
                   callProc.setString(1, chosenAccount.getID());
                   callProc.setString(2, User.getContactID());
                   System.out.println(chosenAccount.getID() + " 444444444444444444444444444444444555555555555555555555555555");
                   callProc.executeUpdate();
                   main.changeExists();
                   UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " анульовано");


               } catch (SQLException e) {
                   e.printStackTrace();
                   DBConnection database = new DBConnection();
                   database.reconnect();
               }

           } else return;
       });
   }
        private void ConfirmRequest(){
            btn_ConfirmRequest.setOnAction(event -> {
                try {
                    chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                } catch (Exception ex) {
                    UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                    return;
                }

                if (UsefulUtils.showConfirmDialog("Ви дійсно бажаєте надіслати запит?") == ButtonType.OK) {

                    String query = "UPDATE [dbo].[tbl_RequestOffering]\n" +
                            "\tSET [StatusID] = '{7CB7F6B9-EB87-48FE-86F6-49ED931A0C0B}',\n" +
                            "\t\t\t\t\tIsReadMeassage = '1',\n" +
                            "\t[ModifiedOn] = CURRENT_TIMESTAMP,\n" +
                            "\t[ModifiedByID] = ?\n" +
                            "WHERE([tbl_RequestOffering].[ID] = ?)";
                    try {
                        pst = con.prepareStatement(query);
                        pst.setString(1, User.getContactID());
                        pst.setString(2, chosenAccount.getID());

                        pst.executeUpdate();
                        chatViewController.messageBox.setText("Новий запит");
                   //     new Listener(chatViewController, chosenAccount);
                        chatViewController.sendButtonAction();
                        main.changeExists();

                        UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " надіслано в обробку");


                    } catch (SQLException e) {
                        e.printStackTrace();
                        DBConnection database = new DBConnection();
                        database.reconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else return;


            });
        }



    @FXML
    private void actionSearchingField(ActionEvent event) {

        String value = searchingField.getText();

        if (value.equals("")) {
            refreshData();
        } else
            findByProperty(value);

    }

    private ObservableList options = FXCollections.observableArrayList();

    String Skrut = null;

    public void findByProperty(String value) {
        data.clear();

        try {
            pst = con.prepareStatement("SELECT RequestID " +
                            "\t\t\t\t\tFROM dbo.tbl_OfferingInRequestOffering \n" +
                            " WHERE [tbl_OfferingInRequestOffering].[OfferingID] IN (SELECT ID\n" +
                            "        FROM [tbl_Offering] WHERE [tbl_Offering].Skrut LIKE '" + value.toString() + "%' OR [tbl_Offering].[Index] LIKE '" + value.toString() + "%')"//+
                    //  "FOR XML PATH('')"
            );
            rs = pst.executeQuery();
            while (rs.next()) {
                Skrut = rs.getString(1);
                List<NotFulfilled> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));


            }
            tableView.setItems(data);

        } catch (SQLException e) {
            DBConnection database = new DBConnection();
            database.reconnect();
        }
    }



    @Override
    public void createTableColumns() {


        try {

            hashColumns.put(colNumber, "[tbl_RequestOffering].[Number]");
            hashColumns.put(colCreatedOn, "[tbl_RequestOffering].[CreatedOn]");
            hashColumns.put(colCreatedBy, "[tbl_Contact].[Name]");
            hashColumns.put(colAccountName, "[tbl_Account].[Name]");
            hashColumns.put(colAccountCode, "[tbl_Account].[Code]");
            hashColumns.put(colAccountSaldo, "[tbl_Account].[SaldoSel]");
            hashColumns.put(colAccountIsSolid, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");
            hashColumns.put(colStatus, "[tbl_RequestOfferingStatus].[Name]");
            hashColumns.put(colStoreCity, "[tbl_StoreCity].[Name]");
            hashColumns.put(colOfferingGroupName, "[tbl_Contact].[Name]");
            hashColumns.put(colOriginalGroupName, "SELECT\n" +
                    "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n");
            hashColumns.put(colGroupChangedBy, "SELECT\n" +
                    "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n");
            hashColumns.put(colSpecialMarginTypeName, "[SMT].[Name]");
            hashColumns.put(colCashType, "[tbl_RequestOffering].[CashType]");
            hashColumns.put(colPricingDescription, "[tbl_RequestOffering].PricingDescription");
            List<?> listColumns = tableView.getColumns();

            colNumber.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("Number"));
            colCreatedOn.setCellValueFactory(new PropertyValueFactory<NotFulfilled, Date>("CreatedOn"));
            colCreatedBy.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("CreatedBy"));
            colAccountCode.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountCode"));
            colAccountName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountName"));
            colAccountSaldo.setCellValueFactory(new PropertyValueFactory<NotFulfilled, Double>("AccountSaldo"));
            colAccountIsSolid.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountIsSolid"));
            colStoreCity.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("StoreCity"));
            colStatus.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("Status"));

            colOfferingGroupName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("OfferingGroupName"));
            colOriginalGroupName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("OriginalGroupName"));
            colGroupChangedBy.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("GroupChangedBy"));
            colSpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("SpecialMarginTypeName"));
            colCashType.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("CashType"));
            colPricingDescription.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("PricingDescription"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
        super.createTableColumns();

    }


    @Override
    public void disableFilter(TableColumn column, Pane content) {
        account.removeStringFilter(column);
        refreshData();

        //column.filteredProperty().setValue(true);

        if (content == null) {
            removeFilterFromHbox(column);
            return;
        }

        hboxFilter.getChildren().remove(content);


        System.out.println("DISABLED");
    }

    public void clearTable() {
        try {
            data.clear();

        } catch (Exception e) {
            data = FXCollections.observableArrayList();
        } finally {
            tableView.setItems(data);
        }
    }


    @Override
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

            hboxFilter.setMargin(content, new Insets(0, 0, 0, 10));

            scrollPaneFilter.setFitToHeight(true);

            System.out.println("SETTTED");
            //content.getStylesheets().add("sample/ua/ucountry/MainTables/Account/MainDictionaryTable.css");
        } catch (IOException exception) {

            throw new RuntimeException(exception);
        }
    }

    @Override
    public void removeFilterFromHbox(TableColumn column) {
        hboxFilter.getChildren().remove(hashMiniFilter.get(column));
    }

    public void loadDataFromDatabase() {
        try {
            List<NotFulfilled> listItems = account.findAllNotFulled(true, (int) toIndex, User.getContactID(), User.getContactID(), pricingBoolean);
            listItems.forEach(item -> data.add(item));
            super.loadDataFromDatabase();

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
    }


    @FXML
    private void UpdateButton(ActionEvent event) {
        refreshData();
    }
    /*public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> {
            UsefulUtils.searchCombination(eventKey, tableView);
            fixSelectedRecord();
        });
    }*/
    public void fixSelectedRecord() {
        NotFulfilled record = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());


        exampleProductTableController.handleTableView(record);

    }

    public void handleTableView() {
        refreshData();
    }

    public void showEdit(NotFulfilled value) {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(ProductAddNewController.class.getResource("/views/EditView.fxml"));
        EditController createRequest = loader.getController();
        Pane serverrLayout = null;
        try {
            serverrLayout = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(serverrLayout);
        EditController con = loader.getController();
        stage.setScene(scene);
        stage.setMaxHeight(520);
        stage.setMaxWidth(620);
        main.changeExists();
        stage.show();
        stage.requestFocus();
        refreshData();
    }

    @FXML
    private void handleCreateRequest(ActionEvent evt) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ProductAddNewController.class.getResource("/views/CreateMainView.fxml"));
        CreateMainController createRequest = loader.getController();
        Pane serverrLayout = loader.load();
        stage.setResizable(false);
        Scene scene = new Scene(serverrLayout);
        CreateMainController con = loader.getController();
        con.setOfferingRequest(this);
        stage.setScene(scene);
        stage.setMaxHeight(480);
        stage.setMaxWidth(620);
        stage.show();
        stage.requestFocus();
    }



    @FXML
    private void actionAddButton(ActionEvent ev) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            NotFulfilled record = null;
            try {
                record = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
            } catch (Exception e) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного запиту");
                return;
            }
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ProductAddNewController.class.getResource("/views/ProductAddView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);

            ProductAddController productAddController = loader.getController();
            productAddController.setNotFulled(record);
            productAddController.setExampleController(exampleProductTableController, this);

            stage.setScene(scene);
            ResizeHelper.addResizeListener(stage);
            stage.setMaxHeight(298);
            stage.setMaxWidth(620);
            ProductAddController edit = new ProductAddController(WindowOperation.ADD, record);
            stage.show();
            stage.requestFocus();
        } catch (Exception e) {

            UsefulUtils.showErrorDialogDown(e.getMessage());

        }
    }


    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }

    NotFulfilled selectedOffering = null;

    @FXML
    private void actionChangeRequest(ActionEvent event) {
        try {
            chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного запиту");
            return;
        }

        EditController editController = new EditController(chosenAccount, false);
    }

    @FXML
    public void handleDelete(ActionEvent e) {

        try {
            selectedOffering = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
            new DerbyNotFulledDAO().deleteOffering(selectedOffering);
            new DerbyNotFulledDAO().deleteOffering(selectedOffering);
            main.changeExists();
            UsefulUtils.showSuccessful("Запит успішно видалено");

        } else return;


    }

    @FXML
    public void handleDeleteProduct(ActionEvent e) {

        try {
            offeringRequest = (OfferingRequest) exampleProductTableController.tableView.getItems().get(exampleProductTableController.tableView.getSelectionModel().getSelectedIndex());

        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
            new DerbyOfferingRequestDAO().deleteAccount(offeringRequest);
            exampleProductTableController.refresh();
            UsefulUtils.showSuccessful("Продукт успішно видалено");
            ;
        } else return;

    }
    public BorderPane createPage(int pageIndex) {
        try {
            data = FXCollections.observableArrayList();
            loadDataFromDatabase();
            fromIndex = pageIndex * itemsPerPage;
            toIndex = Math.min(fromIndex + itemsPerPage, data.size());


            tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
            // Collections.sort(data);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }
    public void refreshData() {
        try {
            tableView.setSelectedRow();
            data.clear();
            loadDataFromDatabase();
            pagination.setPageFactory(this::createPage);
            pageCount = getPageCount(data.size(), itemsPerPage);
            pagination.setPageCount(pageCount);
            UsefulUtils.fadeTransition(tableView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        refreshData();
    }
    @FXML
    public void RefreshRequest(ActionEvent event) {
        try {
            chosenAccount = (NotFulfilled) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного запиту");
            return;
        }

     //   ProductAddController editController = new ProductAddController(chosenAccount, false);
    }
}
