package com.ChatTwoo.controller;

import com.Utils.SearchType;

import java.util.List;

public interface ColumnMessageDAO {

    boolean insertAccount(ColumnMessage offeringRequest);
    boolean updateAccount(ColumnMessage offeringRequest);
    boolean deleteAccount(ColumnMessage offeringRequest);

    List<ColumnMessage> findByProperty(SearchType searchType, Object value);
    List<ColumnMessage> findAll();
}
