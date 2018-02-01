package com.ChatTwoo.controller;

import com.ChatTwoo.model.ChatMessage;
import com.login.User;
import com.mainPage.InProcessing.InProcessing;
import com.mainPage.InProcessing.InProcessingController;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Server {
    // a unique ID for each connection
    private static int uniqueId;
    // an ArrayList to keep the list of the Client
    private ArrayList<ClientThread> clientsConnected;
    // if I am in a GUI
    private ServerController serverController;
    // to display time
    private SimpleDateFormat sdf;
    // the port number to listen for connection
    private static int port;
    // the boolean that will be turned of to stop the server
    private boolean keepGoing;


//ServerController serverController = ServerController();

    /*
     *  server constructor that receive the port to listen to for connection as parameter
     */
    public Server(int port) {
        this(port, null);
    }

    public Server(int port, ServerController serverController) {
        // GUI or not
        this.serverController = serverController;
        // the port
        this.port = port;
        // to display hh:mm:ss
        sdf = new SimpleDateFormat("yyyy-MM-dd'|'HH:mm:ss");
        sdf.format(new Date());
        System.out.println(sdf.format(new Date()));


        // ArrayList for the Client list
        clientsConnected = new ArrayList<ClientThread>();
    }

    public void start() {
        keepGoing = true;
        /* create socket server and wait for connection requests */
        try {
            // the socket used by the server
            ServerSocket serverSocket = new ServerSocket(port);

            // infinite loop to wait for connections
            while (keepGoing) {
                // format message saying we are waiting
                display("Сервер чекає клієнтів на порту " + port + ".");

             //   new ClientThread(serverSocket.accept()).start();

                Socket socket = serverSocket.accept();  // accept connection
                // if I was asked to stop
                if (!keepGoing) {
                    break;
                }
                ClientThread t = new ClientThread(socket);  // make a thread of it
                clientsConnected.add(t); // save it in the ArrayList
                t.start();

            }
            // I was asked to stop
            try {
                serverSocket.close();
                for (int i = 0; i < clientsConnected.size(); ++i) {
                    ClientThread tc = clientsConnected.get(i);
                    try {
                        tc.sInput.close();
                        tc.sOutput.close();
                        tc.socket.close();
                    } catch (IOException ioE) {
                        // not much I can do
                    }
                }
            } catch (Exception e) {
                display("Exception closing the server and clients: " + e);
            }
        }
        // something went bad
        catch (IOException e) {
            String msg = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
            display(msg);
        }
    }

    /*
     * For the GUI to stop the server
     */
    public void stop() {
        keepGoing = false;
        // connect to myself as Client to exit statement
        // Socket socket = serverSocket.accept();
        try {
            new Socket("192.168.10.131", port);
        } catch (Exception e) {
            // nothing I can really do
        }
    }

    /*
     * Display an event (not a message) to the GUI
     */
    private void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg + "\n";

        serverController.appendEvent(time + "\n");

    }

    /*
     *  to broadcast a message to all Clients
     */
    private synchronized void broadcast(String message) {
        // add HH:mm:ss and \n to the message
        String time = sdf.format(new Date());
        String messageLf;
        if (message.contains("WHOISIN") || message.contains("REMOVE")) {

            messageLf = message;

        } else {

            messageLf = time + " " + message + "\n";
            serverController.appendRoom(messageLf);

            // append in the room window

        }

        // we loop in reverse order in case we would have to remove a Client
        // because it has disconnected
        System.out.println(clientsConnected);

        for (int i = clientsConnected.size(); --i >= 0; ) {
            ClientThread ct = clientsConnected.get(i);

            // try to write to the Client if it fails remove it from the list
            if (!ct.writeMsg(messageLf)) {
                clientsConnected.remove(i);
                serverController.remove(ct.username);

                display("Відключений Клієнт " + ct.username + " вилучено зі списку.");
            }
        }
        System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");
    }

    // for a client who logoff using the LOGOUT message
    synchronized void remove(String id) {
        // scan the array list until we found the Id
        for (int i = 0; i < clientsConnected.size(); ++i) {
            ClientThread ct = clientsConnected.get(i);
            // found it
            if (ct.id == id) {
                serverController.remove(ct.username);
                ct.writeMsg(ct.username + ":ВИДАЛИТИ");
                clientsConnected.remove(i);

                return;
            }
        }
    }

   /* public void SoundMessage() {
        Media hit = new Media(getClass().getClassLoader().getResource("sample/resources/sounds/notification.wav").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(hit);
        mediaPlayer.play();
    }*/

    /**
     * One instance of this thread will run for each client
     */
    class ClientThread extends Thread {
        // the socket where to listen/talk
        Socket socket;
        ObjectInputStream sInput;
        ObjectOutputStream sOutput;
        // my unique id (easier for deconnection)
        String id;
        // the Username of the Client
        String username;
        String userName;

        //   InProcessing OfferingRequest;

        String OfferingRequestID;
        // the only type of message a will receive
        ChatMessage cm;
        // the date I connect
        String date;


        // Constructore
        ClientThread(Socket socket) throws IOException {
            // a unique id
            id = User.getContactID();//++uniqueI
            // ClientController clientController = new ClientController(offeringRequest);
//            System.out.println(clientController.offeringRequest.getID() + "888888888888888888888888888888888888888888888888888888888888888888");
            //   OfferingRequestID = OfferingRequest.getID();
            OfferingRequestID = ClientController.offeringRequest.getID();
          //  userName = ClientController.usernameMessage;
            System.out.println(OfferingRequestID + "Id offeringrequest");
            this.socket = socket;
            /* Creating both Data Stream */
            System.out.println("Тема, яка намагається створити потік вхідних / вихідних об'єктів");

                // create output first
                sOutput = new ObjectOutputStream(socket.getOutputStream());
                sInput = new ObjectInputStream(socket.getInputStream());
                // read the username
try{
                username = (String) sInput.readObject();
                //    usernameMessage = (String) sInput.readObject();

                serverController.addUser(username);

                //broadcast(username + ":WHOISIN"); //Broadcast user who logged in
                writeMsg(username + ":WHOISIN");


                for (ClientThread client : clientsConnected) {
                    writeMsg(client.username + ":WHOISIN");
                }

                display(username + " just connected.");
            } catch (IOException e) {
                display("Exception creating new Input/output Streams: " + e);
                return;
            }
            // have to catch ClassNotFoundException
            // but I read a String, I am sure it will work
            catch (ClassNotFoundException e) {
                display("Exception creating new Input/output Streams: " + e);
            }
        }

        /*   for (ClientThread client : clientsConnected) {
                if (client.username.equals(username)) {
                    writeMsg(client.username + ":WHOISIN");
                 //   SoundMessage();
                    break;
                } else {

                    break;
                }
                    System.out.println(client.username + "Username vivid");




            date = new Date().toString() + "\n";

        }*/


public InProcessing chosenAccount;
        // sending private message
        public void sendPrivate(String user, String msg) {
            try {
                //   sdf = new SimpleDateFormat("yyyy-MM-dd'|'HH:mm:ss");
               //    sdf.toString();
                for (int i = 0; i < clientsConnected.size(); i++) {
                    ClientThread tsk = clientsConnected.get(i);
                    String nem = tsk.username;
                    System.out.println(nem + "00000000000000000" + user);
                    if (nem.equalsIgnoreCase(user)) {


                      //  UsefulUtils.showSuccessful("Нове повідомлення від: " + User.getContactName());

                      //  ChatMessage.S


                    //    chatMessage.SoundMessage();

                        tsk.writeMsg(sdf.format(new Date()) + " " + username + ":\n" + msg + "\n");

                        // ClientController clientController = new ClientController(chosenAccount, false);
                        //ChatMessage chatMessage1 = new ChatMessage(1,);
                    //    chatMessage.showCustom();



                      //  UsefulUtils.showCustomDialogDown("Нове повідомлення від " + User.getContactName() +  "\n до запиту " + OfferingRequestID);

                     //   UsefulUtils.showInformationDialog("Запит в обробці " + User.getContactName());



                        //   Controller.serverlog.appendText(username + ":  " + msg+"\n");
                        ///  broadcast(username + ":  " + msg+"\n");
                        //   sOutput.writeUTF(username + ":  " + msg);
                        //  sOutput.flush();
                        //  break;
                    }
                }
            } catch (Exception ex) {
                display("Exception creating new Input/output Streams: " + ex);
                return;
            }
        }

        // checks who is online
        public void whoIsOnline() {
            try {
                String who = "";
                for (int i = 0; i < clientsConnected.size(); i++) {
                    System.out.println(clientsConnected + " Clientconnected");
                    ClientThread tsk = clientsConnected.get(i);
                    who += tsk.username + ",";
                }
                sOutput.writeUTF(who);
                System.out.println(who + "Who is online");
            } catch (Exception ex) {
            }

        }


        private int clientNo = 0;

        ArrayList<User> clients = new ArrayList<>();
        ServerSocket serverSocket = new ServerSocket();

        ChatMessage chatMessage ;
        // what will run forever
        public void run() {
            // to loop until LOGOUT



            boolean keepGoing = true;

                while (true) {
                    try {

                    cm = (ChatMessage) sInput.readObject();

                    System.out.println("77777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777777");

                    // calling the sendprivate msg with two parameters name and message

                   sendPrivate(username, cm.getMessage());


            } catch (Exception ex) {
                // allerting the server if the client logs out or gets a network problem
                System.out.println("client lost connection " + username);
            }
        }


        // remove myself from the arrayList containing the list of the
        // connected Clients


        }


        // try to close everything
        private void close() {
            // try to close the connection
            try {
                if (sOutput != null) sOutput.close();
            } catch (Exception e) {
            }
            try {
                if (sInput != null) sInput.close();
            } catch (Exception e) {
            }

            try {
                if (socket != null) socket.close();
            } catch (Exception e) {
            }

        }

        /*
         * Write a String to the Client output stream
         */
          InProcessingController controller;

        private boolean writeMsg(String msg) {
            // if Client is still connected send the message to it
            if (!socket.isConnected()) {
                close();
                return false;
            }
            // write the message to the stream

            try {
                sOutput.writeObject(msg);
            }
            // if an error occurs, do not abort just inform the user
            catch (IOException e) {
                display("Error sending message to " + username);
                display(e.toString());
            }
            return true;
        }
    }
}



