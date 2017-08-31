package com.example.devops.algs4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FourSum {
    private int n;
    private int[] arr;

    public FourSum(int n, int[] arr) {
        this.n = n;
        this.arr = new int[n];
        for (int i = 0; i < n; i++) {
            this.arr[i] = arr[i];
        }
    }

    public List<String> getFourSum() {
        HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
        List<String> res = new ArrayList<String>();

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int sum = this.arr[i] + this.arr[j];
                int sumHashCode = new Integer(sum).hashCode();
                String payload = "[" + i + "][" + j + "]";
                if (hashMap.containsKey(sumHashCode)) {
                    String key = hashMap.get(sumHashCode);
                    if (!(key.contains(Integer.toString(i)) || key.contains(Integer.toString(j)))) {
                        res.add(hashMap.get(sumHashCode) + " = " + payload);
                    }
                } else {
                    hashMap.put(sum, payload);
                }
            }
        }
        return res;
    }
}
