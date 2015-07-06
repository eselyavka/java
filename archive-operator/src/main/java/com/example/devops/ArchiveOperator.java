package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by eseliavka on 03.07.15.
 */
public class ArchiveOperator {
    private final Configuration conf;
    private final FileSystem fs;
    private final Options op;
    private final Path seqFile;
    private final Path opDir;

    public ArchiveOperator(Configuration conf, FileSystem fs, Options options) {
        this.conf = conf;
        this.fs = fs;
        this.op = options;
        this.seqFile = new Path(op.getSeqFile());
        this.opDir = new Path(op.getDestDir());
    }

    public void archToSeq() throws IOException {
        String pattern = conf.get("file.pattern", ".*\\.gz");
        String message;

        if (!fs.isDirectory(opDir)) {
            message = "Directory " + fs.getUri() + "/" + opDir + " doesn't exists";
            throw new RuntimeException(message);
        }

        FileStatus[] status = fs.listStatus(opDir, new RegexFilter(pattern));
        Path[] listedPath = FileUtil.stat2Paths(status);

        if (listedPath == null) {
            message = "No files found in " + fs.getUri() + "/" + opDir + ", by pattern '" + pattern + "' exiting...";
            throw new RuntimeException(message);
        }

        if (fs.isFile(seqFile)) {
            message = "File " + fs.getUri() + "/" + seqFile + " exists, please delete file";
            throw new RuntimeException(message);
        }

        Text key = new Text();
        BytesWritable value = new BytesWritable();

        SequenceFile.Writer writer = null;

        InputStream in = null;

        try {
            writer = SequenceFile.createWriter(fs, conf, seqFile, key.getClass(), value.getClass());
            for (Path p : listedPath) {
                key = new Text(p.getName());
                try {
                    in = fs.open(p);
                    byte[] data = new byte[in.available()];
                    IOUtils.readFully(in, data, 0, in.available());
                    value = new BytesWritable(data);
                } finally {
                    IOUtils.closeStream(in);
                }
                writer.append(key, value);
            }
        } finally {
            IOUtils.closeStream(writer);
        }
    }

    public void SeqToArch() throws IOException {
        String message;

        if (!fs.isFile(seqFile)) {
            message = "File " + fs.getUri() + "/" + seqFile + " doesn't exists";
            throw new RuntimeException(message);
        }

        if (fs.listStatus(opDir).length != 0) {
            message = "Directory " + fs.getUri() + "/" + opDir + " is not empty";
            throw new RuntimeException(message);
        }

        Text k = new Text();
        BytesWritable v = new BytesWritable();

        SequenceFile.Reader reader = null;

        try {
            reader = new SequenceFile.Reader(fs, seqFile, conf);
            while (reader.next(k, v)) {
                FSDataOutputStream out = fs.create(new Path(opDir + "/" + k));
                IOUtils.copyBytes(new ByteArrayInputStream(v.getBytes()), out, v.getLength(), false);
            }
        } finally {
            IOUtils.closeStream(reader);
        }

    }
}
