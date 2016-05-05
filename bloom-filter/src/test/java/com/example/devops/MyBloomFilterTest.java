package com.example.devops;

import junit.framework.*;

/**
 * Created by eseliavka on 5/5/16.
 */
public class MyBloomFilterTest extends TestCase {
    private MyBloomFilter mbf;

    public void setUp() {
        mbf = new MyBloomFilter(100, 0.1F);
        mbf.add("test1");
        mbf.add("test2");
        mbf.add("test3");
    }

    public void testItemNotExists(){
        assertFalse(mbf.inSet("test"));
    }

    public void testItemExists(){
        assertTrue(mbf.inSet("test1"));
    }
}