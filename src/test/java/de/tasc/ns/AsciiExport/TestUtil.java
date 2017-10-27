package de.tasc.ns.AsciiExport;

import org.bson.conversions.Bson;

import java.util.Calendar;
import java.util.Date;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

public class TestUtil {

    public static Bson getDateExpression() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        Date fromDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.MONTH, Calendar.DECEMBER);
        Date toDate = calendar.getTime();

        return and(
                gt("date", fromDate.getTime()),
                lt("date", toDate.getTime())
        );
    }

}
