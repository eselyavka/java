package tv.okko.devops;

import java.io.*;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.commons.io.FilenameUtils;
import org.apache.hadoop.io.DataOutputBuffer;
import org.apache.hadoop.io.MD5Hash;
import org.apache.hadoop.util.DataChecksum;

/*

Code reference: https://github.com/jpatanooga/IvoryMonkey

 */

public class hdfsCheksumComparator {
    static void printAndExit(String message) {
        System.err.println(message);
        System.exit(1);
    }

    static void usage() {
        System.out.println("Usage : <hdfsFile> <localFile>");
        System.exit(1);
    }

    public static void main(String[] argv) throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        conf.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());
        FileSystem hdfs = FileSystem.get(conf);
        LocalFileSystem localFs = LocalFileSystem.getLocal(conf);
        Path hdfsFile = null;
        Path localFile = null;
        FileChecksum hdfsFileChecksum = null;

        if (argv.length != 2)
            usage();

        hdfsFile = new Path(argv[0]);
        localFile = new Path(argv[1]);

        if (! hdfsFile.isAbsolute()){
            hdfsFile = new Path(hdfs.getHomeDirectory() + "/" + FilenameUtils.getBaseName(argv[0]));
            System.out.println("Using absolute path: " + hdfsFile.toString());
        }
        if (! localFile.isAbsolute()){
            localFile = new Path(localFs.getHomeDirectory() + "/" + FilenameUtils.getBaseName(argv[1]));
            System.out.println("Using absolute path: " + localFile.toString());
        }

        if (!localFs.exists(localFile))
            printAndExit("Local file: " + localFile.getName() + " not found");
        if (!localFs.isFile(localFile))
            printAndExit("Local file: " + localFile.getName() + " should be a file");
        if (!hdfs.exists(hdfsFile))
            printAndExit("Local file: " + localFile.getName() + " not found");
        if (!hdfs.isFile(hdfsFile))
            printAndExit("Hdfs file" + hdfsFile.getName() + " is not a file");

        hdfsFileChecksum = hdfs.getFileChecksum(hdfsFile);
        String [] hdfsHash = hdfsFileChecksum.toString().split(":");

        if (hdfsFileChecksum == null) {
            printAndExit("Can't calculate checksum for file on: " + hdfsFile.toString());
        }

        System.out.println(hdfsFileChecksum.toString());

        long blockSize = Long.parseLong(hdfs.getConf().get("dfs.block.size"));
        int bytesPerChecksum = Integer.parseInt(hdfs.getConf().get("file.bytes-per-checksum"));
        int blockCount= (int) Math.ceil( (double)localFs.getFileStatus(localFile).getLen()/(double) blockSize );

        InputStream in = null;
        DataChecksum chksm = DataChecksum.newDataChecksum(DataChecksum.Type.valueOf(DataChecksum.CHECKSUM_CRC32C), bytesPerChecksum);
        DataOutputBuffer md5OutputBuffer = new DataOutputBuffer();
        MD5MD5CRC32FileChecksum localFileChecksum = null;

        try {
            in = localFs.open(localFile);
            long totalBytesRead = 0L;

            for (int i=0; i<blockCount; i++){

                ByteArrayOutputStream arrayCRCBytes = new ByteArrayOutputStream();
                byte[] crc = new byte[4];
                byte[] buf = new byte[bytesPerChecksum];

                try {
                    int bytesRead = 0;

                    while ( (bytesRead = in.read(buf)) > 0 ){
                        totalBytesRead += bytesRead;

                        chksm.reset();
                        chksm.update(buf, 0, bytesRead);
                        chksm.writeValue(crc,0,true);

                        arrayCRCBytes.write(crc);

                        if (totalBytesRead >= (i + 1) * blockSize){
                            break;
                        }
                    }

                    DataInputStream dis = new DataInputStream(new ByteArrayInputStream(arrayCRCBytes.toByteArray()));
                    final MD5Hash md5Hash = MD5Hash.digest(dis);
                    md5Hash.write(md5OutputBuffer);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }

            final MD5Hash md5Ofmd5 = MD5Hash.digest(md5OutputBuffer.getData());
            localFileChecksum = new MD5MD5CRC32FileChecksum(bytesPerChecksum, (long) Math.ceil(blockSize/bytesPerChecksum), md5Ofmd5);
            System.out.println(localFileChecksum.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (localFileChecksum == null) {
            printAndExit("Can't calculate checksum for file on: " + localFile.toString());
        }

        String [] localHash = localFileChecksum.toString().split(":");

        if (hdfsHash[1].equalsIgnoreCase(localHash[1])){
            System.exit(0);
        } else {
            System.exit(1);
        }

    }
}
