package com.example.devops;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

/**
 * Created by eseliavka on 06.08.15.
 */
public class CountryDriver extends Configured implements Tool {
    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }

        Job job = new Job(getConf(), "CountryDriver");
        job.setJarByClass(getClass());

        DBConfiguration.configureDB(getConf(),
                "org.postgresql.Driver",
                "jdbc:postgresql://test-fw-deploy:5432/geo",
                "geo",
                "geo");

        //DistributedCache.addFileToClassPath(new Path("/tmp/postgresql-9.3-1103.jdbc41.jar"), job.getConfiguration());
//        job.addFileToClassPath(new Path("file:///tmp/postgresql-9.3-1103.jdbc41.jar"));
//        job.addArchiveToClassPath(new Path("/tmp/postgresql-9.3-1103.jdbc41.jar"));

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(CountryMapper.class);
        job.setNumReduceTasks(0);

        job.setOutputKeyClass(DBOutputWritable.class);
        job.setOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(DBOutputWritable.class);

        DBOutputFormat.setOutput(job, "test", new String[]{"name", "latitude", "longtitude"});

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new CountryDriver(), args);
        System.exit(exitCode);
    }
}
