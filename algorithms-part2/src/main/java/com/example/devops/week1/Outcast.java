package com.example.devops.week1;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private static final int MIN_VALUE = Integer.MIN_VALUE;
    private WordNet wordNet;

    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    public String outcast(String[] nouns) {
        int max = MIN_VALUE;
        String outcast = null;
        for (String nounI : nouns) {
            int d = 0;
            for (String nounJ : nouns) {
                d += wordNet.distance(nounI, nounJ);
            }

            if (d > max) {
                max = d;
                outcast = nounI;
            }
        }
        return outcast;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
