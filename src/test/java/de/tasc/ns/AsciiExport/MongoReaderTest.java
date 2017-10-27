package de.tasc.ns.AsciiExport;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

import static com.mongodb.client.model.Filters.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tanja on 25.04.16.
 */
@RunWith(JUnit4.class)
public class MongoReaderTest {
    private static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";
    private static final String databaseName = "tanja3981db";
    private ExportSettings settings;

    @Before
    public void setup() {
        settings = new ExportSettings();
        settings.setDatabaseName(databaseName);
        settings.setMongoURI(mongoURI);
    }

    @Test
    public void testCountEntries() throws UnhandledExportException {

        MongoReader reader = new MongoReader(settings);
        reader.countEntries();
    }

    @Test
    public void testGetEntries() throws UnhandledExportException {

        MongoReader reader = new MongoReader(settings);
        reader.getEntries();
    }

    @Test
    public void getTreatments() throws UnhandledExportException {


        MongoReader reader = new MongoReader(settings);
        FindIterable<Document> docs = reader.getTreatments();
        assertNotNull(docs);
        Block<Document> printer = document -> {
            String dateString = document.getString("created_at");
            System.out.println(dateString);
        };
        //printer = System.out::println;
        docs.forEach(printer);

        Document doc = docs.first();
        assertNotNull(doc);
        String cd = doc.getString("created_at");
        System.out.println(cd);

    }

    public static Bson getDateExpression() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        Date fromDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        Date toDate = calendar.getTime();

        return and(
                gt("created_at", fromDate),
                lt("created_at", toDate)
        );
    }

}

