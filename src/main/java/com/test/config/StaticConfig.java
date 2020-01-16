package com.test.config;

import com.alibaba.fastjson.JSON;
import org.apache.flink.api.java.utils.ParameterTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author hzjiaoguangcai
 * @time 2019/7/29 10:53
 * @city of hangzhou
 * @corp of 163.com
 */
public abstract class StaticConfig implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StaticConfig.class);
    private static final String TEST_ENV = "test";
    private static final String LOCAL_ENV = "local";
    private static final String ONLINE_ENV = "online";
    private volatile static StaticConfig config = null;


    public static StaticConfig init(String[] args) {
        ParameterTool params = ParameterTool.fromArgs(args);
        String env = params.get("env", "null");
        LOGGER.info("application start with env: {}", env);
        switch (env) {
            case LOCAL_ENV: {
                config = new LocalStaticConfig();
                break;
            }
            case TEST_ENV: {
                config = new TestStaticConfig();
                break;
            }
            case ONLINE_ENV: {
                config = new OnlineStaticConfig();
                break;
            }
            default:
                throw new RuntimeException("初始化错误");
        }
        LOGGER.info("static config load,{}", JSON.toJSONString(config));
        return config;
    }

    public boolean sinkHBase() {
        return false;
    }

    public String hBaseTableName() {
        return "aiops:kpi_series_value";
    }

    public static StaticConfig getConfig() {
        return config;
    }

    public static void setConfigIfNull(StaticConfig staticConfig) {
        if (null == config) {
            synchronized (StaticConfig.class) {
                if (null == config && null != staticConfig) {
                    config = staticConfig;
                }
            }
        }
    }

    public Properties consumerProperties() {
        return new Properties() {{
            this.put("bootstrap.servers", bootStrapServers());
            this.put("group.id", consumerGroup());
            this.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            this.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
            this.put("auto.offset.reset", "latest");
        }};
    }

    public Properties producerProperties() {
        return new Properties() {{
            this.put("bootstrap.servers", bootStrapServers());
            this.put("acks", "1");
            this.put("retries", 0);
            this.put("batch.size", 16384);
            this.put("linger.ms", 0);
            this.put("buffer.memory", 33554432);
            this.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            this.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            this.put("auto.offset.reset", "latest");
        }};
    }

    public abstract String bootStrapServers();

    public abstract String producerTopic();

    public abstract String consumerTopic();

    public abstract String consumerGroup();

    public abstract String getHost();

    public abstract int getPort();

    public abstract String configKey();

    public int countOutPut() {
        return 1;
    }


}
