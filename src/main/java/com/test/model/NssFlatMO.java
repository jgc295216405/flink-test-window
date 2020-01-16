package com.test.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Maps;
import com.test.config.DynamicConfig;
import com.test.constant.FieldConstant;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;


/**
 *
 */
public class NssFlatMO implements Serializable {
    private static final long serialVersionUID = -2790700866268512465L;
    private static final List<String> FIELD_FIELDS = Arrays.asList("hostId", "instance", "domainId");

    private String monitor;
    private String model;
    private String cluster;
    private String server;
    private String instance;
    private Integer hostId;
    private long time;


    private Map<String, Double> innerDataMap = new HashMap<>();
    private transient Map<String, Object> aggregation;
    private Map<String, String> innerKeyMap;

    /**
     * 集群分组用的key
     */
    private String keyMd5;
    private boolean valid = true;
    public NssFlatMO(String monitor, String model, String cluster, String server, long time, Map<String, Object> flatData) {
        this.model = model;
        this.monitor = monitor;
        this.cluster = cluster;
        this.server = server;
        this.hostId = (Integer) flatData.get(FIELD_FIELDS.get(0));
        this.instance = (String) flatData.get(FIELD_FIELDS.get(1));
        this.time = time;
        KPIGroupMO kpiGroup = DynamicConfig.getKpiGroup(cluster, model, monitor);
        if (kpiGroup != null) {
            Map<String, String> allKey = new HashMap<>();
            allKey.put(FieldConstant.CLUSTER_FIELD, cluster);
            allKey.put(FieldConstant.MODEL_FIELD, model);
            allKey.put(FieldConstant.MONITOR_FIELD, monitor);
            this.innerKeyMap = Maps.newHashMapWithExpectedSize(8);
            Set<String> keyFields = kpiGroup.getKeyFields();
            if (CollectionUtils.isNotEmpty(keyFields)) {
                Map<String, Pattern> valueFilter = DynamicConfig.modelFilter.get(model);
                for (String keyField : keyFields) {
                    Object o = flatData.get(keyField);
                    if (o == null ||
                            (MapUtils.isNotEmpty(valueFilter) && valueFilter.containsKey(keyField) && !valueFilter.get(keyField).matcher(String.valueOf(o)).matches())) {
                        this.valid = false;
                        return;
                    }
                    innerKeyMap.put(keyField, String.valueOf(o));
                }
            }
            Iterator<Map.Entry<String, Object>> it = flatData.entrySet().iterator();
            Set<String> kpiFocusFieldSet = kpiGroup.getKpiFocusFields();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                Object value = entry.getValue();
                String key = entry.getKey();
                if (kpiFocusFieldSet.contains(key)
                        && !keyFields.contains(key)
                        && !FIELD_FIELDS.contains(key)
                        && value != null
                        && (value instanceof Number)) {
                    this.innerDataMap.put(key, ((Number) value).doubleValue());
                }
            }
            allKey.putAll(innerKeyMap);
            this.keyMd5 = MD5Util.md5(JSON.toJSONString(allKey, SerializerFeature.MapSortField));
        } else {
            this.valid = false;
        }
    }

    public NssFlatMO() {
    }

    public NssFlatMO(NssDataMO nssData, Map<String, Object> flatData) {
        this(nssData.getMonitor(), nssData.getModel(), nssData.getCluster(), nssData.getServer(), nssData.getTime(), flatData);
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    public Map<String, Object> getAggregation() {
        return aggregation;
    }

    public void setAggregation(Map<String, Object> aggregation) {
        this.aggregation = aggregation;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isFocus() {
        return isValid() && DynamicConfig.getKpiGroup(cluster, model, monitor) != null && MapUtils.isNotEmpty(this.innerDataMap);
    }

    public KPIGroupMO kpiGroup() {
        return DynamicConfig.getKpiGroup(cluster, model, monitor);
    }

    public String getKeyMd5() {
        return keyMd5;
    }

    public void setKeyMd5(String keyMd5) {
        this.keyMd5 = keyMd5;
    }

    public Map<String, String> getInnerKeyMap() {
        return innerKeyMap;
    }

    public void setInnerKeyMap(Map<String, String> innerKeyMap) {
        this.innerKeyMap = innerKeyMap;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


    public Map<String, Double> getInnerDataMap() {
        return innerDataMap;
    }

    public void setInnerDataMap(Map<String, Double> innerDataMap) {
        this.innerDataMap = innerDataMap;
    }
}
