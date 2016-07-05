package com.example.devops;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Template multiple output MR Driver class
 */

public class MultipleOutputDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.printf("Usage: %s [generic options] <input>  <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        List<String> inputs = new ArrayList<String>(args.length);
        List<String> other_args = new ArrayList<String>(args.length);

        Job job = new Job(getConf(), "multipleOutputDriver");
        job.setJarByClass(getClass());

        for (int i = 0; i < args.length; ++i) {
            if ("--input".equals(args[i])) {
                inputs.add(args[++i]);
            } else {
                other_args.add(args[i]);
            }
        }

        if (inputs.size() > 0) {
            inputs.add(other_args.get(0));
            for (String p : inputs) {
                MultipleInputs.addInputPath(job, new Path(p), TextInputFormat.class, MultipleOutputMapper.class);
            }
            OutputStream fos = null;
            ObjectOutputStream oos = null;
            try {
                fos = new FileOutputStream("/tmp/inputs.ser");
                oos = new ObjectOutputStream(fos);
                oos.writeObject(inputs);
                oos.close();
                fos.close();
                FileSystem fs = FileSystem.get(getConf());
                fs.copyFromLocalFile(true, true, new Path("/tmp/inputs.ser"), new Path("/tmp/inputs.ser"));
                job.addCacheFile(new URI("hdfs://localhost:8025/tmp/inputs.ser#in"));
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                    if (oos != null) {
                        oos.close();
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        } else {
            TextInputFormat.addInputPath(job, new Path(other_args.get(0)));
        }

        TextOutputFormat.setOutputPath(job, new Path(other_args.get(1)));
        LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);


        job.addCacheFile(new URI("hdfs://localhost:8025/user/eseliavka/conf/my_reports.conf#cfg"));

        job.getConfiguration().set("jobUUID", String.valueOf(UUID.randomUUID()));

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