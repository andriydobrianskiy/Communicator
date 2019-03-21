package com.mainPage.NotFulled.OfferingRequest;

import com.Utils.SearchType;

import java.util.List;

public interface OfferingRequestDAO {
        String getID();
        String getName();

        void setID(String ID);
        void setName(String Name);

        boolean insertAccount(OfferingRequest offeringRequest);
        boolean updateAccount(OfferingRequest offeringRequest);
        boolean deleteAccount(OfferingRequest offeringRequest);

        List<OfferingRequest> findByProperty(SearchType searchType, Object value);
        List<OfferingRequest> findAll(int rowIndex, String requestID);
}
