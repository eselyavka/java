package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class HdfsHelper {
    private Configuration conf;
    private FileSystem fs;

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public void setFs(FileSystem fs) {
        this.fs = fs;
    }

    public HdfsHelper() throws IOException {
        conf = new Configuration();
        fs = FileSystem.get(conf);
    }

    public Boolean changeMakeTime(String path, Long epoch) throws IOException {
        Path p = new Path(path);
        if (fs.exists(p)) {
            fs.setTimes(p, epoch, epoch);
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Please specify path and mtime since epoch");
            System.exit(1);
        }

        Long mtime;
        try {
            mtime = Long.parseLong(args[1]);
        } catch (NumberFormatException nfe) {
            System.err.println(args[1] + " is not a number, going to use NOW()");
            mtime = System.currentTimeMillis();
        }
        try {
            HdfsHelper hdfsHelper = new HdfsHelper();
            if (hdfsHelper.changeMakeTime(args[0], mtime)) {
                System.out.println("mtime successfully changed");
            } else {
                System.err.println("can't change mtime");
            }
        } catch (IOException e) {
            System.err.println("can't perform hdfs operations");
            System.exit(1);
        }
    }
}
