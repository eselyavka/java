package com.example.devops.week1;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SAP {
    private static final int MAX_VALUE = Integer.MAX_VALUE;
    private Digraph G;
    private Map<String, Integer> ancestorCache;
    private Map<String, Integer> lengthCache;

    public SAP(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("null input");
        }

        this.G = new Digraph(G);
        this.ancestorCache = new HashMap<String, Integer>();
        this.lengthCache = new HashMap<String, Integer>();
    }

    public int length(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        String key = Integer.toString(v) + "_" + Integer.toString(w);

        Integer len = lengthCache.get(key);

        if (null == len) {
            ancestor(v, w);
            return lengthCache.get(key);
        }

        return len;
    }

    public int ancestor(int v, int w) {
        validateVertex(v);
        validateVertex(w);

        String key = Integer.toString(v) + "_" + Integer.toString(w);
        Integer anc = ancestorCache.get(key);

        if (null != anc) return anc;

        anc = -1;

        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(G, w);

        int min = MAX_VALUE;

        for (int i = 0; i < G.V(); i++) {
            if (bv.hasPathTo(i) && bw.hasPathTo(i)) {
                int dist = (bv.distTo(i) + bw.distTo(i));
                if (min > dist) {
                    min = dist;
                    anc = i;
                }
            }
        }

        min = (min == MAX_VALUE) ? -1 : min;

        ancestorCache.put(key, anc);
        lengthCache.put(key, min);

        return anc;
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (null == v || null == w) {
            throw new IllegalArgumentException("null input");
        }

        validateVertex(v);
        validateVertex(w);

        String key = v.hashCode() + "_" + w.hashCode();
        Integer anc = ancestorCache.get(key);

        if (null != anc) return anc;

        anc = -1;

        BreadthFirstDirectedPaths bv = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bw = new BreadthFirstDirectedPaths(G, w);

        int min = MAX_VALUE;

        for (int i = 0; i < G.V(); i++) {
            if (bv.hasPathTo(i) && bw.hasPathTo(i)) {
                int dist = (bv.distTo(i) + bw.distTo(i));
                if (min > dist) {
                    min = dist;
                    anc = i;
                }
            }
        }

        min = (min == MAX_VALUE) ? -1 : min;

        ancestorCache.put(key, anc);
        lengthCache.put(key, min);

        return anc;
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (null == v || null == w) {
            throw new IllegalArgumentException("null input");
        }

        validateVertex(v);
        validateVertex(w);

        String key = v.hashCode() + "_" + w.hashCode();

        Integer len = lengthCache.get(key);

        if (null == len) {
            ancestor(v, w);
            return lengthCache.get(key);
        }

        return len;
    }


    private void validateVertex(int v) {
        int V = G.V();
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
    }

    private void validateVertex(Iterable<Integer> v) {
        for (int element : v) {
            validateVertex(element);
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 3;
        int w = 11;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        List<Integer> vi = Arrays.asList(3, 7);
        List<Integer> wi = Arrays.asList(11, 12);
        int ancestor2 = sap.ancestor(vi, wi);
        int length2 = sap.length(vi, wi);
        List<Integer> vi1 = Arrays.asList(9, 7);
        List<Integer> wi1 = Arrays.asList(12, 2);
        int ancestor3 = sap.ancestor(vi1, wi1);
        int length3 = sap.ancestor(vi1, wi1);
        StdOut.printf("length2 = %d, ancestor2 = %d, length3 = %d, ancestor3 = %d",
                length2, ancestor2,
                length3, ancestor3);
    }
}
