package com.mainPage.ArchiveFiles;

import java.util.List;

public interface ArchiveFilesInterface {

    List<ArchiveFiles> findInArchive(boolean pagination, int rowIndex, String createdByID, String offeringGroupID, String filterSorted, Integer pricingBoolean);

}
