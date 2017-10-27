package de.tasc.ns.AsciiExport.cgms;

import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.ExportSettings;
import de.tasc.ns.AsciiExport.MongoReader;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoCgmsExportTest {

    private static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";
    private static final String databaseName = "tanja3981db";
    private ExportSettings settings;
    private File outputFile;

    @Before
    public void setup() throws IOException {
        settings = new ExportSettings();
        settings.setDatabaseName(databaseName);
        settings.setMongoURI(mongoURI);


        outputFile = File.createTempFile("bgentries", "csv");
        outputFile.mkdirs();
    }
    @After
    public void teardown() {
        outputFile.deleteOnExit();
    }

    @Test
    public void exportBgEntries() throws Exception {

        AsciiWriter writer = new AsciiWriter(outputFile.getAbsolutePath());
        MongoReader reader = new MongoReader(settings);

        MongoCgmsExport export = new MongoCgmsExport(writer);
        export.exportBgEntries(reader);

        writer.close();

    }

}