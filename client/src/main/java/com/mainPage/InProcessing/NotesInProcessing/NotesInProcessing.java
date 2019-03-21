package com.mainPage.InProcessing.NotesInProcessing;

public class NotesInProcessing {
    private String ID;
    private String CreatedOn;
    private String CreatedByID;
    private String ModifiedOn;
    private String ModifiedByID;
    private String OfferingID;
    private String RequestID;
    private String Note;
    private String Index;
    private String Skrut;
    private String OfferingName;
    private String DefaultOfferingCode;
    private String Quantity;
    private String NewOfferingCode;
    private String NewDescription;
    private String IsRoot;
public NotesInProcessing () {}
    public NotesInProcessing(String note) {
        Note = note;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedByID() {
        return CreatedByID;
    }

    public void setCreatedByID(String createdByID) {
        CreatedByID = createdByID;
    }

    public String getModifiedOn() {
        return ModifiedOn;
    }

    public void setModifiedOn(String modifiedOn) {
        ModifiedOn = modifiedOn;
    }

    public String getModifiedByID() {
        return ModifiedByID;
    }

    public void setModifiedByID(String modifiedByID) {
        ModifiedByID = modifiedByID;
    }

    public String getOfferingID() {
        return OfferingID;
    }

    public void setOfferingID(String offeringID) {
        OfferingID = offeringID;
    }

    public String getRequestID() {
        return RequestID;
    }

    public void setRequestID(String requestID) {
        RequestID = requestID;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getSkrut() {
        return Skrut;
    }

    public void setSkrut(String skrut) {
        Skrut = skrut;
    }

    public String getOfferingName() {
        return OfferingName;
    }

    public void setOfferingName(String offeringName) {
        OfferingName = offeringName;
    }

    public String getDefaultOfferingCode() {
        return DefaultOfferingCode;
    }

    public void setDefaultOfferingCode(String defaultOfferingCode) {
        DefaultOfferingCode = defaultOfferingCode;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getNewOfferingCode() {
        return NewOfferingCode;
    }

    public void setNewOfferingCode(String newOfferingCode) {
        NewOfferingCode = newOfferingCode;
    }

    public String getNewDescription() {
        return NewDescription;
    }

    public void setNewDescription(String newDescription) {
        NewDescription = newDescription;
    }

    public String getIsRoot() {
        return IsRoot;
    }

    public void setIsRoot(String isRoot) {
        IsRoot = isRoot;
    }
}
