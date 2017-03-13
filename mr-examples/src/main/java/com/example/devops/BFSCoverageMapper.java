package com.example.devops;


import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class BFSCoverageMapper extends Mapper<LongWritable, Text, NullWritable, Text> {
    private String color = Node.Color.BLACK.name();
    private Boolean flag = true;

    @Override
    public void map(LongWritable key, Text value, Context context) throws InterruptedException, IOException {
        Node node = new Node(value.toString());
        if (!color.equalsIgnoreCase(node.getColor().name()) && flag) {
            flag = false;
            color = node.getColor().name();
        }
    }

    @Override
    public void cleanup(Context context) throws IOException, InterruptedException {
        context.write(NullWritable.get(), new Text(color));
    }
}