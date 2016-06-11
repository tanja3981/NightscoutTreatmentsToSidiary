package de.tasc.ns.AsciiExport;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
public class AsciiWriter {

    public static final String NEWLINE = "\r\n";

    private final static java.text.DateFormat df = new SimpleDateFormat("dd.MM.yyyy;HH:mm;");

    private StringBuilder builder = new StringBuilder();

    public AsciiWriter() throws IOException {
        writeHeader();
    }

    public void saveFile(String filename) throws IOException {

    }

    private void writeHeader() throws IOException {
        String header = "DAY;TIME;UDT_CGMS";
        builder.append(header);
        builder.append(NEWLINE);
    }

    public static void writeLine(Writer writer, Date date, Integer bg) throws IOException {

        writer.write(df.format(date) + bg + NEWLINE);
    }

    public static void writeLine(Writer writer, Date date, String text) throws IOException {

        writer.write(df.format(date) + " " + text + NEWLINE);
    }

}
