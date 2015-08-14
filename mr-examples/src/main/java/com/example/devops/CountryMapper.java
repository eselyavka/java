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
    private static final Double zero = new Double(0);
    private static final String none = "none";

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String[] fields = value.toString().split("\t");

        Double latitude = zero;
        Double longtitude = zero;
        String name = none;
        String cc = none;

        if (fields.length >= 8) {
            name = fields[1];
            latitude = Double.parseDouble(fields[4]);
            longtitude = Double.parseDouble(fields[5]);
            cc = fields[8];
        }
        try {
            context.write(new DBOutputWritable(name, latitude, longtitude, cc), NullWritable.get());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
