package de.tasc.ns.AsciiExport.cgms;

import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.ExportSettings;
import de.tasc.ns.AsciiExport.MongoReader;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoCgmsExportTest {

    private static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";
    private static final String databaseName = "tanja3981db";


    @Test
    public void exportBgEntries() throws Exception {

        ExportSettings settings = ExportSettings.getDefaultSettings();
        settings.setMongoURI(mongoURI);
        settings.setDatabaseName(databaseName);
        AsciiWriter writer = new AsciiWriter("bgentries.csv");
        MongoReader reader = new MongoReader(settings);

        MongoCgmsExport export = new MongoCgmsExport(writer);
        export.exportBgEntries(reader);

        writer.close();

    }

}