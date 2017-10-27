package de.tasc.ns.AsciiExport;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoIterable;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import static com.mongodb.client.model.Filters.*;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tanja on 25.04.16.
 */
@RunWith(JUnit4.class)
public class MongoReaderTest {

    private ExportSettings settings;

    @Before
    public void setup() throws Exception {
        String stream = MongoReaderTest.class.getClassLoader().getResource("settings.properties").getPath();
        Properties properties = new Properties();
        properties.load(new FileReader(stream));
        settings = new ExportSettings(properties);
    }

    @Test
    public void testGetEntries() throws UnhandledExportException {

        MongoReader reader = new MongoReader(settings);
        FindIterable<Document> entries = reader.getEntries();
        assertNotNull(entries);
    }

    @Test
    public void getCollections() throws UnhandledExportException {
        MongoReader reader = new MongoReader(settings);
        MongoIterable<String> collections = reader.getCollections();

        for(String c : collections) {
            System.out.println(c);
        }
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

