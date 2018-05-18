package com.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {

    private String name;
    private String offeringID;

    private MessageType type;
    private String msg;
    private int count;
    private ArrayList<User> list;
    private String createdby;
    private String offeringGroupName;
    private ArrayList<User> listId;
    private ArrayList<User> users;
    private ArrayList<User> usersID;
   // private MessageType Notification;

    private Status status;

    public byte[] getVoiceMsg() {
        return voiceMsg;
    }

    private byte[] voiceMsg;

    public String getPicture() {
        return picture;
    }

    private String picture;
    public String getCreatedby () {return createdby;}
    public void setCreatedby (String createdby) {this.createdby = createdby;}
    public String getOfferingGroupName () {return offeringGroupName;}
    public void setOfferingGroupName (String offeringGroupName){this.offeringGroupName = offeringGroupName;}

    public Message() {
    }

  //  public MessageType getNotification () {return  Notification;}
 //   public void setNotification (MessageType notification) {Notification = notification;}
    public String getName() {
        return name;
    }
    public String getOfferingID () {return offeringID;}
    public void setOfferingID (String offeringID){this.offeringID = offeringID;}

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public ArrayList<User> getUserlist() {
        return list;
    }

    public void setUserlist(HashMap<String, User> userList) {
        this.list = new ArrayList<>(userList.values());
    }

    public void setOnlineCount(int count){
        this.count = count;
    }

    public int getOnlineCount(){
        return this.count;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<User> getUserlistId() {
        return listId;
    }

    public void setUserlistId(HashMap<String, User> userlistId) {
        this.listId = new ArrayList<>(userlistId.values());
    }
    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsersID(ArrayList<User> usersID) {
        this.usersID = usersID;
    }
    public ArrayList<User> getUsersID() {
        return usersID;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setVoiceMsg(byte[] voiceMsg) {
        this.voiceMsg = voiceMsg;
    }
}
