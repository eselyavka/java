package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by eseliavka on 23.07.15.
 */

public class MultipleOutputMapper
        extends Mapper<LongWritable, Text, Text, LongWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String line = value.toString();
        String[] srv = line.split("\t");

        context.write(new Text(srv[0] + "\t" + srv[3]), new LongWritable(1L));
    }
}
