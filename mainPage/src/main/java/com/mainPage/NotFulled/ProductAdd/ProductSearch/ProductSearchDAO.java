package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import com.Utils.SearchType;
import com.connectDatabase.DBConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductSearchDAO implements ProductSearchInterface {
    private static Logger log = Logger.getLogger(ProductSearchDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private ProductQuery queries = new ProductQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";


    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setID(String ID) {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public boolean insertAccount(ProductSearch offeringRequest) {
        return false;
    }

    @Override
    public boolean updateAccount(ProductSearch offeringRequest) {
        return false;
    }

    @Override
    public boolean deleteAccount(ProductSearch offeringRequest) {
        return false;
    }

    @Override
    public List findByProperty(Object value, Enum<? extends SearchType> searchType) {


        String whereClause = "";
        String valueClause = "";



        switch ((ProductSearchType) searchType) {
            case INDEX:
                whereClause = " WHERE [tbl_Offering].[Index] = '" + value.toString() + "'";
                valueClause = value.toString();
                break;

            case ISHIDDENSKRUT:
                whereClause = " WHERE [tbl_Offering].[Skrut] LIKE '" + value.toString() + "%'";
                valueClause =  value.toString();
                break;
            case SKRUT:
                whereClause = " WHERE [tbl_Offering].[Skrut] LIKE '" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case OFFERINGNAME:
                whereClause = " WHERE [tbl_Offering].[Name] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
          /*  case HASANALOGUE:
                whereClause = " WHERE [tbl_Offering].[HasAnalogue] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case DEFAULTOFFERINGCODE:
                whereClause = " WHERE [tbl_Offering].[DefaultOfferingCode] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case OFFERINGTYPEID:
                whereClause = " WHERE [tbl_Offering].[OfferingTypeID] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case SUPLIERID:
                whereClause = " WHERE [tbl_Offering].[SupplierID] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case OWNERID:
                whereClause = " WHERE [tbl_Offering].[OwnerID] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;

            case NAMEPOLISH:

                valueClause = "%" + value.toString() + "%";
                whereClause = " WHERE [tbl_Offering].[NamePolish] LIKE '%" + value.toString() + "%'";
                break;
            case REMARK:
                whereClause = " WHERE [tbl_Offering].[Remark] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case OFFERINGVENDORID:
                whereClause = " WHERE [tbl_Offering].[OfferingVendorID] LIKE '%" + value.toString() + "%'";
                valueClause = "'%" + value.toString() + "%'";
                break;
            case OFFERINGVENDORNAME:
                whereClause = " WHERE [tbl_Offering].[OfferingVendorID] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case PRICENOTFROMEUROID:
                whereClause = " WHERE [tbl_Offering].[PriceNotFromEUR] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case CUSTOMSCODE:
                whereClause = " WHERE [tbl_Offering].[CustomsCode] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case OFFERINGANALOGUE:
                whereClause = " WHERE [tbl_Offering].[OfferingAnalogue] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case NAMEWMD:
                whereClause = " WHERE [tbl_Offering].[NameWMD] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case CLEANOFFERINGCODE:
                whereClause = " WHERE [tbl_Offering].[CleanOfferingCode] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case OFFERINGPHOTO:
                whereClause = " WHERE [tbl_OfferingPhoto].[OfferingPhoto] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case HIDDENSKRUT:
                whereClause = " WHERE [tbl_Offering].[HiddenSkrut] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;
            case SEASONINDEX:
                whereClause = " WHERE [tbl_Offering].[SeasonIndex] LIKE '%" + value.toString() + "%'";
                valueClause = "%" + value.toString() + "%";
                break;*/

        }

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            return FXCollections.observableArrayList(dbAccess.query(connection, queries.getMainProductSearch(false, 0) + whereClause, new BeanListHandler<ProductSearch>(ProductSearch.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Column filter exception (findAccountByProperty): " + e);
        }

        return EMPTYLIST;
    }




    @Override
    public ObservableList<ProductSearch> findAll(boolean pagination, int rowIndex) {
        String query = (pagination ? queries.getMainProductSearch(true, rowIndex) : queries.getMainProductSearch(false, 0));
        try (Connection connection = DBConnection.getDataSource().getConnection() ){
            return FXCollections.observableArrayList(dbAccess.query(connection, query, new BeanListHandler<ProductSearch>(ProductSearch.class)));
        } catch (Exception e) {

            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }

}
