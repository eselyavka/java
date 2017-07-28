package com.example.devops;

import org.apache.hadoop.io.*;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class TaggedKey implements Writable, WritableComparable<TaggedKey> {
    private Text joinKey;
    private Text tag;

    public TaggedKey() {
        this.joinKey = new Text();
        this.tag = new Text();
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
        dataOutput.writeUTF(joinKey.toString() + "," + tag.toString());
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.joinKey.readFields(dataInput);
        this.tag.readFields(dataInput);
    }

    public Text getJoinKey() {
        return joinKey;
    }

    public Text getTag() {
        return tag;
    }

    public void setJoinKey(Text joinKey) {
        this.joinKey = joinKey;
    }

    public void setTag(Text tag) {
        this.tag = tag;
    }

    public void set(Text joinKey, Text tag) {
        setJoinKey(joinKey);
        setTag(tag);
    }
}
