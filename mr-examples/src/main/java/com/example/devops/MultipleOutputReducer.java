package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class MultipleOutputReducer
        extends Reducer<Text, LongWritable, Text, LongWritable> {
    MultipleOutputs<Text, LongWritable> mos;

    private static ArrayList<String> reports = new ArrayList<String>();
    private static ArrayList<String> drvInputs = new ArrayList<String>();

    public ArrayList<String> parse_report_file(File fn) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            Scanner s = new Scanner(fn);
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return list;
    }

    public void set_inputs(File fn) {
        try {
            FileInputStream fis = new FileInputStream(fn);
            ObjectInputStream ois = new ObjectInputStream(fis);
            drvInputs = (ArrayList) ois.readObject();
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
            return;
        }
    }

    public String new_generateOuputDirectory(String r) throws IOException {
        if (drvInputs.size() > 0) {
            for (String input : drvInputs) {
                if (input.toLowerCase().contains("/" + r.toLowerCase() + "/")) {
                    return new File(new File(input).getParent()).getParent();
                }
            }
        }
        return null;
    }

    @Override
    public void setup(Context context) {
        mos = new MultipleOutputs(context);
        File[] f = {new File("cfg"), new File("in")};
        reports = parse_report_file(f[0]);
        set_inputs(f[1]);
    }

    @Override
    public void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException {

        Long total = 0L;
        String[] report = key.toString().split("\t");
        String output;

        while (values.iterator().hasNext()) {
            total = total + values.iterator().next().get();
        }

        if (reports.contains(report[0])) {
            output = new_generateOuputDirectory(report[0]);
            if (output != null) {
                mos.write(new Text(report[1]), new LongWritable(total), output + "/data_todo." + context.getConfiguration().get("jobUUID") + "/part");
            } else {
                context.write(new Text(report[1]), new LongWritable(total));
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
}