package de.tasc.ns.AsciiExport;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by tanja on 25.04.16.
 */
public class MongoReader {

    public static final String mongoURI = "mongodb://dbuser:MyDB123@ds013569.mlab.com:13569/tanja3981db";

    private final MongoDatabase database;

    public MongoReader() {
        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongoClient = new MongoClient(connectionString);

        database = mongoClient.getDatabase("tanja3981db");


    }

    public FindIterable<Document> getTreatments(Date from, Date to) {
        MongoCollection<Document> treatments = database.getCollection("treatments");

        FindIterable<Document> results = treatments.find(
                //and(
                //gt("created_at", from.getTime()),
                //lt("created_at", to.getTime())
                //,                eq("type", "sgv")
        //)
        );

        return results;
    }

    public FindIterable<Document> getEntries(Date from, Date to) {
        MongoCollection<Document> entries = database.getCollection("entries");
        FindIterable<Document> results = entries.find(and(
                gt("date", from.getTime()),
                lt("date", to.getTime())
                //,                eq("type", "sgv")
        ));

        return results;
    }

    public void countEntries() {

        MongoCollection<Document> collection = database.getCollection("entries");
        System.out.println(collection.count());
    }
}