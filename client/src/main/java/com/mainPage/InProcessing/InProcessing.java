package com.mainPage.InProcessing;

import com.Utils.GridComp;
import com.Utils.InProcessingIntarface;
import com.Utils.MiniFilterWindow.FilterFunctions;
import com.connectDatabase.DBConnection;
import com.mainPage.createRequest.searchCounterpart.Counterpart;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InProcessing extends GridComp implements InProcessingIntarface, FilterFunctions {
    private static Logger log = Logger.getLogger(Counterpart.class.getName());
    private QueryRunner dbAccess = new QueryRunner();
    private InProcessingQuery accountQueries1 = new InProcessingQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";
    private HashMap<String, String> resultFilterMap = new HashMap<>();
    private UUID uniqueID;

    private HashMap<TableColumn, String> mapFilters = new HashMap();
    public static List<Object> getDictionaryMap() { // Initialization listview labels
        List<Object> list = new ArrayList<Object>();


        list.add(new InProcessing()); // Контрагенти

        return list;
    }

    private String ID;
    private String Number;
    private String CreatedOn;
    private String CreatedBy;
    private String CreatedByID;
    private String AccountCode;
    private String AccountName;
    private String AccountSaldo;
    private String AccountIsSolid;
    private String StoreCity;
    private String Status;
    private String StatusID;
    private Integer IsReadMeassage;
    private String OfferingGroupID;
    private String OfferingGroupName;
    private Integer JointAnnulment;
    private String OriginalGroupName;
    private String GroupChangedBy;
    private String StateName;
    private String SpecialMarginTypeName;
    private String CashType;
    private String PricingDescription;




    private Boolean selected;

    //public boolean isSelected() {return selected.get();}
    public Boolean isSelected(){return selected;}
    public void setSelected (Boolean selected) {this.selected = selected;}
    public InProcessing() {

    }


    public InProcessing(String number, String createdOn, String createdBy, String accountCode, String accountName, String accountSaldo, String accountIsSolid, String storeCity, String status, String offeringGroupName, String originalGroupName, String groupChangedBy, String stateName, String specialMarginTypeName, Integer isReadMeassage, String cashType) {
        this.Number = number;
        this.CreatedOn = createdOn;
        this.CreatedBy = createdBy;
        this.AccountCode = accountCode;
        this.AccountName = accountName;
        this.AccountSaldo = accountSaldo;
        this.AccountIsSolid = accountIsSolid;
        this.StoreCity = storeCity;
        this.Status = status;
        this.OfferingGroupName = offeringGroupName;
        this.OriginalGroupName = originalGroupName;
        this.GroupChangedBy = groupChangedBy;
        this.StateName = stateName;
        this.SpecialMarginTypeName = specialMarginTypeName;
        this.IsReadMeassage = isReadMeassage;
        this.CashType = cashType;

    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        ID = id;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public void setCreatedByID (String createdByID) {CreatedByID = createdByID;}

    public String getCreatedByID () {return CreatedByID;}

    public String getAccountCode() {
        return AccountCode;
    }

    public void setAccountCode(String accountCode) {
        AccountCode = accountCode;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getAccountSaldo() {
        return AccountSaldo;
    }

    public void setAccountSaldo(String accountSaldo) {
        AccountSaldo = accountSaldo;
    }

    public String getAccountIsSolid() {
        return AccountIsSolid;
    }

    public void setAccountIsSolid(String accountIsSolid) {
        AccountIsSolid = accountIsSolid;
    }

    public String getStoreCity() {
        return StoreCity;
    }

    public void setStoreCity(String storeCity) {
        StoreCity = storeCity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getStatusID () {
        return this.StatusID;
    }

    public void setStatusID (String statusID) {
        this.StatusID = statusID;
    }

    public Integer getIsReadMeassage () {return this.IsReadMeassage;}

    public void setIsReadMeassage (Integer isReadMeassage){
        this.IsReadMeassage = isReadMeassage;
    }

    public String getOfferingGroupID () {return OfferingGroupID;}

    public void setOfferingGroupID (String offeringGroupID) {OfferingGroupID = offeringGroupID;}

    public String getOfferingGroupName() {
        return OfferingGroupName;
    }

    public void setOfferingGroupName(String offeringGroupName) {
        OfferingGroupName = offeringGroupName;
    }

    public String getOriginalGroupName() {
        return OriginalGroupName;
    }

    public void setOriginalGroupName(String originalGroupName) {
        OriginalGroupName = originalGroupName;
    }

    public Integer getJointAnnulment (){return JointAnnulment;}

    public void setJointAnnulment (Integer jointAnnulment){
        JointAnnulment = jointAnnulment;
    }

    public String getGroupChangedBy() {
        return GroupChangedBy;
    }

    public void setGroupChangedBy(String groupChangedBy) {
        GroupChangedBy = groupChangedBy;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName = stateName;
    }

    public String getSpecialMarginTypeName() {
        return SpecialMarginTypeName;
    }

    public void setSpecialMarginTypeName(String specialMarginTypeName) {
        SpecialMarginTypeName = specialMarginTypeName;
    }
    public String getCashType() {
        return CashType;
    }

    public void setCashType(String CashType) {
        this.CashType = CashType;
    }

    public String getPricingDescription () {return PricingDescription;}

    public void setPricingDescription(String pricingDescription) { this.PricingDescription = pricingDescription;}



    @Override
    public List<InProcessing> findAllOne(boolean pagination, int rowIndex, String createdByID, String offeringGroupID, String filterSorted, Integer pricingBoolean ) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {
        String query = (pagination ? accountQueries1.getMainInProcessing(true, rowIndex , createdByID , offeringGroupID, pricingBoolean) : accountQueries1.getMainInProcessing(false, 0, createdByID , offeringGroupID, pricingBoolean));

            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query + getStringFilter()+filterSorted, new BeanListHandler<InProcessing>(InProcessing.class)));
        } catch (SQLException e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection dbConnection = new DBConnection();
       //     dbConnection.disconnect();
            dbConnection.reconnect();
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }


    public List<InProcessing> findAllOneSearch( String requestID) {
        try (Connection connection = DBConnection.getDataSource().getConnection()){
        String query =  accountQueries1.getMainInProcessingSearchSkrut( requestID);

            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<InProcessing>(InProcessing.class)));
        } catch (SQLException e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection dbConnection = new DBConnection();
        //    dbConnection.disconnect();
            dbConnection.reconnect();
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }


    @Override
    public List<InProcessing> findAllInProcessing (boolean pagination, int rowIndex, String filterSorted, Integer pricingBoolean ){
        String queryAll = (pagination ? accountQueries1.getMainInProcessingAll(true, rowIndex, pricingBoolean) : accountQueries1.getMainInProcessingAll(false, 0, pricingBoolean));
        try (Connection connection  = DBConnection.getDataSource().getConnection()){
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(),queryAll + getStringFilter()+ filterSorted, new BeanListHandler<InProcessing>(InProcessing.class)));
        }catch (SQLException e){
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection dbConnection = new DBConnection();
          //  dbConnection.disconnect();
            dbConnection.reconnect();
        }
        return FXCollections.observableArrayList(EMPTYLIST);
    }

    public List<InProcessing> findAllInProcessingSearchSkrut (boolean pagination, int rowIndex, String requestID){
        try(Connection  connection = DBConnection.getDataSource().getConnection()){
        String queryAll = (pagination ? accountQueries1.getMainInProcessingAllSearchSkrut(true, rowIndex,requestID) : accountQueries1.getMainInProcessingAllSearchSkrut(false, 0, requestID));

            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(),queryAll, new BeanListHandler<InProcessing>(InProcessing.class)));
        }catch (SQLException e){
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection dbConnection = new DBConnection();
          //  dbConnection.disconnect();
            dbConnection.reconnect();
        }
        return FXCollections.observableArrayList(EMPTYLIST);
    }

    @Override
    public void setStringFilter(TableColumn column, String value) {
        mapFilters.put(column, value);
    }

    @Override
    public void setStringFilterMerge(TableColumn column, String value) {
        mapFilters.merge(column, value, (a, b) -> a + "\n" + b);
    }

    @Override
    public String getStringFilter() {
        StringBuilder builder = new StringBuilder("");
        try {
            mapFilters.forEach((k, v) ->
                    builder.append(v));
        } catch (NullPointerException e) {

        }


        return builder.toString();
    }

    @Override
    public void removeStringFilter(TableColumn key) {
        mapFilters.remove(key);
    }


    @Override
    public String toString() {
        return getID();
    }

}
