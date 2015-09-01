package com.example.threadExample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eseliavka on 26.08.15.
 */
public class UrlRequesterExecutor {
    public static final Short THREAD_NUMBER = 100;

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        if (domain != null) {
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } else {
            return "example.com";
        }
    }

    public static void main(String args[]) throws InterruptedException {
        UrlRequester[] UrlRequester = new UrlRequester[THREAD_NUMBER];
        BufferedReader br = null;
        List<String> urls = new ArrayList<String>();
        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/tmp/urllist.txt"));

            while ((sCurrentLine = br.readLine()) != null) {
                try {
                    urls.add(getDomainName(sCurrentLine.split("\t")[3]));
                } catch (URISyntaxException e) {
                    continue;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        Counter counter = new Counter(urls.size());
        Integer bulkSize = urls.size() / THREAD_NUMBER;
        Integer modBulkSize = urls.size() % THREAD_NUMBER;

        for (Short i = 0; i < THREAD_NUMBER; i++) {
            UrlRequester[i] = new UrlRequester("Thread-" + i.toString(), counter, bulkSize, modBulkSize, new URLList(urls));
            UrlRequester[i].start();
        }
    }
}
