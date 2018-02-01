package com.mainPage.createRequest;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import com.Utils.UsefulUtils;
import com.connectDatabase.DBConnection;
import com.login.User;
import com.mainPage.NotFulled.MainNotFulledQuery;
import com.mainPage.NotFulled.NotFulfilled;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateRequestDAO implements MainNotFulledQuery {
    private static Logger log = Logger.getLogger(CreateRequestDAO.class.getName());


    private QueryRunner dbAccess = new QueryRunner();
    private RequestQuery queries = new RequestQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";

    @Override
    public void showData() {

    }

    @Override
    public boolean insertOffering(NotFulfilled account) {
        UUID ID = UUID.randomUUID();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.format(new Date());


        try (Connection connection = DBConnection.getDataSource().getConnection()){
           dbAccess.update(connection, queries.getInsertCreateRequestQuery(),
                 //   dbAccess.update(connection, queries.getInsertCreateRequestQuery(), new BeanHandler<NotFulfilled>(NotFulfilled.class),
                   // 'CUrrent_TIMESTEMP',

                 //  ID,
                   sdf.format(new Date()),
                         //  "CUrrent_TIMESTEMP", //  account.getDate(),
                    User.getContactID(),
                  sdf.format(new Date()),
                          //  "CUrrent_TIMESTEMP",
                   // account.getDate(),
                    User.getContactID(),
                   account.getAccountID(),
                   account.getStoreCityID(),
                   account.getStatusID(),
                   account.getOfferingGroupID(),
                   account.getOriginalGroupID(),
                   account.getStateID()
                  /* account.getAccountID(),

                   account.getAccountName(),
                    account.getStatusID(),
                   account.getStatus(),
                    account.getStoreCityID(),
                    account.getStoreCity(),
                    account.getOfferingGroupID(),
                   User.getContactName(),
                    account.getStateID(),
                    account.getStateName(),

                    account.getOriginalGroupID(),
                        account.getOriginalGroupName()
*/


            );
            return true;
        } catch (Exception e) {
        //    DBConnection database = new DBConnection();
        //    database.reconnect();

            log.log(Level.SEVERE, "Insert account row exception: " + e);
            return false;
        }

    }
    public boolean insertCreateRequest(NotFulfilled value) {
        return true;
    }

    public boolean insertCreateRequestGroup(NotFulfilled value) {
        try (Connection connection = DBConnection.getDataSource().getConnection()) {


            dbAccess.update(connection, queries.insertCreateQuery(), new BeanHandler<NotFulfilled>(NotFulfilled.class),
                    User.getContactID(),
                    User.getContactID(),
                    value.getName()
            );

            return true;
        } catch (Exception e) {
            UsefulUtils.showErrorDialog(e.getMessage());
            log.log(Level.SEVERE, "Exception: " + e);
            return false;
        }
    }

    public boolean insertCreateRequestInGroup(NotFulfilled value) {
        UUID uniqueID = UUID.randomUUID();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {


            dbAccess.update(connection, queries.getInsertCreateRequestQuery(), new BeanHandler<NotFulfilled>(NotFulfilled.class),
                    uniqueID,
                   // User.getContactID(),
                   // User.getContactID(),
                    value.getAccountName(),
                    value.getStateName(),
                    value.getStatus(),
                    value.getStoreCity(),
                    value.getOfferingGroupName());


            dbAccess.update(connection, queries.insertCreateRequestInGroup(), new BeanHandler<NotFulfilled>(NotFulfilled.class),
                    User.getContactID(),
                    User.getContactID(),
                    uniqueID,
                    value.getGroupID());

            return true;
        } catch (Exception e) {
            UsefulUtils.showErrorDialog(e.getMessage());
            log.log(Level.SEVERE, "Exception: " + e);
            return false;
        }

    }


   /* public boolean insertOffering(NotFulfilled value) {
        UUID uniqueID = UUID.randomUUID();

        try (Connection connection = DBConnection.getDataSource().getConnection()) {


            dbAccess.update(connection, queries.getInsertCreateRequestQuery(), new BeanHandler<NotFulfilled>(NotFulfilled.class),
                    uniqueID,
                    // User.getContactID(),
                    // User.getContactID(),
                    value.getAccountName(),
                    value.getStateName(),
                    value.getStatus(),
                    value.getStoreCity(),
                    value.getOfferingGroupName());


            dbAccess.update(connection, queries.insertCreateRequestInGroup(), new BeanHandler<NotFulfilled>(NotFulfilled.class),
                    User.getContactID(),
                    User.getContactID(),
                    uniqueID,
                    value.getGroupID());

            return true;
        } catch (Exception e) {
            UsefulUtils.showErrorDialog(e.getMessage());
            log.log(Level.SEVERE, "Exception: " + e);
            return false;
        }

    }*/

    @Override
    public boolean updateOffering(NotFulfilled account) {
        return false;
    }

    @Override
    public boolean deleteOffering(NotFulfilled account) {
        return false;
    }
}
