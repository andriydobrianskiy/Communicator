package com.client;

import javafx.scene.layout.HBox;
import org.apache.commons.dbutils.QueryRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ColumnMessage extends HBox {
    private Logger log = Logger.getLogger(ColumnMessage.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private QueryClientChat accountQueries = new QueryClientChat();
    private final List EMPTYLIST = new ArrayList<>();
    private  static final String EMPTYSTRING = "";


    private String ID;
    private String createdOn;
    private String createdByID;
    private String sender;
    private String modifiedOn;
    private String recipientID;
    private String requestID;
    private String message;



    public ColumnMessage(String createdOn, String createdByID,  String sender, String message) {

        this.createdOn = createdOn;
        this.sender = sender;
        this.message = message;
        this.createdByID = createdByID;
    }

    public ColumnMessage() {

    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID (String createdByID){
        this.createdByID = createdByID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public String getRequestID() {
        return requestID;
    }

    public void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /*  public String getID() { return ID.getValue(); }
        public String getCreatedOn() { return createdOn.getValue(); }
        public String getCreatedByID() { return createdByID.getValue(); }
        public String getModifiedOn() { return modifiedOn.getValue(); }
        public String getRecipientID() { return recipientID.getValue(); }
        public String getRequestID() { return requestID.getValue(); }
        public String getMessage() { return message.getValue(); }

        public void setID(String ID) { this.ID = new SimpleStringProperty(ID); }
        public void setCreatedOn(String createdOn) { this.createdOn = new SimpleStringProperty(createdOn); }
        public void setCreatedByID(String createdByID) { this.createdByID = new SimpleStringProperty(createdByID); }
        public void setModifiedOn(String modifiedOn) { this.modifiedOn = new SimpleStringProperty(modifiedOn); }
        public void setRecipientID(String recipientID) { this.recipientID = new SimpleStringProperty(recipientID); }
        public void setRequestID(String requestID) { this.requestID = new SimpleStringProperty(requestID); }
        public void setMessage(String message) { this.message = new SimpleStringProperty(message); }

    */
/*    @Override
    public String toString() { return message.getValue(); }*/

    /*public String getID() {
        return ID.get();
    }*/

    /*@Override
    public String getName() {
        return null;
    }

    public SimpleStringProperty IDProperty() {
        return ID;
    }

    public void setID(String ID) {
        this.ID.set(ID);
    }

    @Override
    public void setName(String name) {

    }

    /*@Override
    public List<ColumnMessage> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        return null;
    }*/

    /*public String getCreatedOn() {
        return CreatedOn.get();
    }

    public SimpleStringProperty createdOnProperty() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        this.CreatedOn.set(createdOn);
    }

    public String getCreatedByID() {
        return CreatedByID.get();
    }

    public SimpleStringProperty createdByIDProperty() {
        return CreatedByID;
    }

    public void setCreatedByID(String createdByID) {
        this.CreatedByID.set(createdByID);
    }

    public String getModifiedOn() {
        return ModifiedOn.get();
    }

    public SimpleStringProperty modifiedOnProperty() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        this.ModifiedOn.set(modifiedOn);
    }

    public String getRecipientID() {
        return RecipientID.get();
    }

    public SimpleStringProperty recipientIDProperty() {
        return RecipientID;
    }

    public void setRecipientID(String recipientID) {
        this.RecipientID.set(recipientID);
    }

    public String getRequestID() {
        return RequestID.get();
    }

    public SimpleStringProperty requestIDProperty() {
        return RequestID;
    }

    public void setRequestID(String requestID) {
        this.RequestID.set(requestID);
    }

    public String getMessage() {
        return Message.get();
    }

    public SimpleStringProperty messageProperty() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message.set(message);
    }*/


    //@Override
    /*public List<ColumnMessage> findAll() {
        String query = (accountQueries.getMainAccount());
        System.out.println(accountQueries.getMainAccount());
        try {
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getConnection(), query, new BeanListHandler<ColumnMessage>(ColumnMessage.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }*/
    @Override
    public String toString() {
        return getCreatedByID();
    }

}
