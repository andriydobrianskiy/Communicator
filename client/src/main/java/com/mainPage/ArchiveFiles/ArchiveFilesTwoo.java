package com.mainPage.ArchiveFiles;

public class ArchiveFilesTwoo {
    private String Number;
    private String CreatedOn;
    private String CreatedBy;
    private String AccountCode;
    private String AccountName;

    public ArchiveFilesTwoo(String number, String createdOn, String createdBy, String accountCode, String accountName) {
        Number = number;
        CreatedOn = createdOn;
        CreatedBy = createdBy;
        AccountCode = accountCode;
        AccountName = accountName;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getCreatedOn() {
        return CreatedOn;
    }

    public void setCreatedOn(String createdOn) {
        CreatedOn = createdOn;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getAccountCode() {
        return AccountCode;
    }

    public void setAccountCode(String accountCode) {
        AccountCode = accountCode;
    }

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }
}
