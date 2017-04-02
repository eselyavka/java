package com.example.devops;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] queue;
    private int size;

    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
        size = 0;
    }

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("test6");
        rq.enqueue("test1");
        rq.enqueue("test2");
        rq.enqueue("test3");
        rq.enqueue("test4");
        rq.enqueue("test5");
        for (String s : rq) {
            System.out.println(s);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }


    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (size == queue.length) {
            extend(queue.length * 2);
        }
        queue[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        int random = StdRandom.uniform(size);
        Item result = queue[random];
        queue[random] = queue[--size];
        queue[size] = null;

        if (size > 0 && size == queue.length / 4) {
            int capacity = queue.length / 2;
            shrink(capacity);
        }
        return result;
    }

    public Item sample() {
        int random = StdRandom.uniform(size);
        Item result = queue[random];
        return result;
    }

    private void extend(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = queue[i];
        }
        queue = copy;
    }

    private void shrink(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int len = 0;
        int i = 0;
        while (len < queue.length) {
            if (queue[len] != null) {
                copy[i++] = queue[len];
            }
            len++;
        }
        queue = copy;
    }

    private class QueueIterator implements Iterator<Item> {
        private Item[] random = (Item[]) new Object[size];
        private int i = 0;

        public QueueIterator() {
            int k = 0;
            for (Item aQueue : queue) {
                if (aQueue != null) {
                    random[k++] = aQueue;
                }
            }
            shuffle(random);
        }

        private void shuffle(Item[] arr) {
            Item buf = null;
            int j = StdRandom.uniform(size);
            for (int k = 0; k < arr.length; k++) {
                buf = arr[j];
                arr[j] = arr[k];
                arr[k] = buf;
            }
        }

        public boolean hasNext() {
            return i < size;
        }

        public Item next() {
            if (i >= size) {
                throw new NoSuchElementException();
            }
            return random[i++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
