package com.Utils;

import javafx.beans.binding.NumberExpression;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;
import org.google.jhsheets.filtered.FilteredTableView;

import java.text.Format;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SummaryTable {

    private static Logger log = Logger.getLogger(SummaryTable.class.getName());

    private SplitPaneDividerSlider bottomSplitPaneDividerSlider;

    private TableView<GridComp> mainTable;
    private TableView<GridComp> sumTable;
    private BorderPane mainBorderPane = new BorderPane();
    private StackPane stackPane;

    private GridDAO gridDAO;
    private HashMap<TableColumn, String> hashColumns;

    private ObservableList<GridComp> sumData;


    public SummaryTable(
            HashMap<TableColumn, String> hashColumns,
            GridDAO gridDAO,
            TableView<GridComp> mainTable
    ) {
        this.gridDAO = gridDAO;
        this.hashColumns = hashColumns;
        this.mainTable = mainTable;

        setWindowComponents();
    }


    public BorderPane setSummaryTable() {

        try {

            if(bottomSplitPaneDividerSlider == null || !bottomSplitPaneDividerSlider.isAimContentVisible()){
                return createBorderPane();
            }
            setupSumTableColumns();

            for (int i = 0; i < mainTable.getColumns().size(); i++) {
                sumTable.getColumns().get(i).prefWidthProperty().bind(mainTable.getColumns().get(i).widthProperty());
                sumTable.getColumns().get(i).visibleProperty().bind(mainTable.getColumns().get(i).visibleProperty());

            }



            getSummaryValues();

            return createBorderPane();

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }

        return null;


    }

    private static ScrollBar getScrollBar(Control source) {
        ScrollBar scrollBar = null;
        for (Node node : source.lookupAll(".scroll-bar")) {
            if (node instanceof ScrollBar && ((ScrollBar) node).getOrientation().equals(Orientation.HORIZONTAL)) {
                scrollBar = (ScrollBar) node;
            }
        }
        return scrollBar;
    }

    private void setupSumTableColumns() {
        try {
            sumTable = new FilteredTableView<>();

            mainTable.getColumns().forEach(column -> {
                TableColumn item = new TableColumn(column.getText());

                if (column.getCellObservableValue(0) instanceof NumberExpression) {
                    item.setCellValueFactory(column.getCellValueFactory());
                    item.setCellFactory(new FormattedTableCellFactory<>(TextAlignment.RIGHT));
                    column.setCellFactory(new FormattedTableCellFactory<>(TextAlignment.RIGHT));
                }

                sumTable.getColumns().add(item);
            });

            sumTable.getStylesheets().add("/styles/SummaryTable.css");

            sumTable.getStyleClass().add("sumTable");
            sumTable.getStyleClass().add("tableview-header-hidden");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }

    private BorderPane createBorderPane() {
        try {
            SplitPane mainSplitPane = (SplitPane)stackPane.getChildren().get(0);
            ((BorderPane)((SplitPane)mainSplitPane.getItems().get(0)).getItems().get(0)).setCenter(mainTable);
            ((BorderPane)mainSplitPane.getItems().get(1)).setCenter(sumTable);


            mainBorderPane = new BorderPane();
            mainBorderPane.setCenter(stackPane);

            return mainBorderPane;
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
            return null;
        }
    }


    private void setWindowComponents() {
        try {
            stackPane = new StackPane();
            SplitPane mainSplitPane = new SplitPane();
            mainSplitPane.setOrientation(Orientation.VERTICAL);
            SplitPane centerSplitPane = new SplitPane();
            centerSplitPane.setOrientation(Orientation.VERTICAL);

            BorderPane centerBorderPane = new BorderPane();
            BorderPane bottomBorderPane = new BorderPane();

            ToggleButton bottomToggleButton = new ToggleButton();


            mainSplitPane.setDividerPositions(0.90f, 0.01f);


            //bottomSplitPaneDividerSlider.setAimContentVisible(false);

            centerBorderPane.setCenter(mainTable);

            AnchorPane anchorToggleButton = new AnchorPane();
            bottomToggleButton.setPrefHeight(10);
            bottomToggleButton.setMinHeight(10);
            anchorToggleButton.getChildren().add(bottomToggleButton);
            anchorToggleButton.setBottomAnchor(bottomToggleButton, 0.0);
            anchorToggleButton.setLeftAnchor(bottomToggleButton, 0.0);
            anchorToggleButton.setRightAnchor(bottomToggleButton, 0.0);
            anchorToggleButton.setTopAnchor(bottomToggleButton, 0.0);


            centerBorderPane.setBottom(anchorToggleButton);

            bottomBorderPane.setCenter(sumTable);

            centerSplitPane.getItems().addAll(centerBorderPane);

            mainSplitPane.getItems().addAll(centerSplitPane, bottomBorderPane);


            stackPane.getChildren().add(mainSplitPane);


            bottomSplitPaneDividerSlider = new SplitPaneDividerSlider(mainSplitPane, 0, SplitPaneDividerSlider.Direction.DOWN);

            bottomToggleButton.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) -> {
                bottomSplitPaneDividerSlider.setAimContentVisible(t1);
            });


            bottomSplitPaneDividerSlider.setAimContentVisible(false);


            mainBorderPane = new BorderPane();
            mainBorderPane.setCenter(stackPane);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }

    }


    private void getSummaryValues() {
        try {
            sumData = FXCollections.observableArrayList();


            HashMap<SummaryTypes, String> sumQueries = new HashMap<>();



            for(TableColumn column : mainTable.getColumns()) {

            }

            hashColumns.forEach((column, query) -> {
                if (column.getCellObservableValue(0) instanceof NumberExpression) {
                    Arrays.asList(SummaryTypes.values()).stream().forEachOrdered(type -> {
                        Matcher mFull = Pattern.compile("([^.]*).([^.]*)").matcher(query);
                        StringBuilder builderQuery = new StringBuilder();

                        if (mFull.matches()) {


                            /*hashColumns.forEach((k,v) -> {
                                System.out.println(k.isVisible()  + " visible ");
                                System.out.println(k.getCellObservableValue(0) + " observablevalue ");
                                System.out.println((k.getCellObservableValue(0) instanceof StringExpression) + " StringExpression");
                                System.out.println((k.getCellObservableValue(0) instanceof StringProperty) + " StringProperty");
                            });

                            String typeNameCol =
                                    hashColumns.get(
                                            hashColumns.keySet().stream()
                                                    .filter(p -> {
                                                        return (p.isVisible() && p.getCellObservableValue(0) instanceof StringExpression);
                                                    })
                                                    .findAny());
                            Matcher mTypeName = Pattern.compile("([^.]*).([^.]*)").matcher(typeNameCol);

                            if(mTypeName.matches()) {
                                builderQuery.append(UsefulUtils.createSQLString(type.getLabel(type) + " AS " + mTypeName.group(2)) + ",\n");
                            }*/

                            builderQuery.append("ISNULL(");
                            builderQuery.append(type);
                            builderQuery.append("(" + query + "), 0)");
                            builderQuery.append(" AS ");
                            builderQuery.append(mFull.group(2) + ",\n");

                            sumQueries.merge(type, builderQuery.toString(), (a, b) -> a + "\n" + b);
                        }




                    });
                }
            });

            sumQueries.values().stream()
                    .map(value -> value.replaceFirst(".$", ""))
                    .map(value -> gridDAO.getTotalValues(value))
                    .forEach(list -> sumData.add(list.get(0)));

            sumTable.setItems(sumData);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Exception: " + e);
        }
    }


    public static class FormattedTableCellFactory<S, T> implements Callback<TableColumn<S, T>, TableCell<S, T>> {

        private TextAlignment alignment = TextAlignment.LEFT;
        private Format format;

        public FormattedTableCellFactory() {
        }

        public FormattedTableCellFactory( TextAlignment alignment) {
            this.alignment = alignment;
        }

        public TextAlignment getAlignment() {
            return alignment;
        }

        public void setAlignment(TextAlignment alignment) {
            this.alignment = alignment;
        }

        public Format getFormat() {
            return format;
        }

        public void setFormat(Format format) {
            this.format = format;
        }

        @Override
        @SuppressWarnings("unchecked")
        public TableCell<S, T> call(TableColumn<S, T> p) {
            TableCell<S, T> cell = new TableCell<S, T>() {
                @Override
                public void updateItem(Object item, boolean empty) {
                    if (item == getItem()) {
                        return;
                    }
                    super.updateItem((T) item, empty);
                    if (item == null) {
                        super.setText(null);
                        super.setGraphic(null);
                    } else if (format != null) {
                        super.setText(format.format(item));
                    } else if (item instanceof Node) {
                        super.setText(null);
                        super.setGraphic((Node) item);
                    } else {
                        super.setText(item.toString());
                        super.setGraphic(null);
                    }
                }
            };
            cell.setTextAlignment(alignment);
            switch (alignment) {
                case CENTER:
                    cell.setAlignment(Pos.CENTER);
                    break;
                case RIGHT:
                    cell.setAlignment(Pos.CENTER_RIGHT);
                    break;
                default:
                    cell.setAlignment(Pos.CENTER_LEFT);
                    break;
            }
            return cell;
        }
    }


    enum SummaryTypes {
        SUM("SUM"),
        COUNT("COUNT");


        private String label;

        SummaryTypes(String label) {
            this.label = label;
        }

        public String getLabel(SummaryTypes type) { return (type.equals(SUM) ? "Сума" : "Кількість");}

        public String toString() {
            return label;
        }

    }

}

