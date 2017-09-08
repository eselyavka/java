package com.example.devops.join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.File;

public class ReduceSideJoinDriverExample extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 3) {
            System.err.printf("Usage: %s [generic options] <file1> <file2> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }
        Configuration conf = getConf();
        conf.setStrings(new File(args[0]).getName(), "0", "2");
        conf.setStrings(new File(args[1]).getName(), "1", "0");
        Job job = new Job(conf, "ReduceSideJoinExampleDriver");
        job.setJarByClass(getClass());

        FileInputFormat.addInputPaths(job, args[0] + "," + args[1]);
        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        job.setPartitionerClass(TaggedJoiningPartitioner.class);
        job.setGroupingComparatorClass(TaggedJoiningGroupingComparator.class);

        job.setMapperClass(ReduceSideJoinMapper.class);
        job.setReducerClass(ReduceSideJoinReducer.class);
        job.setNumReduceTasks(1);

        job.setMapOutputKeyClass(TaggedKey.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new ReduceSideJoinDriverExample(), args);
        System.exit(exitCode);
    }
}
