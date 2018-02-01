package com.mainPage.InTract;

import com.connectDatabase.DBConnection;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InTract implements InTractInterface  {
    private static Logger log = Logger.getLogger(InTract.class.getName());
    private QueryRunner dbAccess = new QueryRunner();
    private InTractQuery accountQueries1 = new InTractQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";

    public static List<Object> getDictionaryMap() { // Initialization listview labels
        List<Object> list = new ArrayList<Object>();


        list.add(new InTract()); // Контрагенти
        //  list.add(new ServicesTable()); // Послуги
        //list.add(new OfferingsTable()); // Запчастини
        // list.add(new AccountVehicleTable()); // Автомобілі

        return list;
    }

    private SimpleStringProperty ID;
    private SimpleStringProperty CreatedOn;
    private SimpleStringProperty CreatedByID;
    private SimpleStringProperty CreatedBy;
    private SimpleStringProperty AccountID;
    private SimpleStringProperty AccountName;
    private SimpleStringProperty AccountCode;
    private SimpleStringProperty AccountSaldo;
    private SimpleStringProperty AccountIsSolid;
    private SimpleStringProperty Number;
    private SimpleStringProperty StatusID;
    private SimpleStringProperty Status;
    private SimpleStringProperty StoreCityID;
    private SimpleStringProperty StoreCity;
    private SimpleStringProperty OfferingGroupID;
    private SimpleStringProperty OfferingGroupName;
    private SimpleStringProperty OriginalGroupID;
    private SimpleStringProperty OriginalGroupName;
    private SimpleStringProperty IsNewMessage;
    private SimpleStringProperty JointAnnulment;
    private SimpleStringProperty Note;
    private SimpleStringProperty LastMessage;
    private SimpleStringProperty GroupChangedByID;
    private SimpleStringProperty GroupChangedBy;
    private SimpleIntegerProperty IsReadMeassage;
    private SimpleStringProperty SpecialMarginTypeID;
    private SimpleStringProperty SpecialMarginTypeName;
    private SimpleStringProperty StateID;
    private SimpleStringProperty StateName;



    public InTract () {

    }
// Getters
    public String getID() {
        return ID.getValue();
    }

    @Override
    public String getName() {
        return null;
    }

    public String getCreatedOn () {
        return CreatedOn.getValue();
    }
    public String getCreatedByID () {
        return CreatedByID.getValue();
    }
    public String getCreatedBy () {
        return CreatedBy.getValue();
    }
    public String getAccountID () {
        return AccountID.getValue();
    }
    public String getAccountName () {
        return AccountName.getValue();
    }
    public String getAccountCode () {
        return AccountCode.getValue();
    }
    public String getAccountSaldo () {
        return AccountSaldo.getValue();
    }
    public String getAccountIsSolid () {
        return AccountIsSolid.getValue();
    }
    public String getNumber () {
        return Number.getValue();
    }
    public String getStatusID () {
        return StatusID.getValue();
    }
    public String getStatus () {
        return Status.getValue();
    }
    public String getStoreCityID () {
        return StoreCityID.getValue();
    }
    public String getStoreCity () {
        return StoreCity.getValue();
    }
    public String getOfferingGroupID () {
        return OfferingGroupID.getValue();
    }
    public String getOfferingGroupName () {
        return OfferingGroupName.getValue();
    }
    public String getIsNewMessage () {
        return IsNewMessage.getValue();
    }
    public String getJointAnnulment () {
        return JointAnnulment.getValue();
    }
    public String getNote () {
        return Note.getValue();
    }
    public String getLastMessage () {
        return LastMessage.getValue();
    }
    public String getGroupChangedByID () {
        return GroupChangedByID.getValue();
    }
    public String getGroupChangedBy () {
        return GroupChangedBy.getValue();
    }
    public Integer getIsReadMeassage () {
        return IsReadMeassage.getValue();
    }
    public String SpecialMarginTypeID () {
        return SpecialMarginTypeID.getValue();
    }
    public String getSpecialMarginTypeName () {
        return SpecialMarginTypeName.getValue();
    }
    public String getStateID () {
        return StateID.getValue();
    }
    public String getStateName () {
        return StateName.getValue();
    }

    //Setters
    public void setID (String ID) {
        this.ID = new SimpleStringProperty(ID);
    }

    @Override
    public void setName(String name) {

    }



    public void setCreatedOn (String createdOn) {
        this.CreatedOn = new SimpleStringProperty(createdOn);
    }
    public void setCreatedByID (String createdByID){
        this.CreatedByID = new SimpleStringProperty(createdByID);
    }
    public void setCreatedBy (String createdBy) {
        this.CreatedBy = new SimpleStringProperty(createdBy);
    }
    public void setAccountID (String accountID) {
        this.AccountID = new SimpleStringProperty(accountID);
    }
    public void setAccountName (String accountName) {
        this.AccountName = new SimpleStringProperty(accountName);
    }
    public void setAccountCode (String accountCode) {
        this.AccountCode = new SimpleStringProperty(accountCode);
    }
    public void setAccountSaldo (String accountSaldo) {
        this.AccountSaldo = new SimpleStringProperty(accountSaldo);
    }
    public void setAccountIsSolid (String accountIsSolid) {
        this.AccountIsSolid = new SimpleStringProperty(accountIsSolid);
    }
    public void setNumber (String number) {
        this.Number = new SimpleStringProperty(number);
    }
    public void setStatusID (String statusID) {
        this.StatusID = new SimpleStringProperty(statusID);
    }
    public void setStatus (String status) {
        this.Status = new SimpleStringProperty(status);
    }
    public void setStoreCityID (String storeCityID) {
        this.StoreCityID = new SimpleStringProperty(storeCityID);
    }
    public void setStoreCity (String storeCity) {
        this.StoreCity = new SimpleStringProperty(storeCity);
    }
    public void setOfferingGroupID (String offeringGroupID) {
        this.OfferingGroupID = new SimpleStringProperty(offeringGroupID);
    }
    public void setOfferingGroupName (String offeringGroupName) {
        this.OfferingGroupName = new SimpleStringProperty(offeringGroupName);
    }
    public void setOriginalGroupID (String originalGroupID) {
        this.OriginalGroupID = new SimpleStringProperty(originalGroupID);
    }
    public void setOriginalGroupName (String originalGroupName) {
        this.OriginalGroupName = new SimpleStringProperty(originalGroupName);
    }
    public void  setIsNewMessage (String isNewMessage) {
        this.IsNewMessage = new SimpleStringProperty(isNewMessage);
    }
    public void setJointAnnulment (String jointAnnulment) {
        this.JointAnnulment = new SimpleStringProperty(jointAnnulment);
    }
    public void setNote (String note) {
        this.Note = new SimpleStringProperty(note);
    }
    public void setLastMessage (String lastMessage) {
        this.LastMessage = new SimpleStringProperty(lastMessage);
    }
    public void setGroupChangedByID (String groupChangedByID) {
        this.GroupChangedByID = new SimpleStringProperty(groupChangedByID);
    }
    public void setGroupChangedBy (String groupChangedBy ) {
        this.GroupChangedBy = new SimpleStringProperty(groupChangedBy);
    }
    public void setIsReadMeassage (Integer isReadMeassage) {
        this.IsReadMeassage = new SimpleIntegerProperty(isReadMeassage);
    }
    public void setSpecialMarginTypeID (String specialMarginTypeID) {
        this.SpecialMarginTypeID = new SimpleStringProperty(specialMarginTypeID);
    }
    public void setSpecialMarginTypeName (String specialMarginTypeName) {
        this.SpecialMarginTypeName = new SimpleStringProperty(specialMarginTypeName);
    }
    public void setStateID (String stateID) {
        this.StateID = new SimpleStringProperty(stateID);
    }
    public void setStateName (String stateName) {
        this.StateName = new SimpleStringProperty(stateName);
    }

    @Override
    public String toString() { return AccountName.getValue(); }



    @Override
    public List<InTract> findAllInTract(boolean pagination, int rowIndex, String createdByID, String offeringGroupID) {
        String query = (pagination ? accountQueries1.getMainInTract(true, rowIndex, createdByID,offeringGroupID ) : accountQueries1.getMainInTract(false, 0, createdByID,offeringGroupID ));

        try {

            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<InTract>(InTract.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }



}
