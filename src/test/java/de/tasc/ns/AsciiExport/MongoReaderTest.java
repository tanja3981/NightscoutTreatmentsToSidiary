package de.tasc.ns.AsciiExport;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import de.tasc.ns.AsciiExport.treatments.MongoTreatmentsToCsvExport;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


        MongoReader reader = new MongoReader(null);
        FindIterable<Document> docs = reader.getTreatments();
        assertNotNull(docs);
        Calendar now = Calendar.getInstance();
        Block<Document> printer = new Block<Document>() {
            @Override
            public void apply(Document document) {
                String dateString = document.getString("created_at");
                System.out.println(dateString);
            }
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

