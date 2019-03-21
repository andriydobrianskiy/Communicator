package com.mainPage.page.contact;

import com.Utils.AccountSearchType;
import com.Utils.GridComp;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.createRequest.CreateInterface;
import com.mainPage.createRequest.CreateRequest;
import com.mainPage.createRequest.Querys.Query;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Contact extends GridComp implements CreateInterface//DictionaryInterface
 {

    private static Logger log = Logger.getLogger(Contact.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private QuerySearchContact conatctQueries = new QuerySearchContact();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";

    private String ID;
    private String CreatedOn;
    private String ContactName;
    private String JobName;


    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }



    public Contact() {
    }




    public String getJobName() {
        return JobName;
    }

    public void setJobName(String jobName) {
        this.JobName = jobName;
    }

    @Override
    public String getID() {
        return ID;
    }

     @Override
     public String getName() {
         return null;
     }

     @Override
    public void setID(String ID) {
this.ID = ID;
    }

     @Override
     public void setName(String Name) {

     }


     @Override
    public List<Contact> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        String whereClause = "";
        String valueClause = "";

        switch ((ContactSearchType) searchType) {
            case NAMECONTACT:
                valueClause = "%" + value.toString() + "%";
                whereClause = " AND [C].[Name] LIKE '%" + value.toString() + "%'";
                break;
            case NAMEJOB:

                valueClause = "%" + value.toString() + "%";
                whereClause = " AND [J].[Name] LIKE '%" + value.toString() + "%'";
                break;


        }

        try {
            return dbAccess.query(DBConnection.getDataSource().getConnection(), conatctQueries.getMainContact(false, 0) + whereClause, new BeanListHandler<Contact>(Contact.class));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Column filter exception (findAccountByProperty): " + e);  DBConnection database = new DBConnection();
            database.reconnect();
        }

        return EMPTYLIST;
    }

    @Override
    public ObservableList<Contact> findAll(boolean pagination, int rowIndex) {
        String query = (pagination ? conatctQueries.getMainContact(true, rowIndex) : conatctQueries.getMainContact(false, 0));
        try{
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<Contact>(Contact.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }

     @Override
    public String toString() {
        return getName();
    }


}
