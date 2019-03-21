package com.mainLogin;

import com.Utils.UsefulUtils;
import com.login.CustomLauncherUI;
import com.login.User;
import com.mainLogin.updateProgram.Model.Modes;
import com.mainLogin.updateProgram.Model.Release;
import com.mainLogin.updateProgram.Model.ReleaseXMLParser;
import com.mainLogin.updateProgram.UpdateHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.mainLogin.updateProgram.UpdateHelper.app_version;

public class Main extends Application {
    private static Stage stageObj;

    @Override
    public void start(Stage stage) throws Exception {
        Platform.setImplicitExit(false);
        stage.setTitle("Комунікатор "+ app_version + " " + User.getContactName());
        CustomLauncherUI customLauncherUI = new CustomLauncherUI();
        stageObj = stage;
        stage.setResizable(false);
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        stage.setScene(new Scene(root));
        Image icon = new Image("/images/LoginPage.png");
        stage.getIcons().add(icon);
        stage.toFront();
        stage.show();
        Platform.setImplicitExit(true);
        stage.setOnCloseRequest((ae) -> {
            Platform.exit();
            System.exit(0);
        });
    }

    private void setVisible(boolean b) {
        b = true;
    }

    public static void main(String[] args) {

        launch(args);


    }



}
