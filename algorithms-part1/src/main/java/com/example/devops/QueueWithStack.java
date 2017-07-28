package com.example.devops;

import java.util.Iterator;

public class QueueWithStack<Item> implements Iterable<Item> {
    private Item[] in;
    private Item[] out;
    private int size;

    public QueueWithStack() {
        in = (Item[]) new Object[1];
        out = (Item[]) new Object[1];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void push(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        if (size == in.length) {
            resize(in.length * 2);
        }

        in[size++] = item;
    }

    private Item pop() {

        if (size > 0 && size == in.length / 4) {
            resize(in.length / 2);
        }

        Item item = in[--size];
        in[size] = null;
        return item;
    }

    public void enqueue(Item item) {
        push(item);
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = in[i];
        }
        in = copy;
    }

    public Iterator<Item> iterator() {
        return null;
    }
}
