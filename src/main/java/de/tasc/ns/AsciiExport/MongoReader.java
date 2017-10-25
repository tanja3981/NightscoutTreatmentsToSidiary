package de.tasc.ns.AsciiExport;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Date;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoReader {

    public static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";

    private final MongoDatabase database;
    private final Bson dateExpr;

    public MongoReader(Bson dateExpr) {
        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient = new MongoClient(connectionString);

        database = mongoClient.getDatabase("tanja3981db");

        this.dateExpr = dateExpr;

    }

    public FindIterable<Document> getTreatments() {
        MongoCollection<Document> treatments = database.getCollection("treatments");

        FindIterable<Document> results;
        if (dateExpr != null) {
            results = treatments.find(
                    dateExpr
            );
        } else {
            results = treatments.find();
        }

        return results;
    }

    public FindIterable<Document> getEntries() {
        MongoCollection<Document> entries = database.getCollection("entries");
        FindIterable<Document> results;
        if (dateExpr != null) {
            results = entries.find(dateExpr);
        } else {
            results = entries.find();
        }
        return results;
    }

    public void countEntries() {

        MongoCollection<Document> collection = database.getCollection("entries");
        System.out.println(collection.count());
    }
}