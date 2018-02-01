package com.mainPage.NotFulled.ProductAdd.ProductSearch;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import org.apache.commons.dbutils.QueryRunner;
import com.Utils.SearchType;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ProductSearch implements ProductSearchInterface {
    private static Logger log = Logger.getLogger(ProductSearchDAO.class.getName());

    private QueryRunner dbAccess = new QueryRunner();
    private ProductQuery queries = new ProductQuery();
    private static final List EMPTYLIST = new ArrayList<>();
    private static final String EMPTYSTRING = "";


    private String ID;
    private SimpleStringProperty offeringID;
    private SimpleStringProperty offeringName;
    private SimpleStringProperty customsCode;
    private SimpleIntegerProperty index;
    private SimpleStringProperty skrut;
    private SimpleStringProperty isHiddenSkrut;
    private SimpleStringProperty offeringPhoto;
    private SimpleDoubleProperty saledByYear;
    private SimpleIntegerProperty seasonIndex;
    private SimpleStringProperty offeringTypeID;
    private SimpleStringProperty offeringTypeName;
    private SimpleStringProperty offeringBrandCode;
    private SimpleDoubleProperty available;
    public ProductSearch() {
    }
    //Getters

  //  public String getID () {return ID.getValue();}
    public String getOfferingID (){return offeringID.getValue();}
    public String getOfferingName () {return offeringName.getValue();}
    public String getCustomsCode () {return customsCode.getValue();}
    public Integer getIndex () {return index.getValue();}
    public String getSkrut () {return skrut.getValue();}
    public String getIsHiddenSkrut () {return isHiddenSkrut.getValue();}
    public String getOfferingPhoto () {return offeringPhoto.getValue();}
    public Double getSaledByYear () {return saledByYear.getValue();}
    public Integer getSeasonIndex () {return seasonIndex.getValue();}
    public String getOfferingTypeID () {return offeringTypeID.getValue();}
    public String getOfferingTypeName () {return offeringTypeName.getValue();}
    public String getOfferingBrandCode () {return offeringBrandCode.getValue();}
    public Double getAvailable () {return available.getValue();}

    //Setters

  /*  public void setID(String ID) {
        this.ID = new SimpleStringProperty(ID);
    }*/
    public void setOfferingID (String offeringID) {this.offeringID = new SimpleStringProperty(offeringID);}
    public void setOfferingName (String offeringName) {this.offeringName = new SimpleStringProperty(offeringName);}
    public void setCustomsCode (String customsCode) {this.customsCode = new SimpleStringProperty(customsCode);}
    public void setIndex (Integer index) {this.index = new SimpleIntegerProperty(index);}
    public void setSkrut (String skrut) {this.skrut = new SimpleStringProperty(skrut);}
    public void setIsHiddenSkrut (String isHiddenSkrut) {this.isHiddenSkrut = new SimpleStringProperty(isHiddenSkrut);}
    public void setOfferingPhoto (String offeringPhoto) {this.offeringPhoto = new SimpleStringProperty(offeringPhoto);}
    public void setSaledByYear (Double saledByYear) {this.saledByYear = new SimpleDoubleProperty(saledByYear);}
    public void setSeasonIndex (Integer seasonIndex) {this.seasonIndex = new SimpleIntegerProperty(seasonIndex);}
    public void setOfferingTypeID (String offeringTypeID) {this.offeringTypeID = new SimpleStringProperty(offeringTypeID);}
    public void setOfferingTypeName (String offeringTypeName) {this.offeringTypeName = new SimpleStringProperty(offeringTypeName);}
    public void setOfferingBrandCode (String offeringBrandCode) {this.offeringBrandCode = new SimpleStringProperty(offeringBrandCode);}
    public void setAvailable (Double available) {this.available = new SimpleDoubleProperty(available);}


    @Override
    public String getID() {
        return ID;
    }
    @Override
    public String getName () {
        return getOfferingName();
    }
    @Override
    public void setID (String ID) {
        this.ID = ID;
    }
    @Override
    public void setName (String name){this.offeringName = new SimpleStringProperty(name);
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
        return null;
    }

    @Override
    public ObservableList<ProductSearch> findAll(boolean pagination, int rowIndex) {
        return null;
    }

    @Override
    public String toString() {
        return getName();
    }

   /* @Override
    public String getID() {
        return null;
    }*/

   /* @Override
    public List<? extends ProductSearchInterface> findAll(boolean pagination, int rowIndex) {
        String query = (pagination ? accountQueries1.getMainProductSearch(true, rowIndex) : accountQueries1.getMainProductSearch(false, 0));
        try {
            return dbAccess.query(DBConnection.getDataSource().getConnection(), accountQueries1.getMainProductSearch(true, 10), new BeanListHandler<>(ProductSearch.class));
        } catch (Exception e) {
            log.log(Level.SEVERE, "getAccount exception: " + e);
        }

        return FXCollections.observableArrayList(EMPTYLIST);
    }
*/

  /*  @Override
    public List<? extends ProductSearchInterface> findByProperty(Object value, Enum<? extends SearchType> searchType) {
        String whereClause = "";
        String valueClause = "";
        ObservableList<ProductSearch> list = FXCollections.observableArrayList(EMPTYLIST);

        if(value == null) return list;
        switch ((ProductSearchType) searchType) {
            case INDEX:
                whereClause = " WHERE [tbl_Offering].[Index] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;

            case ISHIDDENSKRUT:

                valueClause = "%" + value.toString() + "%";
                whereClause = " WHERE [tbl_Offering].[HiddenSkrut] LIKE '%" + value.toString() + "%'";
                break;
            case SKRUT:
                whereClause = " WHERE [tbl_Offering].[Skrut] LIKE '%" + value.toString() + "%'";
                valueClause = value.toString();
                break;
            case OFFERINGNAME:
                whereClause = " WHERE [tbl_Offering].[Name] LIKE '%" + value.toString() + "%'";
                valueClause = "'%" + value.toString() + "%'";
                break;
            case HASANALOGUE:
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
                break;

        }

        try (Connection connection = DBConnection.getDataSource().getConnection()) {
            list = FXCollections.observableArrayList(dbAccess.query(connection, accountQueries1.getMainProductSearch(false, 0) + whereClause, new BeanListHandler<ProductSearch>(ProductSearch.class)));
        } catch (Exception e) {
            log.log(Level.SEVERE, "Column filter exception (findAccountByProperty): " + e);
        }

        return EMPTYLIST;
    }*/

   /* @Override
    public String toString() {
        return getName();
    }*/

}
