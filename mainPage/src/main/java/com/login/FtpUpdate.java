package com.login;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.*;

public class FtpUpdate {

    public static void main(String[] args) {
        String server = "ftp://192.168.10.101";


        FTPClient ftpClient = new FTPClient();
        try {

            ftpClient.connect(server);

            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            // APPROACH #1: using retrieveFile(String, OutputStream)
            String remoteFile1 = "/test/video.mp4";
            File downloadFile1 = new File("ftp://192.168.10.101/mainPage.rar");
            OutputStream outputStream1 = new BufferedOutputStream(new FileOutputStream(downloadFile1));
            boolean success = ftpClient.retrieveFile(remoteFile1, outputStream1);
            outputStream1.close();

            if (success) {
                System.out.println("File #1 has been downloaded successfully.");
            }

            // APPROACH #2: using InputStream retrieveFileStream(String)
            String remoteFile2 = "/test/song.mp3";
            File downloadFile2 = new File("D:/Downloads/song.mp3");
            OutputStream outputStream2 = new BufferedOutputStream(new FileOutputStream(downloadFile2));
            InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
            byte[] bytesArray = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = inputStream.read(bytesArray)) != -1) {
                outputStream2.write(bytesArray, 0, bytesRead);
            }

            success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("Файл №2 був успішно завантажений.");
            }
            outputStream2.close();
            inputStream.close();

        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}