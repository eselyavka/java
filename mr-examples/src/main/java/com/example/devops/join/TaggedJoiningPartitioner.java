package com.example.devops.join;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class TaggedJoiningPartitioner extends Partitioner<TaggedKey, Text> {

    @Override
    public int getPartition(TaggedKey taggedKey, Text text, int i) {
        return taggedKey.getJoinKey().hashCode() % i;
    }
}
