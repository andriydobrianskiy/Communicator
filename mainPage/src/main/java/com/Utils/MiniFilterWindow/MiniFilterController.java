package com.Utils.MiniFilterWindow;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.google.jhsheets.filtered.operators.IFilterOperator;
import org.google.jhsheets.filtered.tablecolumn.AbstractFilterableTableColumn;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;


public class MiniFilterController extends StackPane implements Initializable {

    private MiniFilterFunction window;
    private Pane content;

    @FXML private HBox pane;
    @FXML private JFXButton btnRemoveFilter;
    @FXML private Label lblValue, lblType, lblColumn;


    private TableColumn column;
    private IFilterOperator.Type operator;
    private Object value;


    public MiniFilterController() {

    }

    private void changeFilterEvent() {
        ((AbstractFilterableTableColumn)column).getFilterEditor().getFilterMenu().show(
                (Node) btnRemoveFilter,
                MouseInfo.getPointerInfo().getLocation().getX(),
                MouseInfo.getPointerInfo().getLocation().getY()
        );
    }


    public void setFilter(TableColumn column, IFilterOperator.Type operator, Object value) {
        this.column = column;
        this.operator = operator;
        this.value = value;

        setLabelsInfo();
    }

    private void setLabelsInfo() {
        lblValue.setText(value.toString());

        lblType.setText(operator.toString().toLowerCase());
        lblColumn.setText(column.getText());
    }

    public void setWindow(MiniFilterFunction window, Pane content) {
        this.window = window;
        this.content = content;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane.setOnMouseClicked(event -> changeFilterEvent());
        btnRemoveFilter.setOnMouseClicked(event -> window.disableFilter(column, content));
    }
}
