
package com.mainLogin.updateProgram;

import com.mainLogin.updateProgram.Model.Instruction;
import com.mainLogin.updateProgram.Model.Modes;
import com.mainLogin.updateProgram.Model.UpdateXMLParser;
import org.xml.sax.SAXException;

import java.io.*;
import java.util.Iterator;


public class Updater {

    public void update(String instructionsxml, String tmp, Modes mode) throws SAXException,
            FileNotFoundException, IOException, InterruptedException {

        UpdateXMLParser parser = new UpdateXMLParser();
        Iterator<Instruction> iterator = parser.parse(tmp + File.separator + instructionsxml, mode).iterator();
        Instruction instruction;

        while (iterator.hasNext()) {
            instruction = (Instruction) iterator.next();
            switch (instruction.getAction()) {
                case MOVE:
                    copy(tmp + File.separator + instruction.getFilename(), instruction.getDestination());
                    break;
                case DELETE:
                    delete(instruction.getDestination());
                    break;
                case EXECUTE:
                    Runtime.getRuntime().exec("java -jar " + tmp + File.separator + instruction.getFilename());
                    break;
            }
        }

    }
    
    public void update(String instructionsxml, String tmp, String dstdir, Modes mode) throws SAXException,
            FileNotFoundException, IOException, InterruptedException {

        UpdateXMLParser parser = new UpdateXMLParser();
        Iterator<Instruction> iterator = parser.parse(tmp + File.separator + instructionsxml, mode).iterator();
        Instruction instruction;

        while (iterator.hasNext()) {
            instruction = (Instruction) iterator.next();
            switch (instruction.getAction()) {
                case MOVE:
                    copy(tmp + File.separator + instruction.getFilename(), dstdir+File.separator+instruction.getDestination());
                    break;
                case DELETE:
                    delete(dstdir+File.separator+instruction.getDestination());
                    break;
                case EXECUTE:
                    Runtime.getRuntime().exec("java -jar " + tmp + File.separator + instruction.getFilename());
                    break;
            }
        }

    }

    private void copy(String source, String destination) throws FileNotFoundException, IOException {
        File srcfile = new File(source);
        File dstfile = new File(destination);
        if (dstfile.isDirectory()) {
        	dstfile = new File(destination + File.separator + srcfile.getName());
        }

        InputStream in = new FileInputStream(srcfile);
        OutputStream out = new FileOutputStream(dstfile);

        byte[] buffer = new byte[512];
        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        in.close();
        out.close();
    }

    private void delete(String filename) {
        File file = new File(filename);
        file.delete();
    }
}
