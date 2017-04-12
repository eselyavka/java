package com.example.devops;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        while (StdIn.hasNextChar()) {
            Deque<String> deque = new Deque<String>();
            String[] lines = StdIn.readAllStrings();
            for (String line : lines) {
                if (StdRandom.uniform(lines.length) % 2 == 0) {
                    deque.addFirst(line);
                } else {
                    deque.addLast(line);
                }
            }
            for (int i = 0; i < k; i++) {
                StdOut.println(deque.removeFirst());
            }
        }
    }
}
