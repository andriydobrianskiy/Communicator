package com.ChatTwoo.controller.MessageInRequest;

import javafx.beans.property.SimpleStringProperty;

public class MessageInRequest {
    private SimpleStringProperty ID;
    private SimpleStringProperty createdOn;
    private SimpleStringProperty createdByID;
    private SimpleStringProperty modifiedOn;
    private SimpleStringProperty recipientID;
    private SimpleStringProperty requestID;
    private SimpleStringProperty message;

    public MessageInRequest() {

    }

    public String getID() { return ID.getValue(); }
    public String getCreatedByID() { return createdByID.getValue(); }
    public String getCreatedOn() { return createdOn.getValue(); }
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




}
