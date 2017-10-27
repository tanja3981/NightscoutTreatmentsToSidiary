package de.tasc.ns.AsciiExport.exceptions;

public class UnhandledExportException extends Exception {

    public UnhandledExportException(Exception e) {
        super(e);
    }


    public UnhandledExportException(String s) {
        super(s);
    }
}
