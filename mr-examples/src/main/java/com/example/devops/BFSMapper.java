package com.example.devops;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class BFSMapper extends Mapper<LongWritable, Text, IntWritable, Text> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws InterruptedException, IOException {
        Node node = new Node(value.toString());
        if (node.getColor() == Node.Color.GRAY) {
            for (int edge : node.getEdges()) {
                Node exploydedNode = new Node(edge);
                exploydedNode.setDistance(node.getDistance() + 1);
                exploydedNode.setColor(Node.Color.GRAY);
                context.write(new IntWritable(exploydedNode.getId()), new Text(exploydedNode.getLine()));
            }
            node.setColor(Node.Color.BLACK);
        }
        context.write(new IntWritable(node.getId()), new Text(node.getLine()));
    }
}