package com.example.devops.problem146;

import java.util.Iterator;
import java.util.LinkedHashMap;

public class LRUCache {
    private LinkedHashMap<Integer, Integer> linkedHashMap;
    private int capacity;

    public LRUCache(int capacity) {
        this.linkedHashMap = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true);
        this.capacity = capacity;
    }

    public void put(int key, int value) {
        if (linkedHashMap.containsKey(key)) {
            linkedHashMap.remove(key);
        }

        if (linkedHashMap.size() >= capacity) {
            linkedHashMap.remove(lru());
            linkedHashMap.put(key, value);
        } else {
            linkedHashMap.put(key, value);
        }
    }

    public int get(int key) {
        if (linkedHashMap.get(key) == null) {
            return -1;
        }
        return linkedHashMap.get(key);
    }

    private int lru() {
        Iterator<Integer> iterator = linkedHashMap.keySet().iterator();
        int key = -1;
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return key;
    }
}
