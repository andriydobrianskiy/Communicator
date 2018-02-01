package com.mainPage.createRequest.searchCounterpart;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.AccountSearchType;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;
import com.mainPage.NotFulled.NotFulfilled;
import com.mainPage.createRequest.CreateInterface;
import com.mainPage.createRequest.Querys.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Counterpart extends NotFulfilled implements CreateInterface//DictionaryInterface
 {

    private static Logger log = Logger.getLogger(Counterpart.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private Query accountQueries = new Query();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";

    public static List<Object> getDictionaryMap() { // Initialization listview labels
        List<Object> list = new ArrayList<Object>();


        list.add(new Counterpart()); // Контрагенти
        //  list.add(new ServicesTable()); // Послуги
        //list.add(new OfferingsTable()); // Запчастини
        // list.add(new AccountVehicleTable()); // Автомобілі

        return list;
    }
 private  String ID;
    private Integer Code;
    private String Name;
    private String AccountCashboxName;
    private String IsSolid;
    private String CompanyIdentifier;
    private String AccountStateName;
    private String AccountMedalName;
    private String ActivitiesName;
    private String GroupID;

    public String getGroupID() {
        return GroupID;
    }

    public void setGroupID(String groupID) {
        GroupID = groupID;
    }

    public String getSkrut() {
        return Skrut;
    }

    public void setSkrut(String skrut) {
        Skrut = skrut;
    }

    private String Skrut;


    public Counterpart() {
    }

    public Counterpart(Integer code, String name, String accountCashboxName, String isSolid, String companyIdentifier, String accountStateName, String accountMedalName, String activitiesName) {
        this.Code = code;
        this.Name = name;
        this.AccountCashboxName = accountCashboxName;
        this.IsSolid = isSolid;
        this.CompanyIdentifier = companyIdentifier;
        this.AccountStateName = accountStateName;
        this.AccountMedalName = accountMedalName;
        this.ActivitiesName = activitiesName;
    }

    public Counterpart(Integer code) {
        this.Code = code;
    }

    public Integer getCode() {
        return Code;
    }

    public void setCode(Integer code) {
        this.Code = code;
    }

    @Override
    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    @Override
    public void setID(String ID) {
this.ID = ID;
    }

    public void setName(String name) {
        this.Name = name;
    }



     public String getAccountCashboxName() {
        return AccountCashboxName;
    }

    public void setAccountCashboxName(String accountCashboxName) {
        this.AccountCashboxName = accountCashboxName;
    }

    public String getIsSolid() {
        return IsSolid;
    }

    public void setIsSolid(String isSolid) {
        this.IsSolid = isSolid;
    }

    public String getCompanyIdentifier() {
        return CompanyIdentifier;
    }

    public void setCompanyIdentifier(String companyIdentifier) {
        this.CompanyIdentifier = companyIdentifier;
    }

    public String getAccountStateName() {
        return AccountStateName;
    }

    public void setAccountStateName(String accountStateName) {
        this.AccountStateName = accountStateName;
    }

    public String getAccountMedalName() {
        return AccountMedalName;
    }

    public void setAccountMedalName(String accountMedalName) {
        this.AccountMedalName = accountMedalName;
    }

    public String getActivitiesName() {
        return ActivitiesName;
    }

    public void setActivitiesName(String activitiesName) {
        this.ActivitiesName = activitiesName;
    }


    @Override
    public List<Counterpart> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        String whereClause = "";
        String valueClause = "";

        switch ((AccountSearchType) searchType) {
            case CODE:
                whereClause = " WHERE [tbl_Account].[Code] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;

            case NAME:

                valueClause = "%" + value.toString() + "%";
                whereClause = " WHERE [tbl_Account].[Name] LIKE '%" + value.toString() + "%'";
                break;
            case ACCOUNTCASHBOXNAME:
                whereClause = " WHERE [tbl_AccountCashbox].[Identifier] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case ISSOLID:
                whereClause = " WHERE [tbl_Account].[IsSolid] LIKE '%" + value.toString() + "%'";
                valueClause = "'%" + value.toString() + "%'";
                break;
            case COMPANYIDENTIFIER:
                whereClause = " WHERE [tbl_AccountCashbox].[CompanyIdentifier] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case ACCOUNTSTATENAME:
                whereClause = " WHERE [tbl_AccountState].[Name] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case ACCOUNTMEDALNAME:
                whereClause = " WHERE [tbl_AccountMedal].[Name] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case ACTIVITIESNAME:
                whereClause = " WHERE [tbl_Activities].[Name] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;

        }

        try {
            return dbAccess.query(DBConnection.getDataSource().getConnection(), accountQueries.getMainAccount(false, 0) + whereClause, new BeanListHandler<Counterpart>(Counterpart.class));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Column filter exception (findAccountByProperty): " + e);
        }

        return EMPTYLIST;
    }

    @Override
    public ObservableList<Counterpart> findAll(boolean pagination, int rowIndex) {
        String query = (pagination ? accountQueries.getMainAccount(true, rowIndex) : accountQueries.getMainAccount(false, 0));
        try {
            return FXCollections.observableArrayList(dbAccess.query(DBConnection.getDataSource().getConnection(), query, new BeanListHandler<Counterpart>(Counterpart.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }




     @Override
    public String toString() {
        return getName();
    }

 /*   public String printInfo() {
        return
                "\n name - " + name +
                        "\n officialAccountName - " + officialAccountName +
                        "\n accountTypeID - " + accountTypeID +
                        "\n VATPayerID - " + VATPayerID +
                        "\n paymentTypeID - " + paymentTypeID +
                        "\n taxationID - " + taxationID +
                        "\n BranchCode - " + BranchCode +
                        "\n evidenceRegistryNumber - " + evidenceRegistryNumber +
                        "\n individualOrdinalNumber - " + individualOrdinalNumber +
                        "\n accountProblemID - " + accountProblemID +
                        "\n specialMarginTypeID - " + specialMarginTypeID +
                        "\n evidenceDocument - " + evidenceDocument +
                        "\n bankName - " + bankName +
                        "\n bankInvoice - " + bankInvoice +
                        "\n accountProblemDate - " + accountProblemDate +
                        "\n accountMedalID - " + AccountMedalID +
                        "\n accountDiscount - " + accountDiscount +
                        "\n promiceSel - " + promiceSel +
                        "\n delay - " + delay +
                        "\n creditSel - " + creditSel +
                        "\n limit - " + limit +
                        "\n saldoSel - " + saldoSel +
                        "\n remark - " + remark +
                        "\n contractFOPRepairFrom - " + contractFOPRepairFrom +
                        "\n contractFOPFrom - " + contractFOPFrom +
                        "\n contractLLCFrom - " + contractLLCFrom +
                        "\n ContractLLCTTFrom - " + ContractLLCTTFrom +
                        "\n contractFOPRepairNumber - " + contractFOPRepairNumber +
                        "\n contractFOPNumber - " + contractFOPNumber +
                        "\n contractLLCNumber - " + contractLLCNumber +
                        "\n ContractLLCTTNumber - " + ContractLLCTTNumber +
                        "\n contractFOPRepairReceivedID - " + contractFOPRepairReceivedID +
                        "\n contractFOPREpairReceivedName - " + contractFOPRepairReceivedName +
                        "\n contractFOPReceivedID - " + contractFOPReceivedID +
                        "\n contractLLCReceivedID - " + contractLLCReceivedID +
                        "\n contractLLCTTReceivedID - " + contractLLCTTReceivedID +
                        "\n ownerID - " + ownerID +
                        "\n ownerManagerID - " + ownerManagerID +
                        "\n discountSTO - " + discountSTO +
                        "\n communication1TypeID - " + communication1TypeID +
                        "\n communication2TypeID - " + communication2TypeID +
                        "\n communication3TypeID - " + communication3TypeID +
                        "\n communication4TypeID - " + communication4TypeID +
                        "\n communication5TypeID - " + communication5TypeID +
                        "\n communication1TypeName - " + communication1TypeName +
                        "\n communication2TypeName - " + communication2TypeName +
                        "\n communication3TypeName - " + communication3TypeName +
                        "\n communication4TypeName - " + communication4TypeName +
                        "\n communication5TypeName - " + communication5TypeName +
                        "\n communication1 - " + communication1 +
                        "\n communication2 - " + communication2 +
                        "\n communication3 - " + communication3 +
                        "\n communication4 - " + communication4 +
                        "\n communication5 - " + communication5 +
                        "\n street - " + street +
                        "\n locality - " + locality +
                        "\n district - " + district +
                        "\n region - " + region +
                        "\n ZIPCode - " + ZIPCode;
    }*/


}
