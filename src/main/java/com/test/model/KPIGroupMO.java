package com.test.model;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class KPIGroupMO implements Serializable {
    private String groupId;
    private String monitor;
    private String modelName;
    private Set<String> keyFields=new HashSet<>();
    private String cluster;
    private boolean predictServerEnabled;
    private boolean predictClusterEnabled;
    private long collectInterval;
    private Set<String> kpiFocusFields=new HashSet<>();
    public Set<String> getKpiFocusFields() {
        return kpiFocusFields;
    }

    public void setKpiFocusFields(Set<String> kpiFocusFields) {
        this.kpiFocusFields = kpiFocusFields;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Set<String> getKeyFields() {
        return keyFields;
    }

    public void setKeyFields(Set<String> keyFields) {
        this.keyFields = keyFields;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public boolean isPredictServerEnabled() {
        return predictServerEnabled;
    }

    public void setPredictServerEnabled(boolean predictServerEnabled) {
        this.predictServerEnabled = predictServerEnabled;
    }

    public boolean isPredictClusterEnabled() {
        return predictClusterEnabled;
    }

    public void setPredictClusterEnabled(boolean predictClusterEnabled) {
        this.predictClusterEnabled = predictClusterEnabled;
    }

    public long getCollectInterval() {
        return collectInterval;
    }

    public void setCollectInterval(long collectInterval) {
        this.collectInterval = collectInterval;
    }
}
