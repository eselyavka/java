package com.example.devops.week1;

import edu.princeton.cs.algs4.StdOut;

import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Optional;

public class Test {
    public static void main(String[] args){
        int count = 0;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("test1");
        arrayList.add("test2");
        arrayList.add("test2");
        StdOut.println(arrayList.stream().filter(symbol -> symbol.charAt(4) == '1').count());
//        StdOut.println(count);
    }
}
