package com.mainPage.All.AllRequest;

import com.Utils.SearchType;
import com.mainPage.ArchiveFiles.ArchiveFilesRequest.ArchiveFilesRequest;

import java.util.List;

public interface AllRequestDAO {
    boolean insertAccount(ArchiveFilesRequest offeringRequest);
    boolean updateAccount(ArchiveFilesRequest offeringRequest);
    boolean deleteAccount(ArchiveFilesRequest offeringRequest);

    List<ArchiveFilesRequest> findByProperty(SearchType searchType, Object value);
    List<AllRequest> findAll(String requestID);
}
