package com.example.devops;


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;


public class EmployeeParquetCombiner extends Reducer<IntWritable, EmployeeCompositeDepSalaryWritable, IntWritable, EmployeeCompositeDepSalaryWritable> {

    @Override
    public void reduce(IntWritable key, Iterable<EmployeeCompositeDepSalaryWritable> values, Context context) throws InterruptedException, IOException {
        Set<Integer> deps = new HashSet<Integer>();
        List<Integer> list = new ArrayList<Integer>();
        while (values.iterator().hasNext()) {
            EmployeeCompositeDepSalaryWritable employeeCompositeDepSalaryWritable = values.iterator().next();
            deps.add(employeeCompositeDepSalaryWritable.getDepId().get());
            list.add(employeeCompositeDepSalaryWritable.getSalary().get());
        }
        if (deps.size() == 1) {
            context.write(key, new EmployeeCompositeDepSalaryWritable(deps.iterator().next(), Collections.max(list)));
        }
    }
}
