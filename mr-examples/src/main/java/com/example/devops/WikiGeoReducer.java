package com.example.devops;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.net.URLDecoder;

/**
 * Created by eseliavka on 24.07.15.
 */
public class WikiGeoReducer
        extends Reducer<Text, MyArrayWritable, Text, DoubleWritable> {
    @Override
    public void reduce(Text key, Iterable<MyArrayWritable> values, Context context)
            throws IOException, InterruptedException {
        Double sum=0.0d;

        for (MyArrayWritable value : values) {
            for (DoubleWritable i: (DoubleWritable[]) value.toArray()) {
                sum+= i.get();
            }
        }
        String ud = URLDecoder.decode(key.toString());

        context.write(new Text(ud), new DoubleWritable(sum));
    }
}
