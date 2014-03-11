package a2;

import java.io.*;
import java.util.Scanner;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** 
 * This class implements the Command interface and implements the get command
 * from the command line. This command saves the source code of an html website 
 * or the txt of a parsable document per URL calss and saves it to a file in 
 * current working directory.
 *
 */
public class get implements Command {

    String filename = "";

    /**
     * This method implements the get command in the JShell
     *
     * @param  String the parameter from the command line
     * @param  JShell the command line console
     * @return String any error messages from the execution of the command
     */
    @Override
    public String execute(String parameter, JShell j) throws Exception {
        String returnStr = UrlDownload.fileUrl(parameter, "temp.txt");
        if (returnStr.compareToIgnoreCase("") != 0) {
            return returnStr;
        }

        /** Read the contents of the given file. */
        StringBuilder text = new StringBuilder();
        Scanner scanner = new Scanner(new FileInputStream("temp.txt"), "UTF-8");
        try {
            while (scanner.hasNextLine()) {
                text.append(scanner.nextLine());
                text.append("\n");
            }
        } finally {
            scanner.close();
        }
        String contents = text.toString();
        File url = new File();
        url.setContents(contents);

        //let the title be the name of this file
        String name = "";
        String html = contents.replaceAll("\\s+", "_");
        Pattern p = Pattern.compile("<title>(.*?)</title>");
        Matcher m = p.matcher(html);
        while (m.find() == true) {
            name = (m.group(1));
        }
        if (!name.equals("")) {
            name = name + ".html";
        }

        //let the title be the name of this file
        if (name.equals("")) {

            html = parameter.replaceAll("\\s+", ".");
            p = Pattern.compile("^(.*?)(/.*/)(.*?)$");
            m = p.matcher(html);
            while (m.find() == true) {
                name = (m.group(3));
            }

        }

        //let this be default if we can't find a name\
        if (name.equals("")) {
            name = "default.html";
        }

        url.setName(name);
        j.getCurrentWorkingDirectory().addFile(url);

        return "";
    }

    /** 
     * This is a helper class to help the execute method of the get class
     * The UrlDownload static class downloads a file from the Internet and writes
     * the contents to a local file.
     *
     * @author c0mauric
     * @author c1slynnsc
     * @author c0kuhenr
     * @author c8ulagan
     */
    static class UrlDownload {

        /* The int size 1024 specifies the number of bytes to read at once */
        final static int size = 1024;

        /**
         * The fileURL method downloads a file from the Internet and writes
         * the contents to a local file.
         *
         * @param  String the URL to download from
         * @param  String the path of the local system to write the contents to
         * @return String any error messages from the execution of the command
         */
        public static String fileUrl(String fAddress, String localFileName) {
            OutputStream outStream = null;
            URLConnection uCon = null;
            InputStream is = null;
            try {
                URL Url;
                byte[] buf;
                int ByteRead;
                Url = new URL(fAddress);
                outStream = new BufferedOutputStream(new FileOutputStream(localFileName));
                uCon = Url.openConnection();
                is = uCon.getInputStream();
                buf = new byte[size];
                while ((ByteRead = is.read(buf)) != -1) {
                    outStream.write(buf, 0, ByteRead);
                }
            } catch (Exception e) {
                return "Error: Can not download file from the internet. Please check that the URL is valid, the server is online, and the proper permissions are set. The error message is:" + e.toString();
            } finally {
                try {
                    is.close();
                    outStream.close();
                } catch (IOException e) {
                    return "Error: Can not download file from the internet. Please check that the URL is valid, the server is online, and the proper permissions are set." + e.toString();
                }
            }
            /* Return empty string if there are no errors */
            return "";
        }
    }
}
