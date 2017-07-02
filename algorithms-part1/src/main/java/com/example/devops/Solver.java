package com.example.devops;

import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

public class Solver {
    private boolean isSolvable = false;
    private SearchNode target;
    private Integer moveCnt = -1;
    private final Stack<Board> boards = new Stack<Board>();

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<SearchNode> minPQInitial = new MinPQ<SearchNode>();
        MinPQ<SearchNode> minPQTwin = new MinPQ<SearchNode>();
        Board twinBoard = initial.twin();

        if (initial.isGoal()) {
            target = new SearchNode(initial);
            isSolvable = true;
            moveCnt = target.moves;
            return;
        }

        if (twinBoard.isGoal()) {
            isSolvable = false;
            return;
        }

        SearchNode node = new SearchNode(initial);
        SearchNode twinNode = new SearchNode(twinBoard);
        minPQInitial.insert(node);
        minPQTwin.insert(twinNode);

        while (true) {
            target = iterate(minPQInitial);

            if (target != null || iterate(minPQTwin) != null) {
                break;
            }
        }

        isSolvable = (target != null);

        if (isSolvable) {
            moveCnt = target.moves;
        }
    }

    public int moves() {
        return moveCnt;
    }

    public Iterable<Board> solution() {
        if (this.isSolvable()) {
            while (target != null) {
                boards.push(target.board);
                target = target.previous;
            }
        }

        if (!boards.isEmpty()) {
            return new Iterable<Board>() {
                public Iterator<Board> iterator() {
                    return new MovesIterator();
                }
            };
        } else {
            return null;
        }
    }

    private class MovesIterator implements Iterator<Board> {

        private Stack<Board> stack = (Stack<Board>) boards.clone();

        public boolean hasNext() {
            return !stack.isEmpty();
        }

        public Board next() {
            if (stack.isEmpty()) throw new NoSuchElementException();
            return stack.pop();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private SearchNode iterate(MinPQ<SearchNode> minPQ) {
        if (minPQ.isEmpty()) {
            return null;
        }

        SearchNode node = minPQ.delMin();

        if (node.board.isGoal()) {
            return node;
        }

        for (Board board : node.board.neighbors()) {
            if (node.previous == null || !board.equals(node.previous.board)) {
                minPQ.insert(new SearchNode(board, node));
            }
        }
        return null;
    }

    public boolean isSolvable() {
        return isSolvable;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private SearchNode previous;
        private int moves = 0;


        SearchNode(Board board, SearchNode previous) {
            this.board = board;
            this.previous = previous;
            this.moves = previous.moves + 1;
        }

        SearchNode(Board board) {
            this.board = board;
        }

        public int compareTo(SearchNode that) {
            if ((moves + board.manhattan()) < (that.moves + that.board.manhattan())) {
                return -1;
            }

            if ((moves + board.manhattan()) > (that.moves + that.board.manhattan())) {
                return 1;
            }
            return 0;
        }
    }
}
