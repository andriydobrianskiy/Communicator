package com.Utils.Services;

import com.Utils.SearchType;
import javafx.scene.control.TableColumn;

import java.util.List;

public interface OfferingsDAO {
    void showData();
    boolean insertOffering(Offerings account);
    boolean updateOffering(Offerings account);
    boolean deleteOffering(Offerings account);

    List<Offerings> findOfferingByProperty(SearchType searchType, Object value);
    List<Offerings> findAll(int rowIndex);

    void setStringFilter(TableColumn column, String value);
    String getStringFilter();
    void removeStringFilter(TableColumn key);
}
