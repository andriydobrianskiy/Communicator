package com.login;

import fxlauncher.FXManifest;
import fxlauncher.UIProvider;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CustomLauncherUI implements UIProvider {
    private ProgressBar progressBar;
    private Stage stage;
    private VBox root;
    private Label label;
    private Label header;

    public void init(Stage stage) {
        this.stage = stage;
        stage.getScene().getStylesheets().add(getClass().getResource("/styles/launcherstyles.css").toExternalForm());
    }

    public  Parent createLoader() {
        stage.setTitle("Strans");

        root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPrefSize(300, 500);
        root.getStyleClass().add("Оновлення");

        header = new Label("Комунікатор");
        header.getStyleClass().add("h1");

        label = new Label("Будьласка зачекайте...");

        root.getChildren().addAll(header, label);
        progressBar = new ProgressBar();

        root.getChildren().remove(label);
        root.getChildren().add(progressBar);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(10), new KeyValue(header.scaleXProperty(), 1.5)),
                new KeyFrame(Duration.seconds(10), new KeyValue(header.scaleYProperty(), 1.5))
        );
        tl.play();
        return root;
    }

    public Parent createUpdater(FXManifest manifest) {
        stage.setTitle("Updating...");
        progressBar = new ProgressBar();
        root.getChildren().remove(label);
        root.getChildren().add(progressBar);

        Timeline tl = new Timeline(
                new KeyFrame(Duration.seconds(20), new KeyValue(header.scaleXProperty(), 1.5)),
                new KeyFrame(Duration.seconds(20), new KeyValue(header.scaleYProperty(), 1.5)));
        tl.play();

        return root;
    }

    public void updateProgress(double progress) {
        progressBar.setProgress(progress);
    }

}