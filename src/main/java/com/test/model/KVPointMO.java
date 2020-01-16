package com.test.model;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Objects;


public class KVPointMO implements Serializable {
    /**
     * 时间戳,单位是秒,世界时UT--格林尼治平太阳时间
     */
    private Long ts;
    /**
     * value值
     */
    private String value;
    /**
     * key
     */

    private String key;

    /**
     * kpi信息
     */
    private KPIKeyMO metricInfo;

    public KVPointMO() {
    }

    /**
     * @param key
     * @param value
     * @param timestamp
     * @param metricInfo
     */
    public KVPointMO(String key, String value, Long timestamp, KPIKeyMO metricInfo) {
        this.key = key;
        this.ts = timestamp;
        this.value = value;
        this.metricInfo = metricInfo;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public KPIKeyMO getMetricInfo() {
        return metricInfo;
    }

    public void setMetricInfo(KPIKeyMO metricInfo) {
        this.metricInfo = metricInfo;
    }

    public String toJson() {
        return JSON.toJSONString(this);
    }

    @Override
    public String toString() {
        return this.toJson();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KVPointMO)) return false;
        KVPointMO kvPointMo = (KVPointMO) o;
        return Objects.equals(getTs(), kvPointMo.getTs()) &&
                Objects.equals(getValue(), kvPointMo.getValue()) &&
                Objects.equals(getKey(), kvPointMo.getKey()) &&
                Objects.equals(getMetricInfo(), kvPointMo.getMetricInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTs(), getValue(), getKey(), getMetricInfo());
    }
}
