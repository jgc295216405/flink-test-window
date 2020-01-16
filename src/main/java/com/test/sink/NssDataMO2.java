package com.test.sink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class NssDataMO2 implements Serializable, Cloneable {
    private static final Logger LOGGER = LoggerFactory.getLogger(NssDataMO2.class);
    private String key;
    private int value;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
    //    private String cluster;
//
//
//    private String model;
//
//
//    private String monitor;
//
//
//    private String server;
//
//
//    private long time;
//
//
//    private String data;
//
//    public String getCluster() {
//        return cluster;
//    }
//
//    public void setCluster(String cluster) {
//        this.cluster = cluster;
//    }
//
//    public String getModel() {
//        return model;
//    }
//
//    public void setModel(String model) {
//        this.model = model;
//    }
//
//    public String getMonitor() {
//        return monitor;
//    }
//
//    public void setMonitor(String monitor) {
//        this.monitor = monitor;
//    }
//
//    public String getServer() {
//        return server;
//    }
//
//    public void setServer(String server) {
//        this.server = server;
//    }
//
//    public long getTime() {
//        return time;
//    }
//
//    public void setTime(long time) {
//        this.time = time;
//    }
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
}

