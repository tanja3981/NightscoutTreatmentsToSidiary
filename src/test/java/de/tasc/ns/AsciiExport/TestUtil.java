package de.tasc.ns.AsciiExport;

import org.bson.conversions.Bson;

import java.util.Calendar;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

public class TestUtil {

    public static Bson getDateExpression() {
        Calendar calFrom = Calendar.getInstance();
        calFrom.set(Calendar.DAY_OF_MONTH, 24);
        Calendar calTo = Calendar.getInstance();
        calTo.set(Calendar.DAY_OF_MONTH, 24);


        return and(
                gt("date", calFrom.getTime()),
                lt("date", calTo.getTime())
        );
    }

}
