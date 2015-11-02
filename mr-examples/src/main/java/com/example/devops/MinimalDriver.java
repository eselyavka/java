package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eseliavka on 28.10.15.
 */
public class MinimalDriver extends Configured implements Tool {
    private List<RunningJob> jobs = new ArrayList<RunningJob>();

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }
        JobConf job = new JobConf(new Configuration(), MinimalDriver.class);
        job.setJarByClass(getClass());
        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.setJobName("myjob");
        job.setNumReduceTasks(1);
        try {
            JobClient jc = new JobClient();
            jc.init(job);
            jc.setConf(job);
            jobs.add(jc.submitJob(job));
        } catch (IOException e) {
            return -1;
        }
        return 1;
    }

    public static void main(String[] args) throws Exception {
        MinimalDriver md = new MinimalDriver();
        try {
            int exitCode = ToolRunner.run(md, args);
            for (RunningJob rj : md.jobs) {
                rj.waitForCompletion();
                if (rj.getJobState() != JobStatus.SUCCEEDED) {
                    continue;
                }
                rj.cleanupProgress();
            }
            System.exit(exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
