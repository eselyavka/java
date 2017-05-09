package com.example.devops;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FastCollinearPoints {
    private final Point[] points;
    private List<LineSegment> lineSegments = new ArrayList<LineSegment>();
    private Map<Double, List<List<Point>>> slopWithPoints = new HashMap<Double, List<List<Point>>>();

    public FastCollinearPoints(Point[] points) {
        this.points = points.clone();
        if (this.points == null) {
            throw new NullPointerException("argument is null");
        }

        Arrays.sort(this.points);

        for (int i = 0; i < this.points.length; i++) {
            if (this.points[i] == null) {
                throw new NullPointerException("argument is null");
            } else if (i == this.points.length - 1) {
                break;
            } else if (this.points[i].compareTo(this.points[i + 1]) == 0) {
                throw new IllegalArgumentException("repeated points");
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    private void addSegment(List<Point> dots, double slop) {
        List<Point> copyDots = new ArrayList<Point>(dots);
        Collections.sort(copyDots);
        if (slopWithPoints.containsKey(slop)) {
            List<List<Point>> res = slopWithPoints.get(slop);
            for (List<Point> p : res) {
                List<Point> buf = new ArrayList<Point>(p);
                buf.removeAll(copyDots);
                if (buf.isEmpty()) {
                    return;
                }
            }
            slopWithPoints.get(slop).add(copyDots);
            addSegment(copyDots.get(0), copyDots.get(copyDots.size() - 1));
        } else {
            List<List<Point>> listOfPoints = new ArrayList<List<Point>>();
            listOfPoints.add(copyDots);
            slopWithPoints.put(slop, listOfPoints);
            addSegment(copyDots.get(0), copyDots.get(copyDots.size() - 1));
        }
    }

    private void addSegment(Point p, Point q) {
        lineSegments.add(new LineSegment(p, q));
    }

    public LineSegment[] segments() {
        Point[] localCopy = points.clone();
        LineSegment[] lineSegmentArray = new LineSegment[lineSegments.size()];
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(localCopy, points[i].slopeOrder());
            Point p = points[i];
            double slope;
            double prevSlope = p.slopeTo(p);
            List<Point> dots = new ArrayList<Point>();
            for (int j = 1; j < points.length; j++) {
                slope = p.slopeTo(localCopy[j]);
                if (Double.compare(slope, prevSlope) == 0) {
                    dots.add(localCopy[j]);
                } else {
                    if (dots.size() >= 3) {
                        dots.add(p);
                        addSegment(dots, prevSlope);
                    }
                    dots.clear();
                    dots.add(localCopy[j]);
                }
                prevSlope = slope;
            }
            if (dots.size() >= 3) {
                dots.add(p);
                addSegment(dots, prevSlope);
            }
        }
        return lineSegments.toArray(lineSegmentArray);
    }
}
