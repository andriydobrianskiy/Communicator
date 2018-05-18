package com.mainPage.NotFulled;

import com.Utils.CustomPaginationSkin;
import com.Utils.DictionaryProperties;
import com.Utils.MiniFilterWindow.InitComponents;
import com.Utils.MiniFilterWindow.MiniFilter;
import com.Utils.MiniFilterWindow.MiniFilterController;
import com.Utils.MiniFilterWindow.MiniFilterFunction;
import com.Utils.UsefulUtils;
import com.client.util.ResizeHelper;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.login.User;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.NotFulled.Edit.EditController;
import com.mainPage.NotFulled.OfferingRequest.DerbyOfferingRequestDAO;
import com.mainPage.NotFulled.OfferingRequest.ExampleController;
import com.mainPage.NotFulled.OfferingRequest.OfferingRequest;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.NotFulled.ProductAdd.ProductAddController;
import com.mainPage.NotFulled.ProductAdd.ProductAddNewController;
import com.mainPage.createRequest.CreateRequest;
import com.mainPage.page.MainPageController;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.dbutils.QueryRunner;
import org.google.jhsheets.filtered.FilteredTableView;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.AbstractFilterableTableColumn;
import org.google.jhsheets.filtered.tablecolumn.ColumnFilterEvent;
import org.google.jhsheets.filtered.tablecolumn.FilterableStringTableColumn;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

//import com.sun.java.util.jar.pack.ConstantPool;

public class NotFulledController extends BorderPane implements MiniFilterFunction, Initializable, DictionaryPropertiesNotfulled, ObserverNF, DictionaryProperties {

    public static User userID;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    //  private ObservableList<NotFulfilled> data;
    private QueryRunner dbAccess = new QueryRunner();
    User user = new User();
    // Top table
    private Connection con = null;
    @FXML
    private JFXButton tbn_CreateRequest;
    @FXML
    private ToolBar MaterialDesign;
    @FXML
    private JFXButton btn_changeRequest;
    @FXML
    private JFXButton btn_ConfirmRequest;
    @FXML
    private JFXButton btn_СancelRequest;
    @FXML
    private JFXButton btnDelete;
    @FXML
    private JFXButton btn_IntoProcessing;
    @FXML
    private JFXButton btn_UpdateStatus;
    @FXML
    public TableView tableRequestOffering;

    private MainPageController main;
    @FXML
    InProcessingController inProcessingController;
    @FXML
    private BorderPane anchorPane;
    @FXML
    private BorderPane borderPane;

    @FXML
    private JFXTextField searchingField;
    @FXML
    private HBox hboxFilter;
    @FXML
    private JFXButton btn_AddRequest;
    @FXML
    private JFXButton btn_RefreshRequest;
    @FXML
    private JFXButton btn_DeleteRequest;

    private InitComponents components;
    // @FXML
    //  private JFXComboBox cmbCustomer;
    //   private DerbyOfferingsDAO offeringsDAO = new DerbyOfferingsDAO();
//    private OfferingsQueries offeringsQueries = new OfferingsQueries();

    MenuBar menuBar = new MenuBar();
    Menu mainMenu = new Menu("Файл");
    MenuItem exitCmd = new MenuItem("Вихід");
    MenuItem textCmd = new MenuItem("Пошук");


    //   private long fromIndex;
    // private long toIndex;
    private static Logger log = Logger.getLogger(NotFulledController.class.getName());


    private ObservableList<NotFulfilled> data;
    //  private ObservableList<ProductList> dataBottom;


    private NotFulledQuery accountQueries = new NotFulledQuery();

    private long fromIndex;
    private long toIndex;
    private HashMap<TableColumn, Pane> hashMiniFilter = new HashMap<>();
    private HashMap<TableColumn, String> hashColumns = new HashMap<>();

