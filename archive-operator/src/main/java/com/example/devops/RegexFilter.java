package com.example.devops;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.PathFilter;

import java.util.regex.Pattern;

/**
 * Created by eseliavka on 29.06.15.
 */
public class RegexFilter implements PathFilter {

    String pattern;

    public RegexFilter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(Path path) {
        return Pattern.matches(pattern, path.toString());
    }
}