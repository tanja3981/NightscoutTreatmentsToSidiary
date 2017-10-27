package de.tasc.ns.AsciiExport.treatments;

import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.ExportSettings;
import de.tasc.ns.AsciiExport.MongoReader;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by tanja on 11.06.16.
 */
public class MongoTreatmentsToCsvExportTest {

    private static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";
    private static final String databaseName = "tanja3981db";
    private ExportSettings settings;

    @Before
    public void setup() {
        settings = ExportSettings.getDefaultSettings();
        settings.setDatabaseName(databaseName);
        settings.setMongoURI(mongoURI);
    }
    @Test
    public void exportTreatments() throws IOException, ParseException, UnhandledExportException {

        MongoReader reader = new MongoReader(settings);
        AsciiWriter writer = new AsciiWriter("treatments.csv");
        MongoTreatmentsExport export = new MongoTreatmentsExport(writer);
        export.exportTreatments(reader);

        writer.close();
    }

}