package de.tasc.ns.AsciiExport;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.Document;
import org.bson.conversions.Bson;
import sun.awt.image.PixelConverter;

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
    private final AsciiWriter writer;


    public MongoAsciiExport(AsciiWriter writer) {
        this.writer = writer;
    }

    public void exportBgEntries(Bson dateExp) throws UnhandledExportException {

        MongoReader reader = new MongoReader(dateExp);
        FindIterable<Document> results = reader.getEntries();

        MongoCursor<Document> iterator = results.iterator();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            //Long dateValue = doc.getLong("date");
            String dateString = doc.getString("dateString");
            if (dateString == null) {
                continue;
            }
            Date date = null;
            try {
                date = ds.parse(dateString);
            } catch (ParseException e) {
                throw new UnhandledExportException(e);
            }

            String type = doc.getString("type");
            if (type.equals("sgv")) {
                parseSGV(date, doc);
            } else if (type.equals("sensor")) {
                parseSensor(date, doc);
            } else if (type.equals("cal")) {
                parseCalibration(date, doc);
            } else if (type.equals("mbg")) {
                parseMbg(date, doc);
            } else {
                parseOther(date, doc);
            }
        }
    }


    private void parseMbg(Date date, Document doc) {
        Number mbg = (Number) doc.get("mbg");
        writer.writeLine(date, null, mbg, null, null, null, null);
    }

    private void parseCalibration(Date date, Document doc) {
        Number slope = (Number) doc.get("slope");
        Number intercept = (Number) doc.get("intercept");

        writer.writeLine(date, null, bg, null, null, null, null);
//ate date, Integer cgms, Integer bg, Integer kh, Integer bolus, String remark
    }

    private void parseSensor(Date date, Document doc) {
        writer.writeLine(date, "Sensor");
    }

    private void parseOther(Date date, Document doc) {
        writer.writeLine(date, "Unknown type: " + doc.getString("type"));
    }

    private void parseSGV(Date date, Document doc) {
        Number sgv = (Number) doc.get("sgv");
        writer.writeLine(date, sgv.intValue());

    }

}
