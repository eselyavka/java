package com.example.devops;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;
import java.util.Arrays;

/**
 * Template multiple output MR Driver class
 */

public class MultipleOutputDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.printf("Usage: %s [generic options] <input...>  <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = new Job(getConf(), "multipleOutputDriver");
        job.setJarByClass(getClass());

        for (String arg : Arrays.copyOfRange(args, 0, args.length - 1)) {
            MultipleInputs.addInputPath(job, new Path(arg), TextInputFormat.class, MultipleOutputMapper.class);
        }

        FileOutputFormat.setOutputPath(job, new Path(args[args.length - 1]));
        LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);

        job.addCacheFile(new URI("hdfs://localhost:8025/user/eseliavka/conf/my_reports.conf#cfg"));

        job.setMapperClass(MultipleOutputMapper.class);
        job.setReducerClass(MultipleOutputReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new MultipleOutputDriver(), args);
        System.exit(exitCode);
    }
}
