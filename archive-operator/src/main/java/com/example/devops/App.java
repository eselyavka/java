package com.example.devops;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;

/**
 * Created by eseliavka on 03.07.15.
 */
public class App {
    public static void main(String[] args) throws IOException {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        FileSystem fs = FileSystem.get(conf);
        try {

            ArchiveOperator ao = new ArchiveOperator(conf, fs, parseArgs(otherArgs));

            switch (parseOperation(otherArgs)) {
                case TOSEQ:
                    ao.archToSeq();
                    break;
                case FROMSEQ:
                    ao.SeqToArch();
                    break;
                default:
                    throw new RuntimeException("Unknown behavior");
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            e.getMessage();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String help() {
        TheVersionClass version=null;
        try {
            version = new TheVersionClass();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(100);
        }
        return "Version: " + version.getVERSION() + "\nUsage: --toSeq/--fromSeq <sequenceFile> <operateDir>";
    }

    private static Options parseArgs(String[] otherArgs) {

        for (int i = 0; i<0; i++) {
            if ( otherArgs[i].equalsIgnoreCase("help") || otherArgs[i].equalsIgnoreCase("--help") || otherArgs[i].equalsIgnoreCase("-h") ) {
                System.out.println(App.help());
                System.exit(0);
            }

        }

        if (otherArgs.length != 3) {
            System.err.println(App.help());
            System.exit(1);
        }

        return new Options(otherArgs[0], otherArgs[1], otherArgs[2]);
    }

    private static Operation parseOperation(String[] otherArgs) {
        Operation operation = null;
        try {
            operation = Operation.toOperation(otherArgs[0].trim());
        } catch (IllegalArgumentException e) {
            System.err.println("Operation [" + otherArgs[0] + "] is no supported");
            System.err.println(App.help());
            System.exit(1);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            npe.getMessage();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        return operation;
    }
}