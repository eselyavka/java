package com.example.devops;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class BFSCoverageReducer extends Reducer<NullWritable, Text, NullWritable, Text> {
    private String color = Node.Color.BLACK.name();

    @Override
    public void reduce(NullWritable key, Iterable<Text> values, Context context) throws InterruptedException, IOException {
        while (values.iterator().hasNext()) {
            String value = values.iterator().next().toString();
            if (!color.equalsIgnoreCase(value)) {
                color = value;
                break;
            }
        }
    }

    @Override
    public void cleanup(Context context) throws IOException, InterruptedException {
        context.write(NullWritable.get(), new Text(color));
    }
}
