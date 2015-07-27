package com.example.devops;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by eseliavka on 23.07.15.
 */

public class AvgElectricityMapper
        extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] strings = value.toString().split(" ");
        Boolean isYear = false;
        Integer year = 0;
        Integer sumMonth = 0;
        for (int i = 0; i < strings.length; i++) {
            try {
                if (!isYear) {
                    year = Integer.parseInt(strings[i]);
                    isYear = true;
                } else {
                    sumMonth += Integer.parseInt(strings[i]);
                }
            } catch (NumberFormatException nfe) {
                continue;
            }
        }
        context.write(new IntWritable(year), new IntWritable(sumMonth));
    }
}
