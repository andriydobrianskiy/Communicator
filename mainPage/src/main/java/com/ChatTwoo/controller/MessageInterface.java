package com.ChatTwoo.controller;

import java.util.List;

public interface MessageInterface {
    String getID();
    String getName();

    void setID(String ID);
    void setName(String name);

  //  List<ColumnMessage> findByProperty(Object value, Enum<? extends SearchType> searchType);
    List<ColumnMessage> findAll();
}
