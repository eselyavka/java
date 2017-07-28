package com.example.devops;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.Stat;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/**
 * <h1>Change mtime</h1>
 * The MakeTimeUpdater program implements an application that
 * change mtime for files and folders on hdfs
 */
public class StatWithFullName {
    private Configuration conf;
    private FileSystem fs;
    private static final Log LOG = LogFactory.getLog(StatWithFullName.class);
    private static final byte WRONG_NUMBER_OF_ARGS = -1;
    private static final byte OPERATION_ERROR = 1;

    private Long currTime = null;

    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    public void setFs(FileSystem fs) {
        this.fs = fs;
    }

    public StatWithFullName() throws IOException {
        conf = new Configuration();
        fs = FileSystem.get(conf);
    }

    public void getMtimeWithUri(List<String> paths) throws IOException {
        System.out.println(paths.size());
        for (String s: paths) {
            Path p = new Path(s);
            FileStatus fileStatus = fs.getFileStatus(p);
            System.out.println(fileStatus.getModificationTime() + " " + p.toUri().getRawPath());
            //Stat s = new Stat(p, fs.getDefaultBlockSize(), false, fs);
            //s.getFileStatus()
        }
    }

    /**
     * Print usage help
     *
     * @return <code>-1</code> on invocation
     */
    public static int printUsage() {
        System.out.println(StatWithFullName.class.getName() +
                " <path>\n" +
                "\t<path> on hdfs\n" +
                "\t-h/--help print this help");
        return WRONG_NUMBER_OF_ARGS;
    }

    /**
     * Main method update mtime for file(s)/folder(s) on hdfs
     * <p>
     * Two arguments required:
     * hdfs file/dir path
     * unit timestamp with millis
     *
     * @param args Takes an array of String arguments
     */
    public static void main(String[] args) {
        List<String> argsLst = Arrays.asList(args);
        System.out.println(Arrays.toString(argsLst.toArray()));
        if (argsLst.size() < 1 || argsLst.contains("-h") || argsLst.contains("--help")) {
            System.exit(printUsage());
        }

        StringWriter sw = new StringWriter();

        try {
            StatWithFullName statWithFullName = new StatWithFullName();
            statWithFullName.getMtimeWithUri(argsLst);
        } catch (IOException e) {
            LOG.error("can't perform hdfs operations");
            e.printStackTrace(new PrintWriter(sw));
            LOG.debug(sw.toString());
        }
    }
}
