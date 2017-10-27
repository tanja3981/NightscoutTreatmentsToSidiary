package de.tasc.ns.AsciiExport;

import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
public class AsciiWriter {
    private static final Logger logger = LoggerFactory.getLogger(AsciiWriter.class);

    public static final String NEWLINE = "\r\n";

    private final static java.text.DateFormat df = new SimpleDateFormat("dd.MM.yyyy;HH:mm;");

    private StringBuilder builder = new StringBuilder();

    private final FileWriter fos;
    private final BufferedWriter fw;


    public AsciiWriter(String filename) throws IOException, UnhandledExportException {
        verify(filename);
        fos = new FileWriter(filename);
        fw = new BufferedWriter(fos);
        writeHeader();
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

    public void writeLine(Date date, Number cgms, Number bg, Number kh, Number bolus, String remark) {

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

    public void close() throws IOException {
        flush();
        fw.close();
    }

    public void verify(String path) throws UnhandledExportException {
        File exportFile = new File(path);
        if (!exportFile.canWrite()) {
            logger.error("Can't write export file " + path);
            throw new UnhandledExportException(path + " is not writable.");
        }
    }
}
