package com.example.devops.week5;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static final double XMAX = 1.0;
    private static final double YMAX = 1.0;
    private static final double XMIN = 0.0;
    private static final double YMIN = 0.0;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private int size;
        private int cd;

        public Node(Point2D p, int size, double xmin, double ymin, double xmax, double ymax, int cd) {
            this.p = p;
            this.size = size;
            this.cd = cd;
            this.rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }

    private Node root;

    public KdTree() {
    }

    public boolean isEmpty() {
        return root == null;
    }

    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }

    public int size() {
        return size(root);
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p, XMIN, YMIN, XMAX, YMAX, 0);
    }

    private int compare(Point2D a, Point2D b, int cd) {
        int cmp;
        if (cd % 2 == 0) {
            cmp = new Double(a.x()).compareTo(b.x());
            if (cmp == 0) {
                cmp = new Double(a.y()).compareTo(b.y());
            }
        } else {
            cmp = new Double(a.y()).compareTo(b.y());
            if (cmp == 0) {
                cmp = new Double(a.x()).compareTo(b.x());
            }

        }
        return cmp;
    }

    private Node insert(Node node, Point2D p, double xmin, double ymin, double xmax, double ymax, int cd) {
        if (node == null) return new Node(p, 1, xmin, ymin, xmax, ymax, cd);

        int cmp = compare(p, node.p, cd);

        if (cmp < 0) {
            if (cd % 2 == 0) {
                node.lb = insert(node.lb, p, xmin, ymin, node.p.x(), ymax, cd + 1);
            } else {
                node.lb = insert(node.lb, p, xmin, ymin, xmax, node.p.y(), cd + 1);
            }
        } else if (cmp > 0) {
            if (cd % 2 == 0) {
                node.rt = insert(node.rt, p, node.p.x(), ymin, xmax, ymax, cd + 1);
            } else {
                node.rt = insert(node.rt, p, xmin, node.p.y(), xmax, ymax, cd + 1);
            }
        } else node.p = p;

        node.size = 1 + size(node.lb) + size(node.rt);

        return node;
    }

    private Point2D get(Node x, Point2D p) {
        while (x != null) {
            int cmp = compare(p, x.p, x.cd);
            if (cmp < 0) x = x.lb;
            else if (cmp > 0) x = x.rt;
            else return x.p;
        }
        return null;
    }

    private Point2D get(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p);
    }

    public boolean contains(Point2D p) {
        return get(p) != null;
    }

    public void draw() {
        drawing(root, 0);
    }

    private void drawing(Node node, int cd) {
        if (node == null) return;
        drawing(node.lb, cd + 1);

        StdDraw.setPenRadius();
        if (cd % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }

        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);

        node.p.draw();

        drawing(node.rt, cd + 1);
    }

    private void traverse(RectHV rect, Node node, List<Point2D> list) {
        if (node == null) return;
        if (node.rect.intersects(rect)) {
            if (rect.contains(node.p)) list.add(node.p);
        } else {
            return;
        }
        traverse(rect, node.lb, list);
        traverse(rect, node.rt, list);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> list = new ArrayList<Point2D>();
        traverse(rect, root, list);
        return list;
    }

    private int height() {
        return height(root);
    }

    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.lb), height(x.rt));
    }

    private Point2D nearest(Point2D p, Node node, int cd, double smallestDistance, Node previous) {
        if (node == null) return previous.p;
        double distance = node.rect.distanceSquaredTo(p);
        if (smallestDistance < distance) {
            return previous.p;
        } else {
            int cmp;
            cmp = compare(p, node.p, cd);
            if (cmp < 0) {
                return nearest(p, node.lb, cd + 1, distance, node);
            } else {
                return nearest(p, node.rt, cd + 1, distance, node);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Point2D nearest = nearest(p, root, 0, Double.MAX_VALUE, root);
        return nearest;
    }
}
