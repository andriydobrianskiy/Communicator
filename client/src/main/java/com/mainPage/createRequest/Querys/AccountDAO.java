package com.mainPage.createRequest.Querys;

import com.mainPage.createRequest.searchCounterpart.Counterpart;

public interface AccountDAO {

        void showData();
        boolean insertAccount(Counterpart account);
        boolean updateAccount(Counterpart account);
        boolean deleteAccount(Counterpart account);

}
