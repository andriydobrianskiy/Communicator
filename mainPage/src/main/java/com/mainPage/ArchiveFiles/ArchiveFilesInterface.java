package com.mainPage.ArchiveFiles;

import java.util.List;

public interface ArchiveFilesInterface {
    //String getID();
   // String getName();

   /// void setID(String ID);
   // void setName(String name);

    // List<? extends InProcessingIntarface> findByProperty1(Object value, Enum<? extends SearchType> searchType);
    List<ArchiveFiles> findAllInTract(boolean pagination, int rowIndex/*,String statusID, String createdByID, String offeringGroupID*/);
    // List<InTract> findAllInTractAll(boolean pagination, int rowIndex );
}
