package com.example.devops;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BFSReducer extends Reducer<IntWritable, Text, IntWritable, Text> {

    @Override
    public void reduce(IntWritable key, Iterable<Text> values, Context context) throws InterruptedException, IOException {
        List<Integer> edges = new ArrayList<Integer>();
        Integer distance = -1;
        Node.Color color = Node.Color.WHITE;

        while (values.iterator().hasNext()) {
            Node node = new Node(key.get() + "\t" + values.iterator().next().toString());

            if (node.getEdges().size() > 0) {
                edges = node.getEdges();
            }

            if (node.getDistance() != -1) {
                distance = node.getDistance();
            }

            if (node.getColor().ordinal() > color.ordinal()) {
                color = node.getColor();
            }
        }

        Node unionNode = new Node(key.get());
        unionNode.setEdges(edges);
        unionNode.setDistance(distance);
        unionNode.setColor(color);

        context.write(key, new Text(unionNode.getLine()));
    }
}
