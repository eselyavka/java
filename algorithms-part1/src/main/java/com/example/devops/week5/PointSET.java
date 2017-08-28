package com.example.devops.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;
import java.util.List;

public class PointSET {
    private SET<Point2D> points;

    public PointSET() {
        this.points = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (!contains(p)) {
            points.add(p);
        }
    }

    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> list = new ArrayList<Point2D>();
        for (Point2D point : points) {
            if (point.x() >= rect.xmin() && point.x() <= rect.xmax()
                    && point.y() >= rect.ymin() && point.y() <= rect.ymax()) {
                list.add(point);
            }
        }
        return list;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double min = Double.MAX_VALUE;
        Point2D nearest = null;
        for (Point2D point : points) {
            if (point.equals(p)) continue;
            double distance = point.distanceSquaredTo(p);
            if (distance < min) {
                min = distance;
                nearest = point;
            }
        }
        return nearest;
    }
}
