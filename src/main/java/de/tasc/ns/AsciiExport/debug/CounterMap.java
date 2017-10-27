package de.tasc.ns.AsciiExport.debug;

import de.tasc.ns.AsciiExport.AsciiWriter;
import sun.security.jca.GetInstance;

import java.util.HashMap;
import java.util.Map;

public class CounterMap {

    private Map<String, Counter> map = new HashMap<>();

    private CounterMap() {
    }

    private static CounterMap instance;

    public static CounterMap get() {
        if (instance == null) {
            instance = new CounterMap();
        }
        return instance;
    }

    public void add(String counter) {
        if (!map.containsKey(counter)) {
            map.put(counter, new Counter());
        }
        map.get(counter).plus();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== Counter: ===");
        builder.append(AsciiWriter.NEWLINE);
        for (String key : map.keySet()) {
            builder.append(key);
            builder.append("\t");
            builder.append(map.get(key));
            builder.append(AsciiWriter.NEWLINE);
        }
        builder.append("=== === ===");
        builder.append(AsciiWriter.NEWLINE);
        return builder.toString();
    }
}
