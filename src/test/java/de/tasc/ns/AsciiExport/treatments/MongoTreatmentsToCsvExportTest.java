package de.tasc.ns.AsciiExport.treatments;

import de.tasc.ns.AsciiExport.TestUtil;
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

        FileWriter fos = new FileWriter("treatments.csv");
        BufferedWriter writer = new BufferedWriter(fos);

        MongoTreatmentsToCsvExport export = new MongoTreatmentsToCsvExport(writer);
        export.exportTreatments(TestUtil.getDateExpression());

        writer.flush();
        writer.close();
        fos.close();
    }

}