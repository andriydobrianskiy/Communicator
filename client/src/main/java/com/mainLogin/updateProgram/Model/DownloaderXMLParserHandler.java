
package com.mainLogin.updateProgram.Model;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DownloaderXMLParserHandler extends DefaultHandler {
    private final StringBuilder sb = new StringBuilder();
    private String currentelement = "";
    private final ArrayList<String> files = new ArrayList<>();

    public DownloaderXMLParserHandler() {
        super();
    }

    @Override
    public void startElement(String uri, String name,
            String qName, Attributes atts) {
        currentelement = qName;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentelement.equals("file")) {
            files.add(sb.toString());
        }
        currentelement = "";
        sb.setLength(0);
    }

    @Override
    public void characters(char ch[], int start, int length) {
        if (!currentelement.equals("")) {
            sb.append(String.copyValueOf(ch, start, length).trim());
        }
    }

    public ArrayList<String> getFiles() {
        return files;
    }
}
