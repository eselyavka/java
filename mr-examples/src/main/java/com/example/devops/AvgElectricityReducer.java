package com.example.devops;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by eseliavka on 24.07.15.
 */
public class AvgElectricityReducer
        extends Reducer<IntWritable, IntWritable, IntWritable, LongWritable> {
    @Override
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context)
            throws IOException, InterruptedException {
        long sumYear = 0;
        for (IntWritable value : values) {
            sumYear += value.get();
        }
        context.write(key, new LongWritable(sumYear));
    }
}
