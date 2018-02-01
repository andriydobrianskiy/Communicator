package com.ChatTwoo.controller.MessageInRequest;

import com.Utils.SearchType;

import java.util.List;

public interface MessageInRequestDAO {


    boolean insertAccount(MessageInRequest offeringRequest);
    boolean updateAccount(MessageInRequest offeringRequest);
    boolean deleteAccount(MessageInRequest offeringRequest);

    List<MessageInRequest> findByProperty(SearchType searchType, Object value);
    List<MessageInRequest> findAll();
}
