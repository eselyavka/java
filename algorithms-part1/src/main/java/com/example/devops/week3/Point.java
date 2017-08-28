package com.example.devops.week3;

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {
    private final Comparator<Point> slopeorder = new OrderBySlope();
    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Comparator<Point> slopeOrder() {
        return slopeorder;
    }

    private int getX() {
        return x;
    }

    private int getY() {
        return y;
    }

    public double slopeTo(Point that) {
        if (compareTo(that) == 0) {
            return Double.NEGATIVE_INFINITY;
        }

        if (that.x == x) {
            return Double.POSITIVE_INFINITY;
        }

        double res = (double) (that.y - y) / (double) (that.x - x);

        return Double.compare(res, -0d) == 0 ? Math.abs(res) : res;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int compareTo(Point other) {
        if (y < other.getY()) {
            return -1;
        } else if (y == other.getY() && x < other.getX()) {
            return -1;
        } else if (x == other.getX() && y == other.getY()) {
            return 0;
        } else {
            return 1;
        }
    }

    private class OrderBySlope implements Comparator<Point> {

        public int compare(Point o1, Point o2) {
            return Double.compare(slopeTo(o1), slopeTo(o2));
        }
    }
}
