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
        public String toString () {
        return getName();
    }

}
