package com.client.chatwindow;

import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTract;
import com.mainPage.InTract.InTractController;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;

import java.io.*;
import java.net.Socket;

import static com.messages.MessageType.CONNECTED;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class ListinerTract  implements Runnable{

    private static final String HASCONNECTED = "has connected";

    private static String picture;
    private Socket socket;
    public String hostname;
    public int port;
    public static String username;
    public ChatController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    public InProcessingController inProcessingController;
    public InTractController inTractController;
    public static InTract inTract;
    //Logger logger = LoggerFactory.getLogger(Listener.class);
    public  static   InProcessing inProcessing ;
    public ListinerTract(String hostname, int port, String username, String picture, ChatController controller) {
        this.hostname = hostname;
        this.port = port;
        ListinerTract.username = username;
        ListinerTract.picture = picture;
        this.controller = controller;
    }


    public ListinerTract(String hostname, int port, String username, String picture, ChatController conn, InTractController inTractController, InTract inTract) {

        this.hostname = hostname;
        this.port = port;
        ListinerTract.username = username;
        ListinerTract.picture = picture;
        this.controller = conn;
        this.inTractController = inTractController;
        this.inTract = inTract;
    }


    public void run() {
        try {
            socket = new Socket(hostname, port);
         //   inTractController.showScene();
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            inProcessingController.showErrorDialog("Сервер не підключено");
        }
        //    logger.info("Connection accepted " + socket.getInetAddress() + ":" + socket.getPort());

        try {
            connect();
            //     logger.info("Sockets in and out ready!");

            while (socket.isConnected()) {
                Message message = null;
                message = (Message) input.readObject();

                if (message != null) {
                    //         logger.debug("Message recieved:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                    switch (message.getType()) {
                        case USER:
                            controller.addToChat(message);
                            break;
                        case VOICE:
                            controller.addToChat(message);
                            break;
                        case NOTIFICATION:
                            controller.newUserNotification( message);
                            break;
                        case SERVER:
                            controller.addAsServer(message);
                            break;
                        case CONNECTED:
                            controller.setUserList(message);
                            break;
                        case DISCONNECTED:
                            controller.setUserList(message);
                            break;
                        case STATUS:
                            controller.setUserList(message);
                            break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            //   controller.logoutScene();
        }
    }

    /* This method is used for sending a normal Message
     * @param msg - The message which the user generates
     */
    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setOfferingID(inTract.getID());
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used for sending a voice Message
 * @param msg - The message which the user generates
 */
    public static void sendVoiceMessage(byte[] audio) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        //  createMessage.setOfferingID(inProcessing.getID());
        createMessage.setType(MessageType.VOICE);
        createMessage.setStatus(Status.AWAY);
        createMessage.setVoiceMsg(audio);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used for sending a normal Message
 * @param msg - The message which the user generates
 */
    public static void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setOfferingID(inTract.getID());
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    /* This method is used to send a connecting message */
    public static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setOfferingID(inTract.getID());
        createMessage.setType(CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
    }

}
