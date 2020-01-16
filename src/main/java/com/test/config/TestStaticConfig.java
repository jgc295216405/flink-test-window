package com.test.config;

import java.io.Serializable;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 11:03
 * @city of hangzhou
 * @corp of 163.com
 */
public class TestStaticConfig extends StaticConfig implements Serializable {
    private String bootStrapServers = "10.172.144.139:9092";
    private String producerTopic = "aiops_kpi";
    private String consumerTopic = "copy_nss_online_flink";
    private String consumerGroup = "copy_nss_online.0";
    private String hBaseTableName = "aiops:kpi_series_value";

    @Override
    public String bootStrapServers() {
        return bootStrapServers;
    }

    @Override
    public String producerTopic() {
        return producerTopic;
    }

    @Override
    public String consumerTopic() {
        return consumerTopic;
    }

    @Override
    public String consumerGroup() {
        return consumerGroup;
    }

    @Override
    public String getHost() {
        return "10.165.135.114";
    }

    @Override
    public int getPort() {
        return 8282;
    }

    @Override
    public String configKey() {
        return "flink.key.filter.test";
    }

    @Override
    public String hBaseTableName() {
        return hBaseTableName;
    }

    @Override
    public int countOutPut() {
        return 1;
    }
    /**
     * 生产环境数据切换到redis中了
     *
     * @return
     */
    @Override
    public boolean sinkHBase() {
        return false;
    }


}
