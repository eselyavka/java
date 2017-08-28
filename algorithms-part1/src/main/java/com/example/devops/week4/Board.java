package com.example.devops.week4;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int[][] blocks;
    private int zeroI;
    private int zeroJ;


    public Board(int[][] blocks) {
        this.blocks = new int[blocks.length][blocks.length];

        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                this.blocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }
    }

    public int dimension() {
        return blocks.length;
    }

    public int hamming() {
        int hammingNum = 0;
        for (int i = 0; i < this.blocks.length; i++) {
            for (int j = 0; j < this.blocks.length; j++) {
                if (!blockInPlace(i, j, blocks[i][j]) && blocks[i][j] != 0) {
                    hammingNum++;
                }
            }
        }
        return hammingNum;
    }

    public int manhattan() {
        int manhattanNum = 0;
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[i][j] != 0) {
                    manhattanNum = manhattanNum + Math.abs(i - row(blocks[i][j])) + Math.abs(j - col(blocks[i][j]));
                }
            }
        }
        return manhattanNum;
    }

    private int row(int block) {
        return (block - 1) / blocks.length;
    }

    private int col(int block) {
        return (block - 1) % blocks.length;
    }

    private boolean blockInPlace(int row, int col, int block) {
        return block == row * dimension() + col + 1;
    }

    public boolean isGoal() {
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                if (blocks[i][j] != 0 && !blockInPlace(i, j, blocks[i][j])) {
                    return false;
                }
            }
        }
        return true;
    }

    private int[][] copy(int[][] arr) {
        int[][] arrCopy = new int[arr.length][arr.length];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                arrCopy[i][j] = arr[i][j];
            }
        }
        return arrCopy;
    }

    private int[][] swap(int i, int j, int m, int k) {
        int[][] copyBlocks = copy(blocks);
        int buf = copyBlocks[i][j];
        copyBlocks[i][j] = copyBlocks[m][k];
        copyBlocks[m][k] = buf;
        return copyBlocks;
    }

    public Board twin() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length - 1; j++) {
                if (blocks[i][j] != 0 && blocks[i][j + 1] != 0) {
                    return new Board(swap(i, j, i, j + 1));
                }
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        if (this.dimension() != other.dimension()) return false;
        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                int thisBlock = this.blocks[i][j];
                int otherBlock = other.blocks[i][j];
                if (thisBlock != otherBlock) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<Board>();

        if (zeroI > 0) neighbors.add(new Board(swap(zeroI, zeroJ, zeroI - 1, zeroJ)));
        if (zeroI < dimension() - 1) neighbors.add(new Board(swap(zeroI, zeroJ, zeroI + 1, zeroJ)));
        if (zeroJ > 0) neighbors.add(new Board(swap(zeroI, zeroJ, zeroI, zeroJ - 1)));
        if (zeroJ < dimension() - 1) neighbors.add(new Board(swap(zeroI, zeroJ, zeroI, zeroJ + 1)));
        return neighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dimension() + "\n");
        for (int i = 0; i < dimension(); i++) {
            for (int j = 0; j < dimension(); j++) {
                s.append(String.format("%2d ", blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
