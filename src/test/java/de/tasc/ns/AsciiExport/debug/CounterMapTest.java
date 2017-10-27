package de.tasc.ns.AsciiExport.debug;

import org.junit.Test;

import static org.junit.Assert.*;

public class CounterMapTest {
    @Test
    public void test() throws Exception {

        CounterMap.get().add("test1");


        String result = CounterMap.get().toString();
        assertNotNull(result);
        assertTrue(result.contains("test1\t1"));
    }


}