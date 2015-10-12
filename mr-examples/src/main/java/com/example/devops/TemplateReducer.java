package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Created by eseliavka on 24.07.15.
 */
public class TemplateReducer
        extends Reducer<Text, Text, Text, LongWritable> {

    @Override
    public void reduce(Text key, Iterable<Text> values, Context context)
            throws IOException, InterruptedException {

        Long total = 0L;

        while (values.iterator().hasNext()) {
            total++;
            values.iterator().next();
        }

        context.write(key, new LongWritable(total));
    }
}