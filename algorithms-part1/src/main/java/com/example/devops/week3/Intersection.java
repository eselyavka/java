package com.example.devops.week3;

import edu.princeton.cs.algs4.StdRandom;

public class Intersection {

    private int N = 10;
    private Point[] a = new Point[N];
    private Point[] b = new Point[N];

    public Intersection() {

        for (int i = 0; i < N - 2; i++) {
            Point point = new Point(StdRandom.uniform(N), StdRandom.uniform(N));
            a[i] = point;
            point = new Point(StdRandom.uniform(N), StdRandom.uniform(N));
            b[i] = point;
        }

        a[8] = new Point(1, 1);
        b[8] = new Point(1, 1);

        a[9] = new Point(4, 2);
        b[9] = new Point(4, 2);

        insertionSort(a);
        insertionSort(b);
        assert isSorted(a) && isSorted(b);
    }

    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        assert intersection.intersection() >= 2;

    }

    private boolean isSorted(Point[] arr) {
        for (int i = 1; i < N; i++) {
            if (less(arr[i], arr[i - 1])) {
                return false;
            }
        }
        return true;
    }

    public int getN() {
        return N;
    }

    public void setN(int n) {
        N = n;
    }

    public Point[] getA() {
        return a;
    }

    public void setA(Point[] a) {
        this.a = a;
    }

    public Point[] getB() {
        return b;
    }

    public void setB(Point[] b) {
        this.b = b;
    }

    private int intersection() {
        int count = 0;
        for (int i = 0; i < N; i++) {
            if (binarySearch(b[i]) != null) {
                count++;
            }
        }
        return count;
    }

    private void insertionSort(Point[] arr) {
        for (int i = 0; i < N; i++) {
            for (int j = i; j > 0; j--) {
                if (less(arr[j], arr[j - 1])) {
                    exch(arr, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }

    private Point binarySearch(Point target) {
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid].compareTo(target) == 0) {
                return target;
            } else if (a[mid].compareTo(target) < 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return null;
    }

    private boolean less(Point left, Point right) {
        return left.compareTo(right) < 0;
    }

    private void exch(Comparable[] arr, int i, int j) {
        Point buf = (Point) arr[i];
        arr[i] = arr[j];
        arr[j] = buf;
    }

    private class Point implements Comparable<Point> {
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int compareTo(Point other) {
            if (x < other.getX()) {
                return -1;
            }

            if (x == other.getX() && y < other.getY()) {
                return -1;
            }

            if (x == other.getX() && y == other.getY()) {
                return 0;
            }
            return 1;
        }
    }
}