package de.tasc.ns.AsciiExport;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoReader {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoReader.class);

    public static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";

    private final MongoDatabase database;
    private Bson dateExpr;

    public MongoReader(Bson dateExpr) {
        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient = new MongoClient(connectionString);

        database = mongoClient.getDatabase("tanja3981db");

        //this.dateExpr = dateExpr;

    }

    public FindIterable<Document> getTreatments() {
        MongoCollection<Document> treatments = database.getCollection("treatments");

        FindIterable<Document> results;
        if (dateExpr != null) {
            LOGGER.info("Searching mongo database 'treatments' with date restriction.");
            results = treatments.find(dateExpr);
        } else {
            LOGGER.info("Searching mongo database 'treatments' with no restriction.");
            results = treatments.find();
        }

        return results;
    }

    public FindIterable<Document> getEntries() {
        MongoCollection<Document> entries = database.getCollection("entries");
        FindIterable<Document> results;
        if (dateExpr != null) {
            LOGGER.info("Searching mongo database 'entries' with date restriction.");
            results = entries.find(dateExpr);
        } else {
            LOGGER.info("Searching mongo database 'entries' with no restriction.");
            results = entries.find();
        }
        return results;
    }

    public void countEntries() {

        MongoCollection<Document> collection = database.getCollection("entries");
        System.out.println(collection.count());
    }
}