package com.mainPage.Statistic;

public class Statistic {
    private String ID;
    private String Name;
    private String Processing;
    private String Trakt;
    private String Completed;
    private String Canceled;
    private String CreatedByID;

    public Statistic () {}
    public Statistic (String name, String processing, String trakt, String completed, String canceled, String id){
        this.Name = name;
        this.Processing = processing;
        this.Trakt = trakt;
        this.Completed = completed;
        this.Canceled = canceled;
        this.ID = id;
    }

    //Getters
    public String getID (){
        return this.ID;
    }
    public String getName(){
        return this.Name;
    }
    public String getProcessing () {
        return this.Processing;
    }
    public String getTrakt () {
        return this.Trakt;
    }
    public String getCompleted () {
        return this.Completed;
    }
    public String getCanceled () {
        return this.Canceled;
    }
    public String getCreatedByID () {
        return this.CreatedByID;
    }
    //setters

    public void setID (String id) {
        this.ID = id;
    }
    public void setName (String name) {
        this.Name = name;
    }
    public void setProcessing (String processing) {
        this.Processing = processing;
    }
    public void setTrakt (String trakt) {
        this.Trakt = trakt;
    }
    public void setCompleted (String completed){
        this.Completed = completed;

    }
    public void setCanceled (String canceled) {
        this.Canceled = canceled;
    }
    public void setCreatedByID (String createdByID){
        this.CreatedByID = createdByID;
    }
}
