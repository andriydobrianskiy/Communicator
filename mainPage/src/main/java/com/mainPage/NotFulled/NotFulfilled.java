package com.mainPage.NotFulled;

import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NotFulfilled implements  NotFulledInterface {
        private static Logger log = Logger.getLogger(NotFulfilled.class.getName());
        private QueryRunner dbAccess = new QueryRunner();
        private NotFulledQuery accountQueries1 = new NotFulledQuery();
        private static final List EMPTYLIST = new ArrayList<>();
        private  static final String EMPTYSTRING = "";
    private HashMap<TableColumn, String> mapFilters = new HashMap();

        public static List<Object> getDictionaryMap() { // Initialization listview labels
            List<Object> list = new ArrayList<Object>();


            list.add(new NotFulledController());
            //  list.add(new ServicesTable());
            //list.add(new OfferingsTable());
            // list.add(new AccountVehicleTable());

            return list;

        }
        public NotFulfilled (){

        }
    private String ID;
    private String OutputGroupID;
    private String GroupID;
    private String Number;
    private String Date;
    private String CreatedOn;
    private String CreatedBy;
    private String CreatedByID;
    private String AccountCode;
    private String AccountID;
    private String AccountName;
    private String AccountSaldo;
    private String AccountIsSolid;
    private String StoreCityID;
    private String StoreCity;
    private String Status;
    private String StatusID;
    private String OfferingGroupID;
    private String OfferingGroupName;
    private String StateID;
    private String RequestID;
    private String OriginalGroupName;
    private String OriginalGroupID;
    private String IsNewMessage;
    private String JointAnnulment;
    private String Note;
    private String LastMessage;
    private String GroupChangedBy;
    private String GroupChangedByID;
    private String IsReadMessage;
    private String SpecialMarginTypeID;
    private String SpecialMarginTypeName;
    private String StateName;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getIsNewMessage() {
        return IsNewMessage;
    }

    public void setIsNewMessage(String isNewMessage) {
        IsNewMessage = isNewMessage;
    }

    public String getJointAnnulment() {
        return JointAnnulment;
    }

    public void setJointAnnulment(String jointAnnulment) {
        JointAnnulment = jointAnnulment;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getGroupChangedByID() {
        return GroupChangedByID;
    }

    public void setGroupChangedByID(String groupChangedByID) {
        GroupChangedByID = groupChangedByID;
    }

    public String getIsReadMessage() {
        return IsReadMessage;
    }

    public void setIsReadMessage(String isReadMessage) {
        IsReadMessage = isReadMessage;
    }

    public String getSpecialMarginTypeID() {
        return SpecialMarginTypeID;
    }

    public void setSpecialMarginTypeID(String specialMarginTypeID) {
        SpecialMarginTypeID = specialMarginTypeID;
    }

    /* public NotFulfilled(String number, String createdOn, String createdBy, String accountCode, String accountName, String accountSaldo, String accountIsSolid, String storeCity, String status, String offeringGroupName, String originalGroupName, String groupChangedBy, String specialMarginTypeName) {
            Number = number;
            CreatedOn = createdOn;
            CreatedBy = createdBy;
            AccountCode = accountCode;
            AccountName = accountName;
            AccountSaldo = accountSaldo;
            AccountIsSolid = accountIsSolid;
            StoreCity = storeCity;
            Status = status;
            OfferingGroupName = offeringGroupName;
            OriginalGroupName = originalGroupName;
            GroupChangedBy = groupChangedBy;
            SpecialMarginTypeName = specialMarginTypeName;
        }

        public NotFulfilled(String ID, String number, String createdOn, String createdBy, String accountCode, String accountName, String accountSaldo, String accountIsSolid, String storeCity, String status, String offeringGroupName, String originalGroupName, String groupChangedBy, String specialMarginTypeName) {
            this.ID = ID;
            this.Number=number;
            this.CreatedOn=createdOn;
            this.CreatedBy=createdBy;
            this.AccountCode=accountCode;
            this.AccountName=accountName;
            this.AccountSaldo=accountSaldo;
            this.AccountIsSolid=accountIsSolid;
            this.StoreCity=storeCity;
            this.Status=status;
            this.OfferingGroupName=offeringGroupName;
            this.OriginalGroupName=originalGroupName;
            this.GroupChangedBy=groupChangedBy;
            this.SpecialMarginTypeName=specialMarginTypeName;
        }*/
   public String getGroupID() {
       return GroupID;
   }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getOutputGroupID() {
        return OutputGroupID;
    }

    public void setOutputGroupID(String outputGroupID) {
        OutputGroupID = outputGroupID;
    }

    public String getNumber() {
        return Number;
    }
    public  String getID() { return ID; }

    @Override
    public String getName() {
        return null;
    }


    public void setID(String ID) { this.ID = ID;}

    @Override
    public void setName(String name) {

    }

    @Override
    public List<? extends NotFulledInterface> findByProperty1(Object value, Enum<? extends SearchType> searchType) {
        return null;
    }

    public String getCreatedByID() {
        return CreatedByID;
    }

    public void setCreatedByID(String createdByID) {
        CreatedByID = createdByID;
    }

    @Override
    public List<NotFulfilled> findAllNotFulled(boolean pagination, int rowIndex, String createdByID, String offeringGroupID) {
        String query = (pagination ? accountQueries1.getMainNotFulled(true, rowIndex, createdByID, offeringGroupID) : accountQueries1.getMainNotFulled(false, 0, createdByID, offeringGroupID));
        try {
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<NotFulfilled>(NotFulfilled.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }

    public void setNumber(String number) {
        Number=number;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn=createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy=createdBy;
    }

    public String getAccountCode() {
        return AccountCode;
    }

    public void setAccountCode(String accountCode) {
        AccountCode=accountCode;
    }

    public  String getAccountID (){return AccountID;}

    public  void setAccountID (String accountID) { AccountID=accountID; }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName=accountName;
    }

    public String getAccountSaldo() {
        return AccountSaldo;
    }

    public void setAccountSaldo(String accountSaldo) {
        AccountSaldo=accountSaldo;
    }

    public String getAccountIsSolid() {
        return AccountIsSolid;
    }

    public void setAccountIsSolid(String accountIsSolid) {
        AccountIsSolid=accountIsSolid;
    }

    public String getStoreCity() {
        return StoreCity;
    }

    public void setStoreCity(String storeCity) {
        StoreCity=storeCity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status=status;
    }

    public String getStatusID () {return StatusID;}

    public void setStatusID (String statusID) { StatusID = statusID;}

    public String getStoreCityID () {return StoreCityID;}

    public void setStoreCityID (String storeCityID) { StoreCityID = storeCityID; }

    public String getOfferingGroupID () {return OfferingGroupID;}

    public void setOfferingGroupID (String offeringGroupID) {OfferingGroupID = offeringGroupID;}

    public String getOfferingGroupName() {
        return OfferingGroupName;
    }

    public void setOfferingGroupName(String offeringGroupName) {
        OfferingGroupName=offeringGroupName;
    }

    public String getOriginalGroupID() {
        return OriginalGroupID;
    }

    public void setOriginalGroupID(String originalGroupID) {
        OriginalGroupID=originalGroupID;
    }

    public String getStateName() {
        return StateName;
    }

    public void setStateName(String stateName) {
        StateName=stateName;
    }

    public String getStateID () {return StateID;}

    public void  setStateID (String stateID) {StateID = stateID;}

    public String getRequestID () { return RequestID;}

    public void  setRequestID (String requestID) { RequestID = requestID;}

    public String getOriginalGroupName() {
        return OriginalGroupName;
    }

    public void setOriginalGroupName(String originalGroupName) {
        OriginalGroupName=originalGroupName;
    }

    public String getGroupChangedBy() {
        return GroupChangedBy;
    }

    public void setGroupChangedBy(String groupChangedBy) {
        GroupChangedBy=groupChangedBy;
    }

    public String getSpecialMarginTypeName() {
        return SpecialMarginTypeName;
    }

    public void setSpecialMarginTypeName(String specialMarginTypeName) {
        SpecialMarginTypeName=specialMarginTypeName;
    }




    public void setStringFilter(TableColumn column, String value) {
        mapFilters.put(column, value);
    }

    public String getStringFilter() {
        StringBuilder builder = new StringBuilder("");
        try {
            mapFilters.forEach((k, v) ->
                    builder.append(v));
        } catch (NullPointerException e) {

        }

        System.out.println(builder.toString());
        return builder.toString();
    }

    public void removeStringFilter(TableColumn key) {
        mapFilters.remove(key);

    }



    @Override
    public String toString() {
        return getID();
    }




}
