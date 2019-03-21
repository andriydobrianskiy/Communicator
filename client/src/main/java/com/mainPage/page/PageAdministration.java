package com.mainPage.page;

import com.connectDatabase.DBConnection;
import com.mainPage.createRequest.CreateInterface;
import com.mainPage.page.contact.QuerySearchContact;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageAdministration {

    private static Logger log = Logger.getLogger(PageAdministration.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private QueryPageAdministration queryPageAdministration = new QueryPageAdministration();
    private static final List EMPTYLIST = new ArrayList<>();


        private String ID;
        private String createdOn;
        private String createdByID;
        private String modifiedOn;
        private String modifiedByID;
        private String contactID;
        private String contactName;
        private String jobID;
        private String jobName;
        private String nameElements;
        private Integer access;
        private String type;

        public PageAdministration (){}

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getCreatedByID() {
            return createdByID;
        }

        public void setCreatedByID(String createdByID) {
            this.createdByID = createdByID;
        }

        public String getModifiedOn() {
            return modifiedOn;
        }

        public void setModifiedOn(String modifiedOn) {
            this.modifiedOn = modifiedOn;
        }

        public String getModifiedByID() {
            return modifiedByID;
        }

        public void setModifiedByID(String modifiedByID) {
            this.modifiedByID = modifiedByID;
        }

        public String getContactID() {
            return contactID;
        }

        public void setContactID(String contactID) {
            this.contactID = contactID;
        }

        public String getContactName() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName = contactName;
        }

        public String getNameElements() {
            return nameElements;
        }

        public void setNameElements(String nameElements) {
            this.nameElements = nameElements;
        }

        public Integer getAccess() {
            return access;
        }

        public void setAccess(Integer access) {
            this.access = access;
        }
        public String getType () {return this.type;}

        public void setType (String type) {
            this.type = type;
        }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public ObservableList<PageAdministration> findAll(boolean pagination, int rowIndex, String nameElements) {
        String query = (pagination ? queryPageAdministration.getMainPageAdminostration(true, rowIndex, nameElements) : queryPageAdministration.getMainPageAdminostration(false, 0, nameElements));
        try{
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<PageAdministration>(PageAdministration.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }
    public ObservableList<PageAdministration> findAll( String contactID, String jobID) {
        String query = queryPageAdministration.getMainPageAdminostrationControl(contactID, jobID);
        try{
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<PageAdministration>(PageAdministration.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
            DBConnection database = new DBConnection();
            database.reconnect();
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }

    @Override
    public String toString() {
        return getContactName();
    }


}
