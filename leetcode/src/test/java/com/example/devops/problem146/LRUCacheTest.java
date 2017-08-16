package com.example.devops.problem146;

import org.junit.Test;

import static org.junit.Assert.*;

public class LRUCacheTest {
    @Test
    public void TestLRUCorrectness() {
        LRUCache lruCache = new LRUCache(2);
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        assertEquals(lruCache.get(1), 1);
        lruCache.put(3, 3);
        assertEquals(lruCache.get(2), -1);
        lruCache.put(4, 4);
        assertEquals(lruCache.get(1), -1);
        assertEquals(lruCache.get(3), 3);
        assertEquals(lruCache.get(4), 4);
        lruCache.put(4, 10);
        assertEquals(lruCache.get(4), 10);
        assertEquals(lruCache.get(3), 3);
    }
}