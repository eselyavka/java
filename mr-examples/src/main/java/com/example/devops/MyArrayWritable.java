package com.example.devops;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Writable;

/**
 * Created by eseliavka on 30.07.15.
 */

public class MyArrayWritable extends ArrayWritable {

    public MyArrayWritable(Class<? extends Writable> valueClass, Writable[] values) {
        super(valueClass, values);
    }

    public MyArrayWritable(Class<? extends Writable> valueClass) {
        super(valueClass);
    }

    public MyArrayWritable(){
        super(DoubleWritable.class);
    }

    @Override
    public DoubleWritable[] get() {
        return (DoubleWritable[]) super.get();
    }
}