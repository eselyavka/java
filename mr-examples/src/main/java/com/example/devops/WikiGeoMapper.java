package com.example.devops;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eseliavka on 23.07.15.
 */

public class WikiGeoMapper
        extends Mapper<LongWritable, Text, Text, MyArrayWritable> {
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] fields = value.toString().split(" ");
        String article = fields[0];
        ArrayList<Double> coord = new ArrayList<Double>();

        if (fields.length == 5) {
            coord.add(Double.parseDouble(fields[2].replace("\"", "")));
            coord.add(Double.parseDouble(fields[3].replace("\"", "")));
        }

        DoubleWritable[] co = new DoubleWritable[coord.size()];
        for (int i = 0; i < coord.size(); i++) {
            co[i] = new DoubleWritable(coord.get(i));
        }
        context.write(new Text(article), new MyArrayWritable(DoubleWritable.class, co));
    }
}
