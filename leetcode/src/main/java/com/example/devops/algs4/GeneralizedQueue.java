package com.example.devops.algs4;

import java.util.TreeMap;

public class GeneralizedQueue<Item> {
    private TreeMap<Integer, Item> queue;
    private int n;
    private int head;

    public GeneralizedQueue() {
        queue = new TreeMap<Integer, Item>();
        n = 0;
        head = 0;
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public void append(Item item) {
        queue.put(n++, item);
    }

    public Item remove() {
        if (isEmpty()) return null;
        Item item = queue.remove(head++);
        return item;
    }

    public Item ithItem(int i) {
        return queue.get(i);
    }

    public Item removeIthItem(int i) {
        if (isEmpty()) return null;
        if (i == head) head++;
        return queue.remove(i);
    }

    public int size() {
        return queue.size();
    }
}
