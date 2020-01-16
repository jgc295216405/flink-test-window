package com.test.config;

import java.io.Serializable;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 11:03
 * @city of hangzhou
 * @corp of 163.com
 */
public class OnlineStaticConfig extends StaticConfig implements Serializable {
    @Override
    public String bootStrapServers() {
        return "urs-datastream8.dg.163.org:9092,urs-datastream9.dg.163.org:9092,urs-datastream10.dg.163.org:9092,urs-datastream11.dg.163.org:9092,urs-datastream12.dg.163.org:9092,urs-datastream13.dg.163.org:9092";
    }

    @Override
    public String producerTopic() {
        return "aiops_kpi";
    }

//    @Override
//    public String consumerTopic() {
//        return "copy_nss_online";
//    }
//
//
//    @Override
//    public String consumerGroup() {
//        return "nss_kafka_data_dg.3";
//    }
    //    nss_kafka_data_dg nss_kafka_data_dg_copy_online.1
    @Override
    public String consumerGroup() {
        return "nss_kafka_data_dg_copy_online.1";
    }

    @Override
    public String consumerTopic() {
        return "nss_kafka_data_dg";
    }


    @Override
    public String getHost() {
        return "ops-aiops.service.163.org";
    }

    @Override
    public String configKey() {
        return "flink.key.filter";
    }

    @Override
    public int getPort() {
        return 80;
    }

    @Override
    public int countOutPut() {
        return 10;
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
