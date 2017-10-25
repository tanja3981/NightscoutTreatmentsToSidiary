package de.tasc.ns.AsciiExport;

import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.lt;

/**
 * Executable.
 */
public class NightscoutExportToSidiary {

    private final static Logger logger = LoggerFactory.getLogger(NightscoutExportToSidiary.class);

    private final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");


    public static void main(String[] args) {

        NightscoutExportToSidiary exe = new NightscoutExportToSidiary();
        ExportSettings settings;
        if (args != null && args.length > 0) {
            logger.debug("NightscoutExportToSidiary started with " + args.length + " parameters.");
            String filepath = args[0];
            try {
                Properties properties = exe.readProperties(filepath);
                settings = exe.parseProperties(properties);
            } catch (IOException e) {
                logger.error("Properties file cannot be read. Export aborted.", e);
                return;
            } catch (ParseException e) {
                logger.error("Properties cannot be parsed. Export aborted.");
                return;
            }
        } else {
            logger.debug("NightscoutExportToSidiary started with default settings.");
            //use default settings
            settings = getDefaultSettings();
        }

        try {
            exe.doExport(settings);
        } catch (UnhandledExportException e) {
            logger.error("Fehler beim Export", e);
        }
    }

    private static ExportSettings getDefaultSettings() {
        ExportSettings settings = new ExportSettings();

        JFileChooser chooser = new JFileChooser();
        int resp = chooser.showSaveDialog(null);
        if (resp == JFileChooser.APPROVE_OPTION) {
            settings.setExportFile(chooser.getSelectedFile());
        }

        Locale locale = Locale.GERMANY;
        Locale.setDefault(locale);

        return settings;
    }

    private void doExport(ExportSettings settings) throws UnhandledExportException {
        logger.info("Start export...");

        try {
            AsciiWriter writer = new AsciiWriter(settings.getExportFile());

            MongoAsciiExport export = new MongoAsciiExport(writer);
            Bson dateExpr = createDateExpression(settings.getFrom(), settings.getTo());
            export.exportBgEntries(dateExpr);

            writer.saveFile();
        } catch (IOException e) {
            logger.error("Export file cannot be saved.", e);
        }
    }

    private Bson createDateExpression(Date from, Date to) {

        if (to != null && from != null) {
            return and(
                    gt("created_at", from.getTime()),
                    lt("created_at", to.getTime())
            );
        } else if (to != null) {
            return lt("created_at", to.getTime());
        } else if (from != null) {
            return gt("created_at", from.getTime());
        } else {
            return null;
        }
    }

    private ExportSettings parseProperties(Properties properties) throws ParseException, IOException {

        ExportSettings settings = new ExportSettings();

        String strFrom = properties.getProperty("date.from");
        if (strFrom != null) {
            try {
                Date from = formatter.parse(strFrom);
                settings.setFrom(from);
            } catch (ParseException e) {
                logger.error("Fehler beim Parsen von 'date.from':", e);
                throw e;
            }
        }
        String strTo = properties.getProperty("date.to");
        if (strTo != null) {
            try {
                Date end = formatter.parse(strTo);
                settings.setTo(end);
            } catch (ParseException e) {
                logger.error("Fehler beim Parsen von 'date.to':", e);
                throw e;
            }
        }
        String path = properties.getProperty("export.toFile");
        if (path != null) {
            File exportFile = new File(path);
            if (exportFile.canWrite()) {
                settings.setExportFile(exportFile);
            } else {
                logger.error("Can't write export file " + path);
                throw new IOException(path + " is not writable.");
            }
        }

        return settings;
    }


    public Properties readProperties(String filepath) throws IOException {

        File file = new File(filepath);
        if (file.exists()) {

            Properties properties = new Properties();
            properties.load(new FileInputStream(file));

            return properties;
        } else {
            throw new FileNotFoundException("File (" + filepath + ") couldn't be found");
        }
    }
}
