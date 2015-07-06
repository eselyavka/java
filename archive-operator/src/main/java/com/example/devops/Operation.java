package com.example.devops;

/**
 * Created by eseliavka on 03.07.15.
 */
public enum Operation {
    TOSEQ, FROMSEQ;

    public static Operation toOperation(String operation) throws Exception {
        if (operation == null) {
            throw new NullPointerException(" :: Parameter 'operation' was null inside method toOperation.");
        } else {
            return Operation.valueOf(operation.replace("--", "").toUpperCase());
        }
    }
}
