package com.example.devops;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.MapTask;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.StringTokenizer;

/**
 * Created by eseliavka on 28.09.16.
 */

public class BinaryMapper
        extends Mapper<LongWritable, Text, BytesWritable, NullWritable> {

    public byte[] NumToBytes(Object x) {
        ByteBuffer buffer = null;
        if (x instanceof Integer) {
            buffer = ByteBuffer.allocate(Integer.SIZE / 8);
            buffer.putInt((Integer) x);
        } else {
            buffer = ByteBuffer.allocate(Long.SIZE / 8);
            buffer.putLong((Long) x);
        }
        return buffer.array();
    }


    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String[] line = value.toString().split("\t");
        ByteBuffer row = ByteBuffer.allocate(38);
        row.order(ByteOrder.LITTLE_ENDIAN);
        row.putInt(Integer.parseInt(line[0]));
        row.putInt(Integer.parseInt(line[1]));
        row.putShort(Short.parseShort(line[2]));
        row.put(Byte.parseByte(line[3]));
        row.putShort((short) line[4].length());
        row.put(line[4].getBytes());
        row.putInt(Integer.parseInt(line[5]));
        row.putLong(Long.parseLong(line[6]));
        row.putLong(Long.parseLong(line[7]));
        context.write(new BytesWritable(row.array()), NullWritable.get());
    }
}
