package com.Utils;

import com.jfoenix.controls.JFXTextField;
import com.sun.javafx.scene.control.skin.PaginationSkin;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.layout.HBox;

public class CustomPaginationSkin extends PaginationSkin {

    private HBox controlBox;
    private Button prev;
    private Button next;
    private Button first;
    private Button last;

    private JFXTextField page;

    private void patchNavigation() {
        Pagination pagination = getSkinnable();
        Node control = pagination.lookup(".control-box");
        if (!(control instanceof HBox))
            return;
        controlBox = (HBox) control;
        prev = (Button) controlBox.getChildren().get(0);
        next = (Button) controlBox.getChildren().get(controlBox.getChildren().size() - 1);

        page = new JFXTextField();
        page.setPromptText("Page");


        page.setOnAction(e -> {
            pagination.setCurrentPageIndex(Integer.parseInt(page.getText())-1);
        });
        page.setStyle("");
        page.setStyle("-fx-background-color: lightblue;" +
                "-fx-font-size: 15px;" +
                "-fx-text-fill: black");
        //page.setStyle("-fx-font-size: 15px");
        //page.setStyle("-fx-left");

        //controlBox.setSpacing(12);

        first = new Button("A");
        first.setOnAction(e -> {
            pagination.setCurrentPageIndex(0);
        });
        first.disableProperty().bind(
                pagination.currentPageIndexProperty().isEqualTo(0));

        last = new Button("Z");
        last.setOnAction(e -> {
            pagination.setCurrentPageIndex(pagination.getPageCount());
        });
        last.disableProperty().bind(
                pagination.currentPageIndexProperty().isEqualTo(
                        pagination.getPageCount() - 1));

        ListChangeListener childrenListener =c -> {
            while (c.next()) {
                // implementation detail: when nextButton is added, the setup is complete
                if (c.wasAdded() && !c.wasRemoved() // real addition
                        && c.getAddedSize() == 1 // single addition
                        && c.getAddedSubList().get(0) == next) {
                    addCustomNodes();
                }
            }
        };
        controlBox.getChildren().addListener(childrenListener);
        addCustomNodes();
    }

    protected void addCustomNodes() {
        // guarding against duplicate child exception
        // (some weird internals that I don't fully understand...)
        if (first.getParent() == controlBox) return;
        controlBox.getChildren().add(0, first);
        controlBox.getChildren().add(last);
        controlBox.getChildren().add(page);
    }

    public CustomPaginationSkin(Pagination pagination) {
        super(pagination);
        patchNavigation();
    }

}
