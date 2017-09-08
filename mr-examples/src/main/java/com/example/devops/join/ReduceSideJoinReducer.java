package com.example.devops.join;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class ReduceSideJoinReducer extends Reducer<TaggedKey, Text, NullWritable, Text> {

    @Override
    protected void reduce(TaggedKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key.getJoinKey()).append(",");
        for (Text payload : values) {
            stringBuilder.append(payload).append(",");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        context.write(NullWritable.get(), new Text(stringBuilder.toString()));
    }
}