    private TableColumn<NotFulfilled, String> ID;
    private TableColumn<NotFulfilled, String> CreatedOn;
    private TableColumn<NotFulfilled, String> CreatedByID;
    private TableColumn<NotFulfilled, String> CreatedBy;
    private TableColumn<NotFulfilled, String> AccountID;
    private TableColumn<NotFulfilled, String> AccountName;
    private TableColumn<NotFulfilled, String> AccountCode;
    private TableColumn<NotFulfilled, String> AccountSaldo;
    private TableColumn<NotFulfilled, String> AccountIsSolid;
    private TableColumn<NotFulfilled, String> Number;
    private TableColumn<NotFulfilled, String> StatusID;
    private TableColumn<NotFulfilled, String> Status;
    private TableColumn<NotFulfilled, String> StoreCityID;
    private TableColumn<NotFulfilled, String> StoreCity;
    private TableColumn<NotFulfilled, String> OfferingGroupID;
    private TableColumn<NotFulfilled, String> OfferingGroupName;
    private TableColumn<NotFulfilled, String> OriginalGroupID;
    private TableColumn<NotFulfilled, String> OriginalGroupName;
    private TableColumn<NotFulfilled, String> IsNewMessage;
    private TableColumn<NotFulfilled, String> JointAnnulment;
    private TableColumn<NotFulfilled, String> Note;
    private TableColumn<NotFulfilled, String> LastMessage;
    private TableColumn<NotFulfilled, String> GroupChangedByID;
    private TableColumn<NotFulfilled, String> GroupChangedBy;
    private TableColumn<NotFulfilled, String> IsReadMessage;
    private TableColumn<NotFulfilled, String> SpecialMarginTypeID;
    private TableColumn<NotFulfilled, String> SpecialMarginTypeName;
    private TableColumn<NotFulfilled, String> StateID;
    private TableColumn<NotFulfilled, String> StateName;

    @FXML
    private JFXComboBox cmbPerformer;
    @FXML
    private Pagination pagination;
    @FXML
    private ScrollPane scrollPaneFilter;
    public static NotFulfilled chosenAccount = null;
    public NotFulfilled notFulfilled;
    // Context menu
    private ContextMenu contextMenu;

    private NotFulfilled account = new NotFulfilled();
    // private ProductList productList = new ProductList();

    @FXML
    public ExampleController exampleProductTableController;

    private IntegerProperty index = new SimpleIntegerProperty();


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        //   System.out.println(inProcessingController.refreshData());
       /* searchingField.setOnAction(event1 -> {
            System.out.println("222");
            main.changeExists();
        });*/
        btn_UpdateStatus.setVisible(false);
        btn_IntoProcessing.setVisible(false);
        btnDelete.setVisible(false);
        btn_changeRequest.setVisible(false);
        btn_СancelRequest.setVisible(true); // Менеджер
        btn_RefreshRequest.setVisible(false);
        btn_DeleteRequest.setVisible(true); // Менеджер
        tbn_CreateRequest.setVisible(true); // Менеджер
        btn_ConfirmRequest.setVisible(true);// Менеджер
        btn_AddRequest.setVisible(true);// Менеджер


        System.out.println(User.getContactID() + "666666666666666666655555555555555555554444444444444");


        // tableViewHandles();
        //  btnDelete.setVisible(true);
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

        Image imageIntoProcessing = new Image(getClass().getResourceAsStream("/images/IntoProcessing.png"));
        btn_IntoProcessing.setGraphic(new ImageView(imageIntoProcessing));

        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        btn_ConfirmRequest.setOnAction(event -> {
            try {
                chosenAccount = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }


            // System.out.println(a +  b+c + "ed2wewgweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
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
                    main.changeExists();

                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " надіслано в обробку");

                    //    ExampleController exampleController = loader.getController();
                    //
                    // exampleProductTableController.refresh();
                    //   productAddController.setExampleController(exampleProductTableController);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else return;


        });

      /*  searchingField.setOnAction( event -> {
            try{
                data.clear();
                con = DBConnection.getDataSource().getConnection();
                CallableStatement callProc = con.prepareCall("{call [dbo].[tsp_SearchRequestBySkrut] (?,?)}");
                callProc.setString(1, searchingField.getText());
                callProc.setString(2, searchingField.getText());
                callProc.execute();
                refreshData();
            }catch (SQLException ex){
                ex.printStackTrace();

            }
        });*/


        btn_СancelRequest.setOnAction(event -> {
            try {
                chosenAccount = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());
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
                }

            } else return;
        });


        btn_UpdateStatus.setOnAction(event -> {
            try {
                chosenAccount = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            // System.out.println(a +  b+c + "ed2wewgweeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
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
                }
            } else return;
        });


