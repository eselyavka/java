package com.example.devops;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import static javafx.scene.input.KeyCode.V;

/**
 * Binary output MR Driver class
 */

public class BinaryDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = new Job(getConf(), "BinaryDriver");
        job.setJarByClass(getClass());

        FileInputFormat.addInputPath(job, new Path(args[0]));
        job.setOutputFormatClass(BinaryOutputFormat.class);
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(BinaryMapper.class);
        job.setNumReduceTasks(0);

        job.setMapOutputKeyClass(BytesWritable.class);
        job.setMapOutputValueClass(NullWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new BinaryDriver(), args);
        System.exit(exitCode);
    }
}
