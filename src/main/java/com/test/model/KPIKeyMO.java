package com.test.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Joiner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author hzxiacongling
 * Created on 2019-04-17
 */
public class KPIKeyMO {


    /**
     * 生产key
     *
     * @return
     */
    public String hash() {
        Map<String, String> map = new HashMap<>(this.metric);// 重新打乱Map顺序
        String metricJson = JSON.toJSONString(map, SerializerFeature.MapSortField);
        String key = Joiner.on(StrConstant.JIN).join(groupId == null ? StrConstant.XIA_HUA_XIAN : groupId
                , server == null ? StrConstant.XIA_HUA_XIAN : server, StrConstant.XIA_HUA_XIAN, type, metricJson);//历史遗留问题,instance一直使用的"_"做hash
        return MD5Util.md5(key);
    }

    public KPIKeyMO(String groupId, String server, String instance, Map<String, String> metric, int type) {
        this(groupId, server, null, instance, metric, type);
    }

    public KPIKeyMO(String groupId, String server, Integer hostId, String instance, Map<String, String> metric, int type) {
        this.groupId = groupId;
        this.server = server;
        this.hostId = hostId;
        this.instance = instance;
        this.metric = metric;
        this.type = type;
    }

    public KPIKeyMO(String groupId, String server, Map<String, String> metric, int type) {
        this(groupId, server, null, metric, type);
    }

    public KPIKeyMO(String groupId, Map<String, String> metric, int type) {
        this(groupId, null, null, metric, type);
    }

    /**
     * 数据库中groupId
     */
    private String groupId;

    /**
     * 机器名
     */
    private String server;
    /**
     * 机器id
     */
    private Integer hostId;
    /**
     * 实例
     */
    private String instance;
    /**
     * data指标key-value
     */

    private Map<String, String> metric;
    /**
     * 类型
     *
     * @see DataTypeEnum
     */
    private int type;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Map<String, String> getMetric() {
        return metric;
    }

    public void setMetric(Map<String, String> metric) {
        this.metric = metric;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Integer getHostId() {
        return hostId;
    }

    public void setHostId(Integer hostId) {
        this.hostId = hostId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KPIKeyMO)) return false;
        KPIKeyMO kpiKeyMO = (KPIKeyMO) o;
        return getType() == kpiKeyMO.getType() &&
                Objects.equals(getGroupId(), kpiKeyMO.getGroupId()) &&
                Objects.equals(getServer(), kpiKeyMO.getServer()) &&
                Objects.equals(getHostId(), kpiKeyMO.getHostId()) &&
                Objects.equals(getInstance(), kpiKeyMO.getInstance()) &&
                Objects.equals(getMetric(), kpiKeyMO.getMetric());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGroupId(), getServer(), getHostId(), getInstance(), getMetric(), getType());
    }
}
