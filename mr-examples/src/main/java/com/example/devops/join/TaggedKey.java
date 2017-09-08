package com.example.devops.join;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TaggedKey implements Writable, WritableComparable<TaggedKey> {
    private Text joinKey;
    private IntWritable tag;

    public TaggedKey() {
        this.joinKey = new Text();
        this.tag = new IntWritable();
    }

    @Override
    public int compareTo(TaggedKey taggedKey) {
        int compareValue = this.joinKey.compareTo(taggedKey.getJoinKey());
        if (compareValue == 0) {
            compareValue = this.tag.compareTo(taggedKey.getTag());
        }
        return compareValue;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        joinKey.write(dataOutput);
        tag.write(dataOutput);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.joinKey.readFields(dataInput);
        this.tag.readFields(dataInput);
    }

    public Text getJoinKey() {
        return joinKey;
    }

    public IntWritable getTag() {
        return tag;
    }

    public void setJoinKey(Text joinKey) {
        this.joinKey.set(joinKey);
    }

    public void setTag(int tag) {
        this.tag.set(tag);
    }

    public void set(Text joinKey, int tag) {
        setJoinKey(joinKey);
        setTag(tag);
    }
}
