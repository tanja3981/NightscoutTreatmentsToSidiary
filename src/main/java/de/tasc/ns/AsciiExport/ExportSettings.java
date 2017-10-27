package de.tasc.ns.AsciiExport;

import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

public class ExportSettings {

    private static final Logger logger = LoggerFactory.getLogger(ExportSettings.class);
    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");


    private Date from;
    private Date to;
    private String exportFile;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private Locale locale;
    private String mongoURI;
    private String databaseName;
    private String entriesName = "entries";
    private String treatmentsName = "treatments";

    public ExportSettings(Properties properties) throws ParseException {

        readPropertiesFromProperties(properties);
    }

    private void readPropertiesFromProperties(Properties properties) throws ParseException {

        String strFrom = properties.getProperty("date.from");
        if (!StringUtils.isEmpty(strFrom)) {
            try {
                Date from = formatter.parse(strFrom);
                setFrom(from);
            } catch (ParseException e) {
                logger.error("Fehler beim Parsen von 'date.from':", e);
                throw e;
            }
        }
        String strTo = properties.getProperty("date.to");
        if (strTo != null) {
            try {
                Date end = formatter.parse(strTo);
                setTo(end);
            } catch (ParseException e) {
                logger.error("Fehler beim Parsen von 'date.to':", e);
                throw e;
            }
        }
        String path = properties.getProperty("export.toFile");
        if (path != null) {
            setExportFile(path);
        }
        String db = properties.getProperty("mongo.uri");
        setMongoURI(db);
        String dbname = properties.getProperty("mongo.database");
        setDatabaseName(dbname);
        String entries = properties.getProperty("mongo.entries");
        if (entries != null) {
            setEntriesName(entries);
        }
        String treatments = properties.getProperty("mongo.treatments");
        if (treatments != null) {
            setTreatmentsName(treatments);
        }
    }

    public ExportSettings() {

    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public String getExportFile() {
        return exportFile;
    }

    public void setExportFile(String exportFile) {
        this.exportFile = exportFile;
    }

    public boolean verify() throws UnhandledExportException {
        if (StringUtils.isEmpty(mongoURI)) {
            throw new UnhandledExportException("Mongo URI is missing!");
        }
        if (StringUtils.isEmpty(databaseName)) {
            throw new UnhandledExportException("Database name is missing!");
        }
        if (StringUtils.isEmpty(entriesName)) {
            throw new UnhandledExportException("Entries table name is missing!");
        }
        if (StringUtils.isEmpty(treatmentsName)) {
            throw new UnhandledExportException("Treatments table name is missing!");
        }
        return true;
    }


    public static ExportSettings getDefaultSettings() {
        ExportSettings settings = new ExportSettings();


        return settings;
    }

    public String getMongoURI() {
        return mongoURI;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getEntriesName() {
        return entriesName;
    }

    public String getTreatmentsName() {
        return treatmentsName;
    }

    public void setMongoURI(String mongoURI) {
        this.mongoURI = mongoURI;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public void setEntriesName(String entriesName) {
        this.entriesName = entriesName;
    }

    public void setTreatmentsName(String treatmentsName) {
        this.treatmentsName = treatmentsName;
    }

}
