package com.mainPage.ArchiveFiles.ArchiveFilesRequest;

import com.Utils.SearchType;
import com.mainPage.InTract.InTractRequest.InTractRequest;

import java.util.List;

public interface ArchiveRequestDAO {
    boolean insertAccount(ArchiveFilesRequest offeringRequest);
    boolean updateAccount(ArchiveFilesRequest offeringRequest);
    boolean deleteAccount(ArchiveFilesRequest offeringRequest);

    List<ArchiveFilesRequest> findByProperty(SearchType searchType, Object value);
    List<ArchiveFilesRequest> findAll(String requestID);
}
