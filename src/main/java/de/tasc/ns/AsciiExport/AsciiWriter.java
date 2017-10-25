package de.tasc.ns.AsciiExport;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
public class AsciiWriter {

    public static final String NEWLINE = "\r\n";

    private final static java.text.DateFormat df = new SimpleDateFormat("dd.MM.yyyy;HH:mm;");

    private StringBuilder builder = new StringBuilder();

    private final FileWriter fos;
    private final BufferedWriter fw;


    public AsciiWriter(String filename) throws IOException {
        this(new File(filename));
    }

    public AsciiWriter(File file) throws IOException {
        fos = new FileWriter(file);
        fw = new BufferedWriter(fos);
        writeHeader();
    }

    public void saveFile() throws IOException {

        fw.write(builder.toString());
        fw.close();
    }

    private void writeHeader() {
        String header = "DAY;TIME;UDT_CGMS;BG_LEVEL;CH_GR;BOLUS;REMARK";
        builder.append(header);
        newLine();
    }

    public void writeCgms(Date date, Integer cgms) {
        builder.append(df.format(date) + cgms);
        newLine();
    }

    public void writeLine(Date date, Integer cgms, Integer bg, Integer kh, Integer bolus, String remark) {

        builder.append(df.format(date));
        if (cgms != null) {
            builder.append(cgms);
        }
        separator();
        if (bg != null) {
            builder.append(bg);
        }
        separator();
        if (kh != null) {
            builder.append(kh);
        }
        separator();
        if (bolus != null) {
            builder.append(bolus);
        }
        separator();
        if (remark != null) {
            builder.append(remark);
        }
        newLine();
    }

    private void separator() {
        builder.append(";");
    }

    private void newLine() {
        builder.append(NEWLINE);
    }

    public synchronized void flush() throws IOException {

        fw.write(builder.toString());
        builder = new StringBuilder();
        fw.flush();
    }

    /*public void writeLine(Date date, String text) {

        builder.append(df.format(date) + " " + text);
        newLine();
    }*/

}
