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

        AsciiWriter writer = new AsciiWriter();

        MongoAsciiExport export = new MongoAsciiExport(writer);
        export.exportBgEntries(TestUtil.getDateExpression());

        writer.saveFile("/home/tanja/vm-share/bgentries.csv");

    }

}