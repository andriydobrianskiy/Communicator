package com.mainLogin;

import com.login.CustomLauncherUI;
import com.login.User;
import com.mainLogin.parsers.UpdateHelper;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    private static Stage stageObj;

    @Override
    public void start(Stage stage) throws Exception {
        Platform.setImplicitExit(false);
        stage.setTitle("Комунікатор "+ UpdateHelper.app_version + " " + User.getContactName());
        CustomLauncherUI customLauncherUI = new CustomLauncherUI();
        stageObj = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        stage.setScene(new Scene(root));
        Image icon = new Image("/images/LoginPage.png");
        stage.getIcons().add(icon);
        stage.show();
    }

    private void setVisible(boolean b) {
        b = true;
    }

    public static void main(String[] args) {


       /* try {
            if(new UpdateHelper().checkForUpdate() == true) return;
        } catch (Exception e) {
            Platform.runLater(() -> UsefulUtils.showErrorDialogDown("Оновлення неможливе. Зверніться до адміністраторів!"));
            return;
        }*/
        launch(args);
    }


}
