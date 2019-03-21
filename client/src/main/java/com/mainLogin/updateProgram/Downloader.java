
package com.mainLogin.updateProgram;

import com.mainLogin.updateProgram.Model.Modes;
import com.mainLogin.updateProgram.Model.DownloaderXMLParser;
import org.xml.sax.SAXException;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Iterator;

public class Downloader {

    public void download(String filesxml, String destinationdir, Modes mode) throws SAXException,
            FileNotFoundException, IOException, InterruptedException {

        DownloaderXMLParser parser = new DownloaderXMLParser();
        Iterator iterator = parser.parse(filesxml, mode).iterator();
        java.net.URL url;

        File dir = new File(destinationdir);
        if (!dir.exists()) {
            dir.mkdir();
        }

        while (iterator.hasNext()) {
            url = new java.net.URL((String) iterator.next());
            wget(url, destinationdir + File.separator + new File(url.getFile()).getName());
        }

    }

    private void wget(java.net.URL url, String destination) throws MalformedURLException, IOException {
        java.net.URLConnection conn = url.openConnection();
        InputStream in = conn.getInputStream();

        File dstfile = new File(destination);
        OutputStream out = new FileOutputStream(dstfile);

        byte[] buffer = new byte[512];
        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }
}
