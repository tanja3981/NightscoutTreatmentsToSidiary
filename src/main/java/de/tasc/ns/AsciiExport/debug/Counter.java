package de.tasc.ns.AsciiExport.debug;

public class Counter {
    private int counter;

    public void plus() {
        counter++;
    }

    @Override
    public String toString() {
        return String.valueOf(counter);
    }
}
