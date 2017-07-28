package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class ReduceSideJoinMapper extends Mapper<LongWritable, Text, TaggedKey, Text> {
    private TaggedKey taggedKey = new TaggedKey();
    private Text fileName = new Text();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        FileSplit fileSplit = (FileSplit) context.getInputSplit();
        fileName.set(fileSplit.getPath().getName());
        System.out.println("filename:" + fileName.toString());
    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] tokens = value.toString().split(",");
        String payload = null;
        for (int i=1; i<tokens.length; i++) {
            payload += tokens[i] + ",";
        }
        taggedKey.set(new Text(tokens[0]), fileName);
        System.out.println("setting:" + taggedKey.getJoinKey().toString() + "|" + taggedKey.getTag().toString());
        context.write(taggedKey, new Text(payload));
    }
}
