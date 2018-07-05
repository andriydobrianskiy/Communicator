package com.client.chatwindow;

import com.messages.Message;
import com.messages.User;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract class ClientModelInt implements Remote {

    void notify(String message, int type) throws RemoteException {

    }

    // void reciveMsg(String msg)  throws RemoteException;
    void
    reciveMsg(Message message) throws RemoteException {

    }


    /**
     * receive Announcement from server
     *
     * @param message
     * @throws java.rmi.RemoteException
     */
    void receiveAnnouncement(String message) throws RemoteException {

    }


    /**
     * @param sender
     * @param filename
     * @return url location or null if not file choosen
     * @throws RemoteException
     */
    String getSaveLocation(String sender, String filename) throws RemoteException {
        return null;
    }

    /**
     * save file
     *
     * @param path
     * @param filename
     * @param append
     * @param data
     * @param dataLength
     * @throws RemoteException
     */
    void reciveFile(String path, String filename, boolean append, byte[] data, int dataLength) throws RemoteException {

    }

    /**
     * @param data
     * @param dataLength
     */
    void reciveSponser(byte[] data, int dataLength) throws RemoteException {

    }


    /**
     * check to online or not by server
     *
     * @return true if is active
     * @throws RemoteException
     */
    boolean isOnline() throws RemoteException {
        return false;
    }


    void loadErrorServer() throws RemoteException {

    }

    public abstract ClientModelInt getConnection(String Client);

    public abstract User getUserInformation();
}
