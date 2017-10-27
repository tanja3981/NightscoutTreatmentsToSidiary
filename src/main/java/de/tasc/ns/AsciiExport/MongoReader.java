package de.tasc.ns.AsciiExport;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoReader.class);


    private final MongoDatabase database;

    public MongoReader(ExportSettings settings) throws UnhandledExportException {
        LOGGER.info("Verifying mongo settings...");
        settings.verify();

        final String mongoURI = settings.getMongoURI();
        final String databaseName = settings.getDatabaseName();
        final String entriesname = settings.getEntriesName();
        final String treatmentsname = settings.getTreatmentsName();

        LOGGER.info("Connecting to mongoDB at " + mongoURI);
        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient = new MongoClient(connectionString);
        LOGGER.info("Using database " + databaseName);
        database = mongoClient.getDatabase(databaseName);


    }

    public MongoIterable<String> getCollections() {
        return database.listCollectionNames();
    }

    public FindIterable<Document> getTreatments() {
        MongoCollection<Document> treatments = database.getCollection("treatments");

        FindIterable<Document> results;
        //if (dateExpr != null) {
        //  LOGGER.info("Searching mongo database 'treatments' with date restriction.");
        //results = treatments.find(dateExpr);
        //} else {
        LOGGER.info("Searching mongo database 'treatments' with no restriction.");
        results = treatments.find();
        //}

        return results;
    }

    public FindIterable<Document> getEntries() {
        MongoCollection<Document> entries = database.getCollection("entries");
        FindIterable<Document> results;
        //if (dateExpr != null) {
        //  LOGGER.info("Searching mongo database 'entries' with date restriction.");
        //results = entries.find(dateExpr);
        //} else {
        LOGGER.info("Searching mongo database 'entries' with no restriction.");
        results = entries.find();
        //}
        return results;
    }

}