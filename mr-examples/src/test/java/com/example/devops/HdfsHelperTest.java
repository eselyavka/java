package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DistributedFileSystem;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.junit.*;

import java.io.File;

import static org.junit.Assert.*;

public class HdfsHelperTest {
    private static MiniDFSCluster hdfsCluster = null;
    private static Configuration conf = null;
    private static File baseDir = null;
    private static DistributedFileSystem mfs = null;

    @BeforeClass
    public static void setUp() throws Exception {
        conf = new Configuration();
        baseDir = new File("./target/hdfs/" + HdfsHelper.class.getName()).getAbsoluteFile();
        FileUtil.fullyDelete(baseDir);
        conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
        MiniDFSCluster.Builder builder = new MiniDFSCluster.Builder(conf);
        hdfsCluster = builder.build();
        hdfsCluster.waitActive();
        mfs = hdfsCluster.getFileSystem();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        hdfsCluster.shutdown();
        FileUtil.fullyDelete(baseDir);
    }

    @Test
    public void changeMakeTimeShouldReturnTrue() throws Exception {
        Path file = new Path("somefile");
        mfs.createNewFile(file);
        HdfsHelper hdfsHelper = new HdfsHelper();
        hdfsHelper.setFs(mfs);
        assertTrue(hdfsHelper.changeMakeTime(file.getName(), 962879135000L));
    }
}