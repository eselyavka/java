package com.example.devops;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by eseliavka on 24.07.15.
 */
public class BinaryReducer
        extends Reducer<Text, IntWritable, BytesWritable, BytesWritable> {

    public byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.SIZE/8);
        buffer.putLong(x);
        return buffer.array();
    }

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {

        context.write(new BytesWritable(key.getBytes()), null);
    }
}