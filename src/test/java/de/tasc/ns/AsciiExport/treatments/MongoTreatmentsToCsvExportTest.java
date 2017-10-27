package de.tasc.ns.AsciiExport.treatments;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import de.tasc.ns.AsciiExport.AsciiWriter;
import de.tasc.ns.AsciiExport.ExportSettings;
import de.tasc.ns.AsciiExport.MongoReader;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;

import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tanja on 11.06.16.
 */
public class MongoTreatmentsToCsvExportTest {


    @Test
    public void exportTreatments() throws IOException, ParseException, UnhandledExportException {
        MongoReader reader = mock(MongoReader.class);
        FindIterable<Document> resultMock = mock(FindIterable.class);
        MongoCursor<Document> iteratorMock = mock(MongoCursor.class);
        when(reader.getTreatments()).thenReturn(resultMock);
        when(resultMock.iterator()).thenReturn(iteratorMock);
        when(iteratorMock.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Document obj = Document.parse("{\n" +
                "    \"_id\": {\n" +
                "        \"$oid\": \"5735ebbc82c9fd966ac5130a\"\n" +
                "    },\n" +
                "    \"enteredBy\": \"\",\n" +
                "    \"eventType\": \"Correction Bolus\",\n" +
                "    \"insulin\": 2.8,\n" +
                "    \"created_at\": \"2016-05-13T14:59:08.263Z\"\n" +
                "}");
        Document xDripObj = Document.parse("{\n" +
                "    \"_id\": {\n" +
                "        \"$oid\": \"568a2a6481b8426b89964b56\"\n" +
                "    },\n" +
                "    \"timestamp\": 1508838541136,\n" +
                "    \"eventType\": \"<none>\",\n" +
                "    \"enteredBy\": \"xdrip\",\n" +
                "    \"uuid\": \"568a2a64-81b8-426b-8996-4b56df674846\",\n" +
                "    \"insulin\": 2,\n" +
                "    \"created_at\": \"2017-10-24T09:49:01Z\",\n" +
                "    \"sysTime\": \"2017-10-24T11:49:01.136+0200\"\n" +
                "}");
        Document pebblesObj = Document.parse("{\n" +
                "    \"_id\": {\n" +
                "        \"$oid\": \"59e2433e3a2dacae3e3984fd\"\n" +
                "    },\n" +
                "    \"enteredBy\": \"Pebble\",\n" +
                "    \"eventType\": \"Note\",\n" +
                "    \"carbs\": 24,\n" +
                "    \"created_at\": \"2017-10-14T17:02:54.688Z\"\n" +
                "}");
        when(iteratorMock.next()).thenReturn(obj).thenReturn(xDripObj).thenReturn(pebblesObj);

        AsciiWriter writer = mock(AsciiWriter.class);


        MongoTreatmentsExport export = new MongoTreatmentsExport(writer);
        export.exportTreatments(reader);

        verify(writer).writeLine(anyObject(), eq(null),eq( null), eq(null), eq(2.8),eq( null));
        verify(writer).writeLine(anyObject(), eq(null), eq(null), eq(null), eq(2), eq(null));
        verify(writer).writeLine(anyObject(), eq(null), eq(null), eq(24), eq(null), eq(null));
    }

}