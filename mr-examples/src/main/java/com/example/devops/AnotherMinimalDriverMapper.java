package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

import java.io.IOException;
import java.util.Random;

/**
 * Created by eseliavka on 23.07.15.
 */

public class AnotherMinimalDriverMapper extends MapReduceBase
        implements Mapper<LongWritable, Text, Text, Text> {

    @Override
    public void map(LongWritable key, Text value, OutputCollector<Text, Text> output, Reporter reporter)
            throws IOException {

        String ids[] = {"42222", "51633126", "51454292", "51234193", "51123194", "54433195", "54434196", "54545225", "5545292", "452225395"};

        int rnd = new Random().nextInt(1000);
        if (rnd < 500) {
            output.collect(new Text("field1|field2|field3|" + ids[new Random().nextInt(70)]), value);
        } else {
            output.collect(new Text("field4|field5|field6|" + ids[new Random().nextInt(70)]), value);
        }
    }
}
