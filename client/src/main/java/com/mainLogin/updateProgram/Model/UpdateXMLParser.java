
package com.mainLogin.updateProgram.Model;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class UpdateXMLParser {

    public ArrayList<Instruction> parse(String filename, Modes mode) throws SAXException, FileNotFoundException, IOException, InterruptedException {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        UpdateXMLParserHandler handler = new UpdateXMLParserHandler();
        reader.setContentHandler(handler);
        reader.setErrorHandler(handler);

        if (mode == Modes.FILE) {
            reader.parse(new InputSource(new FileReader(new File(filename))));
        } else {
            java.net.URL u = new java.net.URL(filename);
            java.net.URLConnection conn = u.openConnection();
            java.io.InputStream in = conn.getInputStream();
            reader.parse(new InputSource(in));
        }

        return handler.getInstructions();

    }
}
