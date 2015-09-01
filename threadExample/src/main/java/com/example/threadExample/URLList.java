package com.example.threadExample;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by eseliavka on 31.08.15.
 */
public class URLList {

    private List<String> urllist;

    URLList(List<String> urllist) {
        this.setUrllist(Collections.synchronizedList(new ArrayList<String>(urllist)));
    }

    public List<String> getUrllist() {
        return urllist;
    }

    public void setUrllist(List<String> urllist) {
        this.urllist = urllist;
    }
}
