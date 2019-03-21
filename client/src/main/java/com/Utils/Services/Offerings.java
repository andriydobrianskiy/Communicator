package com.Utils.Services;

import javafx.beans.property.*;

public class Offerings extends GridComp {

    private SimpleStringProperty ID;
    private SimpleStringProperty offeringID;
    private SimpleStringProperty createdOn;
    private SimpleStringProperty modifiedOn;
    private SimpleStringProperty createdByID;
    private SimpleStringProperty modifiedByID;
    private SimpleStringProperty ownerID;
    private SimpleStringProperty offeringName;
    private SimpleStringProperty currencyID;
    private SimpleStringProperty defaultUnitID;
    private SimpleStringProperty defaultUnit;
    private SimpleStringProperty defaultUnitEng;
    private SimpleStringProperty comment;
    private SimpleDoubleProperty remain;
    private SimpleDoubleProperty remainOnStore;
    private SimpleStringProperty namePolish;
    private SimpleStringProperty customsCode;
    private SimpleStringProperty remark;
    private SimpleIntegerProperty index;
    private SimpleStringProperty skrut;
    private SimpleStringProperty isHiddenSkrut;
    private SimpleStringProperty offeringVendorID;
    private SimpleStringProperty offeringVendorName2;
    private SimpleStringProperty priceNotFromEuroID;
    private SimpleStringProperty priceNotFromEuroName;
    private SimpleStringProperty defaultOfferingCode;
    private SimpleStringProperty hasAnalogue;
    private SimpleStringProperty cleanOfferingCode;
    private SimpleLongProperty offeringColor;
    private SimpleStringProperty offeringPhoto;
    private SimpleDoubleProperty complectation;
    private SimpleDoubleProperty normalHour;
    private SimpleStringProperty hiddenSkrut;
    private SimpleIntegerProperty isHitProduct;
    private SimpleIntegerProperty isFakeSale;
    private SimpleIntegerProperty hasPhoto;
    private SimpleIntegerProperty isHot;
    private SimpleStringProperty offeringTypeID;
    private SimpleStringProperty offeringTypeName;
    private SimpleStringProperty supplierID;
    private SimpleStringProperty supplierName;
    private SimpleStringProperty nameEnglish;
    private SimpleDoubleProperty price;
    private SimpleDoubleProperty priceLoad;
    private SimpleDoubleProperty priceExtra;
    private SimpleDoubleProperty basicPrice;
    private SimpleDoubleProperty basicPriceLoad;
    private SimpleDoubleProperty basicPriceExtra;
    private SimpleDoubleProperty minSalePrice;
    private SimpleDoubleProperty minSaleBasicPrice0;
    private SimpleDoubleProperty minsaleBasicPrice1;
    private SimpleDoubleProperty maxSalePrice;
    private SimpleDoubleProperty maxSaleBasicPrice0;
    private SimpleDoubleProperty maxSaleBasicPrice1;
    private SimpleDoubleProperty cPriceVip;
    private SimpleDoubleProperty cBasicPriceVip0;
    private SimpleDoubleProperty cBasicPriceVip1;
    private SimpleDoubleProperty cPriceZero;
    private SimpleDoubleProperty cBasicPriceZero0;
    private SimpleDoubleProperty cBasicPriceZero1;
    private SimpleDoubleProperty cPriceOne;
    private SimpleDoubleProperty cBasicPriceOne0;
    private SimpleDoubleProperty cBasicPriceOne1;
    private SimpleDoubleProperty cPriceTwo;
    private SimpleDoubleProperty cBasicPriceTwo0;
    private SimpleDoubleProperty cBasicPriceTwo1;
    private SimpleDoubleProperty maxDiscountOne;
    private SimpleDoubleProperty maxDiscountTwo;
    private SimpleDoubleProperty minimumMargin;
    private SimpleDoubleProperty maximumMargin;
    private SimpleDoubleProperty specialMarginVip;
    private SimpleDoubleProperty specialMarginZero;
    private SimpleDoubleProperty specialMarginOne;
    private SimpleDoubleProperty specialmarginTwo;
    private SimpleIntegerProperty isStandardHours;
    private SimpleIntegerProperty isUsed;
    private SimpleStringProperty offeringBrandCode;

