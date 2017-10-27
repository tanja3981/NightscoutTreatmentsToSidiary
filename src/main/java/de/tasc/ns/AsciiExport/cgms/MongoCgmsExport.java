package de.tasc.ns.AsciiExport.cgms;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.MongoReader;
import de.tasc.ns.AsciiExport.debug.CounterMap;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoCgmsExport {
    private final static Logger logger = LoggerFactory.getLogger(MongoCgmsExport.class);
    private final java.text.DateFormat ds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final AsciiWriter writer;


    public MongoCgmsExport(AsciiWriter writer) {
        this.writer = writer;
    }

    public void exportBgEntries(MongoReader reader) throws UnhandledExportException {

        FindIterable<Document> results = reader.getEntries();
        MongoCursor<Document> iterator = results.iterator();

        while (iterator.hasNext()) {
            Document doc = iterator.next();
            //Long dateValue = doc.getLong("date");
            String dateString = doc.getString("dateString");
            if (dateString == null) {
                logger.info("Skipping entry because of missing dateString: " + doc.toJson());
                continue;
            }
            Date date;
            try {
                date = ds.parse(dateString);
            } catch (ParseException e) {
                logger.error("Date Parse Fehler bei " + doc.toJson());
                throw new UnhandledExportException(e);
            }

            String type = doc.getString("type");
            switch (type) {
                case "sgv":
                    CounterMap.get().add("cgms");
                    parseSGV(date, doc);
                    break;
                case "sensor":
                    //nothing interesting in here
                    break;
                case "cal":
                    //skip this: the corresponding mbg entry will contain the bg value measured and we won't find any interesting data here
                    break;
                case "mbg":  //calibrations
                    CounterMap.get().add("calibrations");
                    parseCalibration(date, doc);
                    break;
                default:
                    logger.warn("Found unknown type for cgms data: " + type + ": " + doc.toJson());
                    break;
            }
        }
    }

    private void parseCalibration(Date date, Document doc) {
        Number mbg = (Number) doc.get("mbg");
        writer.writeLine(date, null, mbg.intValue(), null, null, null);
    }


    private void parseSGV(Date date, Document doc) {
        Number sgv = (Number) doc.get("sgv");
        writer.writeCgms(date, sgv.intValue());

    }

}
