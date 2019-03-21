package com.mainPage.tableutils;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import org.google.jhsheets.filtered.FilteredTableView;

import java.util.logging.Logger;

public class CustomTableView extends FilteredTableView {
    private static Logger log = Logger.getLogger(CustomTableView.class.getName());
    private int selectedRow = 0;
    private Object selectedItem;
    private ObservableList<?> value;

    private TableColumn sortColumn;
    private TableColumn.SortType sortType;

    public void setTableItems(ObservableList<?> value) {
        this.value = value;

        setupOptions();
    }

    private void setupOptions() {
        try {
            getSortableColumn();

            super.setItems(value);

            setSortableColumn();
            setSelectedItem();

        } catch (Exception e) {
            super.setItems(value);
        }
    }

    private void getSortableColumn() {
        try {
            sortColumn = (TableColumn) getSortOrder().get(0);
            sortType = sortColumn.getSortType();
        } catch (IndexOutOfBoundsException ex) {
            // do nothing
        }
    }

    private Object getSelectedItem() {
        try {
          return getSelectionModel().getSelectedItem();
        } catch (Exception e) {
         return  null;
        }
    }

    private void setSortableColumn() {
        try {
            if(sortColumn == null) return;

            getSortOrder().add(sortColumn);
            sortColumn.setSortType(sortType);
            sortColumn.setSortable(true);
        } catch (Exception e) {

        }
    }

    private void setSelectedItem() {
        try {
            this.getSelectionModel().select(selectedRow);
            this.getFocusModel().focus(selectedRow);


        } catch (Exception e) {

        }
    }

    public void clearData() {

        getSelectedItem();

        value.clear();
    }
    public void setSelectedRow() {
        try {
            if(getSelectedItem() == null) return;

            selectedRow = this.getSelectionModel().getSelectedIndex();
        } catch (Exception ex) {
            System.out.println(1);
        }
    }



}
