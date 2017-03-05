package com.example.devops;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EmployeeCompositeDepSalaryWritable implements Writable {
    public IntWritable getDepId() {
        return depId;
    }

    public IntWritable getSalary() {
        return salary;
    }

    public void setDepId(IntWritable depId) {
        this.depId = depId;
    }

    public void setSalary(IntWritable salary) {
        this.salary = salary;
    }

    private IntWritable depId = new IntWritable();
    private IntWritable salary = new IntWritable();

    public EmployeeCompositeDepSalaryWritable() {

    }

    public EmployeeCompositeDepSalaryWritable(int depId, int salary) {
        this.depId.set(depId);
        this.salary.set(salary);
    }

    @Override
    public String toString() {
        return depId + "|" + salary;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        this.depId.write(dataOutput);
        this.salary.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.depId.readFields(dataInput);
        this.salary.readFields(dataInput);
    }
}
