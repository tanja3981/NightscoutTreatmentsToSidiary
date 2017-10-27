package de.tasc.ns.AsciiExport.cgms;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.ExportSettings;
import de.tasc.ns.AsciiExport.MongoReader;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoCgmsExportTest {

    @Test
    public void exportBgEntries() throws Exception {

        MongoReader reader = mock(MongoReader.class);
        FindIterable<Document> resultMock = mock(FindIterable.class);
        MongoCursor<Document> iteratorMock = mock(MongoCursor.class);
        when(reader.getEntries()).thenReturn(resultMock);
        when(resultMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Document sgv = Document.parse("{\n" +
                "    \"_id\": {\n" +
                "        \"$oid\": \"57081531a6d8a5a5a8c43afc\"\n" +
                "    },\n" +
                "    \"device\": \"xDrip-BluetoothWixel\",\n" +
                "    \"date\": 1460147503964,\n" +
                "    \"dateString\": \"2016-04-08T22:31:43.964+0200\",\n" +
                "    \"sgv\": 209,\n" +
                "    \"direction\": \"FortyFiveUp\",\n" +
                "    \"type\": \"sgv\",\n" +
                "    \"filtered\": 320832,\n" +
                "    \"unfiltered\": 320832,\n" +
                "    \"rssi\": 100,\n" +
                "    \"noise\": 1,\n" +
                "    \"sysTime\": \"2016-04-08T22:31:43.964+0200\"\n" +
                "}");
        Document mbg = Document.parse("{\n" +
                "    \"_id\": {\n" +
                "        \"$oid\": \"57081531a6d8a5a5a8c43afd\"\n" +
                "    },\n" +
                "    \"device\": \"xDrip-BluetoothWixel\",\n" +
                "    \"type\": \"mbg\",\n" +
                "    \"date\": 1460147291744,\n" +
                "    \"dateString\": \"2016-04-08T22:28:11.744+0200\",\n" +
                "    \"mbg\": 201.80383999999998,\n" +
                "    \"sysTime\": \"2016-04-08T22:28:11.744+0200\"\n" +
                "}");
        Document cal = Document.parse("{\n" +
                "    \"_id\": {\n" +
                "        \"$oid\": \"5708145da6d8a5a5a8c43ae1\"\n" +
                "    },\n" +
                "    \"device\": \"xDrip-BluetoothWixel\",\n" +
                "    \"type\": \"cal\",\n" +
                "    \"date\": 1460147291744,\n" +
                "    \"dateString\": \"2016-04-08T22:28:11.744+0200\",\n" +
                "    \"slope\": 769.2307692307692,\n" +
                "    \"intercept\": 159939.1901018785,\n" +
                "    \"scale\": 1,\n" +
                "    \"sysTime\": \"2016-04-08T22:28:11.744+0200\"\n" +
                "}");
        when(iteratorMock.next()).thenReturn(cal).thenReturn(mbg).thenReturn(sgv);

        AsciiWriter writer = mock(AsciiWriter.class);

        MongoCgmsExport export = new MongoCgmsExport(writer);
        export.exportBgEntries(reader);

        verify(writer).writeLine(anyObject(), eq(null), eq(201), eq(null), eq(null), eq(null));
        verify(writer).writeCgms(anyObject(), eq(209));
    }

}