package com.example.devops;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;
import parquet.example.data.Group;

import java.io.IOException;


public class EmployeeParquetMapper extends Mapper<LongWritable, Group, IntWritable, EmployeeCompositeDepSalaryWritable> {

    @Override
    public void map(LongWritable key, Group value, Mapper.Context context) throws InterruptedException, IOException {
        String inputRecord = value.toString();
        String[] raw = inputRecord.split("\n");
        int employee_id = Integer.parseInt(raw[0].split(": ")[1]);
        int dep_id = Integer.parseInt(raw[1].split(": ")[1]);
        int salary = Integer.parseInt(raw[2].split(": ")[1]);
        context.write(new IntWritable(employee_id), new EmployeeCompositeDepSalaryWritable(dep_id, salary));
    }
}