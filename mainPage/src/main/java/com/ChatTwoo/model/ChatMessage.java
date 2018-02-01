package com.ChatTwoo.model;

import javafx.scene.image.Image;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import com.ChatTwoo.controller.ClientController;
import com.login.User;
import com.mainPage.InProcessing.InProcessing;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.Serializable;

public class ChatMessage implements Serializable {

    protected static final long serialVersionUID = 1112122200L;

    // The different types of message sent by the Client
    // WHOISIN to receive the list of the users connected
    // MESSAGE an ordinary message
    // LOGOUT to disconnect from the Server
    public static final int WHOISIN = 0, MESSAGE = 1, LOGOUT = 2;
    private int type;

    private String message;
    public InProcessing chosenAccount ;

    protected String UserName;
ClientController clientController;
 static TrayNotification tray = new TrayNotification();
    //
    public  ChatMessage () {
      //  showCustom();
    //    SoundMessage();
    }
    public ChatMessage(int type, String message) {
        this.type = type;
        this.message = message;
       // this.userName = userName;
showCustom();
SoundMessage();

      //  ExitPrivateWindow();
    }

    private void ExitPrivateWindow() {
        this.clientController.RemovePrivateWindow(this.UserName);
       // this.setVisible(false);
    }

    public void SoundMessage() {
        Media hit = new Media(getClass().getClassLoader().getResource("sounds/not-bad.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }
    public static void showCustom () {

        tray.setNotificationType(NotificationType.CUSTOM);
        tray.setTitle("Сповіщення");
        tray.setMessage("Нове повідомлення від " + User.getContactName() +  "\n до запиту " );

        tray.setAnimationType(AnimationType.SLIDE);
        tray.showAndDismiss(Duration.millis(5000));
        tray.setRectangleFill(Color.valueOf("#39b5ff"));
        tray.setImage(new Image("/sample/resources/images/chat.png"));
    }


    // getters
    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
