package com.example.devops;

/**
 * Created by eseliavka on 02.07.15.
 */
public class Options {
    private final String operation;
    private final String seqFile;
    private final String destDir;

    public Options(String operation, String seqFile, String destDir) {
        this.seqFile = seqFile;
        this.destDir = destDir;
        this.operation = operation;
    }

    public String getSeqFile() {
        return seqFile;
    }

    public String getDestDir() {
        return destDir;
    }

    public String getOperation() {
        return operation;
    }
}
