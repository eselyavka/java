package com.example.devops.join;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class ReduceSideJoinMapper extends Mapper<LongWritable, Text, TaggedKey, Text> {
    private TaggedKey taggedKey = new TaggedKey();
    private String[] joinOrderAndIndex;
    private int joinOrder;
    private int idx;

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        joinOrderAndIndex = context.getConfiguration().get(fileSplit.getPath().getName()).split(",");
        joinOrder = Integer.parseInt(joinOrderAndIndex[0]);
        idx = Integer.parseInt(joinOrderAndIndex[1]);
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        StringBuilder payload = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            if (i == idx) continue;
            payload.append(tokens[i]).append(",");
        }
        payload.setLength(payload.length() - 1);
        taggedKey.set(new Text(tokens[idx]), joinOrder);
        context.write(taggedKey, new Text(payload.toString()));
    }
}
