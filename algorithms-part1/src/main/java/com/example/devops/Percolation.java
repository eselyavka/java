package com.example.devops;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private int sumOpen;
    private int[][] matrix;
    private int[][] state;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private int topValue;
    private int bottomValue;
    private int n;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n should be greater 0");
        }
        this.n = n;
        this.sumOpen = 0;
        int value = 0;
        matrix = new int[n + 1][n + 1];
        state = new int[n + 1][n + 1];
        weightedQuickUnionUF = new WeightedQuickUnionUF((n * n) + 2);
        topValue = n * n + 1;
        bottomValue = n * n;
        for (int i = 1; i <= this.n; i++) {
            for (int j = 1; j <= this.n; j++) {
                matrix[i][j] = value;
                value++;
            }
        }
    }

    private void checkRange(int row, int col) {
        if (row <= 0 && row > n) {
            throw new IndexOutOfBoundsException("index error row");
        }
        if (col <= 0 && col > n) {
            throw new IndexOutOfBoundsException("index error column");
        }
    }

    public void open(int row, int col) {
        checkRange(row, col);

        int upper = (row == 1) ? row : row - 1;
        int bottom = (row == n) ? row : row + 1;
        int left = (col == 1) ? col : col - 1;
        int right = (col == n) ? col : col + 1;

        if (!isOpen(row, col)) {
            state[row][col] = 1;
            sumOpen++;
            if (row == 1) {
                weightedQuickUnionUF.union(matrix[row][col], topValue);
            } else if (row == n) {
                weightedQuickUnionUF.union(matrix[row][col], bottomValue);
            }
            if (isOpen(upper, col)) {
                weightedQuickUnionUF.union(matrix[upper][col], matrix[row][col]);
            }
            if (isOpen(bottom, col)) {
                weightedQuickUnionUF.union(matrix[bottom][col], matrix[row][col]);
            }
            if (isOpen(row, left)) {
                weightedQuickUnionUF.union(matrix[row][left], matrix[row][col]);
            }
            if (isOpen(row, right)) {
                weightedQuickUnionUF.union(matrix[row][right], matrix[row][col]);
            }
        }
    }

    public boolean isOpen(int row, int col) {
        checkRange(row, col);
        return state[row][col] == 1;
    }

    public boolean isFull(int row, int col) {
        checkRange(row, col);
        return weightedQuickUnionUF.connected(matrix[row][col], topValue);
    }

    public int numberOfOpenSites() {
        return sumOpen;
    }

    public boolean percolates() {
        return weightedQuickUnionUF.connected(topValue, bottomValue);
    }
}
