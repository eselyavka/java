package com.example.devops;

import java.util.*;

/**
 * Created by eseliavka on 5/5/16.
 * example of the BloomFilter
 * n - number elements in the set
 * p - probability on element in n between 0 and 1
 * m - number of bits in filter
 * k - number of hash functions
 */

public class MyBloomFilter {

    private Integer n;
    private Float p;
    private Integer m;
    private Integer k;
    private Set<String> dataSet;
    private BitSet bs;
    public static Integer i = 1;

    public MyBloomFilter(int n, float p) {
        this.n = n;
        this.p = p;
        this.m = genM();
        this.k = genK();
        this.dataSet = new HashSet<String>();
        this.bs = new BitSet(m);
    }

    public int genM() {
        return (int) Math.round(n * Math.log(p) / Math.log(1.0 / (Math.pow(2.0, Math.log(2.0)))));
    }

    public int genK() {
        return (int) Math.round(Math.log(2.0) * m / n);
    }

    public Integer calculateHash(String element) {
        String hash = String.valueOf(element.hashCode());
        while (i < k - 1) {
            i++;
            calculateHash(hash);
        }
        return Math.abs(hash.hashCode() % m);
    }

    public void add(String element) {
        dataSet.add(element);
        Integer bi = this.calculateHash(element);
        bs.set(bi);
    }

    public boolean inSet(String element) {
        Integer bi = this.calculateHash(element);
        return bs.get(bi);
    }

    public static void main(String[] args) throws InterruptedException {
        MyBloomFilter mbf = new MyBloomFilter(100, 0.1F);
        mbf.add("test1");
        mbf.add("test2");
        mbf.add("test3");
        System.out.println(mbf.inSet("test"));
    }
}
