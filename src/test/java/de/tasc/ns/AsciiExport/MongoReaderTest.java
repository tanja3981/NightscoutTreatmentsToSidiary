package de.tasc.ns.AsciiExport;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by tanja on 25.04.16.
 */
@RunWith(JUnit4.class)
public class MongoReaderTest {

    @Test
    public void testCountEntries() {
        de.tasc.ns.AsciiExport.MongoReader reader = new de.tasc.ns.AsciiExport.MongoReader();
        reader.countEntries();
    }

    @Test
    public void testGetEntries() {
        de.tasc.ns.AsciiExport.MongoReader reader = new de.tasc.ns.AsciiExport.MongoReader();
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.DAY_OF_MONTH, 24);
        Calendar calTo = Calendar.getInstance();
        calTo.set(Calendar.DAY_OF_MONTH, 24);

        reader.getEntries(calFrom.getTime(), new Date());

    }

    /*
    @Test
    public void getTreatments() {
        MongoReader reader = new MongoReader();
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.DAY_OF_MONTH, 20);
        Calendar calTo = Calendar.getInstance();
        calTo.set(Calendar.DAY_OF_MONTH, 24);

        FindIterable<Document> docs = reader.getTreatments(calFrom.getTime(), new Date());
        Document doc = docs.first();
        assertNotNull(doc);
        String cd = doc.getString("created_at");
        try {
            long d = Date.parse(cd);
            System.out.println(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}