    private BooleanProperty hiddenSkrutActive = new SimpleBooleanProperty();
    private BooleanProperty standardHoursActive = new SimpleBooleanProperty();
    private BooleanProperty hitProductActive = new SimpleBooleanProperty();
    private BooleanProperty fakeSaleActive = new SimpleBooleanProperty();
    private BooleanProperty hasPhotoActive = new SimpleBooleanProperty();
    private BooleanProperty hotActive = new SimpleBooleanProperty();
    private BooleanProperty isUsedActive = new SimpleBooleanProperty();


    public void setRemain(double remain) {
        this.remain.set(remain);
    }


    public void setHotActive(boolean hotActive) {
        this.hotActive.set(hotActive);
    }

    public SimpleDoubleProperty remainProperty() {

        return remain;
    }

    public SimpleIntegerProperty isFakeSaleProperty() {
        return isFakeSale;
    }

    public void setIsFakeSale(int isFakeSale) {
        this.isFakeSale.set(isFakeSale);
    }

    @Override
    public String getID() { try { return ID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getOfferingID() { try { return offeringID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getCreatedOn() { try { return createdOn.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getModifiedOn() { try { return modifiedOn.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getCreatedByID() { try { return createdByID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getModifiedByID() { try { return modifiedByID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getOwnerID() { try { return ownerID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getCurrencyID() {try { return currencyID.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getDefaultUnitID() {try { return defaultUnitID.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getDefaultUnit() {try { return defaultUnit.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getDefaultUnitEng() {try { return defaultUnitEng.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getComment() { try { return comment.getValue(); } catch (NullPointerException ex) { return null; } }
    public Double getRemainOnStore() {try { return remainOnStore.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getNamePolish() {try { return namePolish.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getCustomsCode() {try { return customsCode.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getRemark() {try { return remark.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getIndex() {try { return index.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getSkrut() {try { return skrut.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getIsHiddenSkrut() {try { return isHiddenSkrut.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getOfferingVendorID() {try { return offeringVendorID.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getOfferingVendorName2() {try { return offeringVendorName2.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getPriceNotFromEuroID() {try { return priceNotFromEuroID.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getPriceNotFromEuroName() {try { return priceNotFromEuroName.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getDefaultOfferingCode() {try { return defaultOfferingCode.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getHasAnalogue() {try { return hasAnalogue.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getCleanOfferingCode() {try { return cleanOfferingCode.getValue(); } catch (NullPointerException ex) { return null; }}
    public Long getOfferingColor() {try { return offeringColor.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getOfferingBrandCode() {try { return offeringBrandCode.getValue(); } catch (NullPointerException ex) { return null; } }

    public String getOfferingPhoto() {try { return offeringPhoto.getValue(); } catch (NullPointerException ex) { return null; }}


    public Double getRemain() { try { return remain.getValue(); } catch (NullPointerException ex) { return null; } }
    public Double getComplectation() {try { return complectation.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getHiddenSkrut() {try { return hiddenSkrut.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getIsHitProduct() {try { return isHitProduct.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getIsFakeSale() {try { return isFakeSale.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getHasPhoto() {try { return hasPhoto.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getIsHot() {try { return isHot.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getOfferingTypeID() { try { return offeringTypeID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getOfferingTypeName() {try { return offeringTypeName.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getSupplierID() {try { return supplierID.getValue(); } catch (NullPointerException ex) { return null; } }
    public String getSupplierName() { try { return supplierName.getValue(); } catch (NullPointerException ex) { return null; }}
    public String getNameEnglish() { try { return nameEnglish.getValue(); } catch (NullPointerException ex) { return null; } }
    public Double getPrice() {try { return price.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getPriceLoad() {try { return priceLoad.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getPriceExtra() {try { return priceExtra.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getBasicPrice() {try { return basicPrice.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getBasicPriceLoad() {try { return basicPriceLoad.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getBasicPriceExtra() {try { return basicPriceExtra.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMinSalePrice() {try { return minSalePrice.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMinSaleBasicPrice0() {try { return minSaleBasicPrice0.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMinsaleBasicPrice1() {try { return minsaleBasicPrice1.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMaxSalePrice() {try { return maxSalePrice.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMaxSaleBasicPrice0() {try { return maxSaleBasicPrice0.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMaxSaleBasicPrice1() {try { return maxSaleBasicPrice1.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCPriceVip() {try { return cPriceVip.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceVip0() {try { return cBasicPriceVip0.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceVip1() {try { return cBasicPriceVip1.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCPriceZero() {try { return cPriceZero.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceZero0() {try { return cBasicPriceZero0.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceZero1() {try { return cBasicPriceZero1.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCPriceOne() {try { return cPriceOne.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceOne0() {try { return cBasicPriceOne0.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceOne1() {try { return cBasicPriceOne1.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCPriceTwo() {try { return cPriceTwo.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceTwo0() {try { return cBasicPriceTwo0.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getCBasicPriceTwo1() {try { return cBasicPriceTwo1.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMaxDiscountOne() {try { return maxDiscountOne.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMaxDiscountTwo() {try { return maxDiscountTwo.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMinimumMargin() {try { return minimumMargin.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getMaximumMargin() {try { return maximumMargin.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getSpecialMarginVip() {try { return specialMarginVip.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getSpecialMarginZero() {try { return specialMarginZero.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getSpecialMarginOne() {try { return specialMarginOne.getValue(); } catch (NullPointerException ex) { return null; }}
    public Double getSpecialMarginTwo() {try { return specialmarginTwo.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getIsStandardHours() {try { return isStandardHours.getValue(); } catch (NullPointerException ex) { return null; }}
    public Integer getIsUsed() { try { return isUsed.getValue(); } catch (NullPointerException ex) { return null; } }

    @Override
    public void setID(String ID) {this.ID = new SimpleStringProperty(ID); }
    public void setOfferingID(String offeringID) {this.offeringID = new SimpleStringProperty(offeringID);}
    public void setCreatedOn(String createdOn) { this.createdOn = new SimpleStringProperty(createdOn); }
    public void setModifiedOn(String modifiedOn) { this.modifiedOn = new SimpleStringProperty(modifiedOn); }
    public void setCreatedByID(String createdByID) { this.createdByID = new SimpleStringProperty(createdByID); }
    public void setModifiedByID(String modifiedByID) { this.modifiedByID = new SimpleStringProperty(modifiedByID); }
    public void setOwnerID(String ownerID) { this.ownerID = new SimpleStringProperty(ownerID); }
    public void setCurrencyID(String currencyID) {this.currencyID = new SimpleStringProperty(currencyID);}
    public void setDefaultUnitID(String defaultUnitID) {this.defaultUnitID = new SimpleStringProperty(defaultUnitID); }
    public void setDefaultUnit(String defaultUnit) {this.defaultUnit = new SimpleStringProperty(defaultUnit);}
    public void setDefaultUnitEng(String defaultUnitEng) {this.defaultUnitEng = new SimpleStringProperty(defaultUnitEng);}
    public void setComment(String comment) { this.comment = new SimpleStringProperty(comment); }
    public void setNamePolish(String namePolish) {this.namePolish = new SimpleStringProperty(namePolish);}
    public void setCustomsCode(String customsCode) {this.customsCode = new SimpleStringProperty(customsCode);}
    public void setRemainOnStore(Double remainOnStore) { this.remainOnStore = new SimpleDoubleProperty(remainOnStore); }
    public void setRemain(Double remain) { this.remain = new SimpleDoubleProperty(remain); }
    public void setRemark(String remark) {this.remark = new SimpleStringProperty(remark);}
    public void  setIndex(Integer index) {this.index = new SimpleIntegerProperty(index);}
    public void setSkrut(String skrut) {this.skrut = new SimpleStringProperty(skrut);}
    public void setIsHiddenSkrut(String isHiddenSkrut) {this.isHiddenSkrut = new SimpleStringProperty(isHiddenSkrut);}
    public void setOfferingVendorID(String offeringVendorID) {this.offeringVendorID = new SimpleStringProperty(offeringVendorID);}
    public void setOfferingVendorName2(String offeringVendorName2) {this.offeringVendorName2 = new SimpleStringProperty(offeringVendorName2);}
    public void setPriceNotFromEuroID(String priceNotFromEuroID) {this.priceNotFromEuroID = new SimpleStringProperty(priceNotFromEuroID);}
    public void setPriceNotFromEuroName(String priceNotFromEuroName) {this.priceNotFromEuroName = new SimpleStringProperty(priceNotFromEuroName);}
    public void setDefaultOfferingCode(String defaultOfferingCode) {this.defaultOfferingCode = new SimpleStringProperty(defaultOfferingCode);}
    public void setHasAnalogue(String hasAnalogue) {this.hasAnalogue = new SimpleStringProperty(hasAnalogue);}
    public void setCleanOfferingCode(String cleanOfferingCode) {this.cleanOfferingCode = new SimpleStringProperty(cleanOfferingCode);}
    public void  setOfferingColor(long offeringColor) {this.offeringColor = new SimpleLongProperty(offeringColor);}
    public void setOfferingBrandCode(String offeringBrandCode) { this.offeringBrandCode = new SimpleStringProperty(offeringBrandCode); }
    public void setOfferingPhoto(String offeringPhoto) {this.offeringPhoto = new SimpleStringProperty(offeringPhoto);}








    public SimpleDoubleProperty normalHourProperty() {
        return normalHour;
    }
    public void setNormalHour(double normalHour) {
        this.normalHour.set(normalHour);
    }
    public void setNormalHour(Double normalHour) { this.normalHour = new SimpleDoubleProperty(normalHour); }
    public Double getNormalHour() { try { return normalHour.getValue(); } catch (NullPointerException ex) { return null; } }

    public SimpleStringProperty offeringNameProperty() {
        return offeringName;
    }
    public void setOfferingName(String offeringName) { this.offeringName = new SimpleStringProperty(offeringName); }
    public String getOfferingName() { try { return offeringName.getValue(); } catch (NullPointerException ex) { return null; } }




    public SimpleDoubleProperty remainOnStoreProperty() {
        return remainOnStore;
    }

    public void setRemainOnStore(double remainOnStore) {
        this.remainOnStore.set(remainOnStore);
    }

    public SimpleDoubleProperty maxSaleBasicPrice0Property() {
        return maxSaleBasicPrice0;
    }

    public void setMaxSaleBasicPrice0(double maxSaleBasicPrice0) {
        this.maxSaleBasicPrice0.set(maxSaleBasicPrice0);
    }

    public SimpleDoubleProperty maxSaleBasicPrice1Property() {
        return maxSaleBasicPrice1;
    }

    public void setMaxSaleBasicPrice1(double maxSaleBasicPrice1) {
        this.maxSaleBasicPrice1.set(maxSaleBasicPrice1);
    }

    public void setComplectation(Double complectation) {this.complectation = new SimpleDoubleProperty(complectation);}
    public void setOfferingTypeID(String offeringTypeID) { this.offeringTypeID = new SimpleStringProperty(offeringTypeID); }
    public void setOfferingTypeName(String offeringTypeName) {this.offeringTypeName = new SimpleStringProperty(offeringTypeName);}
    public void setSupplierID(String supplierID) { this.supplierID = new SimpleStringProperty(supplierID); }
    public void setSupplierName(String supplierName) { this.supplierName = new SimpleStringProperty(supplierName); }
    public void setNameEnglish(String nameEnglish) { this.nameEnglish = new SimpleStringProperty(nameEnglish); }
    public void setPrice(Double price) {this.price = new SimpleDoubleProperty(price);}
    public void setPriceLoad(Double priceLoad) {this.priceLoad = new SimpleDoubleProperty(priceLoad);}
    public void setPriceExtra(Double priceExtra) {this.priceExtra = new SimpleDoubleProperty(priceExtra);}
    public void setBasicPrice(Double basicPrice) {this.basicPrice = new SimpleDoubleProperty(basicPrice);}
    public void setBasicPriceLoad(Double basicPriceLoad) {this.basicPriceLoad = new SimpleDoubleProperty(basicPriceLoad);}
    public void setBasicPriceExtra(Double basicPriceExtra) {this.basicPriceExtra = new SimpleDoubleProperty(basicPriceExtra);}
    public void setMinSalePrice(Double minSalePrice) {this.minSalePrice = new SimpleDoubleProperty(minSalePrice);}
    public void setMinSaleBasicPrice0(Double minSaleBasicPrice0) {this.minSaleBasicPrice0 = new SimpleDoubleProperty(minSaleBasicPrice0);}
    public void setMinsaleBasicPrice1(Double minSaleBasicPrice1) {this.minsaleBasicPrice1 = new SimpleDoubleProperty(minSaleBasicPrice1);}
    public void setMaxSalePrice(Double maxSalePrice) {this.maxSalePrice = new SimpleDoubleProperty(maxSalePrice);}
    public void setMaxSaleBasicPrice0(Double maxSaleBasicPrice0) {this.maxSaleBasicPrice0 = new SimpleDoubleProperty(maxSaleBasicPrice0);}
    public void setMaxSaleBasicPrice1(Double maxSaleBasicPrice1) {this.maxSaleBasicPrice1 = new SimpleDoubleProperty(maxSaleBasicPrice1);}
    public void setCPriceVip(Double cPriceVip) {this.cPriceVip = new SimpleDoubleProperty(cPriceVip);}
    public void setCBasicPriceVip0(Double cBasicPriceVip0) {this.cBasicPriceVip0 = new SimpleDoubleProperty(cBasicPriceVip0);}
    public void setCBasicPriceVip1(Double cBasicPriceVip1) {this.cBasicPriceVip1 = new SimpleDoubleProperty(cBasicPriceVip1);}
    public void setCPriceZero(Double cPriceZero) {this.cPriceZero = new SimpleDoubleProperty(cPriceZero);}
    public void setCBasicPriceZero0(Double cBasicPriceZero0) {this.cBasicPriceZero0 = new SimpleDoubleProperty(cBasicPriceZero0);}
    public void setCBasicPriceZero1(Double cBasicPriceZero1) {this.cBasicPriceZero1 = new SimpleDoubleProperty(cBasicPriceZero1);}
    public void setCPriceOne(Double cPriceOne) {this.cPriceOne = new SimpleDoubleProperty(cPriceOne);}
    public void setCBasicPriceOne0(Double cBasicPriceOne0) {this.cBasicPriceOne0 = new SimpleDoubleProperty(cBasicPriceOne0);}
    public void setCBasicPriceOne1(Double cBasicPriceOne1) {this.cBasicPriceOne1 = new SimpleDoubleProperty(cBasicPriceOne1);}
    public void setCPriceTwo(Double cPriceTwo) {this.cPriceTwo = new SimpleDoubleProperty(cPriceTwo);}
    public void setCBasicPriceTwo0(Double cBasicPriceTwo0) {this.cBasicPriceTwo0 = new SimpleDoubleProperty(cBasicPriceTwo0);}
    public void setCBasicPriceTwo1(Double cBasicPriceTwo1) {this.cBasicPriceTwo1 = new SimpleDoubleProperty(cBasicPriceTwo1);}
    public void setMaxDiscountOne(Double maxDiscountOne) {this.maxDiscountOne = new SimpleDoubleProperty(maxDiscountOne);}
    public void setMaxDiscountTwo(Double maxDiscountTwo) {this.maxDiscountTwo = new SimpleDoubleProperty(maxDiscountTwo);}
    public void setMinimumMargin(Double minimumMargin) {this.minimumMargin = new SimpleDoubleProperty(minimumMargin);}
    public void setMaximumMargin(Double maximumMargin) {this.maximumMargin = new SimpleDoubleProperty(maximumMargin);}
    public void setSpecialMarginVip(Double specialMarginVip) {this.specialMarginVip = new SimpleDoubleProperty(specialMarginVip);}
    public void setSpecialMarginZero(Double specialMarginZero) {this.specialMarginZero = new SimpleDoubleProperty(specialMarginZero);}
    public void setSpecialMarginOne(Double specialMarginOne) {this.specialMarginOne = new SimpleDoubleProperty(specialMarginOne);}
    public void setSpecialMarginTwo(Double specialMarginTwo) {this.specialmarginTwo = new SimpleDoubleProperty(specialMarginTwo);}

    public void  setIsStandardHours(Integer isStandardHours) {this.isStandardHours = new SimpleIntegerProperty(isStandardHours);}
    public void  setIsUsed(Integer isUsed) { this.isUsed = new SimpleIntegerProperty(isUsed); }
    public void  setHiddenSkrut(String hiddenSkrut) {this.hiddenSkrut = new SimpleStringProperty(hiddenSkrut);}
    public void  setIsHitProduct(Integer isHitProduct) {this.isHitProduct = new SimpleIntegerProperty(isHitProduct);}
    public void  setIsFakeSale(Integer isFakeSale) {this.isFakeSale = new SimpleIntegerProperty(isFakeSale);}
    public void  setHasPhoto(Integer hasPhoto) {this.hasPhoto = new SimpleIntegerProperty(hasPhoto);}
    public void  setIsHot(Integer isHot) {this.isHot = new SimpleIntegerProperty(isHot);}

    public BooleanProperty activePropertyIsHitProduct() {return hitProductActive ;}
    public BooleanProperty activePropertyIsFakeSale() {return fakeSaleActive ;}
    public BooleanProperty activePropertyHasPhoto() {return hasPhotoActive ;}
    public BooleanProperty activePropertyIsHot() {return hotActive ;}
    public BooleanProperty activePropertyIsStandardHours() {return standardHoursActive ;}
    public BooleanProperty activePropertyIsUsed() { return isUsedActive; }

    public final boolean isHitProductActive() {return activePropertyIsHitProduct().get();}
    public final boolean isFakeSaleActive() {return activePropertyIsFakeSale().get();}
    public final boolean hasPhotoActive() {return activePropertyHasPhoto().get();}
    public final boolean isHotActive() {return activePropertyIsHot().get();}
    public final boolean isStandardHoursActive() { return activePropertyIsStandardHours().get();}
    public final boolean isUsedActive() { return activePropertyIsUsed().get(); }

    public final void setIsHitProductActive(boolean active) {activePropertyIsHitProduct().set(active);}
    public final void setIsFakeSaleActive(boolean active) {activePropertyIsFakeSale().set(active);}
    public final void setHasPhotoActive(boolean active) {activePropertyHasPhoto().set(active);}
    public final void setIsHotActive(boolean active) {activePropertyIsHot().set(active);}
    public final void setIsStandardHoursActive(boolean active) {
        activePropertyIsStandardHours().set(active);
    }
    public final void setIsUsedActive(boolean active) { activePropertyIsUsed().set(active); }


    @Override
    public String toString() {
        return getOfferingName();
    }

    public String printInfo() {
        return
                " ID - " + ID + "\n" +
                        " offeringID - " + offeringID + "\n" +
                        " createdOn - " + createdOn + "\n" +
                        " modifiedOn - " + modifiedOn + "\n" +
                        " createdByID - " + createdByID + "\n" +
                        " modifiedByID - " + modifiedByID + "\n" +
                        " ownerID - " + ownerID + "\n" +
                        " offeringName - " + offeringName + "\n" +
                        " currencyID - " + currencyID + "\n" +
                        " defaultUnitID - " + defaultUnitID + "\n" +
                        " defaultUnit - " + defaultUnit + "\n" +
                        " defaultUnitEng - " + defaultUnitEng + "\n" +
                        " remain - " + remain + "\n" +
                        " remainOnStore - " + remainOnStore + "\n" +
                        " namePolish - " + namePolish + "\n" +
                        " customsCode - " + customsCode + "\n" +
                        " remark - " + remark + "\n" +
                        " comment - " + comment + "\n" +
                        " index - " +  index + "\n" +
                        " skrut - " + skrut + "\n" +
                        " isHiddenSkrut - " + isHiddenSkrut + "\n" +
                        " offeringVendorID - " + offeringVendorID + "\n" +
                        " offeringVendorName2 - " + offeringVendorName2 + "\n" +
                        " priceNotFromEuroID - " + priceNotFromEuroID + "\n" +
                        " priceNotFromEuroName - " + priceNotFromEuroName + "\n" +
                        " defaultOfferingCode - " + defaultOfferingCode + "\n" +
                        " hasAnalogue - " + hasAnalogue + "\n" +
                        " cleanOfferingCode - " + cleanOfferingCode + "\n" +
                        "offeringColor - " + offeringColor + "\n" +
                        " offeringPhoto - " + offeringPhoto + "\n" +
                        " complectation - " + complectation + "\n" +
                        " hiddenSkrut - " + hiddenSkrut + "\n" +
                        " isHitProduct - " +  isHitProduct + "\n" +
                        " isFakeSale - " +  isFakeSale + "\n" +
                        " hasPhoto - " +  hasPhoto + "\n" +
                        " isHot - " +  isHot + "\n" +
                        " offeringTypeID - " + offeringTypeID + "\n" +
                        " offeringTypeName - " + offeringTypeName + "\n" +
                        " supplierID - " + supplierID + "\n" +
                        " supplierName - " + supplierName + "\n" +
                        " price - " + price + "\n" +
                        " priceLoad - " + priceLoad + "\n" +
                        " priceExtra - " + priceExtra + "\n" +
                        " basicPrice - " + basicPrice + "\n" +
                        " basicPriceLoad - " + basicPriceLoad + "\n" +
                        " basicPriceExtra - " + basicPriceExtra + "\n" +
                        " minSalePrice - " + minSalePrice + "\n" +
                        " minSaleBasicPrice0 - " + minSaleBasicPrice0 + "\n" +
                        " minsaleBasicPrice1 - " + minsaleBasicPrice1 + "\n" +
                        " maxSalePrice;\n" + maxSalePrice + "\n" +
                        " maxSaleBasicPrice0 - " + maxSaleBasicPrice0 + "\n" +
                        " maxSaleBasicPrice1 - " + maxSaleBasicPrice1 + "\n" +
                        " cPriceVip - " + cPriceVip + "\n" +
                        " cBasicPriceVip0 - " + cBasicPriceVip0 + "\n" +
                        " cBasicPriceVip1 - " + cBasicPriceVip1 + "\n" +
                        " cPriceZero - " + cPriceZero + "\n" +
                        " cBasicPriceZero0 - " + cBasicPriceZero0 + "\n" +
                        " cBasicPriceZero1 - " + cBasicPriceZero1 + "\n" +
                        " cPriceOne - " + cPriceOne + "\n" +
                        " cBasicPriceOne0 - " + cBasicPriceOne0 + "\n" +
                        " cBasicPriceOne1 - " + cBasicPriceOne1 + "\n" +
                        " cPriceTwo - " + cPriceTwo + "\n" +
                        " cBasicPriceTwo0 - " + cBasicPriceTwo0 + "\n" +
                        " cBasicPriceTwo1 - " + cBasicPriceTwo1 + "\n" +
                        " maxDiscountOne - " + maxDiscountOne + "\n" +
                        " maxDiscountTwo - " + maxDiscountTwo + "\n" +
                        " minimumMargin - " + minimumMargin + "\n" +
                        " maximumMargin - " + maximumMargin + "\n" +
                        " specialMarginVip - "  + specialMarginVip + "\n" +
                        " specialMarginZero - " + specialMarginZero + "\n" +
                        " specialMarginOne - " + specialMarginOne + "\n" +
                        " specialmarginTwo - " + specialmarginTwo + "\n" +
                        " isStandardHours - " +  isStandardHours + "\n" +
                        " isUsed - " + isUsed + "\n" +
                        " offeringBrandCode - "  + offeringBrandCode + "\n";
    }


}
