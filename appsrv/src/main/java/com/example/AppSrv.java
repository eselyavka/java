package com.example;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Logger;

import com.maxmind.geoip.*;

public class AppSrv extends HttpServlet {

    private static Logger myLogger = Logger.getLogger(AppSrv.class.getName());
    private static String geoDbFile = "/opt/geoip/GeoLiteCity.dat";
    private LookupService lookupService = null;

    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static final String DB_URL = "jdbc:postgresql://localhost/tst";

    private static final String USER = "tst";
    private static final String PASS = "s3cr3t";

    private String header =
            "<html>\n" +
                    "<body>\n" +
                    "<h2>Hello from Simple Servlet</h2>\n";
    private String footer = "</body>\n" +
            "</html>";

    protected void doDb(HttpServletRequest request) {
        Connection conn = null;
        PreparedStatement st = null;
        java.util.Date today = new java.util.Date();
        try {
            Class.forName(JDBC_DRIVER);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            st = conn.prepareStatement("INSERT INTO requests VALUES (?::inet,?,?,?,?,?)");
            st.setString(1, request.getRemoteAddr());
            st.setString(2, request.getRemoteUser());
            st.setTimestamp(3, new java.sql.Timestamp(today.getTime()));
            st.setString(4, request.getRequestURI());
            st.setShort(5, (short) 200);
            st.setLong(6, 0);
            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    @Override
    public void init() throws ServletException {
        try {
            lookupService = new LookupService(geoDbFile,
                    LookupService.GEOIP_MEMORY_CACHE);
        } catch (IOException e) {
            myLogger.warning("Can't load " + geoDbFile);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.process(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String ip = request.getRemoteAddr();
        Location location = null;
        String city = "Undefined";
        if (lookupService != null) location = lookupService.getLocation(ip);
        city = (location != null ? location.city : city);
        doDb(request);
        response.setStatus(200);
        response.getWriter().write(header + "<p>Request from " + ip + "(" + city + ")</p>" + footer);
    }
}