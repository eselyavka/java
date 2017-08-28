package com.example.devops.week5;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

public class Week5Checker {

    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);

        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
        KdTree kdTree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            brute.insert(p);
            kdTree.insert(p);
        }

        Point2D a = new Point2D(0.909359, 0.676973);
        Point2D b = new Point2D(0.1, 0.918481);
        StdOut.println(kdTree.isEmpty());
        StdOut.println(brute.isEmpty());
        StdOut.println(kdTree.size());
        StdOut.println(brute.size());
        StdOut.println(kdTree.contains(a));
        StdOut.println(brute.contains(a));
        StdOut.println(kdTree.contains(b));
        StdOut.println(brute.contains(b));
        RectHV rect = new RectHV(0.0, 0.0, 0.5, 0.5);
        StdOut.println(kdTree.range(rect));
        StdOut.println(brute.range(rect));
        StdOut.println(kdTree.nearest(new Point2D(0.81, 0.30)));
        StdOut.println(brute.nearest(new Point2D(0.81, 0.30)));
        StdOut.println(kdTree.contains(kdTree.nearest(new Point2D(0.81, 0.30))));
        StdOut.println(brute.contains(brute.nearest(new Point2D(0.81, 0.30))));
    }
}
