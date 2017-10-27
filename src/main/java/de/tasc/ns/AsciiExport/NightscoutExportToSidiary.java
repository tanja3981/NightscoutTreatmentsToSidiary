package de.tasc.ns.AsciiExport;

import de.tasc.ns.AsciiExport.cgms.MongoCgmsExport;
import de.tasc.ns.AsciiExport.debug.CounterMap;
import de.tasc.ns.AsciiExport.exceptions.UnhandledExportException;
import de.tasc.ns.AsciiExport.treatments.MongoTreatmentsExport;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import static com.mongodb.client.model.Filters.*;

/**
 * Executable.
 */
public class NightscoutExportToSidiary {

    private final static Logger logger = LoggerFactory.getLogger(NightscoutExportToSidiary.class);


    public static void main(String[] args) {

        NightscoutExportToSidiary exe = new NightscoutExportToSidiary();
        ExportSettings settings;
        String filepath = "settings.properties";
        if (args != null && args.length > 0) {
            logger.debug("NightscoutExportToSidiary started with " + args.length + " parameters.");
            filepath = args[0];

        } else {
            logger.debug("NightscoutExportToSidiary started with default settings.");
        }
        try {
            Properties properties = exe.readProperties(filepath);
            settings = exe.parseProperties(properties);

            Locale locale = Locale.GERMANY;
            if (settings.getLocale() != null) {
                locale = settings.getLocale();
            }
            Locale.setDefault(locale);

        } catch (IOException e) {
            logger.error("Properties file cannot be read. Export aborted.", e);
            return;
        } catch (ParseException e) {
            logger.error("Properties cannot be parsed. Export aborted.");
            return;
        }

        try {
            exe.doExport(settings);
        } catch (UnhandledExportException e) {
            logger.error("Fehler beim Export", e);
        }

        cleanup();
    }

    private static String queryExportFile() throws UnhandledExportException {
        JFileChooser chooser = new JFileChooser();
        int resp = chooser.showSaveDialog(null);
        if (resp == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        throw new UnhandledExportException("Ohne Exportdatei kann der Export nicht gestartet werden.");
    }

    private static void cleanup() {
        logger.info("Export finished.");
        logger.info(CounterMap.get().toString());
    }


    private void doExport(ExportSettings settings) throws UnhandledExportException {
        logger.info("Start export...");

        try {
            AsciiWriter writer = new AsciiWriter(settings.getExportFile());

            //Bson dateExpr = createDateExpression(settings.getFrom(), settings.getTo());
            MongoReader reader = new MongoReader(settings);

            MongoCgmsExport cgms = new MongoCgmsExport(writer);
            cgms.exportBgEntries(reader);
            writer.flush();

            MongoTreatmentsExport treatments = new MongoTreatmentsExport(writer);
            treatments.exportTreatments(reader);

            writer.close();
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

        ExportSettings settings = new ExportSettings(properties);
        return settings;
    }

    private Properties readProperties(String filepath) throws IOException {

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
