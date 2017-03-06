package com.example.devops;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class EmployeeParquetReducer extends Reducer<IntWritable, EmployeeCompositeDepSalaryWritable, IntWritable, NullWritable> {
    private EmployeeCompositeDepSalaryWritable max = new EmployeeCompositeDepSalaryWritable(0,0);

    @Override
    public void reduce(IntWritable key, Iterable<EmployeeCompositeDepSalaryWritable> values, Context context) throws InterruptedException, IOException {
        while (values.iterator().hasNext()) {
            EmployeeCompositeDepSalaryWritable employeeCompositeDepSalaryWritable = values.iterator().next();
            if (max.getSalary().compareTo(employeeCompositeDepSalaryWritable.getSalary()) < 0) {
                max.setSalary(employeeCompositeDepSalaryWritable.getSalary());
            }
        }
    }

    @Override
    public void cleanup(Context context) throws IOException, InterruptedException {
        context.write(max.getSalary(), NullWritable.get());
    }
}
