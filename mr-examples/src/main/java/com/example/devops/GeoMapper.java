package com.example.devops;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by eseliavka on 23.07.15.
 */

public class GeoMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        String fileName = ((FileSplit) context.getInputSplit()).getPath().getName();
        context.write(new Text(URLDecoder.decode(fields[0])), one);
    }
}
