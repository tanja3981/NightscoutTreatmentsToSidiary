package de.tasc.ns.AsciiExport;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoAsciiExport {
    private final java.text.DateFormat ds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final Writer writer;

    MongoReader reader = new MongoReader();

    public MongoAsciiExport(Writer writer){
        this.writer = writer;
    }

    public void exportBgEntries(Date fromDate, Date toDate) throws IOException, ParseException {

        FindIterable<Document> results = reader.getEntries(fromDate, toDate);

        MongoCursor<Document> iterator = results.iterator();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            //Long dateValue = doc.getLong("date");
            String dateString = doc.getString("dateString");
            if(dateString == null) {
                continue;
            }
            Date date = ds.parse(dateString);

            String type = doc.getString("type");
            if (type.equals("sgv")) {

                parseSGV(date, doc);
            } else if (type.equals("sensor")) {
                parseSensor(date, doc);
            } else if (type.equals("cal")) {
                parseCalibration(date, doc);
            } else if (type.equals("mbg")) {
                parseMbg( date, doc);
            } else {
                parseOther(date, doc);
            }
        }
    }

    private void parseMbg(Date date, Document doc) throws IOException {
        Double mbg = doc.getDouble("mbg");
        AsciiWriter.writeLine(writer, date, "mbg: " + Double.toString(mbg));
    }

    private void parseCalibration(Date date, Document doc) throws IOException {
        Double slope = doc.getDouble("slope");
        Double intercept = doc.getDouble("intercept");
        AsciiWriter.writeLine(writer, date, "Calibration " + slope + " " + intercept);

    }

    private void parseSensor(Date date, Document doc) throws IOException {
        AsciiWriter.writeLine(writer, date, "Sensor");
    }

    private void parseOther(Date date, Document doc) throws IOException {
        AsciiWriter.writeLine(writer, date, "Unknown type: " + doc.getString("type"));
    }

    private void parseSGV(Date date, Document doc) throws IOException {

        AsciiWriter.writeLine(writer, date, doc.getInteger("sgv"));

    }

}
