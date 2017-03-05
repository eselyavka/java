package com.example.devops;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class EmployeeParquetReducer extends Reducer<IntWritable, EmployeeCompositeDepSalaryWritable, IntWritable, IntWritable> {
    private Integer max = 0;

    @Override
    public void reduce(IntWritable key, Iterable<EmployeeCompositeDepSalaryWritable> values, Context context) throws InterruptedException, IOException {
        while (values.iterator().hasNext()) {
            EmployeeCompositeDepSalaryWritable employeeCompositeDepSalaryWritable = values.iterator().next();
            if (max.compareTo(employeeCompositeDepSalaryWritable.getSalary().get()) < 0) {
                max = employeeCompositeDepSalaryWritable.getSalary().get();
            }
        }
        context.write(key, new IntWritable(max));
    }
}
