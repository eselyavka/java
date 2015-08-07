package com.example.devops;

import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by eseliavka on 07.08.15.
 */
public class DBOutputWritable implements Writable, DBWritable {
    private String name;
    private Double latitude;
    private Double longtitude;
    private String cc;

    public DBOutputWritable(String name, Double latitude, Double longtitude, String cc) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.cc = cc;
    }

    public void readFields(DataInput in) throws IOException {
        name = in.readUTF();
        latitude = in.readDouble();
        longtitude = in.readDouble();
        cc = in.readUTF();
    }

    public void readFields(ResultSet rs) throws SQLException {
        name = rs.getString(1);
        latitude = rs.getDouble(2);
        longtitude = rs.getDouble(3);
        cc = rs.getString(4);
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(name);
        out.writeDouble(latitude);
        out.writeDouble(longtitude);
        out.writeUTF(cc);
    }

    public void write(PreparedStatement ps) throws SQLException {
        ps.setString(1, name);
        ps.setDouble(2, latitude);
        ps.setDouble(3, longtitude);
        ps.setString(4, cc);
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongtitude() {
        return longtitude;
    }

    public String getName() {
        return name;
    }

    public String getCc() {
        return cc;
    }
}
