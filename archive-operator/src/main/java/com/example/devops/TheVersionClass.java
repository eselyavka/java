package com.example.devops;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by eseliavka on 03.07.15.
 */
public class TheVersionClass {

    private static Properties prop;

    public TheVersionClass() throws IOException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");
        this.prop = new Properties();

        try {
            this.prop.load(resourceAsStream);
        } catch (IOException e) {
            throw e;
        }
    }

    public static String getVERSION() {
        return prop.getProperty("version", "0.1");
    }

    public static String getGROUPID() {
        return prop.getProperty("groupId", "unset");
    }

    public static String getARTIFACTID() {
        return prop.getProperty("artifactId", "unset");
    }
}
