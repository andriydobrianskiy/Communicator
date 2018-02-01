package com.mainPage.InTract.InTractRequest;

public class InTractRequest {

    private String ID;
    private String CreatedOn;
    private String CreatedByID;
    private String ModifiedOn;
    private String ModifiedByID;
    private String OfferingID;
    private String RequestID;
    private String Index;
    private String Skrut;
    private String OfferingName;
    private String DefaultOfferingCode;
    private String Quantity;
    private String NewOfferingCode;
    private String NewDescription;
    private String IsRoot;

    public InTractRequest(String index, String skrut, String newDescription, String offeringName, String quantity, String defaultOfferingCode, String newOfferingCode) {
        Index = index;
        Skrut = skrut;
        NewDescription = newDescription;
        OfferingName = offeringName;
        Quantity = quantity;
        DefaultOfferingCode = defaultOfferingCode;
        NewOfferingCode = newOfferingCode;


    }

    // Getter

    public String getID() {
        return ID;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public String getCreatedByID() {
        return CreatedByID;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public String getModifiedByID() {
        return ModifiedByID;
    }

    public String getOfferingID() {
        return OfferingID;
    }

    public String getRequestID() {
        return RequestID;
    }

    public String getIndex() {
        return Index;
    }

    public String getSkrut() {
        return Skrut;
    }

    public String getOfferingName() {
        return OfferingName;
    }

    public String getDefaultOfferingCode() {
        return DefaultOfferingCode;
    }

    public String getQuantity() {
        return Quantity;
    }

    public String getNewOfferingCode() {
        return NewOfferingCode;
    }

    public String getNewDescription() {
        return NewDescription;
    }

    public String getIsRoot() {
        return IsRoot;
    }

    // Setter

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCreatedOn(String createdOn) {
        this.CreatedOn = createdOn;
    }

    public void setCreatedByID(String createdByID) {
        this.CreatedByID = createdByID;
    }

    public void setModifiedOn(String modifiedOn) {
        this.ModifiedOn = modifiedOn;
    }

    public void setModifiedByID(String modifiedByID) {
        this.ModifiedByID = modifiedByID;
    }

    public void setOfferingID(String offeringID) {
        this.OfferingID = offeringID;
    }

    public void setRequestID(String requestID) {
        this.RequestID = requestID;
    }

    public void setIndex(String index) {
        this.Index = index;
    }

    public void setSkrut(String skrut) {
        this.Skrut = skrut;
    }

    public void setOfferingName(String offeringName) {
        this.OfferingName = offeringName;
    }

    public void setDefaultOfferingCode(String defaultOfferingCode) {
        this.DefaultOfferingCode = defaultOfferingCode;
    }

    public void setQuantity(String quantity) {
        this.Quantity = quantity;
    }

    public void setNewOfferingCode(String newOfferingCode) {
        this.NewOfferingCode = newOfferingCode;
    }

    public void setNewDescription(String newDescription) {
        this.NewDescription = newDescription;
    }

    public void setIsRoot(String isRoot) {
        this.IsRoot = isRoot;
    }


}
