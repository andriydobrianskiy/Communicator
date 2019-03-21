package com.Utils;

import com.mainPage.InProcessing.InProcessing;

import java.util.List;

public interface InProcessingIntarface {

    List<InProcessing> findAllOne(boolean pagination, int rowIndex, String createdByID, String offeringGroupID, String filterSorted, Integer pricingBoolean);

    List<InProcessing> findAllInProcessing(boolean pagination, int rowIndex, String filterSorted, Integer pricingBoolean);


}
