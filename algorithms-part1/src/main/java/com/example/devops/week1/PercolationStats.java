package com.example.devops.week1;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] results;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n/trials should be greater 0");
        }
        results = new double[trials];
        Percolation percolation;
        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(n + 1);
                int col = StdRandom.uniform(n + 1);
                row = (row == 0) ? row + 1 : row;
                col = (col == 0) ? col + 1 : col;
                percolation.open(row, col);
            }
            results[i] = (double) percolation.numberOfOpenSites() / Math.pow(n, 2.0);
        }
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, t);
        StdOut.printf("mean\t= %f\n", percolationStats.mean());
        StdOut.printf("stddev\t= %f\n", percolationStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n",
                percolationStats.confidenceLo(),
                percolationStats.confidenceHi());
    }

    public double mean() {
        return StdStats.mean(results);
    }

    public double stddev() {
        return StdStats.stddev(results);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(results.length);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(results.length);
    }
}