/*tableRequestOffering.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
    @Override
    public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
        index.set(data.indexOf(newValue));
        System.out.println("OK index is:" + data.indexOf(newValue));

    }


});*/


        createTableColumns();
        tableViewHandles();
        tableRequestOffering.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableRequestOffering);


        // customizeScene();
        //tableRequestOffering.getSelectionModel().setCellSelectionEnabled(true);
        tableRequestOffering.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

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

        UsefulUtils.installCopyPasteHandler(tableRequestOffering);
        //  UsefulUtils.copySelectedCell(tableRequestOffering);


        MenuItem item = new MenuItem("Copy");
        ContextMenu menu = new ContextMenu();
        menu.getItems().add(item);
        tableRequestOffering.setContextMenu(menu);


        try {
            CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination

            pagination.setSkin(pageSkin);
            pagination.setPageFactory(this::createPage);

        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вдається відкрити сторінки");

        }

        BorderPane root = new BorderPane();
        Text text = new Text("BEFORE");
        root.setCenter(text);
        mainMenu.getItems().addAll(textCmd, exitCmd);
        root.setTop(menuBar);


        exitCmd.setOnAction(new EventHandler() {

            @Override
            public void handle(Event event) {

            }
        });

        textCmd.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                createTableColumns();
            }
        });
        exitCmd.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
        textCmd.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));
        // searchCode();
        this.tableRequestOffering.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    //TreeItem selectedPerformer = treeTableView.getSelectionModel().getSelectedItem();

                    //chosenElement = (PerformersInGroup) selectedPerformer.getValue();
                    try {
                        //chosenElement.getPerformerNumber();

                    } catch (Exception e) {

                    }


                }
            }
        });

    /*    cmbPerformer.setOnMouseClicked(event -> {

      //      if(account.getChosenElement() != null) {
         //       cmbCustomer.getItems().add(accountDictionary.getChosenElement());
         //       cmbCustomer.getSelectionModel().select(accountDictionary.getChosenElement());
         //   }
            hboxFilter.getChildren().remove(hashMiniFilter.get(Number));
            account.setStringFilter(Number, new FilterQuery(hashColumns.get(Number), IFilterOperator.Type.CONTAINS, ((NotFulfilled) cmbPerformer.getValue()).getName()).getWhereClause());
            fillHboxFilter(Number, IFilterOperator.Type.EQUALS, ((NotFulfilled) cmbPerformer.getValue()).getName());
            refreshData();

        });*/
    }


    @FXML
    private void actionSearchingField(ActionEvent event) {

        /*String value = searchingField.getText();

        if(value.equals("")) {
            refreshData();
        }else
            findByProperty(value);*/

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
                //  options.add(rs.getString(1));
                List<NotFulfilled> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));


            }
            tableRequestOffering.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /// Skrut = (String) options.get(0);


    }
    @FXML
    private void toolbarButton (ActionEvent event) {
      //  MaterialDesign.focusedProperty((KeyEvent keyEvent)->{

     //   });
    }

    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setID(String ID) {

    }

    @Override
    public void setName(String name) {

    }


  /*  protected void initComponents() {

        components = new InitComponents(this, this);

        HashMap<Control, Method> items = new HashMap();

        Method createMFrom = null;
        Method createMTo = null;
        Method storeM = null;
        Method orderTypeM = null;
        Method orderStateM = null;
        Method orderStatusM = null;
        try {
            createMFrom = NotFulledController.class.getMethod("handleCreateDateFrom", null);
            createMTo = NotFulledController.class.getMethod("handleCreateDateTo", null);
            storeM = NotFulledController.class.getMethod("setStoreFilter", null);
            orderTypeM = NotFulledController.class.getMethod("setOrderType", null);
            orderStateM = NotFulledController.class.getMethod("setStateFilter", null);
            orderStatusM = NotFulledController.class.getMethod("handleOrderStatus", null);
        } catch (NoSuchMethodException e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }


        items.put(orderType, orderTypeM);

        components.setAllComponents(items, true);
    }*/

    @Override
    public void createTableColumns() {


        try {


            tableRequestOffering = new FilteredTableView<>();


            ID = new FilterableStringTableColumn<>();
            hashColumns.put(ID, "[tbl_RequestOffering].[ID]");

            CreatedOn = new FilterableStringTableColumn<>("Дата");
            hashColumns.put(CreatedOn, "[tbl_RequestOffering].[CreatedOn]");

            CreatedByID = new FilterableStringTableColumn<>();
            hashColumns.put(CreatedByID, "[tbl_RequestOffering].[CreatedByID]");

            CreatedBy = new FilterableStringTableColumn<>("Створив");
            hashColumns.put(CreatedBy, "[tbl_Contact].[Name]");

            AccountID = new FilterableStringTableColumn<>();
            hashColumns.put(AccountID, "[tbl_RequestOffering].[AccountID]");

            AccountName = new FilterableStringTableColumn<>("Контрагент");
            hashColumns.put(AccountName, "[tbl_Account].[Name]");

            AccountCode = new FilterableStringTableColumn<>("Код");
            hashColumns.put(AccountCode, "[tbl_Account].[Code]");

            AccountSaldo = new FilterableStringTableColumn<>("Сальдо");
            hashColumns.put(AccountSaldo, "[tbl_Account].[SaldoSel]");

            AccountIsSolid = new FilterableStringTableColumn<>("Солідність");
            hashColumns.put(AccountIsSolid, "(CASE\n" +
                    "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                    "    THEN ''\n" +
                    "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                    "    THEN 'Не солідний'\n" +
                    "    ELSE ''\n" +
                    "END)");

            Number = new FilterableStringTableColumn<>("Номер запиту");
            hashColumns.put(Number, "[tbl_RequestOffering].[Number]");

            StatusID = new FilterableStringTableColumn<>();
            hashColumns.put(StatusID, "[tbl_RequestOffering].[StatusID]");

            Status = new FilterableStringTableColumn<>("Статус");
            hashColumns.put(Status, "[tbl_RequestOfferingStatus].[Name]");

            StoreCityID = new FilterableStringTableColumn<>();
            hashColumns.put(StoreCityID, "[tbl_RequestOffering].[StoreCityID]");

            StoreCity = new FilterableStringTableColumn<>("Місто поставки");
            hashColumns.put(StoreCity, "[tbl_StoreCity].[Name]");

            OfferingGroupID = new FilterableStringTableColumn<>();
            hashColumns.put(OfferingGroupID, "[tbl_RequestOffering].[OfferingGroupID]");

            OfferingGroupName = new FilterableStringTableColumn<>("Група товарів");
            hashColumns.put(OfferingGroupName, "[OfferingGroup].[Name]");

            OriginalGroupID = new FilterableStringTableColumn<>();
            hashColumns.put(OriginalGroupID, "[tbl_RequestOffering].[OriginalGroupID]");

            OriginalGroupName = new FilterableStringTableColumn<>("Початкова група");
            hashColumns.put(OriginalGroupName, "SELECT\n" +
                    "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n");

            IsNewMessage = new FilterableStringTableColumn<>();
            hashColumns.put(IsNewMessage, "[tbl_RequestOffering].[IsNewMessage]");

            JointAnnulment = new FilterableStringTableColumn<>();
            hashColumns.put(JointAnnulment, "[tbl_RequestOffering].[JointAnnulment]");

            Note = new FilterableStringTableColumn<>();
            hashColumns.put(Note, "[tbl_RequestOffering].[Note]");

            LastMessage = new FilterableStringTableColumn<>();
            hashColumns.put(LastMessage, "[tbl_RequestOffering].[LastMessage]");

            GroupChangedByID = new FilterableStringTableColumn<>();
            hashColumns.put(GroupChangedByID, "[tbl_RequestOffering].[GroupChangedByID]");

            GroupChangedBy = new FilterableStringTableColumn<>("Змінив групу");
            hashColumns.put(GroupChangedBy, "SELECT\n" +
                    "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                    "\tFROM\n" +
                    "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n");

            IsReadMessage = new FilterableStringTableColumn<>();
            hashColumns.put(IsReadMessage, "[tbl_RequestOffering].[IsReadMeassage]");

            SpecialMarginTypeID = new FilterableStringTableColumn<>();
            hashColumns.put(SpecialMarginTypeID, "[tbl_Account].[SpecialMarginTypeID]");

            SpecialMarginTypeName = new FilterableStringTableColumn<>("Тип спец націнки");
            hashColumns.put(SpecialMarginTypeName, "[SMT].[Name]");

            StateID = new FilterableStringTableColumn<>();
            hashColumns.put(StateID, "[tbl_RequestOffering].[StateID]");

            StateName = new FilterableStringTableColumn<>();
            hashColumns.put(StateName, "[tbl_RequestOfferingState].[Name]");


            /*TableColumn<NotFulfilled, String> Number = new TableColumn<NotFulfilled, String>("Номер запиту");
            TableColumn<NotFulfilled, String> CreatedOn = new TableColumn<NotFulfilled, String>("Дата");
            TableColumn<NotFulfilled, String> CreatedBy = new TableColumn<NotFulfilled, String>("Створив");
            TableColumn<NotFulfilled, String> AccountCode = new TableColumn<NotFulfilled, String>("Код");
            TableColumn<NotFulfilled, String> AccountName = new TableColumn<NotFulfilled, String>("Контрагент");
            TableColumn<NotFulfilled, String> AccountSaldo = new TableColumn<NotFulfilled, String>("Сальдо");
            TableColumn<NotFulfilled, String> AccountIsSolid = new TableColumn<NotFulfilled, String>("Солідність");
            TableColumn<NotFulfilled, String> StoreCity = new TableColumn<NotFulfilled, String>("Місто поставки");
            TableColumn<NotFulfilled, String> Status = new TableColumn<NotFulfilled, String>("Статус");
            TableColumn<NotFulfilled, String> OfferingGroupName = new TableColumn<NotFulfilled, String>("Група товарів");
            TableColumn<NotFulfilled, String> OriginalGroupName = new TableColumn<NotFulfilled, String>("Початкова група");
            TableColumn<NotFulfilled, String> GroupChangedBy = new TableColumn<NotFulfilled, String>("Змінив групу");
            TableColumn<NotFulfilled, String> SpecialMarginTypeName = new TableColumn<NotFulfilled, String>("Тип спец націнки");*/

            // number.setVisible(false);
            // tableRequestOffering.setTableMenuButtonVisible(true);

            Number.setMinWidth(150);
            Number = new FilterableStringTableColumn<>("Номер запиту");
            //FilterableStringTableColumnNumber);
            //    id.setVisible(false);

            tableRequestOffering.getColumns().addAll(
                    Number,
                    CreatedOn,
                    CreatedBy,
                    AccountCode,
                    AccountName,
                    AccountSaldo,
                    AccountIsSolid,
                    StoreCity,
                    Status,
                    OfferingGroupName,
                    OriginalGroupName,
                    GroupChangedBy,
                    SpecialMarginTypeName);


            List<?> listColumns = tableRequestOffering.getColumns();

      //      listColumns.forEach(item -> System.out.println("COLUMN " + item));
         //   hashColumns.forEach((k, v) -> System.out.println(" COLUMN1 " + k + ' ' + v));

            Number.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("Number"));
            CreatedOn.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("CreatedOn"));
            CreatedBy.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("CreatedBy"));
            AccountCode.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountCode"));
            AccountName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountName"));
            AccountSaldo.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountSaldo"));
            AccountIsSolid.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("AccountIsSolid"));
            StoreCity.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("StoreCity"));
            Status.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("Status"));


            OfferingGroupName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("OfferingGroupName"));
            OriginalGroupName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("OriginalGroupName"));
            GroupChangedBy.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("GroupChangedBy"));
            SpecialMarginTypeName.setCellValueFactory(new PropertyValueFactory<NotFulfilled, String>("SpecialMarginTypeName"));


            //FilterableIntegerTableColumn

            tableRequestOffering.setRowFactory(new Callback<TableView<NotFulfilled>, TableRow<NotFulfilled>>() {
                @Override
                public TableRow<NotFulfilled> call(TableView<NotFulfilled> tableView) {
                    final TableRow<NotFulfilled> row = new TableRow<>();
                    contextMenu = new ContextMenu();


                    return row;
                }
            });
            tableRequestOffering.addEventHandler(ColumnFilterEvent.FILTER_CHANGED_EVENT, new EventHandler<ColumnFilterEvent>() {
                @Override
                public void handle(ColumnFilterEvent t) {
                    //  data.clear();
                    //System.out.println("Filtered column count: " + tableView.getFilteredColumns().size());
                    System.out.println("Filtering changed on column: " + t.sourceColumn().getText());
                    System.out.println("Current filters on column " + t.sourceColumn().getText() + " are:");


                    new MiniFilter(NotFulledController.this, account, hashColumns, t).setFilter();
                    //  refreshData();
                }
            });


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
        try {
            tableRequestOffering.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {

                    if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                        try {
                            chosenAccount = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());
                            EditController editController = new EditController(chosenAccount, false);
                        } catch (Exception e) {
                            log.log(Level.SEVERE, "Exception: " + e);
                        }
                    }
                }
            });


            tableRequestOffering.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            borderPane.setCenter(tableRequestOffering);

            //    tableRequestOffering.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            //  borderPane.setCenter(tableRequestOffering);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }
        tableRequestOffering.setTableMenuButtonVisible(true);
        UsefulUtils.installCopyPasteHandler(tableRequestOffering);


        // customizeScene();
        //tableRequestOffering.getSelectionModel().setCellSelectionEnabled(true);
        tableRequestOffering.getStylesheets().add
                (NotFulledController.class.getResource("/styles/TableStyle.css").toExternalForm());
        tableRequestOffering.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        UsefulUtils.installCopyPasteHandler(tableRequestOffering);

    }

    @Override
    public void createTableColumnsAll() {

    }

    @Override
    public void disableFilter(TableColumn column, Pane content) {
        account.removeStringFilter(column);
        ((AbstractFilterableTableColumn) column).getFilterEditor().getFilterMenu().getResetButton().fire();
        refreshData();


        //column.filteredProperty().setValue(true);

        if (content == null) {
            removeFilterFromHbox(column);
            return;
        }

        hboxFilter.getChildren().remove(content);


        System.out.println("DISABLED");
    }
  /*  public NotFulledController() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                    "/sample/ua/ucountry/MainWindow/TabReports/Workers/STOSalaryMain/STOSalaryWindow.fxml"));
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);

            try {
                Parent content = fxmlLoader.load();
                //content.getStylesheets().add("sample/ua/ucountry/MainTables/Account/MainDictionaryTable.css");
            } catch (IOException exception) {
                log.log(Level.SEVERE, "Exception:" + exception);
                throw new RuntimeException(exception);
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }*/


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

    @Override
    public void loadDataFromDatabaseAll() {

    }

    /* @Override
     public void disableFilter(TableColumn column, Pane content) {


         account.removeStringFilter(column);
         refreshData();

         //column.filteredProperty().setValue(true);

         hboxFilter.getChildren().remove(content);
         System.out.println("DISABLED");
     }
 */
    /* private void fillHboxFilter(TableColumn column, IFilterOperator.Type type, Object value) {
         FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                 ""));

         Pane content;
         try {
             content = fxmlLoader.load();



             MiniFilterController miniC = fxmlLoader.getController();

             miniC.setWindow(this, content);
             miniC.setFilter(column, type, value);

             hboxFilter.getChildren().add(content);

             hboxFilter.setMargin(content, new Insets(0,0,0,10));

             System.out.println("SETTTED");
             //content.getStylesheets().add("sample/ua/ucountry/MainTables/Account/MainDictionaryTable.css");
         } catch (IOException exception) {

             throw new RuntimeException(exception);
         }
     }
 */
    public void loadDataFromDatabase() {
        try {


            List<NotFulfilled> listItems = account.findAllNotFulled(true, (int) toIndex, User.getContactID(), User.getContactID());

            listItems.forEach(item -> data.add(item));

            tableRequestOffering.setItems(data);


        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
      //  return null;
    }


    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainNotFulledCount());


            loadDataFromDatabase();


            tableRequestOffering.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return anchorPane;
    }

    private void searchCode() {
        FilteredList<OfferingRequest> filteredData = new FilteredList<OfferingRequest>(exampleProductTableController.data, e -> true);
        searchingField.setOnKeyReleased(e -> {
            searchingField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super OfferingRequest>) user -> {


                    if (newValue == null || newValue.isEmpty()) {

                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    if (user.getSkrut().contains(newValue)) {
                        return true;
                    } else if (user.getSkrut().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;

                });
            });
            SortedList<OfferingRequest> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(tableRequestOffering.comparatorProperty());
            tableRequestOffering.setItems(sortedData);

        });
    }


    public void tableViewHandles() {
        tableRequestOffering.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableRequestOffering.setOnKeyReleased(eventKey -> fixSelectedRecord());
    }

    public void fixSelectedRecord() {
        NotFulfilled record = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());

        System.out.println(record);
        exampleProductTableController.handleTableView(record);

    }


    @FXML
    CreateRequest createRequest;

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

        // Show the scene containing the root layout.
        Scene scene = new Scene(serverrLayout);
        EditController con = loader.getController();
        // con.setOfferingRequest(value);
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


        loader.setLocation(ProductAddNewController.class.getResource("/views/CreateView.fxml"));
        CreateRequest createRequest = loader.getController();
        Pane serverrLayout = loader.load();
        // Show the scene containing the root layout.

        Scene scene = new Scene(serverrLayout);
        CreateRequest con = loader.getController();
        con.setOfferingRequest(this);

        stage.setScene(scene);
        ResizeHelper.addResizeListener(stage);
        stage.setMaxHeight(480);
        stage.setMaxWidth(620);
        //     main.changeExists();
        stage.show();
        stage.requestFocus();
        //refreshData();


    }

    OfferingRequest offeringRequest;

    @FXML
    private void actionAddButton(ActionEvent ev) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            NotFulfilled record = null;
            try {
                record = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());
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
            chosenAccount = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного запиту");
            return;
        }

        EditController editController = new EditController(chosenAccount, false);
    }

    @FXML
    public void handleDelete(ActionEvent e) {

        try {
            selectedOffering = (NotFulfilled) tableRequestOffering.getItems().get(tableRequestOffering.getSelectionModel().getSelectedIndex());

        } catch (Exception ex) {
            UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
            return;
        }
        if (UsefulUtils.showConfirmDialog("Ви дійсно хочете видалити запис?") == ButtonType.OK) {
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

    public void refreshData() {
        data.clear();
        loadDataFromDatabase();
        UsefulUtils.fadeTransition(tableRequestOffering);
    }

    @Override
    public void update() {
        refreshData();
    }
}
