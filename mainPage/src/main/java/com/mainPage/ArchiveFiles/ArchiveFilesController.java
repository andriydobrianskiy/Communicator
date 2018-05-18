package com.mainPage.ArchiveFiles;

import com.Utils.CustomPaginationSkin;
import com.Utils.UsefulUtils;
import com.client.chatwindow.ChatController;
import com.connectDatabase.DBConnection;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.login.User;
import com.mainPage.ArchiveFiles.ArchiveFilesRequest.ArchiveFilesRequestController;
import com.mainPage.NotFulled.ProductAdd.ObserverNF;
import com.mainPage.page.MainPageController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchiveFilesController extends BorderPane implements Initializable, DictionaryPropertiesArchivesFiles, ObserverNF {
    @FXML
    private ArchiveFilesRequestController archiveFilesRequestViewController;
    private MainPageController main;
    private static Logger log = Logger.getLogger(ArchiveFilesController.class.getName());
    /*  @FXML
      private TableView tableView;*/
    @FXML
    private BorderPane borderPane;
    private ArchiveFiles account = new ArchiveFiles();
    private ObservableList<ArchiveFiles> data;
    private long fromIndex;
    private long toIndex;
    private ArchiveFilesQuery accountQueries = new ArchiveFilesQuery();
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    @FXML
    private Pagination pagination;
    @FXML
    public TableView tableView;
    @FXML
    private JFXButton btn_ReturnInProcessing;
    @FXML
    private ChatController chatViewController;
    @FXML
    private SplitPane splitPane1;
    @FXML
    private JFXTextField searchingField;
    public ArchiveFiles chosenAccount;

    public ChatController conn;

    private double xOffset;
    private double yOffset;
    private Scene scene;
   /* @FXML TableColumn<?,?> colunmNumber;
    @FXML TableColumn<?,?> columnCreatedOn;
    @FXML TableColumn<?,?> columnCreatedBy;
    @FXML TableColumn<?,?> columnAccountCode;
    @FXML TableColumn<?,?> columnAccountName;*/

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        archiveFilesRequestViewController.init(this);
        chatViewController.init(this);
        splitPane1.setDividerPositions(0.75);
        createTableColumns();
        loadDataFromDatabase();
        tableViewHandles();

        CustomPaginationSkin pageSkin = new CustomPaginationSkin(pagination); // custom pagination
        try {
            con = DBConnection.getDataSource().getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pagination.setSkin(pageSkin);
        pagination.setPageFactory(this::createPage);

        tableView.getSelectionModel().setCellSelectionEnabled(false);
        tableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        btn_ReturnInProcessing.setOnAction(event -> {

            try {

                chosenAccount = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
                System.out.println(chosenAccount.getID() + "8888888888888888888888888885555555555555555555555555555566666666666666666666666");
            } catch (Exception ex) {
                UsefulUtils.showErrorDialogDown("Не вибрано жодного елемента з таблиці!");
                return;
            }
            if (UsefulUtils.showConfirmDialog("Ви бажаєте перевести в обробку?") == ButtonType.OK) {
                String query = ("");
                try {
                    pst = con.prepareStatement(query);
                    // pst.setString(1, User.getContactID());
                    pst.setString(1, chosenAccount.getID());
                    pst.setString(2, User.getContactID());
                    pst.setString(3, chosenAccount.getID());
                    pst.executeUpdate();
                    main.changeExists();
                    UsefulUtils.showSuccessful("Запит " + chosenAccount.getNumber() + " завершено");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else return;
        });


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
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
                    try {


                        //  NotesInProcessingController notesInProcessingController = new NotesInProcessingController(chosenAccount, false);

                        //  ClientController.recordInTract = chosenAccount;
                        //     System.out.println(chosenAccount.getID() + "5649848949849845316546854684896");
                        //     ClientController clientController = new ClientController(chosenAccount, false);
                    } catch (Exception e) {
                        log.log(Level.SEVERE, "Exception: " + e);
                    }
                }
            }
        });
    }

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
                List<ArchiveFiles> listItems = account.findSearchSkrut(false, (int) toIndex, User.getContactID(), User.getContactID(), Skrut);
                listItems.forEach(item -> data.add(item));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        /// Skrut = (String) options.get(0);


        tableView.setItems(data);


    }

    public void loginButtonAction(ArchiveFiles chosenAccount) throws IOException {
        // String hostname = "192.168.0.100";
        //   int port = 9001;
        //     String username = User.getContactName();
        //     String picture = "Default";


        //  FXMLLoader fmxlLoader = new FXMLLoader(getClass().getResource("/views/ChatView.fxml"));

        //  Parent window = (Pane) fmxlLoader.load();
        //       conn = fmxlLoader.<ChatController>getController();
        //  instance = fmxlLoader.<InProcessingController>getController();
        chatViewController.setOfferingArchive(chosenAccount);
        chatViewController.setArchiveController(this);
        chatViewController.splitPane.setDividerPositions(1);
        //this.scene = new Scene(window);
        //   Stage stage = new Stage();
        //   stage.initModality(Modality.APPLICATION_MODAL);
        // (Stage) hostnameTextfield.getScene().getWindow();
        //   stage.getScene();
//stage.showAndWait();
        //    stage.setResizable(true);
        //      stage.setWidth(1040);
        //   stage.setHeight(620);

        //        stage.setOnCloseRequest((WindowEvent e) -> {
        //           Platform.exit();
        //       System.exit(0);
        //    });
        //  stage.setScene(this.scene);

        //  stage.setMinWidth(800);
        //   stage.setMinHeight(300);

        //  ResizeHelper.addResizeListener(stage);
        //      stage.showAndWait();
        //  System.out.println("chatFive");

        chatViewController.setUsernameLabel(User.getContactName());
        //   conn.setImageLabel("Default");
        //   conn.setIdTextFild(chosenAccount.getID());
        //   stage.showAndWait();
        //  Listener listener = new Listener(hostname, port, username, picture, conn, this, chosenAccount);
        //   Thread x = new Thread(listener);
        //   x.start();


    }


    public void init(MainPageController mainPageController) {
        main = mainPageController;
    }


 /*   private void setCellTable() {
        colunmNumber.setCellValueFactory(new PropertyValueFactory<>("Number"));
        columnCreatedOn.setCellValueFactory(new PropertyValueFactory<>("CreatedOn"));
        columnCreatedBy.setCellValueFactory(new PropertyValueFactory<>("CreatedBy"));
        columnAccountCode.setCellValueFactory(new PropertyValueFactory<>("AccountCode"));
        columnAccountName.setCellValueFactory(new PropertyValueFactory<>("AccountName"));
    }

    private StringBuilder query;
    public List<ArchiveFilesTwoo> loadDataFromDatabase(){
        try {

            query = new StringBuilder();
            data = FXCollections.observableArrayList();
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainArchiveFilesCount());




            // if(top) query.append( rowIndex + "\n");
            System.out.println(toIndex+" 3333333333333333333333333333333333");
            //  System.out.println(rowIndex+" 8888888888888888888888888888888888888888888888888888888");
            try {
                pst = con.prepareStatement("SELECT \n"+
                        "TOP "+this.toIndex+"\n"+
                        "[tbl_RequestOffering].[ID] AS [ID],\n" +
                        "\t[tbl_RequestOffering].[CreatedOn] AS [CreatedOn],\n" +
                        "\t[tbl_RequestOffering].[CreatedByID] AS [CreatedByID],\n" +
                        "\t[tbl_Contact].[Name] AS [CreatedBy],\n" +
                        "\t[tbl_RequestOffering].[AccountID] AS [AccountID],\n" +
                        "\t[tbl_Account].[Name] AS [AccountName],\n" +
                        "\t[tbl_Account].[Code] AS [AccountCode],\n" +
                        "\t[tbl_Account].[SaldoSel]\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "/*( \n" +
                        "(ISNULL((SELECT\n" +
                        "     SUM(ISNULL(CashCredit.RemainPaid, 0))\n" +
                        " FROM \n" +
                        "     tbl_Cashflow as CashCredit\n" +
                        " WHERE \n" +
                        "    (CashCredit.TypeID = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                        "     CashCredit.DestinationID <> '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' AND\n" +
                        "     CashCredit.PayerID = tbl_Account.ID AND\n" +
                        "     CashCredit.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')),0)\n" +
                        ")\n" +
                        "-\n" +
                        "(ISNULL((SELECT\n" +
                        "     SUM(ISNULL(CashPromice.RemainPaid, 0))\n" +
                        " FROM \n" +
                        "     tbl_Cashflow as CashPromice\n" +
                        "WHERE (\n" +
                        "    (CashPromice.TypeID = '{484C8429-DABF-482A-BC7B-4C75D1436A1B}' AND \n" +
                        "     NOT CashPromice.DestinationID IN('{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}', '{0D6BE797-DDE0-477C-AE28-978D2C2C1EFD}') AND\n" +
                        "     CashPromice.RecipientID = tbl_Account.ID AND\n" +
                        "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}') \n" +
                        "     OR\n" +
                        "    (CashPromice.TypeID = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND \n" +
                        "     CashPromice.DestinationID = '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' AND\n" +
                        "     CashPromice.PayerID = tbl_Account.ID AND\n" +
                        "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')\n" +
                        "    )), 0) - \n" +
                        "  \n" +
                        "ISNULL((SELECT\n" +
                        "     SUM(ISNULL(CashPromice.RemainPaid, 0))\n" +
                        " FROM \n" +
                        "     tbl_Cashflow as CashPromice\n" +
                        "WHERE\n" +
                        "    (CashPromice.TypeID = '{484C8429-DABF-482A-BC7B-4C75D1436A1B}' AND \n" +
                        "     (CashPromice.DestinationID = '{D84B87E0-5A56-4318-B0AB-B0FBD95F50E1}' OR\n" +
                        "     CashPromice.DestinationID = '{0D6BE797-DDE0-477C-AE28-978D2C2C1EFD}') AND\n" +
                        "     CashPromice.RecipientID = tbl_Account.ID AND\n" +
                        "     CashPromice.StatusID = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}')),0)\n" +
                        ")\n" +

                        "\t(CASE\n" +
                        "    WHEN CONVERT(DATETIME,ISNULL([tbl_Account].[UnblockDate], '2000-01-01')) > CONVERT(DATETIME, CURRENT_TIMESTAMP) \n" +
                        "    THEN ''\n" +
                        "    WHEN [tbl_Account].[IsSolid] = 1\n" +
                        "    THEN 'Не солідний'\n" +
                        "    ELSE ''\n" +
                        "END)\n" +
                        "\n" +
                        "\n" +
                        "/*CASE WHEN [tbl_Account].[UnblockDate] > GETDATE() THEN\n" +
                        "\t(SELECT TOP 1\n" +
                        "\t\tN'Не солидный' AS [Exist]\n" +
                        "\tFROM\n" +
                        "\t\ttbl_Cashflow AS [Cashflow]\n" +
                        "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                        "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                        "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                        "\t\t([Cashflow].[PayDate] < (CAST(FLOOR(CAST(GETDATE() AS FLOAT)) AS DATETIME)))) \n" +
                        "                   AND [tbl_Account].[UnblockDate] < GETDATE()))\n" +
                        "\tELSE \n" +
                        "\t(SELECT TOP 1\n" +
                        "\t\tN'Не солидный' AS [Exist]\n" +
                        "\tFROM\n" +
                        "\t\ttbl_Cashflow AS [Cashflow]\n" +
                        "\tWHERE(([tbl_Account].[ID] = [Cashflow].[PayerID] AND\n" +
                        "\t\t[Cashflow].[StatusID] = '{D7141996-2996-4BB2-BCCD-E422A54AA02E}' AND\n" +
                        "\t\t[Cashflow].[TypeID] = '{358DA0CD-9EA6-43B8-A099-CD77DA3C6114}' AND\n" +
                        "\t\t([Cashflow].[PayDate] < GETDATE()))))\n" +
                        "END*//*
                        "\t[tbl_RequestOffering].[Number] AS [Number],\n" +
                        "\t[tbl_RequestOffering].[StatusID] AS [StatusID],\n" +
                        "\t[tbl_RequestOfferingStatus].[Name] AS [Status],\n" +
                        "\t[tbl_RequestOffering].[StoreCityID] AS [StoreCityID],\n" +
                        "\t[tbl_StoreCity].[Name] AS [StoreCity],\n" +
                        "\t[tbl_RequestOffering].[OfferingGroupID] AS [OfferingGroupID],\n" +
                        "\t[OfferingGroup].[Name] AS [OfferingGroupName],\n" +
                        "\t[tbl_RequestOffering].[OriginalGroupID] AS [OriginalGroupID],\n" +
                        "\t(SELECT\n" +
                        "\t\t[OriginalGroupName].[Name] AS [Name]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_Contact] AS [OriginalGroupName]\n" +
                        "\tWHERE([tbl_RequestOffering].[OriginalGroupID] = [OriginalGroupName].[ID])) AS [OriginalGroupName],\n" +
                        "\t[tbl_RequestOffering].[IsNewMessage] AS [IsNewMessage],\n" +
                        "\t[tbl_RequestOffering].[JointAnnulment] AS [JointAnnulment],\n" +
                        "\t[tbl_RequestOffering].[Note] AS [Note],\n" +
                        "\t[tbl_RequestOffering].[LastMessage] AS [LastMessage],\n" +
                        "\t[tbl_RequestOffering].[GroupChangedByID] AS [GroupChangedByID],\n" +
                        "\t(SELECT\n" +
                        "\t\t[GroupChangedBy].[Name] AS [Name]\n" +
                        "\tFROM\n" +
                        "\t\t[dbo].[tbl_Contact] AS [GroupChangedBy]\n" +
                        "\tWHERE([tbl_RequestOffering].[GroupChangedByID] = [GroupChangedBy].[ID])) AS [GroupChangedBy],\n" +
                        "\t[tbl_RequestOffering].[IsReadMeassage] AS [IsReadMeassage],\n" +
                        "\t[tbl_Account].[SpecialMarginTypeID] AS [SpecialMarginTypeID],\n" +
                        "\t[SMT].[Name] AS [SpecialMarginTypeName],\n" +
                        "\t[tbl_RequestOffering].[StateID] AS [StateID],\n" +
                        "\t[tbl_RequestOfferingState].[Name] AS [StateName],\n" +
                        "\t(CASE \n" +
                        "    WHEN  \n" +
                        "       tbl_RequestOffering.ModifiedByID = 'CE646F21-D075-47BF-A8B8-17583200AEEB'  -- autouser\n" +
                        "    THEN\n" +
                        "       1\n" +
                        "    ELSE\n" +
                        "       0\n" +
                        "END) AS [ChangeByAutouser]\n" +
                        "FROM\n" +
                        "\t[dbo].[tbl_RequestOffering] AS [tbl_RequestOffering]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_StoreCity] AS [tbl_StoreCity] ON [tbl_StoreCity].[ID] = [tbl_RequestOffering].[StoreCityID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_Account] AS [tbl_Account] ON [tbl_Account].[ID] = [tbl_RequestOffering].[AccountID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_RequestOfferingStatus] AS [tbl_RequestOfferingStatus] ON [tbl_RequestOfferingStatus].[ID] = [tbl_RequestOffering].[StatusID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_Contact] AS [tbl_Contact] ON [tbl_Contact].[ID] = [tbl_RequestOffering].[CreatedByID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_Contact] AS [OfferingGroup] ON [OfferingGroup].[ID] = [tbl_RequestOffering].[OfferingGroupID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_SpecialMarginType] AS [SMT] ON [SMT].[ID] = [tbl_Account].[SpecialMarginTypeID]\n" +
                        "LEFT OUTER JOIN\n" +
                        "\t[dbo].[tbl_RequestOfferingState] AS [tbl_RequestOfferingState] ON [tbl_RequestOfferingState].[ID] = [tbl_RequestOffering].[StateID]\n" +
                        "WHERE(([tbl_RequestOffering].[StatusID] = '{3E7F90E2-335C-41B2-828A-576A06375B8C}' OR\n" +
                        "\t[tbl_RequestOffering].[StatusID] = '{6F784E48-1474-4CB9-B28D-A4B580FB346C}') AND\n" +
                        "\t([tbl_RequestOffering].[CreatedByID] = '{8DE11B0A-01E4-4842-9F76-EDA6B0395ABD}' OR\n" +
                        "\t[tbl_RequestOffering].[OfferingGroupID] = '{8DE11B0A-01E4-4842-9F76-EDA6B0395ABD}'))\n" +
                        "ORDER BY\n" +
                        "\t10 DESC");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println(query );
            rs = pst.executeQuery();
            while (rs.next()){
                data.add(new ArchiveFilesTwoo(rs.getString(3),rs.getString(4),
                        rs.getString(5), rs.getString(6), rs.getString(7)));


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableView.setItems(data);
        tableView.setItems(FXCollections.observableArrayList(data.subList((int)fromIndex, (int)toIndex)));
        return null;
    }*/

    public void tableViewHandles() {
        tableView.setOnMouseClicked(mouseEvent -> fixSelectedRecord());
        tableView.setOnKeyReleased(eventKey -> fixSelectedRecord());
    }

    private void fixSelectedRecord() {
        ArchiveFiles record = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());

        System.out.println("lllllllll" + record);
        archiveFilesRequestViewController.handleTableView(record);

        chosenAccount = (ArchiveFiles) tableView.getItems().get(tableView.getSelectionModel().getSelectedIndex());
        try {
            loginButtonAction(chosenAccount);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTableColumns() {

        try {
            TableColumn<ArchiveFiles, String> number = new TableColumn<ArchiveFiles, String>("Номер запиту");
            TableColumn<ArchiveFiles, String> createdOn = new TableColumn<ArchiveFiles, String>("Дата");
            TableColumn<ArchiveFiles, String> createdBy = new TableColumn<ArchiveFiles, String>("Створив");
            TableColumn<ArchiveFiles, String> accountCode = new TableColumn<ArchiveFiles, String>("Код контрагента");
            TableColumn<ArchiveFiles, String> accountName = new TableColumn<ArchiveFiles, String>("Контрагент");
            TableColumn<ArchiveFiles, String> accountSaldo = new TableColumn<ArchiveFiles, String>("Сальдо");
            TableColumn<ArchiveFiles, String> accountIsSolid = new TableColumn<ArchiveFiles, String>("Солідність");
            TableColumn<ArchiveFiles, String> storeCity = new TableColumn<ArchiveFiles, String>("Місто поставки");
            TableColumn<ArchiveFiles, String> status = new TableColumn<ArchiveFiles, String>("Статус");
            TableColumn<ArchiveFiles, String> groupChangedBy = new TableColumn<ArchiveFiles, String>("Змінив групу");
            TableColumn<ArchiveFiles, String> specialMarginTypeName = new TableColumn<ArchiveFiles, String>("Тип спец націнки");
            TableColumn<ArchiveFiles, String> stateName = new TableColumn<ArchiveFiles, String>("Статус запиту");
            TableColumn<ArchiveFiles, String> chengeByAutouser = new TableColumn<ArchiveFiles, String>("Змінив автомат");
            TableColumn<ArchiveFiles, String> offeringGroupName = new TableColumn<ArchiveFiles, String>("Група товарів");
            TableColumn<ArchiveFiles, String> originalGroupName = new TableColumn<ArchiveFiles, String>("Початкова група");


            number.setVisible(false);
            //    tableView.set(true);

            number.setMinWidth(150);

            //    id.setVisible(false);
            tableView.setTableMenuButtonVisible(true);
//number.setVisible(false);
            tableView.getColumns().addAll(
                    number,
                    createdOn,
                    createdBy,
                    accountCode,
                    accountName,
                    accountSaldo,
                    accountIsSolid,
                    storeCity,
                    status,
                    groupChangedBy,
                    specialMarginTypeName,
                    stateName,
                    chengeByAutouser,
                    offeringGroupName,
                    originalGroupName);
            number.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("Number"));
            createdOn.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("CreatedOn"));
            createdBy.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("CreatedBy"));
            accountCode.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountCode"));
            accountName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountName"));
            accountSaldo.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountSaldo"));
            accountIsSolid.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("AccountIsSolid"));
            storeCity.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("StoreCity"));
            status.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("Status"));
            groupChangedBy.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("GroupChangedBy"));
            specialMarginTypeName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("SpecialMarginTypeName"));
            stateName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("StateName"));
            chengeByAutouser.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("ChangeByAutouser"));
            offeringGroupName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("OfferingGroupName"));
            originalGroupName.setCellValueFactory(new PropertyValueFactory<ArchiveFiles, String>("OriginalGroupName"));


        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception in creating columns: " + e);
        }

    }

    @Override
    public List<ArchiveFiles> loadDataFromDatabase() {
        //  System.out.println(chosenAccount.getOfferingGroupID()+ "7899999999999999999999999999989854645613256131651156" + User.getContactID());
        try {

            List<ArchiveFiles> listItems = account.findAllInArchive(true, (int) toIndex,  User.getContactID(), User.getContactID());

            listItems.forEach(item -> data.add(item));

            tableView.setItems(data);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Load data from database exception: " + e);
        }
        return null;
    }

    public BorderPane createPage(int pageIndex) {
        try {


            data = FXCollections.observableArrayList();

            fromIndex = pageIndex * 40;
            toIndex = Math.min(fromIndex + 40, accountQueries.getMainArchiveFilesCount());


            loadDataFromDatabase();


            // tableView.setItems(FXCollections.observableArrayList(data.subList((int) fromIndex, (int) toIndex)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Switch page exception: " + e);
        }


        return borderPane;
    }

    public void refreshData() {
        try {
            data.clear();
        } catch (NullPointerException ex) {

        } finally {
            loadDataFromDatabase();
        }

        UsefulUtils.fadeTransition(tableView);
    }


    @Override
    public void update() {
        refreshData();
    }
}
