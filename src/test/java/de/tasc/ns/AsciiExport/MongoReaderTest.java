package de.tasc.ns.AsciiExport;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Calendar;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tanja on 25.04.16.
 */
@RunWith(JUnit4.class)
public class MongoReaderTest {

    @Test
    public void testCountEntries() {
        de.tasc.ns.AsciiExport.MongoReader reader = new de.tasc.ns.AsciiExport.MongoReader(TestUtil.getDateExpression());
        reader.countEntries();
    }

    @Test
    public void testGetEntries() {

        Bson dateExpr = TestUtil.getDateExpression();
        MongoReader reader = new MongoReader(dateExpr);
        reader.getEntries();
    }

    @Test
    public void getTreatments() {


        MongoReader reader = new MongoReader(TestUtil.getDateExpression());
        FindIterable<Document> docs = reader.getTreatments();
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

}

