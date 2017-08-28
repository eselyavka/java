package com.example.devops.week2;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int size;

    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public static void main(String[] args) {
        String[] strings = {"A", "B", "C", "D", "E", "F", "G", "H", "I"};
        Deque<String> deque = new Deque<String>();
        for (String str : strings) {
            if (StdRandom.uniform(strings.length) % 2 == 0) {
                deque.addFirst(str);
            } else {
                deque.addLast(str);
            }
        }
        for (String s : deque) {
            System.out.println(s);
        }
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (last == null) {
            last = first;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new NullPointerException();
        }

        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        size++;
    }

    public Item removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item item = first.item;
        first = first.next;
        if (isEmpty()) {
            last = null;
        }
        size--;
        return item;
    }

    public Item removeLast() {
        if (size == 0) {
            throw new NoSuchElementException();
        }

        Item item;

        if (first.next == null) {
            item = first.item;
            first = null;
            last = null;
        } else {
            Node penultimate = first;
            while (penultimate.next != last) {
                penultimate = penultimate.next;
            }
            item = last.item;
            last = penultimate;
            last.next = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Item item;
        private Node next;
    }
}
