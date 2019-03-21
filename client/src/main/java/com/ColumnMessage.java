package com;

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
    private String offeringGroupID;
    private String number;

public ColumnMessage (String createdByID, String offeringGroupID, String number){
    this.createdByID = createdByID;
    this.offeringGroupID = offeringGroupID;
    this.number = number;
}

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

    public void setOfferingGroupID (String offeringGroupID) { this.offeringGroupID = offeringGroupID;}

    public String getOfferingGroupID () {return offeringGroupID;}

    public void setNumber(String number){this.number = number;}

    public String getNumber () {return this.number;}


    @Override
    public String toString() {
        return getCreatedByID();
    }

}
