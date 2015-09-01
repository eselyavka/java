package com.example.threadExample;

/**
 * Created by eseliavka on 27.08.15.
 */
public class Counter {
    public long count = 0;

    Counter(long count) {
        this.count = count;
    }

    public synchronized void decrement(long value) {
        this.count -= value;
    }
}
