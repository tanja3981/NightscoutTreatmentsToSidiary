package de.tasc.ns.AsciiExport;

import java.io.File;
import java.util.Date;

public class ExportSettings {
    private Date from;
    private Date to;
    private File exportFile;

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

    public File getExportFile() {
        return exportFile;
    }

    public void setExportFile(File exportFile) {
        this.exportFile = exportFile;
    }
}
