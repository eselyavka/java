package com.example.devops;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.HashSet;
import java.util.Set;

/*
http://www.johnandcailin.com/blog/cailin/breadth-first-graph-search-using-iterative-map-reduce-algorithm
*/

public class BFSDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {

        int counter = 0;

        if (args.length != 1) {
            System.err.printf("Usage: %s [generic options] <input>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }
        String in = args[0];
        String out = args[0] + "_out_" + counter;

        Set<Boolean> set = new HashSet<Boolean>(2);

        while (counter != 4) {
            System.out.println("ITERATION:" + counter + "|IN:" + in + "|OUT:" + out);
            String[] covArgs = {in, "coverage"};
            if (counter > 0 && ToolRunner.run(new BFSCoverage(), covArgs) == 0) {
                break;
            }
            Job job = new Job(getConf(), "BFSDriver");
            job.setJarByClass(getClass());

            FileInputFormat.addInputPath(job, new Path(in));
            FileOutputFormat.setOutputPath(job, new Path(out));

            job.setMapperClass(BFSMapper.class);
            job.setReducerClass(BFSReducer.class);

            job.setNumReduceTasks(1);

            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(Text.class);

            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(Text.class);

            job.setInputFormatClass(TextInputFormat.class);
            job.setOutputFormatClass(TextOutputFormat.class);

            set.add(job.waitForCompletion(true));
            in = args[0] + "_out_" + counter;
            counter++;
            out = args[0] + "_out_" + counter;
        }
        return set.size() > 1 ? 1 : 0;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new BFSDriver(), args);
        System.exit(exitCode);
    }
}
