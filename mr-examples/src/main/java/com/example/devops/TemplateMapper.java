package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Created by eseliavka on 23.07.15.
 */

public class TemplateMapper
        extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString().toLowerCase();
        int idx = line.indexOf("\t");
        if (idx > -1)
            idx = line.indexOf("\t", idx + 1);

        if (idx == -1) {
            System.err.println("Bad line [" + line + "]");
            context.getCounter(TemplateCounters.ParseError).increment(1);
            return;
        }

        context.write(new Text(line.substring(0, idx)), new Text(line.substring(idx + 1)));
    }
}
