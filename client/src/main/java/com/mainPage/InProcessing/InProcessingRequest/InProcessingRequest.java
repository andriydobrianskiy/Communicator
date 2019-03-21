package com.mainPage.InProcessing.InProcessingRequest;

import com.Utils.GridComp;

public class InProcessingRequest extends GridComp {
    private String ID;
    private String createdOn;
    private String createdByID;
    private String modifiedOn;
    private String modifiedByID;
    private String offeringID;
    private String requestID;
    private String index;
    private String skrut;
    private String offeringName;
    private String defaultOfferingCode;
    private String quantity;
    private String newOfferingCode;
    private String newDescription;
    private Integer colorByOfferingSale;

    public InProcessingRequest() {
    }

    public InProcessingRequest(String index, String skrut, String newDescription, String offeringName, String quantity, String defaultOfferingCode, String newOfferingCode, int colorByOfferingSale, String id) {
        this.index = index;
        this.skrut = skrut;
        this.offeringName = offeringName;
        this.quantity = quantity;
        this.defaultOfferingCode = defaultOfferingCode;
        this.newDescription = newDescription;
        this.newOfferingCode = newOfferingCode;
        this.colorByOfferingSale = colorByOfferingSale;
        this.ID = id;

    }

    public String getID() { return ID;}
    public String getCreatedOn() { return createdOn; }
    public String getCreatedByID() { return createdByID; }
    public String getModifiedOn() { return modifiedOn; }
    public String getModifiedByID() { return modifiedByID; }
    public String getOfferingID() { return offeringID; }
    public String getRequestID() { return requestID; }
    public String getIndex() { return index; }
    public String getSkrut() { return skrut; }
    public String getOfferingName() { return offeringName; }
    public String getDefaultOfferingCode() { return defaultOfferingCode; }
    public String getQuantity() { return quantity; }
    public String getNewOfferingCode() { return newOfferingCode; }
    public String getNewDescription() { return newDescription; }
    public Integer getColorByOfferingSale () {return colorByOfferingSale;}



    public void setID(String ID) { this.ID = ID;}
    public void setCreatedOn(String createdOn) { this.createdOn = createdOn; }
    public void setCreatedByID(String createdByID) { this.createdByID = createdByID; }
    public void setModifiedOn(String modifiedOn) { this.modifiedOn = modifiedOn; }
    public void setModifiedByID(String modifiedByID) { this.modifiedByID = modifiedByID; }
    public void setOfferingID(String offeringID) { this.offeringID = offeringID; }
    public void setRequestID(String requestID) { this.requestID = requestID; }
    public void setIndex(String index) { this.index = index; }
    public void setSkrut(String skrut) { this.skrut = skrut; }
    public void setOfferingName(String offeringName) { this.offeringName = offeringName; }
    public void setDefaultOfferingCode(String defaultOfferingCode) { this.defaultOfferingCode = defaultOfferingCode; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public void setNewOfferingCode(String newOfferingCode) { this.newOfferingCode = newOfferingCode; }
    public void setNewDescription(String newDescription) { this.newDescription = newDescription; }
    public void setColorByOfferingSale(Integer colorByOfferingSale) {
        this.colorByOfferingSale = colorByOfferingSale;
    }



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
                "newDescrip - " + newDescription + "\n" +
                "colorBy - " + colorByOfferingSale + "\n";
    }



}
