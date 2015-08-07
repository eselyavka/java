package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by eseliavka on 06.08.15.
 */
public class CountryMapper
        extends Mapper<LongWritable, Text, DBOutputWritable, NullWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");
        context.write(new DBOutputWritable(fields[1],Double.parseDouble(fields[4]),Double.parseDouble(fields[5]),fields[8]), NullWritable.get());
    }
}
