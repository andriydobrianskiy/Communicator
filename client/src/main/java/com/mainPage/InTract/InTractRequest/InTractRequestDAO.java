package com.mainPage.InTract.InTractRequest;

import com.Utils.SearchType;

import java.util.List;

public interface InTractRequestDAO {
    boolean insertAccount(InTractRequest offeringRequest);
    boolean updateAccount(InTractRequest offeringRequest);
    boolean deleteAccount(InTractRequest offeringRequest);

    List<InTractRequest> findByProperty(SearchType searchType, Object value);
    List<InTractRequest> findAll(String requestID);
}
