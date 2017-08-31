package com.example.devops.algs4;

import edu.princeton.cs.algs4.In;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class FourSumTest {
    @Test
    public void getFourSum() throws Exception {
        String filename = getClass().getResource("arr.txt").getPath();
        In in = new In(filename);
        int n = in.readInt();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = in.readInt();
        }
        FourSum fourSum = new FourSum(n, arr);
        String[] expected = {"[0][3] = [1][2]",
                "[0][4] = [1][3]",
                "[0][2] = [1][5]",
                "[0][7] = [1][6]",
                "[0][8] = [1][7]",
                "[1][4] = [2][3]",
                "[0][6] = [2][4]",
                "[0][3] = [2][5]",
                "[0][8] = [2][6]",
                "[1][8] = [2][7]",
                "[0][7] = [3][4]",
                "[0][4] = [3][5]",
                "[1][8] = [3][6]",
                "[2][8] = [3][7]",
                "[2][8] = [4][6]",
                "[3][8] = [4][7]",
                "[0][7] = [5][6]",
                "[0][8] = [5][7]"};
        List<String> actual = fourSum.getFourSum();
        assertEquals(Arrays.asList(expected), actual);
    }
}