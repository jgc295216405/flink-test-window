package com.test.config;

import java.io.Serializable;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 11:03
 * @city of hangzhou
 * @corp of 163.com
 */
public class LocalStaticConfig extends StaticConfig implements Serializable {
    @Override
    public String bootStrapServers() {
        return "10.172.144.139:9092";
    }

    @Override
    public String producerTopic() {
        return "aiops_kpi3";
    }

    public boolean sinkHBase() {
        return false;
    }

    @Override
    public String hBaseTableName() {
        return "aiops:kpi_series_value2";
    }

    @Override
    public String consumerTopic() {
        return "copy_nss_online_flink";
    }

    @Override
    public String consumerGroup() {
        return "copy_nss_online_flink.0";
    }

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 8282;
    }

    @Override
    public String configKey() {
        return "flink.key.filter.test";
    }


}
