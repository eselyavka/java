package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by eseliavka on 28.09.16.
 */

public class BinaryOutputFormat extends FileOutputFormat<BytesWritable, NullWritable> {

    @Override
    public RecordWriter<BytesWritable, NullWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        Configuration conf = taskAttemptContext.getConfiguration();
        boolean isCompressed = getCompressOutput(taskAttemptContext);
        CompressionCodec codec = null;
        String extension = "";
        if (isCompressed) {
            Class<? extends CompressionCodec> codecClass = getOutputCompressorClass(taskAttemptContext, GzipCodec.class);
            codec = ReflectionUtils.newInstance(codecClass, conf);
            extension = codec.getDefaultExtension();
        }
        Path file = getDefaultWorkFile(taskAttemptContext, extension);
        FileSystem fs = file.getFileSystem(conf);
        if (!isCompressed) {
            FSDataOutputStream fileOut = fs.create(file, false);
            return new ByteRecordWriter(fileOut);
        } else {
            FSDataOutputStream fileOut = fs.create(file, false);
            return new ByteRecordWriter(new DataOutputStream(codec.createOutputStream(fileOut)));
        }
    }

    protected static class ByteRecordWriter extends RecordWriter<BytesWritable, NullWritable> {
        private DataOutputStream out;

        public ByteRecordWriter(DataOutputStream out) {
            this.out = out;
        }

        @Override
        public void write(BytesWritable key, NullWritable value) throws IOException {
                out.write(key.getBytes(), 0, key.getLength());
        }

        @Override
        public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
            out.close();
        }
    }
}