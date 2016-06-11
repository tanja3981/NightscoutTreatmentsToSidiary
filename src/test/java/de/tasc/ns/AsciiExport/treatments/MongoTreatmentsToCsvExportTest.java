package de.tasc.ns.AsciiExport.treatments;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by tanja on 11.06.16.
 */
public class MongoTreatmentsToCsvExportTest {

    @Test
    public void exportTreatments() throws IOException, ParseException {


        LocalDateTime fromDate = new LocalDateTime(2015, 1, 1, 0, 0, 0);
        LocalDateTime toDate = new LocalDateTime(2017, 1, 1, 0, 0, 0);

        FileWriter fos = new FileWriter("/home/tanja/vm-share/treatments.csv");
        BufferedWriter writer = new BufferedWriter(fos);

        MongoTreatmentsToCsvExport export = new MongoTreatmentsToCsvExport(writer);
        export.exportTreatments(fromDate.toDate(), toDate.toDate());

        writer.flush();
        writer.close();
        fos.close();
    }

}