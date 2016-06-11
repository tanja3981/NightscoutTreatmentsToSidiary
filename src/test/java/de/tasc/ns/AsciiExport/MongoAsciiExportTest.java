package de.tasc.ns.AsciiExport;

import org.joda.time.LocalDateTime;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoAsciiExportTest {



    @Test
    public void exportBgEntries() throws Exception {

        LocalDateTime fromDate = new LocalDateTime(2016, 4, 1, 0, 0, 0);
        LocalDateTime toDate = new LocalDateTime(2016, 4, 26, 0, 0, 0);

        FileWriter fos = new FileWriter("/home/tanja/bgentries.csv");
        BufferedWriter writer = new BufferedWriter(fos);
        try {
            MongoAsciiExport export = new MongoAsciiExport(writer);
            export.exportBgEntries(fromDate.toDate(), toDate.toDate());
        } catch (IOException e) {
            e.printStackTrace();
        }

        writer.flush();
        writer.close();
        fos.close();

    }

}