package com.mainPage.NotFulled.OfferingRequest;

import com.Utils.GridComp;
import javafx.beans.property.SimpleStringProperty;

public class OfferingRequest extends GridComp {

    private SimpleStringProperty ID;
    private SimpleStringProperty createdOn;
    private SimpleStringProperty createdByID;
    private SimpleStringProperty modifiedOn;
    private SimpleStringProperty modifiedByID;
    private SimpleStringProperty offeringID;
    private SimpleStringProperty requestID;
    private SimpleStringProperty index;
    private SimpleStringProperty skrut;
    private SimpleStringProperty offeringName;
    private SimpleStringProperty defaultOfferingCode;
    private SimpleStringProperty quantity;
    private SimpleStringProperty newOfferingCode;
    private SimpleStringProperty newDescription;
  //  private SimpleStringProperty isRoot;

    public OfferingRequest() {

    }

    public String getID() { return ID.getValue();}
    public String getCreatedOn() { return createdOn.getValue(); }
    public String getCreatedByID() { return createdByID.getValue(); }
    public String getModifiedOn() { return modifiedOn.getValue(); }
    public String getModifiedByID() { return modifiedByID.getValue(); }
    public String getOfferingID() { return offeringID.getValue(); }
    public String getRequestID() { return requestID.getValue(); }
    public String getIndex() { return index.getValue(); }
    public String getSkrut() { return skrut.getValue(); }
    public String getOfferingName() { return offeringName.getValue(); }
    public String getDefaultOfferingCode() { return defaultOfferingCode.getValue(); }
    public String getQuantity() { return quantity.getValue(); }
    public String getNewOfferingCode() { return newOfferingCode.getValue(); }
    public String getNewDescription() { return newDescription.getValue(); }
  //  public String getIsRoot () {return isRoot.getValue();}


    public void setID(String ID) { this.ID = new SimpleStringProperty(ID);}
    public void setCreatedOn(String createdOn) { this.createdOn = new SimpleStringProperty(createdOn); }
    public void setCreatedByID(String createdByID) { this.createdByID = new SimpleStringProperty(createdByID); }
    public void setModifiedOn(String modifiedOn) { this.modifiedOn = new SimpleStringProperty(modifiedOn); }
    public void setModifiedByID(String modifiedByID) { this.modifiedByID = new SimpleStringProperty(modifiedByID); }
    public void setOfferingID(String offeringID) { this.offeringID = new SimpleStringProperty(offeringID); }
    public void setRequestID(String requestID) { this.requestID = new SimpleStringProperty(requestID); }
    public void setIndex(String index) { this.index = new SimpleStringProperty(index); }
    public void setSkrut(String skrut) { this.skrut = new SimpleStringProperty(skrut); }
    public void setOfferingName(String offeringName) { this.offeringName = new SimpleStringProperty(offeringName); }
    public void setDefaultOfferingCode(String defaultOfferingCode) { this.defaultOfferingCode = new SimpleStringProperty(defaultOfferingCode); }
    public void setQuantity(String quantity) { this.quantity = new SimpleStringProperty(quantity); }
    public void setNewOfferingCode(String newOfferingCode) { this.newOfferingCode = new SimpleStringProperty(newOfferingCode); }
    public void setNewDescription(String newDescription) { this.newDescription = new SimpleStringProperty(newDescription); }
  //  public void setIsRoot (String isRoot) {this.isRoot = new SimpleStringProperty(isRoot);}


    public String printInfo() {
        return "ID - " + ID + "\n" +
                "createdOn - " + createdOn + "\n" +
                "createdByID - " + createdByID + "\n" +
                "modifiedOn - " + modifiedOn + "\n" +
                "modifiedByID - " + modifiedByID + "\n" +
                "offeringID - " + offeringID + "\n" +
                "requestID - " + requestID + "\n" +
                " index - " +  index + "\n" +
                "skrut - " + skrut + "\n" +
                "offeringName - " + offeringName + "\n" +
                "defaultOfferingCo - " + defaultOfferingCode + "\n" +
                "quantity - " + quantity + "\n" +
                "newOfferingCode - " + newOfferingCode + "\n" +
                "newDescrip - " + newDescription + "\n";
    }
}
