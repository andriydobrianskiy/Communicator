package com.mainPage.InProcessing;

public interface MainInProcessingQuery {
    void showData();
    boolean insertOffering(InProcessing account);
    boolean updateOffering(InProcessing account);
    boolean deleteOffering(InProcessing account);
}
