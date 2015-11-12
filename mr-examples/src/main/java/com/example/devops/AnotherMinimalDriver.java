package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Random;

/**
 * Created by eseliavka on 28.10.15.
 */
public class AnotherMinimalDriver extends Configured implements Tool {
    public static final String TSCONF = "/user/eseliavka/config/test.conf";

    @Override
    public int run(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.printf("Usage: %s [generic options] <input> <output>\n",
                    getClass().getSimpleName());
            ToolRunner.printGenericCommandUsage(System.err);
            return -1;
        }
        JobConf conf = new JobConf(new Configuration(), AnotherMinimalDriver.class);
        conf.setJarByClass(AnotherMinimalDriver.class);
        conf.setOutputKeyClass(Text.class);
        conf.setOutputValueClass(Text.class);
        conf.setMapperClass(AnotherMinimalDriverMapper.class);
        conf.setNumReduceTasks(0);
        conf.setOutputFormat(TextOutputFormat.class);
        conf.setJobName("eseliavkaMinDrv");
        DistributedCache.addCacheFile(new Path(TSCONF).toUri(), conf);
        TextInputFormat.addInputPath(conf, new Path(args[0]));
        TextOutputFormat.setOutputPath(conf, new Path(args[1]));
        conf.set("jobId", String.valueOf(new Random().nextInt()));
        JobClient.runJob(conf);
        return 0;
    }

    public static void main(String[] args) throws Exception {
        int res = ToolRunner.run(new Configuration(), new AnotherMinimalDriver(), args);
        System.exit(res);
    }
}
