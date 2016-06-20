package com.example.devops;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class MultipleOutputReducer
        extends Reducer<Text, LongWritable, Text, LongWritable> {
    MultipleOutputs<Text, LongWritable> mos;

    ArrayList<String> reports = new ArrayList<String>();

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

    ArrayList generateFileName(Context context) {
        ArrayList<String> inputs = new ArrayList<String>();
        for (String i : context.getConfiguration().get("mapreduce.input.multipleinputs.dir.mappers").split(",")) {
            inputs.add(i.split(";")[0]);
        }
        return inputs;
    }

    public String generateOuputDirectory(String output, String report) throws IOException {
        return "/user/" + UserGroupInformation.getCurrentUser().getUserName() + "/" + output + "/" + report + "/";
    }

    @Override
    public void setup(Context context) {
        mos = new MultipleOutputs(context);
        File f = new File("cfg");
        reports = parse_report_file(f);
    }

    @Override
    public void reduce(Text key, Iterable<LongWritable> values, Context context)
            throws IOException, InterruptedException {

        Long total = 0L;
        String[] report = key.toString().split("\t");
        ArrayList<String> outputs = generateFileName(context);
        Iterator<String> iterator = outputs.iterator();
        String output;


        while (values.iterator().hasNext()) {
            total = total + values.iterator().next().get();
        }

        while (iterator.hasNext()) {
            output = new File(iterator.next()).getParent();
            if (reports.contains(report[0]) && output.contains(report[0])) {
                mos.write(new Text(report[1]), new LongWritable(total), generateOuputDirectory(output, report[0]) + "/part");
            }
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        mos.close();
    }
}