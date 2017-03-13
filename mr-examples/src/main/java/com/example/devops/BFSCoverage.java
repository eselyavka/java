package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class BFSCoverage extends Configured implements Tool {

    public boolean isAllBlack(FileSystem fs, Path file) throws IOException {
        String line = Node.Color.WHITE.name();

        if (fs.exists(file)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fs.open(file)));
            line = br.readLine();
        }

        return line.equalsIgnoreCase(Node.Color.BLACK.name());
    }

    @Override
    public int run(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path dir = new Path(args[1]);
        Path file = new Path(args[1] + "/part-r-00000");

        if (isAllBlack(fs, file)) {
            fs.delete(dir, true);
            return 0;
        }

        fs.delete(dir, true);

        Job job = new Job(getConf(), "BFSCoverage");
        job.setJarByClass(getClass());

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(BFSCoverageMapper.class);
        job.setReducerClass(BFSCoverageReducer.class);

        job.setNumReduceTasks(1);

        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.waitForCompletion(true);

        return isAllBlack(fs, file) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new BFSCoverage(), args);
        System.exit(exitCode);
    }
}
