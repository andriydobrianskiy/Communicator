package com.mainLogin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class Main extends Application
{
    private static Stage stageObj;
  //  User user;

    @Override
    public void start(Stage stage) throws Exception{

     stageObj = stage;
      //FXMLLoader
    //   loader = new FXMLLoader(getClass().getResource("resources/views/TestFile.fxml"));
       // loader.setController(new MainController(path));
      //  Pane mainPane = loader.load();
        Parent root = FXMLLoader.load(getClass().getResource( "/views/Login.fxml" ));
          stage.setScene(new Scene(root));
        Image icon = new Image("/images/LoginPage.png");
       // System.out.println(+"6564898989");
        stage.getIcons().add(icon);
        //  stage.getIcons().add( new Image( String.valueOf( getClass().getClassLoader().getResource( "/images/plug.png" ) ) ) );
        stage.show();
       // stage.setOnCloseRequest(e -> Platform.exit());*/
    }

    public static void main(String[] args) {
        launch(args);
    }


    public static Stage getPrimaryStage() {
        return stageObj;
    }
}