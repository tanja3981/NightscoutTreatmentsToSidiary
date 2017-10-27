package de.tasc.ns.AsciiExport.treatments;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import de.tasc.ns.AsciiExport.MongoReader;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoTreatmentsToCsvExport {
    public static final DateFormat ds = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

    public final String NEW_LINE = System.getProperty("line.separator");
    public final String COMMA = ";";

    SimpleDateFormat dayDF = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat timeDF = new SimpleDateFormat("HH:mm");
    Writer writer;

    public MongoTreatmentsToCsvExport(Writer writer) {
        this.writer = writer;
    }

    public void exportTreatments(Bson dateExpr) throws IOException, ParseException {

        MongoReader reader = new MongoReader(dateExpr);
        FindIterable<Document> results = reader.getTreatments();
        MongoCursor<Document> iterator = results.iterator();
        StringBuilder builder;
        writer.write("DAY;TIME;BG_LEVEL;CH_GR;BOLUS;REMARK");
        writer.write(NEW_LINE);
        while (iterator.hasNext()) {
            builder = new StringBuilder();

            Document doc = iterator.next();
            String dateString = doc.getString("created_at");
            Date date = ds.parse(dateString);

            String enteredBy = doc.getString("enteredBy");
            String created_at = doc.getString("created_at");
            String eventType = doc.getString("eventType");
            Integer carbs = doc.getInteger("carbs");
            Number insulin = (Number) doc.get("insulin");
            Number glucose = (Number) doc.get("glucose");
            String glucoseType = doc.getString("glucoseType");
            String units = doc.getString("units");
            Integer duration = doc.getInteger("duration");
            Integer percent = doc.getInteger("percent");
            Integer preBolus = doc.getInteger("preBolus");
            String notes = doc.getString("notes");

            builder.append(dayDF.format(date));
            builder.append(COMMA);
            builder.append(timeDF.format(date));
            builder.append(COMMA);
            if (glucose != null) {
                builder.append(glucose);
            }

            builder.append(COMMA);
            if (carbs != null) {
                builder.append(carbs);
            }
            builder.append(COMMA);
            if (insulin != null) {
                builder.append(insulin);
            }
            builder.append(COMMA);

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

            writer.write(builder.toString());
            writer.write(NEW_LINE);
        }
    }


}
