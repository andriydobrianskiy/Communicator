package com.mainPage.InProcessing.InProcessingRequest;

import com.Utils.SearchType;

import java.util.List;

public interface InProcessingRequestDAO {

    boolean insertAccount(InProcessingRequest inProcessingRequest);
    boolean updateAccount(InProcessingRequest inProcessingRequest);
    boolean deleteAccount(InProcessingRequest inProcessingRequest);

    List<InProcessingRequest> findByProperty(SearchType searchType, Object value);
    List<InProcessingRequest> findAll(int rowIndex, String RequestID);
}
