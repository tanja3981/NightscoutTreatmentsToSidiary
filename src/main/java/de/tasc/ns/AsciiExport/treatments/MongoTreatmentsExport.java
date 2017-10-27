package de.tasc.ns.AsciiExport.treatments;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.MongoReader;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoTreatmentsExport {
    private static final Logger logger = LoggerFactory.getLogger(MongoTreatmentsExport.class);

    private static final SimpleDateFormat timeDF = new SimpleDateFormat("HH:mm");

    private static final DateFormat ds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static final DateFormat dsFallback1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private static final DateFormat dsFallback2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");


    private AsciiWriter writer;

    public MongoTreatmentsExport(AsciiWriter writer) {
        this.writer = writer;
    }

    public void exportTreatments(MongoReader reader) throws IOException {

        FindIterable<Document> results = reader.getTreatments();
        MongoCursor<Document> iterator = results.iterator();
        StringBuilder builder;

        while (iterator.hasNext()) {
            builder = new StringBuilder();

            Document doc = iterator.next();
            String dateString = doc.getString("created_at");
            Date date = null;
            try {
                date = parseDate(dateString);
            } catch (ParseException e) {
                logger.warn("Unparseable date: " + doc.toJson());
            }

//            String enteredBy = doc.getString("enteredBy");
            String eventType = doc.getString("eventType");
            Number carbs = (Number) doc.get("carbs");
            Number insulin = (Number) doc.get("insulin");
            Number glucose = (Number) doc.get("glucose");
            //String glucoseType = doc.getString("glucoseType");
            //String units = doc.getString("units");
            Integer duration = doc.getInteger("duration");
            Integer percent = doc.getInteger("percent");
            //Integer preBolus = doc.getInteger("preBolus");
            String notes = doc.getString("notes");


            if (eventType.equals("Temp Basal") && duration != null && percent != null) {
                int hours = duration / 60;
                int minutes = duration % 60;
                //Temporäre Basalrate (-40%/00:10)
                builder.append(timeDF.format(date));
                builder.append(": Temp Basalrate (");
                builder.append(percent.intValue());
                builder.append("%/");
                builder.append(String.format("%02d", hours));
                builder.append(":");
                builder.append(String.format("%02d", minutes));
                builder.append(")");
                //builder.append(duration + " Minuten");
            } else if (notes != null) {
                builder.append(notes);
            }
            String comment = builder.toString();
            if (StringUtils.isEmpty(comment)) {
                comment = null;
            }
            writer.writeLine(date, null, glucose, carbs, insulin, comment);
        }
    }

    private Date parseDate(String dateString) throws ParseException {
        try {
            return ds.parse(dateString);
        } catch (ParseException e1) {
//try 2nd version of date
            try {
                return dsFallback1.parse(dateString);
            } catch (ParseException e2) {
                return dsFallback2.parse(dateString);
            }
        }

    }


}
