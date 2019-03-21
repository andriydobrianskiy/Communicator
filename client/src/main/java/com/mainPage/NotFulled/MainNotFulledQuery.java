package com.mainPage.NotFulled;

public interface MainNotFulledQuery {
    void showData();
    boolean insertOffering(NotFulfilled account);
    boolean updateOffering(NotFulfilled account);
    boolean deleteOffering(NotFulfilled account);
}
