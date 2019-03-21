package com.client.chatwindow;

import com.Utils.GridComp;
import com.Utils.UsefulUtils;
import com.login.User;
import com.mainPage.InProcessing.InProcessingController;
import com.mainPage.InTract.InTract;
import com.mainPage.InTract.InTractController;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

import static com.messages.MessageType.CONNECTED;

public class Listener implements Runnable {

    private static final String HASCONNECTED = "has connected";

    private static String picture;
    public static Socket socket;
    public String hostname;
    public int port;
    public static String username;
    public ChatController controller;
    private static ObjectOutputStream oos;
    private InputStream is;
    private ObjectInputStream input;
    private OutputStream outputStream;
    Logger logger = LoggerFactory.getLogger(Listener.class);
    public InProcessingController inProcessingController;
    public InTractController inTractController;
    public static InTract inTract;
    //Logger logger = (Logger) LoggerFactory.getLogger(Listener.class);
    public static GridComp requestID;

    public Listener() {
    }

    public Listener(String hostname, int port, String username, String picture, ChatController controller) {
        this.hostname = hostname;
        this.port = port;
        Listener.username = username;
        Listener.picture = picture;
        this.controller = controller;
    }

    public Listener(ChatController controller, GridComp requestID) {
        this.requestID = requestID;
        this.controller = controller;
    }

    public Listener(InProcessingController inProcessingController) {
        this.inProcessingController = inProcessingController;

    }

    public void run() {
        try {
            socket = new Socket(hostname, port);
            outputStream = socket.getOutputStream();
            oos = new ObjectOutputStream(outputStream);
            is = socket.getInputStream();
            input = new ObjectInputStream(is);
        } catch (IOException e) {
            UsefulUtils.showErrorDialogDown("Сервер не підключено");
            logger.error("Could not Connect");
        }

        try {
            connect();
            logger.info("Sockets in and out ready!");
            while (socket.isConnected()) {
                Message message = null;
                message = (Message) input.readObject();

                if (message != null) {
                    logger.debug("Message recieved:" + message.getMsg() + " MessageType:" + message.getType() + "Name:" + message.getName());
                    switch (message.getType()) {
                        case USER:
                            controller.addToChat(message);
                            controller.newUserNotification(message);
                            break;
                        case VOICE:
                            controller.addToChat(message);
                            break;
                        case NOTIFICATION:
                            controller.newUserNotification(message);
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
            socket = null;

        }
    }

    public static void send(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        String id = String.valueOf(requestID);
        createMessage.setID(id);
        createMessage.setCreatedByID(requestID.getCreatedByID());
        createMessage.setNumber(requestID.getNumber());
        createMessage.setContactID(User.getContactID());
        createMessage.setOfferingGroupID(requestID.getOfferingGroupID());
        createMessage.setType(MessageType.USER);
        createMessage.setStatus(Status.AWAY);
        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public static void sendNotification(String msg) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        String id = String.valueOf(requestID);
        createMessage.setID(id);
        createMessage.setCreatedByID(requestID.getCreatedByID());
        createMessage.setNumber(requestID.getNumber());
        createMessage.setContactID(User.getContactID());
        createMessage.setOfferingGroupID(requestID.getOfferingGroupID());
        createMessage.setType(MessageType.NOTIFICATION);
        createMessage.setStatus(Status.AWAY);

        createMessage.setMsg(msg);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }


    public static void sendVoiceMessage(byte[] audio) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.VOICE);
        createMessage.setStatus(Status.AWAY);
        createMessage.setCreatedByID(requestID.getCreatedByID());
        createMessage.setContactID(User.getContactID());
        createMessage.setOfferingGroupID(requestID.getOfferingGroupID());
        createMessage.setVoiceMsg(audio);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public static void sendStatusUpdate(Status status) throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(MessageType.STATUS);
        createMessage.setStatus(status);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
        oos.flush();
    }

    public static void connect() throws IOException {
        Message createMessage = new Message();
        createMessage.setName(username);
        createMessage.setType(CONNECTED);
        createMessage.setMsg(HASCONNECTED);
        createMessage.setPicture(picture);
        oos.writeObject(createMessage);
    }

}