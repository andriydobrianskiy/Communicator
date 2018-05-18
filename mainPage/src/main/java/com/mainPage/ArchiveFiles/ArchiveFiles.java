package com.mainPage.ArchiveFiles;

import com.connectDatabase.DBConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArchiveFiles implements ArchiveFilesInterface {
    private static Logger log = Logger.getLogger(ArchiveFiles.class.getName());
    private QueryRunner dbAccess = new QueryRunner();
    private ArchiveFilesQuery accountQueries1 = new ArchiveFilesQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";



    public static List<Object> getDictionaryMap() { // Initialization listview labels
        List<Object> list = new ArrayList<Object>();


        list.add(new ArchiveFiles()); // Контрагенти
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
    private SimpleStringProperty IsReadMessage;
    private SimpleStringProperty SpecialMarginTypeID;
    private SimpleStringProperty SpecialMarginTypeName;
    private SimpleStringProperty StateID;
    private SimpleStringProperty StateName;
    private SimpleStringProperty ChangeByAutouser;

    public ArchiveFiles () {}

    public ArchiveFiles(SimpleStringProperty number, SimpleStringProperty createdOn, SimpleStringProperty createdBy, SimpleStringProperty accountCode , SimpleStringProperty accountName) {
        Number = number;
        CreatedOn = createdOn;
        CreatedBy = createdBy;
        AccountCode = accountCode;
        AccountName = accountName;


    }

    // getters
    public String getID() {
        return ID.getValue();
    }

    public String getCreatedOn() {
        return CreatedOn.getValue();
    }

    public String getCreatedByID() {
        return CreatedByID.getValue();
    }

    public String getCreatedBy() {
        return CreatedBy.getValue();
    }

    public String getAccountID() {
        return AccountID.getValue();
    }

    public String getAccountName() {
        return AccountName.getValue();
    }

    public String getAccountCode() {
        return AccountCode.getValue();
    }

    public String getAccountSaldo() {
        return AccountSaldo.getValue();
    }

    public String getAccountIsSolid() {
        return AccountIsSolid.getValue();
    }

    public String getNumber() {
        return Number.getValue();
    }

    public String getStatusID() {
        return StatusID.getValue();
    }

    public String getStatus() {
        return Status.getValue();
    }

    public String getStoreCityID() {
        return StoreCityID.getValue();
    }

    public String getStoreCity() {
        return StoreCity.getValue();
    }

    public String getOfferingGroupID() {
        return OfferingGroupID.getValue();
    }

    public String getOfferingGroupName() {
        return OfferingGroupName.getValue();
    }

    public String getOriginalGroupID() {
        return OriginalGroupID.getValue();
    }

    public String getOriginalGroupName() {
        return OriginalGroupName.getValue();
    }

    public String getIsNewMessage() {
        return IsNewMessage.getValue();
    }

    public String getJointAnnulment() {
        return JointAnnulment.getValue();
    }

    public String getNote() {
        return Note.getValue();
    }

    public String getLastMessage() {
        return LastMessage.getValue();
    }

    public String getGroupChangedByID() {
        return GroupChangedByID.getValue();
    }

    public String getGroupChangedBy() {
        return GroupChangedBy.getValue();
    }

    public String getIsReadMessage() {
        return IsReadMessage.getValue();
    }

    public String getSpecialMarginTypeID() {
        return SpecialMarginTypeID.getValue();
    }

    public String getSpecialMarginTypeName() {
        return SpecialMarginTypeName.getValue();
    }

    public String getStateID() {
        return StateID.getValue();
    }

    public String getStateName() {
        return StateName.getValue();
    }

    public String getChangeByAutouser() {
        return ChangeByAutouser.getValue();
    }


    // setters

    public void setID(String ID) {
        this.ID = new SimpleStringProperty(ID);
    }

    public void setCreatedOn(String createdOn) {
        this.CreatedOn = new SimpleStringProperty(createdOn);
    }

    public void setCreatedByID(String createdByID) {
        this.CreatedByID = new SimpleStringProperty(createdByID);
    }

    public void setCreatedBy(String createdBy) {
        this.CreatedBy = new SimpleStringProperty(createdBy);
    }

    public void setAccountID(String accountID) {
        this.AccountID = new SimpleStringProperty(accountID);
    }

    public void setAccountName(String accountName) {
        this.AccountName = new SimpleStringProperty(accountName);
    }

    public void setAccountCode(String accountCode) {
        this.AccountCode = new SimpleStringProperty(accountCode);
    }

    public void setAccountSaldo(String accountSaldo) {
        this.AccountSaldo = new SimpleStringProperty(accountSaldo);
    }

    public void setAccountIsSolid(String accountIsSolid) {
        this.AccountIsSolid = new SimpleStringProperty(accountIsSolid);
    }

    public void setNumber(String number) {
        this.Number = new SimpleStringProperty(number);
    }

    public void setStatusID(String statusID) {
        this.StatusID = new SimpleStringProperty(statusID);
    }

    public void setStatus(String status) {
        this.Status = new SimpleStringProperty(status);
    }

    public void setStoreCityID(String storeCityID) {
        this.StoreCityID = new SimpleStringProperty(storeCityID);
    }

    public void setStoreCity(String storeCity) {
        this.StoreCity = new SimpleStringProperty(storeCity);
    }

    public void setOfferingGroupID(String offeringGroupID) {
        this.OfferingGroupID = new SimpleStringProperty(offeringGroupID);
    }

    public void setOfferingGroupName(String offeringGroupName) {
        this.OfferingGroupName = new SimpleStringProperty(offeringGroupName);
    }

    public void setOriginalGroupID(String originalGroupID) {
        this.OriginalGroupID = new SimpleStringProperty(originalGroupID);
    }

    public void setOriginalGroupName(String originalGroupName) {
        this.OriginalGroupName = new SimpleStringProperty(originalGroupName);
    }

    public void setIsNewMessage(String isNewMessage) {
        this.IsNewMessage = new SimpleStringProperty(isNewMessage);
    }

    public void setJointAnnulment(String jointAnnulment) {
        this.JointAnnulment = new SimpleStringProperty(jointAnnulment);
    }

    public void setNote(String note) {
        this.Note = new SimpleStringProperty(note);
    }

    public void setLastMessage(String lastMessage) {
        this.LastMessage = new SimpleStringProperty(lastMessage);
    }

    public void setGroupChangedByID(String groupChangedByID) {
        this.GroupChangedByID = new SimpleStringProperty(groupChangedByID);
    }

    public void setGroupChangedBy(String groupChangedBy) {
        this.GroupChangedBy = new SimpleStringProperty(groupChangedBy);
    }

    public void setIsReadMessage(String isReadMessage) {
        this.IsReadMessage = new SimpleStringProperty(isReadMessage);
    }

    public void setSpecialMarginTypeID(String specialMarginTypeID) {
        this.SpecialMarginTypeID = new SimpleStringProperty(specialMarginTypeID);
    }

    public void setSpecialMarginTypeName(String specialMarginTypeName) {
        this.SpecialMarginTypeName = new SimpleStringProperty(specialMarginTypeName);
    }

    public void setStateID(String stateID) {
        this.StateID = new SimpleStringProperty(stateID);
    }

    public void setStateName(String stateName) {
        this.StateName = new SimpleStringProperty(stateName);
    }

    public void setChangeByAutouser(String changeByAutouser) {
        this.ChangeByAutouser = new SimpleStringProperty(changeByAutouser);
    }



   /* @Override
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

    }*/

    @Override
    public List<ArchiveFiles> findAllInArchive(boolean pagination, int rowIndex, String createdByID, String offeringGroupID) {
        {
            String query = (pagination ? accountQueries1.getMainArchiveFiles(true, rowIndex, createdByID , offeringGroupID) : accountQueries1.getMainArchiveFiles(false, 0, createdByID , offeringGroupID));

            try {

                return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<ArchiveFiles>(ArchiveFiles.class)/*, statusID, createdByID, offeringGroupID*/));
            } catch (Exception e) {
                log.log(Level.SEVERE, "getAccount exception: " + e);
            }

            return FXCollections.observableArrayList(EMPTYLIST);
        }

    }
    public List<ArchiveFiles> findSearchSkrut(boolean pagination, int rowIndex, String createdByID, String offeringGroupID, String requestID) {
        String query = (pagination ? accountQueries1.getMainArchiveFilesSearch(true, rowIndex, requestID) : accountQueries1.getMainArchiveFilesSearch(false, 0, requestID));
        try {
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<ArchiveFiles>(ArchiveFiles.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }
}
