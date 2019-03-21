package com.login;

import com.connectDatabase.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;

public class User {

    private static Logger log = Logger.getLogger(User.class.getName());


    private static String name = "";
    private static String password = "";

    private static String adminUnitID = "";
    private static String contactID = "";
    private static String contactName = "";
    private static String jobID = "";


    public User() {

    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static String getName() {
        return name;
    }

    public static String getPassword() {
        return password;
    }

    public static String getAdminUnitID() {
        return adminUnitID;
    }

    public static String getContactID() {
        return contactID;
    }
    public static String getJobID(){return jobID;}

    public  void setJobID (String jobID) { this.jobID = jobID; }

    public static String getContactName() {
        return contactName;
    }

    public static void setAdminUnit() {
        StringBuilder builderQuery = new StringBuilder();

        builderQuery.append(
                "SELECT \n" +
                        "\tAdminUnit.ID AS AdminUnitID,\n" +
                        "\tAdminUnit.Name, \n" +
                        "\tAdminUnit.UserContactID AS ContactID,\n" +
                        "\tContact.Name AS ContactName,\n" +
                        "Contact.JobID AS JobID\n" +
                        "FROM tbl_AdminUnit AdminUnit\n" +
                        "LEFT OUTER JOIN tbl_Contact Contact ON AdminUnit.UserContactID = Contact.ID\n" +
                        "\n" +
                        "WHERE AdminUnit.Name = '" + name + "'");


        try (Connection connection = DBConnection.getDataSource().getConnection()) {

            ResultSet rs = connection.createStatement().executeQuery(builderQuery.toString());

            while (rs.next()) {
                adminUnitID = rs.getString(1);
                contactID = rs.getString(3);
                contactName = rs.getString(4);
                jobID = rs.getString(5);
            }
            System.out.println(contactID);
        } catch (Exception e) {

            e.printStackTrace();
            return;
        }

    }


}
